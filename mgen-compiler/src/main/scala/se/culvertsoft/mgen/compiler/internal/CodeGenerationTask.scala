package se.culvertsoft.mgen.compiler.internal

import se.culvertsoft.mgen.api.model.GeneratedSourceFile

object CodeGenerationTask {

  def apply(expr: => GeneratedSourceFile): CodeGenerationTask = {
    new CodeGenerationTask() {
      override def apply(): GeneratedSourceFile = {
        expr
      }
    }
  }

}

trait CodeGenerationTask {
  def apply(): GeneratedSourceFile
}