package se.culvertsoft.mgen.javapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import scala.collection.JavaConversions.collectionAsScalaIterable

import org.junit.Test

import gameworld.types.ClassRegistry
import gameworld.types.basemodule1.Car
import gameworld.types.basemodule1.Item
import gameworld.types.basemodule1.VectorR3
import gameworld.types.basemodule1.Vehicle
import gameworld.types.basemodule1.World
import gameworld.types.basemodule1.kind
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth
import se.culvertsoft.mgen.javapack.serialization.BinaryReader
import se.culvertsoft.mgen.javapack.serialization.BinaryWriter
import se.culvertsoft.mgen.javapack.serialization.BuiltInReader
import se.culvertsoft.mgen.javapack.serialization.BuiltInWriter
import se.culvertsoft.mgen.javapack.serialization.JsonPrettyWriter
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter

class ObjectSerialization {

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

  def mkTestObjects()(implicit state: TestState) = {
    val allObjects = state.classRegEntries.map(_.construct())
    val validObjects = allObjects.filter(_._validate(FieldSetDepth.DEEP))
    val invalidObjects = allObjects.filterNot(_._validate(FieldSetDepth.DEEP))
    (allObjects, validObjects, invalidObjects)
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
  def haveWriters() {
    implicit val state = new TestState()
    assert(state.writers.nonEmpty)
  }

  @Test
  def testCanWrite() {
    implicit val state = new TestState()

    val (all, valid, invalid) = mkTestObjects()

    assert(valid.nonEmpty)
    assert(invalid.nonEmpty)

    for (writer <- state.writers) {
      valid.foreach(o => /*AssertNoThrow*/ (writer.writeObject(o)))
      invalid.foreach(o => AssertThrows(writer.writeObject(o)))
    }

  }

  @Test
  def testMap() {
    implicit val state = new TestState()

    val car = new Car
    val world = new World
    world._setAllFieldsSet(true, FieldSetDepth.DEEP)
    car._setAllFieldsSet(true, FieldSetDepth.DEEP)
    world.getEntities().put(1, car)

    state.jsonPrettyWriterCompact.writeObject(world)

    val reader = getReader(state.jsonPrettyWriterCompact)
    val worldBack = reader.readObject()
    
    assert(world == worldBack)

  }

  @Test
  def testCanRead() {
    implicit val state = new TestState()

    val (all, valid, invalid) = mkTestObjects()

    all foreach (_._setAllFieldsSet(true, FieldSetDepth.DEEP))

    for (writer <- state.writers) {

      for (o <- all) {
        writer.writeObject(o)
      }

      val reader = getReader(writer)

      for (written <- all) {
        val readBack = reader.readObject()
        assert(written == readBack)
      }

      state.reset()
    }

  }

  @Test
  def testExplicitReadType() {
    implicit val state = new TestState()

    val (all, valid, invalid) = mkTestObjects()

    all foreach (_._setAllFieldsSet(true, FieldSetDepth.DEEP))

    val car = new Car
    car._setAllFieldsSet(true, FieldSetDepth.DEEP)

    for (writer <- state.writers) {

      state.reset()

      {
        writer.writeObject(car)
        val reader = getReader(writer)
        val carBack = reader.readObject(classOf[Car])
        assert(carBack == car)
        state.reset()
      }

      {
        writer.writeObject(car)
        val reader = getReader(writer)
        val carBack = reader.readObject(classOf[Vehicle])
        assert(carBack == car)
        state.reset()
      }

      {
        writer.writeObject(car)
        val reader = getReader(writer)
        AssertThrows(reader.readObject(classOf[Item]))
        state.reset()
      }

    }

  }

  @Test
  def testEnums() {
    implicit val state = new TestState()

    val v1 = new VectorR3
    val v2 = new VectorR3
    v1._setAllFieldsSet(true, FieldSetDepth.DEEP)
    v2._setAllFieldsSet(true, FieldSetDepth.DEEP)

    assert(v1 == v2)
    v1.setKind(kind.ugly)
    assert(v1 != v2)

    for (o <- List(v1, v2)) {

      for (writer <- state.writers) {

        state.reset()

        {
          writer.writeObject(o)
          val reader = getReader(writer)
          val oBack = reader.readObject(classOf[VectorR3])
          assert(oBack == o)
          state.reset()
        }

      }
    }

  }

}
