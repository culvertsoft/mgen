package se.culvertsoft.mgen.visualdesigner.util

import java.awt.Dimension
import java.awt.Point

import scala.language.implicitConversions

object AwtMath {

   implicit class RichPoint(a: Point) {
      def -(b: Point): Point = new Point(a.x - b.x, a.y - b.y)
      def +(b: Point): Point = new Point(a.x + b.x, a.y + b.y)
      def -(b: Dimension): Point = new Point(a.x - b.width, a.y - b.height)
      def +(b: Dimension): Point = new Point(a.x + b.width, a.y + b.height)
      def /(d: Int): Point = new Point(a.x / d, a.y / d)
      def *(m: Int): Point = new Point(a.x * m, a.y * m)
      def /(d: Double): Point = new Point((a.x / d).round.toInt, (a.y / d).round.toInt)
      def *(m: Double): Point = new Point((a.x * m).round.toInt, (a.y * m).round.toInt)
   }

   implicit class RichDimension(a: Dimension) {
      def -(b: Point): Dimension = new Dimension(a.width - b.x, a.height - b.y)
      def +(b: Point): Dimension = new Dimension(a.width + b.x, a.height + b.y)
      def -(b: Dimension): Dimension = new Dimension(a.width - b.width, a.height - b.height)
      def +(b: Dimension): Dimension = new Dimension(a.width + b.width, a.height + b.height)
      def /(d: Int): Dimension = new Dimension(a.width / d, a.height / d)
      def *(m: Int): Dimension = new Dimension(a.width * m, a.height * m)
      def /(d: Double): Dimension = new Dimension((a.width / d).round.toInt, (a.height / d).round.toInt)
      def *(m: Double): Dimension = new Dimension((a.width * m).round.toInt, (a.height * m).round.toInt)
   }

}