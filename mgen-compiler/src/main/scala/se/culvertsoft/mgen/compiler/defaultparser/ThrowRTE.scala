package se.culvertsoft.mgen.compiler.defaultparser

object ThrowRTE {
  def apply(msg: String):Nothing = { throw new RuntimeException(msg) }
}