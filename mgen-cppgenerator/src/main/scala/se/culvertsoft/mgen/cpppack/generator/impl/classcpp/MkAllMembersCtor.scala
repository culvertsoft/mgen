package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.canBeNull
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.writeInitializerList
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName

object MkAllMembersCtor {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {
    implicit val currentModule = module
    val allFields = t.getAllFieldsInclSuper()

    def mkArgumentList() {
      for (field <- allFields) {
        txtBuffer.tabs(if (field != allFields.head) 3 else 0).text(s"const ${getTypeName(field)}& ${field.name()}")
        if (field != allFields.last)
          ln(", ")
      }
    }

    def mkInitializerList() {

      val initializerList = new ArrayBuffer[String]
      val fieldsToSuper = allFields -- t.fields
      val nonNullFields = t.fields().filterNot(canBeNull)

      if (fieldsToSuper.nonEmpty)
        initializerList += MkCtorHelper.mkPassToSuper(fieldsToSuper, t, module)

      if (t.fields.nonEmpty) {

        for (field <- t.fields)
          initializerList += s"m_${field.name()}(${field.name()})"

        for (field <- nonNullFields)
          initializerList += s"${isSetName(field)}(true)"

      }

      writeInitializerList(initializerList)
    }

    if (allFields.nonEmpty) {

      txt(s"${t.name()}::${t.name()}(")
      mkArgumentList()
      txt(")")
      mkInitializerList()
      ln(" {")
      ln("}").endl()
    }
  }

}