package se.culvertsoft.mgen.javapack.generator

import se.culvertsoft.mgen.compiler.internal.FancyHeaders

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

  val validatorClsStringQ = s"${utilPkg}.${validatorClsString}"
  val setFieldSetClsStringQ = s"${utilPkg}.${setFieldSetClsString}"
  val deepCopyerClsStringQ = s"${utilPkg}.${deepCopyerClsString}"
  val fieldHasherClsStringQ = s"${utilPkg}.${fieldHasherClsString}"

  val mgenBaseClsString = s"${javapackPkg}.classes.MGenBase"
  val mgenEnumClsString = s"${javapackPkg}.classes.MGenEnum"
  val clsRegistryClsString = s"${javapackPkg}.classes.ClassRegistry"
  val clsRegistryEntryClsString = s"ClassRegistryEntry"
  val clsRegistryEntryClsStringQ = s"$javapackPkg.classes.$clsRegistryEntryClsString"
  val eqTesterClsString = s"EqualityTester"
  val eqTesterClsStringQ = s"${javapackPkg}.util.${eqTesterClsString}"

  val fieldClsString = s"Field"
  val readerClsString = s"Reader"
  val fieldVisitorClsString = s"FieldVisitor"

  val fieldClsStringQ = s"${modelPkg}.${fieldClsString}"
  val readerClsStringQ = s"${javapackPkg}.serialization.${readerClsString}"
  val fieldVisitorClsStringQ = s"${javapackPkg}.serialization.${fieldVisitorClsString}"

  val fieldSetDepthClsString = s"FieldSetDepth"
  val fieldSetDepthClsStringQ = s"${metadataPkg}.${fieldSetDepthClsString}"

  val fileHeader = FancyHeaders.fileHeader
  val serializationSectionHeader = FancyHeaders.serializationSectionHeader
  val metadataSectionHeader = FancyHeaders.metadataSectionHeader

}