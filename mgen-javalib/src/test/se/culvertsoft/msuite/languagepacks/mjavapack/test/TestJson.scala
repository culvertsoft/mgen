package se.culvertsoft.msuite.languagepacks.mjavapack.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import org.json.simple.JSONValue
import org.junit.Test

import gameworld.types.basemodule1.Car
import gameworld.types.basemodule1.GarageViewer
import gameworld.types.basemodule1.Positioning
import gameworld.types.basemodule1.VectorR3
import gameworld.types.basemodule1.Vehicle
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter

class TestJson {

   @Test
   def modifiedObject() {

      val bos = new ByteArrayOutputStream
      val registry = new gameworld.types.ClassRegistry
      val writer = new JsonWriter(bos, registry)

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

      writer.writeMGenObject(car)

      val carStr = new String(bos.toByteArray())

      println(carStr)

      val carJson = JSONValue.parse(carStr)

      println(carJson)

      val garage = new GarageViewer

      bos.reset()
      val vehicles = new Array[Vehicle](3)
      vehicles(0) = car
      vehicles(1) = car
      vehicles(2) = car
      garage.setVehicles(vehicles)

      writer.writeMGenObject(garage)

      val garageStr = new String(bos.toByteArray())

      println(garageStr)

      val garageJson = JSONValue.parse(garageStr)

      println(garageJson)

      val reader = new JsonReader(new ByteArrayInputStream(bos.toByteArray()), registry)

      val readBackObj = reader.readMGenObject()

      println(readBackObj)

   }
}