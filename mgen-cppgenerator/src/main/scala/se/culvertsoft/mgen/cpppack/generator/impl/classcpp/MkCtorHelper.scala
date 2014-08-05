package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.canBeNull
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName
import se.culvertsoft.mgen.cpppack.generator.impl.HasDefaultCtor

object MkCtorHelper {

  def mkPassToSuper(
    fieldsToSuper: Seq[Field],
    t: ClassType,
    module: Module): String = {

    var passToSuperString = ""

    if (fieldsToSuper.nonEmpty) {
      passToSuperString += s"${CppGenUtils.getSuperTypeString(t)}("
      for (i <- 0 until fieldsToSuper.size) {
        val field = fieldsToSuper(i)
        val isLastField = i + 1 == fieldsToSuper.size
        passToSuperString += field.name
        if (!isLastField) {
          passToSuperString += ", "
        }
      }
      passToSuperString += ")"
    }

    passToSuperString
  }

  def mkReqNonNullFields(fields: Seq[Field]): Seq[String] = {
    fields map { field =>
      s"${isSetName(field)}(${if (field.isRequired) "true" else field.hasDefaultValue})"
    }
  }

  def requiredInitializerListValue(field: Field): Boolean = {
    field.hasDefaultValue ||
      field.isRequired ||
      !HasDefaultCtor(field) ||
      canBeNull(field)
  }

  def mkReqMemberCtorInitListValues(fields: Seq[Field], module: Module): Seq[String] = {
    implicit val currentModule = module
    fields.filter(requiredInitializerListValue) map { field =>
      if (field.isRequired())
        s"m_${field.name()}(${field.name()})"
      else
        s"m_${field.name()}(${MkDefaultValue.apply(field)})"
    }
  }

}