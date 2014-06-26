package se.culvertsoft.mgen.jspack.generator

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.{GeneratedSourceFile, Generator}
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._

class JavascriptGenerator extends Generator {

  implicit val txtBuffer = new SuperStringBuffer

  override def generate(modules: java.util.List[Module], generatorSettings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {

    val filePath = MkFilePath(generatorSettings)

    MkIntro(generatorSettings)
    MkModuleClassRegistry(modules)
    MkModuleHashRegistry(modules)
    MkOutro(generatorSettings)

    val out = new java.util.ArrayList[GeneratedSourceFile]
    out.add(new GeneratedSourceFile(filePath, txtBuffer))
    out
  }
}