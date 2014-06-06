package se.culvertsoft.mgen.visualdesigner.util

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object Util {

  def manage[T <: Closeable, ReturnType](fopen: => T)(fuse: T => ReturnType): ReturnType = {
    var t: T = null.asInstanceOf[T]
    try {
      t = fopen
      fuse(t)
    } finally {
      if (t != null) t.close
    }
  }

  def clamp(_min: Short, _val: Short, _max: Short): Short = math.max(_min, math.min(_val, _max)).toShort
  def clamp(_min: Int, _val: Int, _max: Int) = math.max(_min, math.min(_val, _max))
  def clamp(_min: Float, _val: Float, _max: Float) = math.max(_min, math.min(_val, _max))
  def clamp(_min: Double, _val: Double, _max: Double) = math.max(_min, math.min(_val, _max))

  def manageFileIn(file: File)(fUse: BufferedInputStream => Unit) {
    manage(new BufferedInputStream(new FileInputStream(file)))(fUse)
  }

  def manageFileIn(fName: String)(fUse: BufferedInputStream => Unit) {
    manageFileIn(new File(fName))(fUse)
  }

  def manageFileOut(file: File)(fUse: BufferedOutputStream => Unit) {
    manage(new BufferedOutputStream(new FileOutputStream(file)))(fUse)
  }

  def manageFileOut(fName: String)(fUse: BufferedOutputStream => Unit) {
    manageFileOut(new File(fName))(fUse)
  }

  //   def levenshtein(_a: String, _b: String) : Int = {
  //        val a = _a.toLowerCase();
  //        val b = _b.toLowerCase();
  //        // i == 0
  //        val costs = new Array[Int](b.length() + 1)
  //        
  //        for(j <- 0 until costs.length){
  //           costs(j) = j;
  //        }
  //        for(i <- 1 until a.length){
  //            // j == 0; nw = lev(i - 1, j)
  //            costs(0) = i;
  //            var nw = i - 1;
  //            for (j <- 1 until b.length){
  //            	val cj = math.min(1 + math.min(costs(j), costs(j - 1)), ( if (a(i - 1) == b(j - 1)) nw else nw + 1));
  //                nw = costs(j);
  //                costs(j) = cj;
  //            }
  //        }
  //        return costs(b.length());
  //    }

  def levenshtein(str1: String, str2: String): Int = {
    val lenStr1 = str1.length
    val lenStr2 = str2.length

    val d: Array[Array[Int]] = Array.ofDim(lenStr1 + 1, lenStr2 + 1)

    for (i <- 0 to lenStr1) d(i)(0) = i
    for (j <- 0 to lenStr2) d(0)(j) = j

    for (i <- 1 to lenStr1; j <- 1 to lenStr2) {
      val cost = if (str1(i - 1) == str2(j - 1)) 0 else 1

      d(i)(j) = min(
        d(i - 1)(j) + 1, // deletion
        d(i)(j - 1) + 1, // insertion
        d(i - 1)(j - 1) + cost // substitution
        )
    }

    val ret = d(lenStr1)(lenStr2)
    println(s"str1: $str1, str2: $str2, ret: $ret")
    ret
  }

  def min(nums: Int*): Int = nums.min

}