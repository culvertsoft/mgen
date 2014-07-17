package se.culvertsoft.mgen.javapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import scala.collection.JavaConversions.collectionAsScalaIterable
import org.junit.Test
import se.culvertsoft.ClassRegistry
import se.culvertsoft.mgen.javapack.serialization.BinaryReader
import se.culvertsoft.mgen.javapack.serialization.BinaryWriter
import se.culvertsoft.mgen.javapack.serialization.BuiltInReader
import se.culvertsoft.mgen.javapack.serialization.BuiltInWriter
import se.culvertsoft.mgen.javapack.serialization.JsonPrettyWriter
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth

class BasicReadWrite {

  val registry = new ClassRegistry

  def getReader(writer: BuiltInWriter, bytes: Array[Byte]): BuiltInReader = {
    writer match {
      case writer: JsonWriter =>
        new JsonReader(new ByteArrayInputStream(bytes), registry)
      case writer: BinaryWriter =>
        new BinaryReader(new ByteArrayInputStream(bytes), registry)
    }
  }

  def getWriters(stream: OutputStream) = {
    Seq(
      new JsonWriter(stream, registry, false),
      new JsonWriter(stream, registry, true),
      new JsonPrettyWriter(stream, registry, false),
      new JsonPrettyWriter(stream, registry, true),
      new BinaryWriter(stream, registry, false),
      new BinaryWriter(stream, registry, true))
  }

  @Test
  def canCreateAllTypes() {
    assert(registry.entries.nonEmpty)
    for (e <- registry.entries) {
      val instance = e.construct()
      assert(instance != null)
      assert(instance == instance)
    }
  }

  @Test
  def canCopyAllTypes() {
    for (e <- registry.entries) {
      val instance = e.construct()
      val instance2 = instance.deepCopy()
      assert(instance == instance2)
    }
  }

  @Test
  def canWriteReadAllTypes() {
    val stream = new ByteArrayOutputStream
    val writers = getWriters(stream)

    for (writer <- writers) {
      println(writer)

      stream.reset()

      for (e <- registry.entries) {
        val instance = e.construct()
        instance._setAllFieldsSet(true, FieldSetDepth.DEEP)
        writer.writeObject(instance)
      }

      val reader = getReader(writer, stream.toByteArray())
      for (e <- registry.entries) {
        val o = reader.readObject()
      }

    }

  }

}