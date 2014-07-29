package se.culvertsoft.mgen.javapack.test

import scala.collection.JavaConversions.collectionAsScalaIterable
import org.junit.Test
import gameworld.types.ClassRegistry
import gameworld.types.basemodule1.Car
import gameworld.types.basemodule1.GarageViewer
import gameworld.types.basemodule1.Vehicle
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth
import gameworld.types.basemodule1.Matrix4x4d

class ObjectManipulation {

  val classRegistry = new ClassRegistry
  val classRegEntries = classRegistry.entries()

  @Test
  def testHaveEnoughTestMaterial() {
    assert(classRegEntries.size >= 10)
  }

  @Test
  def testCompareAfterCreate() {
    for (e <- classRegEntries) {
      val a = e.construct()
      val b = e.construct()
      assert(a == b, "a was not == b")
    }
  }

  @Test
  def testFieldsNotSetAfterCreate() {
    for (e <- classRegEntries) {
      val a = e.construct()
      val b = e.construct()
      for (o <- List(a, b)) {
        if (!o.isInstanceOf[Matrix4x4d]) {
          for (f <- o._fields()) {
            assert(!o._isFieldSet(f, FieldSetDepth.SHALLOW))
          }
        }
      }
    }
  }

  @Test
  def testFieldsCanBeSetShallow() {
    for (e <- classRegEntries) {
      val a = e.construct()
      val b = e.construct()
      if (!a.isInstanceOf[Matrix4x4d]) {
        for (o <- List(a, b)) {
          for (f <- o._fields()) {
            assert(!o._isFieldSet(f, FieldSetDepth.SHALLOW))
          }
          o._setAllFieldsSet(true, FieldSetDepth.SHALLOW)
          for (f <- o._fields()) {
            assert(o._isFieldSet(f, FieldSetDepth.SHALLOW))
          }
        }
      }
    }
  }

  @Test
  def testFieldsCanBeSetDeep() {
    for (e <- classRegEntries) {
      val a = e.construct()
      val b = e.construct()
      if (!a.isInstanceOf[Matrix4x4d]) {
        for (o <- List(a, b)) {
          for (f <- o._fields()) {
            assert(!o._isFieldSet(f, FieldSetDepth.DEEP))
          }
          o._setAllFieldsSet(true, FieldSetDepth.DEEP)
          for (f <- o._fields()) {
            assert(o._isFieldSet(f, FieldSetDepth.DEEP))
          }
        }
      }
    }
  }

  @Test
  def testModifyAndCompareAgain() {

    val car1 = new Car
    val car2 = new Car

    assert(car1 == car2)

    car1.setBrand("abc")
    car1.setId(23)
    car2.setNWheels(123)
    car2.setTopSpeed(321)

    assert(car1 != car2)

    val garage1 = new GarageViewer
    val garage2 = new GarageViewer

    assert(garage1 == garage2)

    garage1.setVehicles(new Array[Vehicle](1))

    assert(garage1 != garage2)

    garage1.getVehicles()(0) = car1

    garage2.setVehicles(new Array[Vehicle](1))
    garage2.getVehicles()(0) = car1

    assert(garage1 == garage2)

    garage2.getVehicles()(0) = car2

    assert(garage1 != garage2)

  }

  @Test
  def testDeepCopy() {

    for (e <- classRegEntries) {

      val a = e.construct()
      val b = a.deepCopy()

      assert(a == b)
      assert(!(a eq b))

      a._setAllFieldsSet(true, FieldSetDepth.SHALLOW)

      assert(a != b)

    }

  }

}