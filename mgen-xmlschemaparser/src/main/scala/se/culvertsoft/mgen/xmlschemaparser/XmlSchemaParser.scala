package se.culvertsoft.mgen.xmlschemaparser

import java.io.File
import java.io.FileInputStream

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaIterator

import org.xml.sax.InputSource

import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JPackage
import com.sun.tools.xjc.api.XJC

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Parser
import se.culvertsoft.mgen.xmlschemaparser.RichJDefinedClass.rdclass

class XmlSchemaParser extends Parser {

  override def parse(
    sources: java.util.List[File],
    settings: java.util.Map[String, String],
    parent: Project) {

    // Taken from:
    // http://stackoverflow.com/questions/4248099/dynamically-generate-java-sources-without-xjc

    for (file <- sources) {

      // Creating a new compiler for each file is unfortunately required
      val compiler = XJC.createSchemaCompiler()
      val forcedPackage = settings.get("replace_module_path")

      if (forcedPackage != null)
        compiler.forcePackageName(forcedPackage)

      val is = new InputSource(new FileInputStream(file))
      is.setSystemId(file.getAbsolutePath)

      compiler.parseSchema(is)
      val intermediateModel = compiler.bind()
      val jCodeModel = intermediateModel.generateCode(null, null);

      for (pkg <- jCodeModel.packages) {

        val moduleName = pkg.name

        if (moduleName == null)
          throw new AnalysisException(s"Null module/package name provided to $this. Need to specify a package name or use the 'replace_module_path' ")

        val dir = new File(parent.absoluteFilePath).getParent
        val fileName = pkg.name() + ".xml"
        val module = parent.getOrCreateModule(pkg.name(), fileName, dir + "/" + fileName, settings);

        for (typ <- pkg.classes) {
          if (typ.isClass) {
            module.addClass(cvtClass(typ, module, pkg))
          } else if (typ.isEnum) {
            module.addEnum(cvtEnum(typ, module, pkg))
          }
        }
      }

    }

  }

  def cvtClass(typ: JDefinedClass, module: Module, pkg: JPackage): ClassType = {
    ???
  }

  def cvtEnum(typ: JDefinedClass, module: Module, pkg: JPackage): EnumType = {
    ???
  }

}