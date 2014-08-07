package se.culvertsoft.mgen.jsonschemaparser

import org.junit.Test
import se.culvertsoft.mgen.compiler.MGenCompiler

class TestCompiler {
  
  @Test
  def test(){
    MGenCompiler.run(Map("project" -> "src/test/resources/project.xml"));
  }

}