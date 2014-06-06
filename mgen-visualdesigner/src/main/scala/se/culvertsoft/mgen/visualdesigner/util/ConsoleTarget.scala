package se.culvertsoft.mgen.visualdesigner.util

import java.awt.Color

trait ConsoleTarget {
	def getLength(): Int
	def isVisible(): Boolean
	def setVisible(state: Boolean)
	def append(msg: String, c: Color)
	def toFront()
}