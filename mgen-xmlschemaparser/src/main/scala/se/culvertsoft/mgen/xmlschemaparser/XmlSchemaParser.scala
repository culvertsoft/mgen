package se.culvertsoft.mgen.xmlschemaparser

import java.io.File
import java.io.FileInputStream

import scala.collection.JavaConversions.asScalaBuffer

import org.xml.sax.InputSource

import com.sun.tools.xjc.api.XJC

import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Parser

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
    }

  }

}