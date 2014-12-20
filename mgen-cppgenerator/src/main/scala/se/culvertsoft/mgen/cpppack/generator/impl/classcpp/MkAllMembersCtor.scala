package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.canBeNull
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.writeInitializerList
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName

object MkAllMembersCtor {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    implicit val module = t.module
    val allFields = t.fieldsInclSuper()

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
        initializerList += MkCtorHelper.mkPassToSuper(fieldsToSuper, t)

      if (t.fields.nonEmpty) {

        for (field <- t.fields)
          initializerList += s"m_${field.name()}(${field.name()})"

        for (field <- nonNullFields)
          initializerList += s"${isSetName(field)}(true)"

      }

      writeInitializerList(initializerList)
    }

    if (allFields.nonEmpty) {

      txt(s"${t.shortName}::${t.shortName}(")
      mkArgumentList()
      txt(")")
      mkInitializerList()
      ln(" {")
      ln("}").endl()
    }
  }

}