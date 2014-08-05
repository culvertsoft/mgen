package se.culvertsoft.mgen.compiler.util

object ParseKeyValuePairs {

  def apply(params: Seq[String]): Map[String, String] = {

    print("Parsing command line args...")
    try {
      val settings = params.map(_.split("="))
        .map(arr => (
          trimKeyVal(arr(0).filter(_ != '-').toLowerCase()),
          trimKeyVal(arr(1))))
        .toMap
      println("ok")
      for ((key, value) <- settings) {
        println(s"  $key: $value")
      }
      println("")
      settings
    } catch {
      case t: Exception =>
        throw new RuntimeException("Failed parsing key-value pairs from command line arguments", t)
    }
  }

  private def trimKeyVal(in: String): String = {

    if (in == null)
      return ""

    val out = in.trim
    if (out.startsWith("\"") && out.endsWith("\""))
      out.substring(1, out.length() - 1)
    else
      out
  }

}