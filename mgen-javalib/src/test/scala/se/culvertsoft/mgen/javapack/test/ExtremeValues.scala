package se.culvertsoft.mgen.javapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION

import org.junit.Test

import gameworld.types.ClassRegistry
import gameworld.types.basemodule1.Car
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth
import se.culvertsoft.mgen.javapack.serialization.BinaryReader
import se.culvertsoft.mgen.javapack.serialization.BinaryWriter
import se.culvertsoft.mgen.javapack.serialization.BuiltInReader
import se.culvertsoft.mgen.javapack.serialization.BuiltInWriter
import se.culvertsoft.mgen.javapack.serialization.JsonPrettyWriter
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter

case class Precision(val p: Double)

object FloatsAlmostEqual {
  implicit class DoubleWithAlmostEquals(val d: Double) extends AnyVal {
    def ~=(d2: Double)(implicit p: Precision) = (d - d2).abs <= p.p
  }
}

class ExtremeValues {

  class TestState() {
    val classRegistry = new ClassRegistry
    val classRegEntries = classRegistry.entries()
    val stream = new ByteArrayOutputStream
    val jsonWriter = new JsonWriter(stream, classRegistry)
    val jsonWriterCompact = new JsonWriter(stream, classRegistry, true)
    val jsonPrettyWriter = new JsonPrettyWriter(stream, classRegistry)
    val jsonPrettyWriterCompact = new JsonPrettyWriter(stream, classRegistry, true)
    val binaryWriter = new BinaryWriter(stream, classRegistry)
    val binaryWriterCompact = new BinaryWriter(stream, classRegistry, true)

    val writers = Seq(
      jsonWriter,
      jsonWriterCompact,
      jsonPrettyWriter,
      jsonPrettyWriterCompact,
      binaryWriter,
      binaryWriterCompact)

    def reset() { stream.reset() }
  }

  def getReader(
    writer: BuiltInWriter)(implicit state: TestState): BuiltInReader = {
    writer match {
      case writer: JsonWriter =>
        new JsonReader(new ByteArrayInputStream(state.stream.toByteArray()), state.classRegistry)
      case writer: BinaryWriter =>
        new BinaryReader(new ByteArrayInputStream(state.stream.toByteArray()), state.classRegistry)
    }
  }

  @Test
  def testFixedPoint() {

    implicit val state = new TestState

    for (writer <- state.writers) {
      val car = new Car
      car._setAllFieldsSet(true, FieldSetDepth.DEEP)

      writer.writeObject(car.setId(Long.MaxValue))
      writer.writeObject(car.setId(Long.MinValue))
      writer.writeObject(car.setId(0))
      writer.writeObject(car.setId(1))
      writer.writeObject(car.setId(-1))

      writer.writeObject(car.setTopSpeed(Int.MaxValue))
      writer.writeObject(car.setTopSpeed(Int.MinValue))
      writer.writeObject(car.setTopSpeed(0))
      writer.writeObject(car.setTopSpeed(1))
      writer.writeObject(car.setTopSpeed(-1))

      val reader = getReader(writer)

      assert(reader.readObject(classOf[Car]).getId() == Long.MaxValue)
      assert(reader.readObject(classOf[Car]).getId() == Long.MinValue)
      assert(reader.readObject(classOf[Car]).getId() == 0)
      assert(reader.readObject(classOf[Car]).getId() == 1)
      assert(reader.readObject(classOf[Car]).getId() == -1)

      assert(reader.readObject(classOf[Car]).getTopSpeed() == Int.MaxValue)
      assert(reader.readObject(classOf[Car]).getTopSpeed() == Int.MinValue)
      assert(reader.readObject(classOf[Car]).getTopSpeed() == 0)
      assert(reader.readObject(classOf[Car]).getTopSpeed() == 1)
      assert(reader.readObject(classOf[Car]).getTopSpeed() == -1)

      state.reset

    }

  }

  @Test
  def testFloatingPoint() {
    import FloatsAlmostEqual._
    implicit val p = Precision(1e-5)

    implicit val state = new TestState

    for (writer <- state.writers) {
      val car = new Car
      car._setAllFieldsSet(true, FieldSetDepth.DEEP)

      car.getPositioning().getPosition().setY(0.0f)
      writer.writeObject(car)
      car.getPositioning().getPosition().setY(1.5f)
      writer.writeObject(car)
      car.getPositioning().getPosition().setY(-1.5f)
      writer.writeObject(car)
      car.getPositioning().getPosition().setZ(0.0)
      writer.writeObject(car)
      car.getPositioning().getPosition().setZ(1.5)
      writer.writeObject(car)
      car.getPositioning().getPosition().setZ(-1.5)
      writer.writeObject(car)

      val reader = getReader(writer)

      assert(reader.readObject(classOf[Car]).getPositioning().getPosition().getY() ~= -0.0f)
      assert(reader.readObject(classOf[Car]).getPositioning().getPosition().getY() ~= 1.5f)
      assert(reader.readObject(classOf[Car]).getPositioning().getPosition().getY() ~= -1.5f)

      assert(reader.readObject(classOf[Car]).getPositioning().getPosition().getZ() ~= -0.0)
      assert(reader.readObject(classOf[Car]).getPositioning().getPosition().getZ() ~= 1.5)
      assert(reader.readObject(classOf[Car]).getPositioning().getPosition().getZ() ~= -1.5)

      state.reset

    }

  }

}