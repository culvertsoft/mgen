package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Graphics2D

trait PaintListener {
  def prePaint(g: Graphics2D) {}
  def postPaint(g: Graphics2D) {}
}