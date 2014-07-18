package se.culvertsoft.mgen.javapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable.ArrayBuffer
import org.junit.Test
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
import se.culvertsoft.testmodule.ClassRegistry

class BasicReadWrite {

  val modelName = "write"
  val dataFolder = s"../../generated/${modelName}/data_generated"

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

  def getSerializerNames() = {
    Seq(
      "json",
      "jsonCompact",
      "jsonPretty",
      "jsonPrettyCompact",
      "binary",
      "binaryCompact")
  }

  def getReader(registry: ClassRegistryBase, serializerName: String, data: Array[Byte]): BuiltInReader = {
    val stream = new ByteArrayInputStream(data)
    if (serializerName.startsWith("json")) {
      new JsonReader(stream, registry)
    } else if (serializerName.startsWith("binary")) {
      new BinaryReader(stream, registry)
    } else {
      null
    }
  }

  def file2bytes(path: String): Array[Byte] = {
    Files.readAllBytes(Paths.get(path))
  }

  def getEmptyObjectsData(serializerName: String): Array[Byte] = {
    file2bytes(s"$dataFolder/emptyObjects_$serializerName.data")
  }

  def getRandomizedObjectsData(serializerName: String): Array[Byte] = {
    file2bytes(s"$dataFolder/randomizedObjects_$serializerName.data")
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

  @Test
  def testEmptyObjects() {

    val gatheredObjects = new ArrayBuffer[MGenBase]

    {

      val registry = new ClassRegistry
      val sNames = getSerializerNames()

      for (sName <- sNames) {

        val srcData = getEmptyObjectsData(sName)
        val reader = getReader(registry, sName, srcData)

        for (e <- registry.entries) {
          val o1 = reader.readObject()
          val o2 = o1.deepCopy()
          assert(o1._validate(FieldSetDepth.DEEP))
          assert(o2._validate(FieldSetDepth.DEEP))
          assert(o1 == o2)
          assert(o1.hashCode() == o2.hashCode())
          gatheredObjects += o1
          // Not possible since map order might change
          // assert(o.toString() == o2.toString())
        }

        AssertThrows(reader.readObject())

      }

    }

    val registry = new ClassRegistry

    val stream = new ByteArrayOutputStream
    val writers = getWriters(stream, registry)

    for (writer <- writers) {

      for (o1 <- gatheredObjects) {
        writer.writeObject(o1)
        val reader = getReader(writer, registry, stream.toByteArray())
        val o2 = reader.readObject()

        assert(o1._validate(FieldSetDepth.DEEP))
        assert(o2._validate(FieldSetDepth.DEEP))
        assert(o1 == o2)
        assert(o1.hashCode() == o2.hashCode())

        AssertThrows(reader.readObject())
        stream.reset()
      }

    }

  }

  @Test
  def testRandomizedObjects() {

    val gatheredObjects = new ArrayBuffer[MGenBase]

    {
      val registry = new ClassRegistry
      val sNames = getSerializerNames()

      for (sName <- sNames) {

        val srcData = getRandomizedObjectsData(sName)
        val reader = getReader(registry, sName, srcData)

        for (e <- registry.entries) {
          val o1 = reader.readObject()
          val o2 = o1.deepCopy()
          assert(o1._validate(FieldSetDepth.DEEP))
          assert(o2._validate(FieldSetDepth.DEEP))
          assert(o1 == o2)
          assert(o1.hashCode() == o2.hashCode())
          gatheredObjects += o1
          // Not possible since map order might change
          // assert(o.toString() == o2.toString())
        }

        AssertThrows(reader.readObject())

      }
    }

    val registry = new ClassRegistry

    val stream = new ByteArrayOutputStream
    val writers = getWriters(stream, registry)

    for (writer <- writers) {

      for (o1 <- gatheredObjects) {

        writer.writeObject(o1)
        val reader = getReader(writer, registry, stream.toByteArray())
        val o2 = reader.readObject()

        assert(o1._validate(FieldSetDepth.DEEP))
        assert(o2._validate(FieldSetDepth.DEEP))

        if (o1 != o2) {
          Files.write(Paths.get("o1.txt"), o1.toString().getBytes())
          Files.write(Paths.get("o2.txt"), o2.toString().getBytes())
        }

        assert(o1 == o2)
        assert(o1.hashCode() == o2.hashCode())

        AssertThrows(reader.readObject())
        stream.reset()
      }

    }

  }

}