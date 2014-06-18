package se.culvertsoft.mgen.javapack.generator

import java.io.File
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.seqAsJavaList
import JavaConstants.arrayListClsString
import JavaConstants.clsRegistryClsString
import JavaConstants.colClsString
import JavaConstants.deepCopyerClsString
import JavaConstants.eqTesterClsString
import JavaConstants.fieldClsString
import JavaConstants.fieldHasherClsString
import JavaConstants.fieldSetDepthClsString
import JavaConstants.fieldVisitorClsString
import JavaConstants.metadataSectionHeader
import JavaConstants.modelPkg
import JavaConstants.readerClsString
import JavaConstants.serializationSectionHeader
import JavaConstants.setFieldSetClsString
import JavaConstants.validatorClsString
import JavaConstruction.defaultConstructNull
import JavaReadCalls.mkReadCall
import JavaTypeNames.fieldTypeName
import JavaTypeNames.getTypeName
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.makers.Alias._
import se.culvertsoft.mgen.javapack.generator.makers._
import se.culvertsoft.mgen.javapack.generator.makers.MkFancyHeader.MkMetadataComment
import se.culvertsoft.mgen.javapack.generator.makers.MkFancyHeader.MkMetadataMethodsComment
import se.culvertsoft.mgen.javapack.generator.makers.MkAcceptVisitor

class JavaGenerator extends BuiltInStaticLangGenerator {
  import JavaConstants._
  import JavaTypeNames._
  import JavaConstruction._
  import JavaToString._
  import JavaReadCalls._

  implicit val txtBuffer = new SuperStringBuffer

  override def generateTopLevelMetaSources(
    folder: String,
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val fileName = "MGenClassRegistry" + ".java"
    val sourceCode = MkClassRegistry(referencedModules, packagePath)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  override def generateModuleMetaSources(
    module: Module,
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, generatorSettings)
    val fileName = "MGenModuleClassRegistry" + ".java"
    val sourceCode = MkModuleClassRegistry(module)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  override def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, generatorSettings)
    val fileName = t.name() + ".java"
    val sourceCode = generateClassSourceCode(t)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }


  def generateClassSourceCode(t: CustomType): String = {

    txtBuffer.clear()

    MkFancyHeader(t)

    MkPackage(currentModule)
    MkImports(t, currentModule)
    MkClassStart(t, currentModule)

    MkMembers(t, currentModule)
    MkDefaultCtor(t, currentModule)
    MkRequiredMembersCtor(t, currentModule)
    MkAllMembersCtor(t, currentModule)
    MkGetters(t, currentModule)
    MkSetters(t, currentModule)
    MkToString(t, currentModule)
    MkHashCode(t, currentModule)
    MkEquals(t, currentModule)
    MkDeepCopy(t, currentModule)

    MkMetadataMethodsComment(t)

    MkTypeIdMethods(t, currentModule)
    MkAcceptVisitor(t, currentModule)
    MkReadField(t, currentModule)
    MkGetFields(t, currentModule)
    MkIsFieldSet(t, currentModule)
    MkMarkFieldsSet(t, currentModule)
    MkValidate(t, currentModule)
    MkNFieldsSet(t, currentModule)
    MkFieldById(t, currentModule)

    MkMetadataComment(t)

    MkTypeIdFields(t, currentModule)
    MkFieldMetaData(t, currentModule)
    MkClassEnd()

    txtBuffer.toString()
  }

}