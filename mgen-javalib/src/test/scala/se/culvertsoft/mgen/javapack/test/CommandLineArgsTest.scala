package se.culvertsoft.mgen.javapack.test

import org.junit.Test

import gameworld.types.ClassRegistry
import gameworld.types.basemodule1.Car
import se.culvertsoft.mgen.javapack.serialization.CommandLineArgHelp
import se.culvertsoft.mgen.javapack.serialization.CommandLineArgParser

class CommandLineArgstest {
  import CommandLineArgstest._

  @Test
  def canPrintHelp() {

    println(s"Input parameters for type ${classOf[Car]} : \n")
    val help = new CommandLineArgHelp(classOf[Car])
    println(help)
  }

  @Test
  def canCreateObjectFromCmdLineArgs() {

    val parser = new CommandLineArgParser(classOf[Car], classRegistry)

  }

}

object CommandLineArgstest {

  val classRegistry = new ClassRegistry
  val cls = classOf[Car]

  def main(args: Array[String]) {
    val parser = new CommandLineArgParser(classOf[Car], classRegistry)
    parser.parse(args)
  }
}