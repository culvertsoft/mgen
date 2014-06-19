package se.culvertsoft.msuite.languagepacks.mjavapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import scala.collection.JavaConversions.collectionAsScalaIterable

import org.junit.Test

import gameworld.types.basemodule1.Car
import gameworld.types.basemodule1.Positioning
import gameworld.types.basemodule1.VectorR3
import se.culvertsoft.mgen.api.util.internal.ByteArrayOutputStreamExposed
import se.culvertsoft.mgen.javapack.serialization.BinaryReader
import se.culvertsoft.mgen.javapack.serialization.BinaryWriter

class RWTest {

   @Test
   def modifiedObject() {

      val bos = new ByteArrayOutputStream
      val registry = new gameworld.types.ClassRegistry
      val writer = new BinaryWriter(bos, registry)

      val car = new Car
      
      car.setPositioning(new Positioning())
      car.getPositioning().setPosition(new VectorR3())
      car.getPositioning().setVelocity(new VectorR3())
      car.getPositioning().setAcceleration(new VectorR3())
      car.getPositioning().getAcceleration().setX(1).setY(2).setZ(3)
      car.getPositioning().getPosition().setX(1).setY(2).setZ(3)
      car.getPositioning().getVelocity().setX(1).setY(2).setZ(3)
      car.setTopSpeed(12345)
      car.setBrand("abv")

      writer.writeMGenObject(car, true)

      println(bos.toByteArray().length)

      println(new String(bos.toByteArray()))

      val bis = new ByteArrayInputStream(bos.toByteArray())

      val reader = new BinaryReader(bis, registry)

      val out = reader.readMGenObject().asInstanceOf[Car]

      println(car)
      println(out)

      assert(car == out)

      out.setBrand("Fail")

      assert(car != out)

   }

   @Test
   def allTestClasses() {

      val bos = new ByteArrayOutputStreamExposed(10 * 1024)
      val bis = new ByteArrayInputStream(bos.backingArray())
      val registry = new gameworld.types.ClassRegistry
      val writer = new BinaryWriter(bos, registry)
      val reader = new BinaryReader(bis, registry)

      assert(registry.entries().size() > 6)

      val t0 = System.nanoTime() / 1e9
      var size = 0L;
      var n = 0;
      while (n < 10000000) {

         for (cls <- registry.entries()) {

            val original = cls.construct()
            writer.writeMGenObject(original, true)
            val readBack = reader.readMGenObject()

            assert(original == readBack)

            size += bos.size()

            bos.reset()
            bis.reset()

            n += 1;

         }

      }
      val t1 = System.nanoTime() / 1e9

      println("Byte count: " + size)
      println("MB count: " + size / 1e6)
      println("Msgs/sec " + (n.toDouble) / (t1 - t0))

   }
}

