package se.culvertsoft.mgen.javapack.generator

import se.culvertsoft.mgen.compiler.internal.FancyHeaders
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.RuntimeEnumType
import se.culvertsoft.mgen.api.model.RuntimeClassType
import se.culvertsoft.mgen.javagenerator.BuildVersion

object JavaConstants {

  val colClsString = "java.util.Collection"
  val listClsString = "java.util.List"
  val arrayListClsString = "java.util.ArrayList"

  val utilPkg = "se.culvertsoft.mgen.javapack.util"
  val metadataPkg = "se.culvertsoft.mgen.javapack.metadata"
  val apiPkg = "se.culvertsoft.mgen.api"
  val modelPkg = s"${apiPkg}.model"
  val javapackPkg = "se.culvertsoft.mgen.javapack"

  val validatorClsString = s"Validator"
  val setFieldSetClsString = s"Marker"
  val deepCopyerClsString = s"DeepCopyer"
  val fieldHasherClsString = s"FieldHasher"
  
  val listMakerClsString = s"ListMaker"
  val mapMakerClsString = s"MapMaker"

  val listMakerClsStringQ = s"${utilPkg}.${listMakerClsString}"
  val mapMakerClsStringQ = s"${utilPkg}.${mapMakerClsString}"
  val validatorClsStringQ = s"${utilPkg}.${validatorClsString}"
  val setFieldSetClsStringQ = s"${utilPkg}.${setFieldSetClsString}"
  val deepCopyerClsStringQ = s"${utilPkg}.${deepCopyerClsString}"
  val fieldHasherClsStringQ = s"${utilPkg}.${fieldHasherClsString}"
  
  val stringifyerCls = s"Stringifyer"
  val stringifyerClsQ = s"${utilPkg}.${stringifyerCls}"

  val mgenBaseClsString = s"${javapackPkg}.classes.MGenBase"
  val mgenEnumClsString = s"${javapackPkg}.classes.MGenEnum"
  val clsRegistryClsString = s"${javapackPkg}.classes.ClassRegistryBase"
  val clsRegistryEntryClsString = s"ClassRegistryEntry"
  val clsRegistryEntryClsStringQ = s"$javapackPkg.classes.$clsRegistryEntryClsString"
  val eqTesterClsString = s"EqualityTester"
  val eqTesterClsStringQ = s"${javapackPkg}.util.${eqTesterClsString}"

  val fieldIfcClsString = s"Field"
  val readerClsString = s"Reader"
  val fieldVisitorClsString = s"FieldVisitor"
  
  val runtimeEnumClsString = classOf[RuntimeEnumType].getSimpleName
  val runtimeEnumClsStringQ = classOf[RuntimeEnumType].getName
  val runtimeClassClsString = classOf[RuntimeClassType].getSimpleName
  val runtimeClassClsStringQ = classOf[RuntimeClassType].getName

  val fieldIfcClsStringQ = s"${modelPkg}.${fieldIfcClsString}"
  val readerClsStringQ = s"${javapackPkg}.serialization.${readerClsString}"
  val fieldVisitorClsStringQ = s"${javapackPkg}.serialization.${fieldVisitorClsString}"

  val fieldSetDepthClsString = s"FieldSetDepth"
  val fieldVisitSelectionClsString = s"FieldVisitSelection"
  val fieldSetDepthClsStringQ = s"${metadataPkg}.${fieldSetDepthClsString}"
  val fieldVisitSelectionClsStringQ = s"${metadataPkg}.${fieldVisitSelectionClsString}"

  val fileHeader = FancyHeaders.fileHeader(BuildVersion.GIT_TAG + " " + BuildVersion.GIT_COMMIT_DATE)
  val serializationSectionHeader = FancyHeaders.serializationSectionHeader
  val metadataSectionHeader = FancyHeaders.metadataSectionHeader

}