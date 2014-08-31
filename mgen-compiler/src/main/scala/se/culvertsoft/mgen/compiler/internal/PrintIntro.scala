package se.culvertsoft.mgen.compiler.internal

object PrintIntro {

  private def nChars(n: Int, c: Character): String = {
    val sb = new StringBuilder
    for (i <- 0 until n)
      sb.append(c)
    sb.toString
  }
  private def nStars(n: Int): String = {
    nChars(n, '*')
  }

  private def nSpaces(n: Int): String = {
    nChars(n, ' ')
  }

  def apply(version: String) {
    val stars = nStars(version.size)
    val spaces = nSpaces(version.size)
    println(s"                                           ")
    println(s"    *****************${stars}****************")
    println(s"    **               ${spaces}              **")
    println(s"    **               ${spaces}              **")
    println(s"    **        MGen Compiler $version       **")
    println(s"    **               ${spaces}              **")
    println(s"    *****************${stars}****************")
    println(s"                                           ")
  }

}