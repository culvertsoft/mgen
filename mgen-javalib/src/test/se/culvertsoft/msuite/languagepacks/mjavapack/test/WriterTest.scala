package se.culvertsoft.msuite.languagepacks.mjavapack.test

import java.io.ByteArrayOutputStream

import org.junit.Test

import gameworld.types.basemodule1.Car
import se.culvertsoft.mgen.api.util.internal.XmlPrettifier
import se.culvertsoft.mgen.javapack.serialization.XmlWriter

class WriterTest {

   @Test
   def writeXml() {

      val bos = new ByteArrayOutputStream
      val registry = new gameworld.types.ClassRegistry
      val useHashes = true

      val xmlWriter = new XmlWriter(bos, registry, useHashes)

      xmlWriter.writeMgenObject("TheCar", new Car)

      println(new String(bos.toByteArray()))

      println(XmlPrettifier.prettyPrint(bos.toByteArray()))

   }

}

