package se.culvertsoft.mgen.visualdesigner.util

object TimeUtil {

   def clock(f: => Unit): Double = {
      val t0 = System.nanoTime() / 1e9
      f
      System.nanoTime() / 1e9 - t0
   }

   def clockPrint(id: String)(f: => Unit) {
      println(s"$id: " + clock(f))
   }
   
   def clockPrint(f: => Unit) {
      println("Time elapsed(s): " + clock(f))
   }

}
