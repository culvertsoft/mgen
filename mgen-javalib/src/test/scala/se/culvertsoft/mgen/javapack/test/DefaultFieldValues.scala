package se.culvertsoft.mgen.javapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION
import scala.collection.JavaConversions.collectionAsScalaIterable

import org.junit.Test

import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth
import se.culvertsoft.mgen.javapack.serialization.BinaryReader
import se.culvertsoft.mgen.javapack.serialization.BinaryWriter
import se.culvertsoft.mgen.javapack.serialization.BuiltInReader
import se.culvertsoft.mgen.javapack.serialization.BuiltInWriter
import se.culvertsoft.mgen.javapack.serialization.JsonPrettyWriter
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter
import defaultval.model.ClassRegistry
import defaultval.model._

class DefaultFieldValues {

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
  def testDefaultInitialized() {
    implicit val state = new TestState()

    val (all, valid, invalid) = mkTestObjects()

    assert(all.nonEmpty)
    assert(all.forall(_._nFieldsSet(FieldSetDepth.SHALLOW, true) > 0))

    for (writer <- state.writers) {
      all.foreach(o => /*AssertNoThrow*/ (writer.writeObject(o)))
    }

  }

  @Test
  def testWRiteRead() {
    implicit val state = new TestState()

    val (all, valid, invalid) = mkTestObjects()

    assert(all.nonEmpty)
    assert(all.forall(_._nFieldsSet(FieldSetDepth.SHALLOW, true) > 0))

    for (writer <- state.writers) {
      for (o <- all) {
        {
          writer.writeObject(o)
          val reader = getReader(writer)
          val oBack = reader.readObject()
          assert(o == oBack)
          state.reset
        }

        {
          val emptyObject = o.deepCopy()._setAllFieldsSet(false, FieldSetDepth.SHALLOW)
          writer.writeObject(emptyObject)
          assert(emptyObject._nFieldsSet(FieldSetDepth.DEEP, true) == 0)
          val reader = getReader(writer)
          val oBack = reader.readObject()
          assert(o == oBack)
          state.reset
        }

      }

    }

  }

}
