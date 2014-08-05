package se.culvertsoft.mgen.visualdesigner.model

import java.io.File

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

import ModelConversion.ApiArrayType
import ModelConversion.ApiBoolTypeInstance
import ModelConversion.ApiClass
import ModelConversion.ApiEntity
import ModelConversion.ApiEnum
import ModelConversion.ApiEnumEntry
import ModelConversion.ApiEnumType
import ModelConversion.ApiField
import ModelConversion.ApiFloat32TypeInstance
import ModelConversion.ApiFloat64TypeInstance
import ModelConversion.ApiGenerator
import ModelConversion.ApiInt16TypeInstance
import ModelConversion.ApiInt32TypeInstance
import ModelConversion.ApiInt64TypeInstance
import ModelConversion.ApiInt8TypeInstance
import ModelConversion.ApiListType
import ModelConversion.ApiMapType
import ModelConversion.ApiModule
import ModelConversion.ApiProject
import ModelConversion.ApiStringTypeInstance
import ModelConversion.ApiType
import ModelConversion.ApiUserDefinedType
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
import se.culvertsoft.mgen.api.model.UnlinkedType
import se.culvertsoft.mgen.api.util.CRC16
import se.culvertsoft.mgen.compiler.components.LinkTypes
import se.culvertsoft.mgen.idlparser.IdlDefaultValue
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
    settings.put("class_path", vdGenerator.getGeneratorClassName())
    settings.put("output_path", vdGenerator.getOutputFolder())
    settings.put("classregistry_path", vdGenerator.getClassRegistryPath())
    new ApiGenerator(vdGenerator.getName(), vdGenerator.getGeneratorClassName(), settings)
  }

  private def getApiCustomType(fullClassPath: String)(implicit cvState: Vd2ApiConversionState): ApiUserDefinedType = {
    cvState.apiObjLkup.getOrElseUpdate(fullClassPath, new UnlinkedType(fullClassPath)).asInstanceOf[ApiUserDefinedType]
  }

  private def getApiCustomType(vdType: VdClass)(implicit cvState: Vd2ApiConversionState): ApiUserDefinedType = {
    getApiCustomType(Type2String.getClassPath(vdType)(cvState.srcModel))
  }

  private def getApiCustomType(vdType: UserTypeRef)(implicit cvState: Vd2ApiConversionState): ApiUserDefinedType = {
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
      case t: VdListType => new ApiListType(cvtFieldType(t.getElementType()))
      case t: VdArrayType => new ApiArrayType(cvtFieldType(t.getElementType()))
      case t: VdMapType => new ApiMapType(cvtFieldType(t.getKeyType), cvtFieldType(t.getValueType))
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
    new ApiField(
      parentClass.fullName(),
      vdField.getName(),
      cvtFieldType(vdField.getType()),
      vdField.getFlags(),
      getId16Bit(vdField),
      if (vdField.hasDefaultValue()) new IdlDefaultValue(vdField.getDefaultValue()) else null)
  }

  private def cvtEnumEntry(vdEntry: VdEnumEntry, parentEnum: ApiEnum)(implicit cvState: Vd2ApiConversionState): ApiEnumEntry = {
    new ApiEnumEntry(vdEntry.getName(), vdEntry.getConstant())
  }

  private def cvtEnum(vdEnum: VdEnum, parentModule: ApiModule)(implicit cvState: Vd2ApiConversionState): ApiEnumType = {

    implicit val model = cvState.srcModel

    val t = new ApiEnumType(vdEnum.getName(), Type2String.getClassPath(vdEnum), parentModule)

    cvState.apiObjLkup.put(Type2String.getClassPath(vdEnum), t)

    t.setEntries(vdEnum.getEntries.map(cvtEnumEntry(_, t)))

    t
  }

  private def cvtType(vdClass: VdClass, parentModule: ApiModule)(implicit cvState: Vd2ApiConversionState): ApiClass = {

    implicit val model = cvState.srcModel

    val apiSuperType =
      if (vdClass.hasSuperType() && vdClass.getSuperType() != null)
        getApiCustomType(model.getEntity(vdClass.getSuperType).get.asInstanceOf[VdClass])
      else
        null

    val t = new ApiClass(vdClass.getName(), parentModule, getId16Bit(vdClass), apiSuperType)

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

  private def cvtModule(vdModule: VdModule, apiProject: ApiProject, parentPath: String = "")(implicit cvState: Vd2ApiConversionState): Seq[ApiModule] = {

    val out = new ArrayBuffer[ApiModule]

    val fullModuleName = if (parentPath.nonEmpty) s"${parentPath}.${vdModule.getName()}" else vdModule.getName
    val savePath = getSavePath(vdModule, fullModuleName)

    val apiModule = new ApiModule(
      fullModuleName,
      savePath.getWritten(),
      savePath.getAbsolute(),
      vdModule.getSettings(),
      apiProject)

    cvState.apiObjLkup.put(fullModuleName, apiModule)

    apiModule.setEnums(vdModule.getEnums().map(cvtEnum(_, apiModule)))
    apiModule.setClasses(vdModule.getTypes().map(cvtType(_, apiModule)))

    out += apiModule
    out ++= vdModule.getSubmodules().flatMap(cvtModule(_, apiProject, fullModuleName))

    out
  }

  private def cvtProject(vdProject: VdProject, isRoot: Boolean, apiParent: ApiProject = null)(implicit cvState: Vd2ApiConversionState): ApiProject = {

    cvState.apiObjLkup.getOrElse(vdProject.getFilePath().getAbsolute(), {

      val apiProject = new ApiProject(
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

    }).asInstanceOf[ApiProject]

  }

  private def linkTypes(apiProject: ApiProject) {
    LinkTypes(apiProject)
  }

  def apply(model: VdModel): ApiProject = {

    implicit val cvState = new Vd2ApiConversionState(model)

    val out = cvtProject(model.project, true)

    linkTypes(out)

    out
  }

}