package se.culvertsoft.mgen.compiler.internal

object PrintHelp {

  def apply() {
    println(s"Valid MGen compiler arguments are: ")
    println("  -help: displays this help ")
    println("  -project=\"myProjectFile.xml\": specify project file (Required)")
    println("  -plugin_paths=\"my/external/path1, my/external/path2\": specify additional plugin paths (Optional) ")
    println("  -output_path=\"specify output path (Optional) ")
    println("  -fail_on_missing_generator=true/false: Default false (Optional)")
    println("  -check_conflicts=\"true/false\" (default=true): If false: the compiler will ignore any type name/id/hash conflicts (Optional). Useful for IDL<->IDL translation")
    println("  -include_paths=\"/home/me,/home/you\": The paths to search for project files")
  }

}