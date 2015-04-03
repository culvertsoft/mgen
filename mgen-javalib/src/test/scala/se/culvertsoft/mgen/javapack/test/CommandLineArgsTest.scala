package se.culvertsoft.mgen.javapack.test

import defaultval.model.{MyEnum, BaseWithDefValues}
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
    val car = parser.parse(Array("--brand", "volvo", "--topSpeed", "300"))

    assert(car != null)
    assert(car.getBrand == "volvo")
    assert(car.getTopSpeed == 300)
  }

  @Test
  def enumField(): Unit = {

    val reg = new defaultval.model.ClassRegistry
    val parser = new CommandLineArgParser(classOf[BaseWithDefValues], reg)
    val o = parser.parse(
      Array(
        "--ba", /* "true",*/
        "--bb", "a",
        "--bc", "1",
        "--bd", "356",
        "--be", "100000",
        "--bf", "10000000000",
        "--bg", "1.5",
        "--bh", "1.23456789",
        "--bi", "hejåäö"
      ))

    assert(o != null)
    assert(o.getBa == true)
    assert(o.getBb == MyEnum.a)
    assert(o.getBc == 1)
    assert(o.getBd == 356)
    assert(o.getBe == 100000)
    assert(o.getBf == 10000000000L)
    assert(o.getBg == 1.5)
    assert(o.getBh == 1.23456789)
    assert(o.getBi == "hejåäö")
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