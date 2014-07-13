package se.culvertsoft.mgen.compiler.plugins

import scala.collection.mutable.ArrayBuffer

object FindClasses {
  def apply[T](names: Seq[String]): Seq[Class[_ <: T]] = {
    val out = new ArrayBuffer[Class[_ <: T]]
    for (name <- names) {
      try {
        out += Class.forName(name).asInstanceOf[Class[_ <: T]]
      } catch {
        case e: ClassNotFoundException =>
      }
    }
    out
  }
  def apply[T](name: String): Seq[Class[_ <: T]] = {
    apply(List(name))
  }
}