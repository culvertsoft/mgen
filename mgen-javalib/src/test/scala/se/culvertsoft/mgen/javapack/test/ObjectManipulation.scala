package se.culvertsoft.mgen.javapack.test

import org.junit.Test
import gameworld.types.basemodule1.Car
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth

class ObjectManipulation {

  @Test
  def testCompareAfterCreate() {
    val a = new Car
    val b = new Car
    assert(a == b, "a was not == b")
  }

  @Test
  def testFieldsNotSetAfterCreate() {
    val a = new Car
    val b = new Car
    for (o <- List(a, b)) {
      for (f <- o._fields()) {
        assert(!o._isFieldSet(f, FieldSetDepth.SHALLOW))
      }
    }
  }

  @Test
  def testFieldsCanBeSetShallow() {
    val a = new Car
    val b = new Car
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

  @Test
  def testFieldsCanBeSetDeep() {
    val a = new Car
    val b = new Car
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