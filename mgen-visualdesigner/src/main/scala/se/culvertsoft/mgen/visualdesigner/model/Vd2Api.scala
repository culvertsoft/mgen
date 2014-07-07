package se.culvertsoft.mgen.visualdesigner.model

import java.io.File

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

import ModelConversion.ApiArrayTypeImpl
import ModelConversion.ApiBoolTypeInstance
import ModelConversion.ApiClassImpl
import ModelConversion.ApiCustomType
import ModelConversion.ApiCustomTypeImpl
import ModelConversion.ApiEntity
import ModelConversion.ApiField
import ModelConversion.ApiFloat32TypeInstance
import ModelConversion.ApiFloat64TypeInstance
import ModelConversion.ApiGenerator
import ModelConversion.ApiInt16TypeInstance
import ModelConversion.ApiInt32TypeInstance
import ModelConversion.ApiInt64TypeInstance
import ModelConversion.ApiInt8TypeInstance
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
import ModelConversion.VdCustomTypeRef
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
import se.culvertsoft.mgen.api.model.MGenBaseType
import se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl
import se.culvertsoft.mgen.compiler.defaultparser.LinkTypes
import se.culvertsoft.mgen.compiler.defaultparser.ParseState
import se.culvertsoft.mgen.visualdesigner.classlookup.Type2String

class Vd2ApiConversionState(val srcModel: Model) {
  import ModelConversion._
  val apiObjLkup = new HashMap[String, ApiEntity] // classpath -> Entity
}

object Vd2Api {
  import ModelConversion._

  private def cvtGenerator(vdGenerator: VdGenerator)(implicit cvState: Vd2ApiConversionState): ApiGenerator = {
    val settings = new java.util.HashMap[String, String](vdGenerator.getSettings())
    settings.put("jar_file_folder", vdGenerator.getGeneratorJarFileFolder())
    settings.put("generator_class_path", vdGenerator.getGeneratorClassName())
    settings.put("output_path", vdGenerator.getOutputFolder())
    settings.put("classregistry_path", vdGenerator.getClassRegistryPath())
    new ApiGenerator(vdGenerator.getName(), vdGenerator.getGeneratorClassName(), settings)
  }

  private def getApiCustomType(fullClassPath: String)(implicit cvState: Vd2ApiConversionState): ApiType = {
    cvState.apiObjLkup.getOrElseUpdate(fullClassPath, new UnknownCustomTypeImpl(fullClassPath, -1)).asInstanceOf[ApiType]
  }

  private def getApiCustomType(vdType: VdClass)(implicit cvState: Vd2ApiConversionState): ApiType = {
    getApiCustomType(Type2String.getClassPath(vdType)(cvState.srcModel))
  }

  private def getApiCustomType(vdType: CustomTypeRef)(implicit cvState: Vd2ApiConversionState): ApiType = {
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
      case t: VdCustomTypeRef => getApiCustomType(t)
      case _ => throw new RuntimeException(s"Unknown field type: ${t}")
    }
  }

  private def cvtField(vdField: VdField, parentClass: ApiType)(implicit cvState: Vd2ApiConversionState): ApiField = {
    new ApiField(
      parentClass.fullName(),
      vdField.getName(),
      cvtFieldType(vdField.getType()),
      vdField.getFlags(),
      vdField.getIdOverride())
  }

  private def cvtType(vdClass: VdClass, parentModule: ApiModuleImpl)(implicit cvState: Vd2ApiConversionState): ApiClassImpl = {

    implicit val model = cvState.srcModel

    val apiSuperType =
      if (vdClass.hasSuperType() && vdClass.getSuperType() != null)
        getApiCustomType(model.getEntity(vdClass.getSuperType).get.asInstanceOf[VdClass])
      else
        MGenBaseType.INSTANCE

    val t = new ApiClassImpl(vdClass.getName(), parentModule, apiSuperType)
    
    t.override16BitId(vdClass.getIdOverride())
    
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

  private def cvtModule(vdModule: VdModule, parentPath: String = "")(implicit cvState: Vd2ApiConversionState): Seq[ApiModuleImpl] = {

    val out = new ArrayBuffer[ApiModuleImpl]

    val fullModuleName = if (parentPath.nonEmpty) s"${parentPath}.${vdModule.getName()}" else vdModule.getName
    val savePath = getSavePath(vdModule, fullModuleName)

    val apiModule = new ApiModuleImpl(
      fullModuleName,
      savePath.getWritten(),
      savePath.getAbsolute(),
      vdModule.getSettings())

    cvState.apiObjLkup.put(fullModuleName, apiModule)

    apiModule.setTypes(vdModule.getTypes().map(cvtType(_, apiModule)))

    out += apiModule
    out ++= vdModule.getSubmodules().flatMap(cvtModule(_, fullModuleName))

    out
  }

  private def cvtProject(vdProject: VdProject, isRoot: Boolean)(implicit cvState: Vd2ApiConversionState): ApiProjectImpl = {

    cvState.apiObjLkup.getOrElse(vdProject.getFilePath().getAbsolute(), {

      val apiProject = new ApiProjectImpl(
        vdProject.getName(),
        vdProject.getFilePath().getWritten(),
        vdProject.getFilePath().getAbsolute(),
        isRoot)

      cvState.apiObjLkup.put(vdProject.getFilePath().getAbsolute(), apiProject)

      if (vdProject.hasGenerators)
        apiProject.setGenerators(vdProject.getGenerators.map(cvtGenerator))

      if (vdProject.hasModules)
        apiProject.setModules(vdProject.getModules.flatMap(cvtModule(_)))

      if (vdProject.hasSettings)
        apiProject.setSettings(vdProject.getSettings)

      if (vdProject.hasDependencies)
        apiProject.setDependencies(vdProject.getDependencies.map(cvtProject(_, false)))

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
        case e: ApiCustomTypeImpl =>
          parseState.typeLookup.typesFullName.put(path, e)
        case _ =>
      }
    }

    // Add the types that need to be linked
    val doneProjects = new HashSet[String]
    def checkProjectLinkage(project: ApiProject) {

      def checkClassLinkage(t: ApiCustomType) {
        if ((t.hasSuperType() && !t.superType().isTypeKnown()) ||
          t.fields().exists(!_.typ().isTypeKnown())) {
          parseState.needLinkage.types += t.asInstanceOf[ApiCustomTypeImpl]
        }
      }

      def checkModuleLinkage(m: ApiModule) {
        m.types.values foreach checkClassLinkage
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