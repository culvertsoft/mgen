package se.culvertsoft.mgen.compiler.util

object MeasureTime {
  def apply[A](dbgString: String)(f: => A): A = {
    val t0 = System.nanoTime() / 1e9
    val out = f
    val t1 = System.nanoTime() / 1e9
    val dt = t1 - t0
    println(s"MeasureTime [$dbgString]: $dt s")
    out
  }
}