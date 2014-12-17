package se.culvertsoft.mgen.compiler.components

import se.culvertsoft.mgen.compiler.internal.PrintHelp
import se.culvertsoft.mgen.compiler.util.ParseKeyValuePairs

object ParseCmdLineArgs {

  def apply(cmdLineArgs: Array[String]): Map[String, String] = {

    // Parse input parameters
    val params_raw = cmdLineArgs.map(_.trim()).toBuffer
    if (params_raw.isEmpty || params_raw.contains("-help")) {
      PrintHelp()
      return Map.empty
    }

    // Parse explicit key-value arguments
    val params = params_raw.filter(_.contains("="))
    val settings_keyVal = ParseKeyValuePairs(params)

    // Grab any implicitly assigned arguments
    if (settings_keyVal.contains("project")) {
      settings_keyVal
    } else {
      settings_keyVal + ("project" -> params_raw(0))
    }
  }
}