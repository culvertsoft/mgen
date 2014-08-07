package se.culvertsoft.mgen.compiler.internal

object PrintIntro {

  def apply(version: Any) {
    println(s"                                           ")
    println(s"    ***************************************")
    println(s"    **                                   **")
    println(s"    **                                   **")
    println(s"    **        MGen Compiler v$version         **")
    println(s"    **                                   **")
    println(s"    ***************************************")
    println(s"                                           ")
  }

}