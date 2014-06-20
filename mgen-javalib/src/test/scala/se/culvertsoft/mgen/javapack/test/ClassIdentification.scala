package se.culvertsoft.mgen.javapack.test

import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable.ArrayBuffer
import org.junit.Test
import gameworld.dependency.depmodule1.DepCar
import gameworld.types.ClassRegistry
import gameworld.types.basemodule1.Car
import gameworld.types.basemodule1.Entity
import gameworld.types.basemodule1.GarageViewer
import gameworld.types.basemodule1.Vehicle
import se.culvertsoft.mgen.javapack.classes.MGenBase
import gameworld.types.Dispatcher

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

    val ident = new Dispatcher() {

      override def handleUnknown(o: MGenBase) {
        assert(false)
      }

      override def handle(car: Car) {

      }
    }

    objects foreach ident.dispatch

  }

  @Test
  def testCallToSuper() {

    var discardOk: Boolean = false
    var vehicleOk: Boolean = false
    var carOk: Boolean = false

    val car = new Car

    val identDiscard = new Dispatcher() {
      override def handleDiscard(o: MGenBase) {
        discardOk = true
      }
    }

    val identVehicle = new Dispatcher() {
      override def handle(v: Vehicle) {
        vehicleOk = true
      }
    }

    val identCar = new Dispatcher() {
      override def handle(v: Car) {
        carOk = true
      }
    }

    identDiscard.dispatch(car)
    identVehicle.dispatch(car)
    identCar.dispatch(car)

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

    val ident = new Dispatcher() {
      override def handle(o: Car) { out += o }
      override def handle(o: Vehicle) { out += o }
      override def handle(o: GarageViewer) { out += o }
      override def handle(o: Entity) { out += o }
      override def handle(o: DepCar) { out += o }
    }

    in foreach ident.dispatch

    assert(in == out)

  }

}