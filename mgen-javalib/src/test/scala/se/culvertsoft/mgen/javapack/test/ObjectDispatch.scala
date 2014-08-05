package se.culvertsoft.mgen.javapack.test

import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable.ArrayBuffer

import org.junit.Test

import gameworld.dependency.depmodule1.DepCar
import gameworld.types.ClassRegistry
import gameworld.types.Dispatcher.dispatch
import gameworld.types.Handler
import gameworld.types.basemodule1.Car
import gameworld.types.basemodule1.Entity
import gameworld.types.basemodule1.GarageViewer
import gameworld.types.basemodule1.Vehicle
import se.culvertsoft.mgen.javapack.classes.MGenBase

class ClassIdentification {

  val classRegistry = new ClassRegistry
  val classRegEntries = classRegistry.entries()

  @Test
  def testHaveEnoughTestMaterial() {
    assert(classRegEntries.size >= 10)
  }

  @Test
  def testNoClassUnknown() {

    val objects = classRegEntries.map(_.construct())

    val handler = new Handler() {
      override def handleUnknown(o: MGenBase) {
        assert(false)
      }
    }

    objects foreach (dispatch(_, handler))

  }

  @Test
  def testCallToSuper() {

    var discardOk: Boolean = false
    var vehicleOk: Boolean = false
    var carOk: Boolean = false

    val car = new Car

    val discardHandler = new Handler() {
      override def handleDiscard(o: MGenBase) {
        discardOk = true
      }
    }

    val vehicleHandler = new Handler() {
      override def handle(v: Vehicle) {
        vehicleOk = true
      }
    }

    val carHandler = new Handler() {
      override def handle(v: Car) {
        carOk = true
      }
    }

    dispatch(car, discardHandler)
    dispatch(car, vehicleHandler)
    dispatch(car, carHandler)

    assert(discardOk)
    assert(vehicleOk)
    assert(carOk)

  }

  @Test
  def testIdentifyAll() {

    val in = List(
      new Car,
      new Vehicle,
      new GarageViewer,
      new Entity,
      new DepCar)

    val out = new ArrayBuffer[MGenBase]

    val handler = new Handler() {
      override def handle(o: Car) { out += o }
      override def handle(o: Vehicle) { out += o }
      override def handle(o: GarageViewer) { out += o }
      override def handle(o: Entity) { out += o }
      override def handle(o: DepCar) { out += o }
    }

    in foreach (dispatch(_, handler))

    assert(in == out)

  }

}