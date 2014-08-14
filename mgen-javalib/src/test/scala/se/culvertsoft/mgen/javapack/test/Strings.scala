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
import java.io.InputStream
import gameworld.types.ClassRegistry

class Strings {

  class TestState() {
    val classRegistry = new ClassRegistry
    val classRegEntries = classRegistry.entries()
    val stream = new ByteArrayOutputStream
    val jsonWriter = new JsonWriter(stream, classRegistry)
    val jsonWriterCompact = new JsonWriter(stream, classRegistry, true)
    val jsonPrettyWriter = new JsonPrettyWriter(stream, classRegistry)
    val jsonPrettyWriterCompact = new JsonPrettyWriter(stream, classRegistry, true)
    val jsonReader = new JsonReader(classRegistry)

    val writers = Seq(
      jsonWriter,
      jsonWriterCompact,
      jsonPrettyWriter,
      jsonPrettyWriterCompact)

    def reset() { stream.reset() }
  }

  def mkTestObjects()(implicit state: TestState) = {
    val allObjects = state.classRegEntries.map(_.construct())
    val validObjects = allObjects.filter(_._validate(FieldSetDepth.DEEP))
    val invalidObjects = allObjects.filterNot(_._validate(FieldSetDepth.DEEP))
    (allObjects, validObjects, invalidObjects)
  }

  @Test
  def haveWriters() {
    implicit val state = new TestState()
    assert(state.writers.nonEmpty)
  }

  @Test
  def testCanRead() {
    implicit val state = new TestState()

    val (all, valid, invalid) = mkTestObjects()

    assert(valid.size >= 5)

    for (writer <- state.writers) {
      for (o <- valid) {
        val s = writer.writeObjectToString(o)
        val oBack = state.jsonReader.readObject(s)
        assert(o == oBack)
      }
    }

  }

}
