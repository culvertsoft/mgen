package se.culvertsoft.mgen.javapack.test

import scala.util.Try

object AssertThrows {
  def apply(f: => Unit) {
    assert(Try(f).isFailure)
  }
}