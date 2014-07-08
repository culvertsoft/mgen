package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenerator._
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName

object MkRequiredMembersCtor {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val reqAndOptFields = t.fieldsInclSuper().toBuffer
    val reqFields = t.fieldsInclSuper().filter(_.isRequired())
    val fieldsToSuper = reqFields -- t.fields
    val nonNullFields = t.fields.filterNot(canBeNull)

    def mkInitializerList() {
      val initializerList = new ArrayBuffer[String]
      if (fieldsToSuper.nonEmpty)
        initializerList += MkCtorHelper.mkPassToSuper(fieldsToSuper, t, module)
      if (t.fields.nonEmpty)
        initializerList ++= MkCtorHelper.mkReqMemberValues(t.fields, module)
      if (nonNullFields.nonEmpty)
        initializerList ++= MkCtorHelper.mkReqNonNullFields(nonNullFields)
      writeInitializerList(initializerList)
    }

    def mkArgumentList() {
      for (field <- reqFields) {
        txt(if (field != reqFields.head) 3 else 0, s"const ${getTypeName(field)}& ${field.name()}")
        if (field != reqFields.last)
          ln(",")
      }
    }

    if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
      txt(s"${t.name()}::${t.name()}(")
      mkArgumentList()
      txt(")")
      mkInitializerList()
      ln("{")
      ln("}")
      endl()
    }

  }

}