package se.culvertsoft.mgen.javapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream

import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable.ArrayBuffer

import org.junit.Test

import se.culvertsoft.ClassRegistry
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase
import se.culvertsoft.mgen.javapack.classes.EmptyClassRegistry
import se.culvertsoft.mgen.javapack.classes.MGenBase
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth
import se.culvertsoft.mgen.javapack.serialization.BinaryReader
import se.culvertsoft.mgen.javapack.serialization.BinaryWriter
import se.culvertsoft.mgen.javapack.serialization.BuiltInReader
import se.culvertsoft.mgen.javapack.serialization.BuiltInWriter
import se.culvertsoft.mgen.javapack.serialization.JsonPrettyWriter
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter

class BasicReadWrite {

  def getReader(writer: BuiltInWriter, registry: ClassRegistryBase, bytes: Array[Byte]): BuiltInReader = {
    writer match {
      case writer: JsonWriter =>
        new JsonReader(new ByteArrayInputStream(bytes), registry)
      case writer: BinaryWriter =>
        new BinaryReader(new ByteArrayInputStream(bytes), registry)
    }
  }

  def getWriters(stream: OutputStream, registry: ClassRegistryBase) = {
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
    val registry = new ClassRegistry

    assert(registry.entries.nonEmpty)
    for (e <- registry.entries) {
      val instance = e.construct()
      assert(instance != null)
      assert(instance == instance)
    }
  }

  @Test
  def canCopyAllTypes() {
    val registry = new ClassRegistry
    for (e <- registry.entries) {
      val instance = e.construct()
      val instance2 = instance.deepCopy()
      assert(instance == instance2)
    }
  }

  @Test
  def canWriteReadAllTypes() {

    val registry = new ClassRegistry

    val stream = new ByteArrayOutputStream
    val writers = getWriters(stream, registry)

    for (writer <- writers) {

      stream.reset()

      val written = new ArrayBuffer[MGenBase]
      val readBack = new ArrayBuffer[MGenBase]

      for (e <- registry.entries) {
        val instance = e.construct()
        instance._setAllFieldsSet(true, FieldSetDepth.DEEP)
        writer.writeObject(instance)
        written += instance
      }

      val reader = getReader(writer, registry, stream.toByteArray())
      for (e <- registry.entries) {
        readBack += reader.readObject()
      }

      assert(written == readBack)

    }

  }

  @Test
  def canSkipAllTypes() {

    val registryWithTypes = new ClassRegistry
    val registry = new EmptyClassRegistry

    val stream = new ByteArrayOutputStream
    val writers = getWriters(stream, registry)

    for (writer <- writers) {

      for (e <- registryWithTypes.entries) {

        val instance = e.construct()

        instance._setAllFieldsSet(true, FieldSetDepth.DEEP)

        writer.writeObject(instance)
        val reader = getReader(writer, registry, stream.toByteArray())
        reader.readObject()
        AssertThrows(reader.readObject())
        stream.reset()
      }

    }
  }

}