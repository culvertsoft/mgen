package se.culvertsoft.mgen.pythongenerator

import se.culvertsoft.mgen.compiler.internal.FancyHeaders

object PythonConstants {

  val fileHeader = adapt(FancyHeaders.fileHeader(BuildVersion.GIT_TAG + " " + BuildVersion.GIT_COMMIT_DATE))
  val serializationSectionHeader = adapt(FancyHeaders.serializationSectionHeader)
  val metadataSectionHeader = adapt(FancyHeaders.metadataSectionHeader)

  def adapt(in: String): String = {
    in.map(c => c match {
      case '/' => '#'
      case '*' => '#'
      case c => c
    })
  }

}