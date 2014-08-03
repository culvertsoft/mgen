package se.culvertsoft.mgen.visualdesigner.model

import java.io.File

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

import ModelConversion.ApiArrayTypeImpl
import ModelConversion.ApiBoolTypeInstance
import ModelConversion.ApiClassImpl
import ModelConversion.ApiCustomType
import ModelConversion.ApiEntity
import ModelConversion.ApiEnum
import ModelConversion.ApiEnumEntryImpl
import ModelConversion.ApiEnumTypeImpl
import ModelConversion.ApiField
import ModelConversion.ApiFieldImpl
import ModelConversion.ApiFloat32TypeInstance
import ModelConversion.ApiFloat64TypeInstance
import ModelConversion.ApiGeneratorImpl
import ModelConversion.ApiInt16TypeInstance
import ModelConversion.ApiInt32TypeInstance
import ModelConversion.ApiInt64TypeInstance
import ModelConversion.ApiInt8TypeInstance
import ModelConversion.ApiLinkedCustomType
import ModelConversion.ApiListTypeImpl
import ModelConversion.ApiMapTypeImpl
import ModelConversion.ApiModule
import ModelConversion.ApiModuleImpl
import ModelConversion.ApiProject
import ModelConversion.ApiProjectImpl
import ModelConversion.ApiStringTypeInstance
import ModelConversion.ApiType
import ModelConversion.VdArrayType
import ModelConversion.VdBoolType
import ModelConversion.VdClass
import ModelConversion.VdEntity
import ModelConversion.VdEnum
import ModelConversion.VdEnumEntry
import ModelConversion.VdField
import ModelConversion.VdFieldType
import ModelConversion.VdFloat32Type
import ModelConversion.VdFloat64Type
import ModelConversion.VdGenerator
import ModelConversion.VdInt16Type
import ModelConversion.VdInt32Type
import ModelConversion.VdInt64Type
import ModelConversion.VdInt8Type
import ModelConversion.VdListType
import ModelConversion.VdMapType
import ModelConversion.VdModel
import ModelConversion.VdModule
import ModelConversion.VdProject
import ModelConversion.VdStringType
import ModelConversion.VdUserTypeRef
import se.culvertsoft.mgen.api.model.impl.UnlinkedCustomType
import se.culvertsoft.mgen.api.model.impl.UnlinkedDefaultValueImpl
import se.culvertsoft.mgen.api.util.CRC16
import se.culvertsoft.mgen.compiler.components.LinkTypes
import se.culvertsoft.mgen.compiler.defaultparser.ParseState
import se.culvertsoft.mgen.visualdesigner.classlookup.Type2String

class Vd2ApiConversionState(val srcModel: Model) {
  import ModelConversion._
  val apiObjLkup = new HashMap[String, ApiEntity] // classpath -> Entity
}

object Vd2Api {
  import ModelConversion._

  private def cvtGenerator(vdGenerator: VdGenerator)(implicit cvState: Vd2ApiConversionState): ApiGeneratorImpl = {
    val settings = new java.util.HashMap[String, String](vdGenerator.getSettings())
    settings.put("jar_file_folder", vdGenerator.getGeneratorJarFileFolder())
    settings.put("class_path", vdGenerator.getGeneratorClassName())
    settings.put("output_path", vdGenerator.getOutputFolder())
    settings.put("classregistry_path", vdGenerator.getClassRegistryPath())
    new ApiGeneratorImpl(vdGenerator.getName(), vdGenerator.getGeneratorClassName(), settings)
  }

  private def getApiCustomType(fullClassPath: String)(implicit cvState: Vd2ApiConversionState): ApiCustomType = {
    cvState.apiObjLkup.getOrElseUpdate(fullClassPath, new UnlinkedCustomType(fullClassPath, -1)).asInstanceOf[ApiCustomType]
  }

  private def getApiCustomType(vdType: VdClass)(implicit cvState: Vd2ApiConversionState): ApiCustomType = {
    getApiCustomType(Type2String.getClassPath(vdType)(cvState.srcModel))
  }

  private def getApiCustomType(vdType: UserTypeRef)(implicit cvState: Vd2ApiConversionState): ApiType = {
    getApiCustomType(cvState.srcModel.getEntity(vdType.getId()).get.asInstanceOf[VdClass])
  }

  private def cvtFieldType(t: VdFieldType)(implicit cvState: Vd2ApiConversionState): ApiType = {
    t match {
      case t: VdBoolType => ApiBoolTypeInstance
      case t: VdInt8Type => ApiInt8TypeInstance
      case t: VdInt16Type => ApiInt16TypeInstance
      case t: VdInt32Type => ApiInt32TypeInstance
      case t: VdInt64Type => ApiInt64TypeInstance
      case t: VdFloat32Type => ApiFloat32TypeInstance
      case t: VdFloat64Type => ApiFloat64TypeInstance
      case t: VdStringType => ApiStringTypeInstance
      case t: VdListType => new ApiListTypeImpl(cvtFieldType(t.getElementType()))
      case t: VdArrayType => new ApiArrayTypeImpl(cvtFieldType(t.getElementType()))
      case t: VdMapType => new ApiMapTypeImpl(cvtFieldType(t.getKeyType), cvtFieldType(t.getValueType))
      case t: VdUserTypeRef => getApiCustomType(t)
      case _ => throw new RuntimeException(s"Unknown field type: ${t}")
    }
  }

  def getId16Bit(e: VdEntity)(implicit cvState: Vd2ApiConversionState): Short = {
    implicit val model = cvState.srcModel
    val (has, get, name) = e match {
      case e: VdField => (() => e.hasId16Bit, () => e.getId16Bit, () => e.getName)
      case e: VdClass => (() => e.hasId16Bit, () => e.getId16Bit, () => Type2String.getClassPath(e))
    }
    if (has()) {
      get()
    } else {
      CRC16.calc(name())
    }

  }

  private def cvtField(vdField: VdField, parentClass: ApiType)(implicit cvState: Vd2ApiConversionState): ApiField = {
    new ApiFieldImpl(
      parentClass.fullName(),
      vdField.getName(),
      cvtFieldType(vdField.getType()),
      vdField.getFlags(),
      getId16Bit(vdField),
      if (vdField.hasDefaultValue()) new UnlinkedDefaultValueImpl(vdField.getDefaultValue()) else null)
  }

  private def cvtEnumEntry(vdEntry: VdEnumEntry, parentEnum: ApiEnum)(implicit cvState: Vd2ApiConversionState): ApiEnumEntryImpl = {
    new ApiEnumEntryImpl(vdEntry.getName(), vdEntry.getConstant())
  }
  
  private def cvtEnum(vdEnum: VdEnum, parentModule: ApiModuleImpl)(implicit cvState: Vd2ApiConversionState): ApiEnumTypeImpl = {

    implicit val model = cvState.srcModel

    val t = new ApiEnumTypeImpl(vdEnum.getName(), Type2String.getClassPath(vdEnum), parentModule)

    cvState.apiObjLkup.put(Type2String.getClassPath(vdEnum), t)

    t.setEntries(vdEnum.getEntries.map(cvtEnumEntry(_, t)))

    t
  }

  private def cvtType(vdClass: VdClass, parentModule: ApiModuleImpl)(implicit cvState: Vd2ApiConversionState): ApiClassImpl = {

    implicit val model = cvState.srcModel

    val apiSuperType =
      if (vdClass.hasSuperType() && vdClass.getSuperType() != null)
        getApiCustomType(model.getEntity(vdClass.getSuperType).get.asInstanceOf[VdClass])
      else
        null

    val t = new ApiClassImpl(vdClass.getName(), parentModule, getId16Bit(vdClass), apiSuperType)

    cvState.apiObjLkup.put(Type2String.getClassPath(vdClass), t)

    t.setFields(vdClass.getFields.map(cvtField(_, t)))

    t
  }

  private def getSavePath(
    vdModule: VdModule,
    fullModuleName: String): FilePath = {

    val writtenDir = vdModule.getSaveDir.getWritten
    val absoluteDir = vdModule.getSaveDir.getAbsolute

    val writtenPrepend = if (writtenDir.nonEmpty) (writtenDir + File.separator) else ""
    val absolutePrepend = absoluteDir + File.separator

    new FilePath(
      writtenPrepend + fullModuleName + ".xml",
      absolutePrepend + fullModuleName + ".xml")
  }

  private def cvtModule(vdModule: VdModule, apiProject: ApiProjectImpl, parentPath: String = "")(implicit cvState: Vd2ApiConversionState): Seq[ApiModuleImpl] = {

    val out = new ArrayBuffer[ApiModuleImpl]

    val fullModuleName = if (parentPath.nonEmpty) s"${parentPath}.${vdModule.getName()}" else vdModule.getName
    val savePath = getSavePath(vdModule, fullModuleName)

    val apiModule = new ApiModuleImpl(
      fullModuleName,
      savePath.getWritten(),
      savePath.getAbsolute(),
      vdModule.getSettings(),
      apiProject)

    cvState.apiObjLkup.put(fullModuleName, apiModule)

    apiModule.setEnums(vdModule.getEnums().map(cvtEnum(_, apiModule)))
    apiModule.setTypes(vdModule.getTypes().map(cvtType(_, apiModule)))

    out += apiModule
    out ++= vdModule.getSubmodules().flatMap(cvtModule(_, apiProject, fullModuleName))

    out
  }

  private def cvtProject(vdProject: VdProject, isRoot: Boolean, apiParent: ApiProjectImpl = null)(implicit cvState: Vd2ApiConversionState): ApiProjectImpl = {

    cvState.apiObjLkup.getOrElse(vdProject.getFilePath().getAbsolute(), {

      val apiProject = new ApiProjectImpl(
        vdProject.getName(),
        vdProject.getFilePath().getWritten(),
        vdProject.getFilePath().getAbsolute(),
        apiParent)

      cvState.apiObjLkup.put(vdProject.getFilePath().getAbsolute(), apiProject)

      if (vdProject.hasGenerators)
        apiProject.setGenerators(vdProject.getGenerators.map(cvtGenerator))

      if (vdProject.hasModules)
        apiProject.setModules(vdProject.getModules.flatMap(cvtModule(_, apiProject)))

      if (vdProject.hasSettings)
        apiProject.setSettings(vdProject.getSettings)

      if (vdProject.hasDependencies)
        apiProject.setDependencies(vdProject.getDependencies.map(cvtProject(_, false, apiParent)))

      apiProject

    }).asInstanceOf[ApiProjectImpl]

  }

  private def linkTypes(apiProject: ApiProjectImpl)(implicit cvState: Vd2ApiConversionState) {

    // Use the same linking algorithm that the compiler uses
    implicit val parseState = new ParseState

    // Fill in the type lookup
    for (pair <- cvState.apiObjLkup) {
      val path = pair._1
      val e = pair._2
      e match {
        case e: ApiLinkedCustomType =>
          parseState.typeLookup.typesFullName.put(path, e)
        case _ =>
      }
    }

    // Add the types that need to be linked
    val doneProjects = new HashSet[String]
    def checkProjectLinkage(project: ApiProject) {

      def checkClassLinkage(t: ApiCustomType) {
        if ((t.hasSuperType() && !t.superType().isLinked()) ||
          t.fields().exists(!_.typ().isLinked())) {
          parseState.needLinkage.types += t.asInstanceOf[ApiLinkedCustomType]
        }
      }

      def checkModuleLinkage(m: ApiModule) {
        m.types foreach checkClassLinkage
      }

      if (!doneProjects.contains(project.filePath)) {
        doneProjects += project.filePath
        project.dependencies foreach checkProjectLinkage
        project.modules foreach checkModuleLinkage
      }

    }

    checkProjectLinkage(apiProject)

    LinkTypes(apiProject)

  }

  def apply(model: VdModel): ApiProjectImpl = {

    implicit val cvState = new Vd2ApiConversionState(model)

    val out = cvtProject(model.project, true)

    linkTypes(out)

    out
  }

}