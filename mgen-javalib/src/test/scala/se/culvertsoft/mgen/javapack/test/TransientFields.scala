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
import trans.testmodel.ClassRegistry
import trans.testmodel.MyType

class TransientFields {

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

    for (writer <- state.writers) {
      valid.foreach(o => /*AssertNoThrow*/ (writer.writeObject(o)))
    }

  }

  @Test
  def checkTransientFieldsNotWritten() {
    implicit val state = new TestState()

    val o = new MyType
    o._setAllFieldsSet(true, FieldSetDepth.DEEP)

    for (writer <- state.writers) {
      writer.writeObject(o)
      val oBack = getReader(writer).readObject(classOf[MyType])
      assert(o._nFieldsSet(FieldSetDepth.SHALLOW, true) != 0)
      assert(oBack._nFieldsSet(FieldSetDepth.SHALLOW, true) != 0)
      assert(o._nFieldsSet(FieldSetDepth.SHALLOW, true) > oBack._nFieldsSet(FieldSetDepth.SHALLOW, true))
      state.reset()
    }

  }

}
