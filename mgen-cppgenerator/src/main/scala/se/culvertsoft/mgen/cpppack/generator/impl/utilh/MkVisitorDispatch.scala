package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object MkVisitorDispatch {

  def apply(
    referencedModules: Seq[Module],
    namespaceString: String,
    generatorSettings: java.util.Map[String, String])(implicit txtBuffer: SuperStringBuffer) {

    for (constString <- List("", "const ")) {

      txtBuffer.tabs(1).textln(s"template<typename VisitorType>")
      txtBuffer.tabs(1).textln(s"void visitObject(${constString}mgen::MGenBase& o, VisitorType& visitor) const {")

      // TODO: Make type switch here

      /*
     * txtBuffer.tabs(2).textln(s"switch (o._typeHash16bit()) {")
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
      txtBuffer.tabs(7).textln(s"${quote(s"${namespaceString}::ClassRegistry::visitObject: Incorrect usage. Class '")}).append(")
      txtBuffer.tabs(7).textln("o._typeName()).append(\" not registered.\"));")
      txtBuffer.tabs(2).textln(s"}")
      
      */

      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.endl()

    }

  }
}