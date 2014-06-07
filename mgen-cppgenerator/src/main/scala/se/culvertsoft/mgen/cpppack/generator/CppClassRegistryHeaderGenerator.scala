package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable

import se.culvertsoft.mgen.api.model.Module

object CppClassRegistryHeaderGenerator extends CppClassRegistryGenerator(".h") {

   override def mkIncludes(
      referencedModules: Seq[Module],
      generatorSettings: java.util.Map[String, String]) {

      txtBuffer.textln("#include \"mgen/classes/ClassRegistryBase.h\"")
      
      for (referencedModule <- referencedModules)
         for (t <- referencedModule.types().values())
            CppGenUtils.include(t)

      txtBuffer.endl()

   }

   override def mkClassStart(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
      CppGenUtils.mkClassStart("ClassRegistry", "mgen::ClassRegistryBase")
      txtBuffer.textln("public:")
      txtBuffer.endl()
   }

   override def mkDefaultCtor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
      txtBuffer.tabs(1).textln(s"ClassRegistry();")
   }

   override def mkDestructor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
      txtBuffer.tabs(1).textln(s"virtual ~ClassRegistry();")
      txtBuffer.endl()
   }

   def quote(s: String): String = {
      '"' + s + '"'
   }

   override def mkReadObjectFields(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {

      txtBuffer.tabs(1).textln(s"template<typename ContextType, typename ReaderType>")
      txtBuffer.tabs(1).textln(s"void readObjectFields(mgen::MGenBase& o, ContextType& context, ReaderType& reader) const {")
      txtBuffer.tabs(2).textln(s"switch (o._typeHash16bit()) {")
      for (module <- referencedModules) {
         for (t <- module.types().values()) {
            val fullName = t.fullName().replaceAllLiterally(".", "::")
            txtBuffer.tabs(2).textln(s"case ($fullName::_TYPE_HASH_16BIT):")
            txtBuffer.tabs(3).textln(s"reader.readFields(reinterpret_cast<$fullName&>(o), context);")
            txtBuffer.tabs(3).textln(s"break;")
         }
      }
      txtBuffer.tabs(2).textln("default: // should not happen...INCORRECT USAGE!")
      txtBuffer.tabs(3).textln("throw mgen::Exception(")
      txtBuffer.tabs(5).textln("std::string(")
      txtBuffer.tabs(7).textln(s"${quote(s"${namespacesstring}::ClassRegistry::readObjectFields: Incorrect usage. Class '")}).append(")
      txtBuffer.tabs(7).textln("o._typeName()).append(\" not registered.\"));")
      txtBuffer.tabs(2).textln(s"}")
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.endl()

   }

   override def mkVisitObjectFields(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {

      for (constString <- List("", "const ")) {

         txtBuffer.tabs(1).textln(s"template<typename VisitorType>")
         txtBuffer.tabs(1).textln(s"void visitObject(${constString}mgen::MGenBase& o, VisitorType& visitor) const {")
         txtBuffer.tabs(2).textln(s"switch (o._typeHash16bit()) {")
         for (module <- referencedModules) {
            for (t <- module.types().values()) {
               val fullName = t.fullName().replaceAllLiterally(".", "::")
               txtBuffer.tabs(2).textln(s"case ($fullName::_TYPE_HASH_16BIT):")
               txtBuffer.tabs(3).textln(s"reinterpret_cast<${constString}${fullName}&>(o)._accept<VisitorType>(visitor);")
               txtBuffer.tabs(3).textln(s"break;")
            }
         }
         txtBuffer.tabs(2).textln("default: // should not happen...INCORRECT USAGE!")
         txtBuffer.tabs(3).textln("throw mgen::Exception(")
         txtBuffer.tabs(5).textln("std::string(")
         txtBuffer.tabs(7).textln(s"${quote(s"${namespacesstring}::ClassRegistry::visitObject: Incorrect usage. Class '")}).append(")
         txtBuffer.tabs(7).textln("o._typeName()).append(\" not registered.\"));")
         txtBuffer.tabs(2).textln(s"}")
         txtBuffer.tabs(1).textln(s"}")
         txtBuffer.endl()

      }

   }

   override def mkClassEnd(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
      CppGenUtils.mkClassEnd("ClassRegistry")
   }

}