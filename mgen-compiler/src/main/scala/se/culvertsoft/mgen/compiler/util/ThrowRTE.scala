package se.culvertsoft.mgen.compiler.util

object ThrowRTE {
  def apply(msg: String):Nothing = { throw new RuntimeException(msg) }
}