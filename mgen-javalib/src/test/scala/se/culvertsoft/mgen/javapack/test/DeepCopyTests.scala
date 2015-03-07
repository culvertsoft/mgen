package se.culvertsoft.mgen.javapack.test

import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION

import org.junit.Test

import gameworld.types.basemodule1.Matrix4x4d

class DeepCopyTests {

  @Test
  def deepCopyArray() {
    val mat = new Matrix4x4d
    assert(mat.hasValues())
    assert(mat.getValues.size == 4)
    assert(mat.getValues.head.size == 4)

    for (i <- 0 until mat.getValues.size) {
      for (e <- 0 until mat.getValues()(i).size) {
        mat.getValues()(i)(e) = math.random
      }
    }

    assert(mat.getValues.flatMap { _.toSeq }.exists { x => math.abs(x) > 0.0001 })

    assert(mat.toString() == mat.deepCopy().toString())
    assert(mat.deepCopy() == mat)

  }

}