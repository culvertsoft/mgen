package se.culvertsoft.mgen.jspack.generator

import scala.collection.JavaConversions.asScalaBuffer
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.impl.GeneratedSourceFileImpl
import se.culvertsoft.mgen.api.plugins.Generator
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer.SuperStringBuffer2String
import se.culvertsoft.mgen.api.model.Project

class JavascriptGenerator extends Generator {

  implicit val txtBuffer = new SuperStringBuffer

  override def generate(project: Project, generatorSettings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {
 
    val modules = project.allModulesRecursively()
    val filePath = MkFilePath(generatorSettings)

    MkIntro(generatorSettings)
    MkModuleClassRegistry(modules)
    MkModuleHashRegistry(modules)
    MkOutro(generatorSettings)

    val out = new java.util.ArrayList[GeneratedSourceFile]
    out.add(new GeneratedSourceFileImpl(filePath, txtBuffer))
    out
  }
}