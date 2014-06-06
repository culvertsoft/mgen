package se.culvertsoft.mgen.visualdesigner.util

import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.collection.mutable.ArrayBuffer

object Asof {

  implicit class AsofOps(x: Any) {
    def as[T: ClassTag]: Option[T] = {
      x match {
        case x: T => Some(x)
        case _ => None
      }
    }
    def ifIs[T: ClassTag](f: T => Unit) {
      x.as[T].foreach(f)
    }
  }

  implicit class RichFilterable[T0](xs: Seq[T0]) {
    def filterOfType[T: ClassTag](): Seq[T0 with T] = {
      val out = new ArrayBuffer[T0 with T]
      xs foreach (_ match {
        case x: T => out += x.asInstanceOf[T0 with T]
        case _ =>
      })
      out
    }
  }

}
