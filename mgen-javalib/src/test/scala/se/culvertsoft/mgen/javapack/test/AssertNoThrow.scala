package se.culvertsoft.mgen.javapack.test

import scala.util.Try

object AssertNoThrow {
  def apply(f: => Unit) {
    assert(Try(f).isSuccess)
  }
}