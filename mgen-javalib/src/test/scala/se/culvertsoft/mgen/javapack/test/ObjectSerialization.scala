package se.culvertsoft.mgen.javapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import scala.collection.JavaConversions.collectionAsScalaIterable

import org.junit.Test

import gameworld.types.ClassRegistry
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth
import se.culvertsoft.mgen.javapack.serialization.BinaryReader
import se.culvertsoft.mgen.javapack.serialization.BinaryWriter
import se.culvertsoft.mgen.javapack.serialization.BuiltInReader
import se.culvertsoft.mgen.javapack.serialization.BuiltInWriter
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter

class ObjectSerialization {

  class TestState() {
    val classRegistry = new ClassRegistry
    val classRegEntries = classRegistry.entries()
    val stream = new ByteArrayOutputStream
    val jsonWriter = new JsonWriter(stream, classRegistry)
    val binaryWriter = new BinaryWriter(stream, classRegistry)
    val writers = Seq( /*jsonWriter, */ binaryWriter)
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

    state.stream.reset()

    val (all, valid, invalid) = mkTestObjects()

    assert(valid.nonEmpty)
    assert(invalid.nonEmpty)

    for (writer <- state.writers) {
      valid.foreach(o => AssertNoThrow(writer.writeMGenObject(o)))
      invalid.foreach(o => AssertThrows(writer.writeMGenObject(o)))
    }

  }

  @Test
  def testCanRead() {
    implicit val state = new TestState()

    state.stream.reset()

    val (all, valid, invalid) = mkTestObjects()

    all foreach (_._setAllFieldsSet(true, FieldSetDepth.DEEP))

    for (writer <- state.writers) {

      for (o <- all) {
        writer.writeMGenObject(o)
      }

      val reader = getReader(writer)

      for (o <- all) {
        val oBack = reader.readMGenObject()
      }

      state.stream.reset()
    }

  }

}