package se.culvertsoft.mgen.compiler.components

import se.culvertsoft.mgen.api.model.Project

object CreateProject {

  def apply(settings: Map[String, String], pluginFinder: PluginFinder): Project = {

    val checkForConflicts = settings.get("check_conflicts").map(_.toBoolean).getOrElse(true)

    // Parse the project shallow
    println("Parsing project...")
    val project = ParseProject(settings, pluginFinder)
    println("ok\n")

    // Link custom types and enums
    print("Linking types...")
    LinkTypes(project)
    println("ok\n")

    // Check for type conflicts (ids, names, hashes etc)
    if (checkForConflicts) {
      print("Checking for type conflicts...")
      CheckConflicts(project)
      println("ok\n")
    }

    project

  }
}