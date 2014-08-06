package se.culvertsoft.mgen.xmlschemaparser

import com.sun.codemodel.JClass
import com.sun.codemodel.JClassContainer
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JEnumConstant
import com.sun.codemodel.JFieldVar
import com.sun.codemodel.JMods

object RichJDefinedClass {

  private val cls = classOf[JDefinedClass]

  implicit class rdclass(base: JDefinedClass) {

    private def get[T](fieldName: String): T = {
      val nameField = cls.getDeclaredField("name")
      nameField.setAccessible(true)
      nameField.get(base).asInstanceOf[T]
    }

    def getName() = {
      get[String]("name")
    }

    def getSuper() = {
      get[JClass]("superClass")
    }

    def getModifiers() = {
      get[JMods]("mods")
    }

    def getFields() = {
      get[java.util.Map[String, JFieldVar]]("fields")
    }

    def getParent(): JClassContainer = {
      get[JClassContainer]("outer")
    }

    def getEnumConstants() = {
      get[java.util.Map[String, JEnumConstant]]("enumConstantsByName")
    }

    def getInnerClasses() = {
      get[JDefinedClass]("classes")
    }

  }
}