package se.culvertsoft.mgen.visualdesigner.uiInit

import java.awt.BorderLayout
import java.awt.Component

import javax.swing.JFrame
import javax.swing.WindowConstants
import se.culvertsoft.mgen.visualdesigner.util.TextPanel
import se.culvertsoft.mgen.visualdesigner.view.ContentPane

object PanelsInit {

  def mkWindow(): JFrame = {
    val window = new JFrame("MGen Visual Designer")
    //window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
    window.setSize(1400, 1024)
    window
  }

  def mkTextPanel(): TextPanel = {
    val panel = new TextPanel
    panel
  }

  def mkContentPane(name: String) = {
    val c = new ContentPane()
    c.setName(s"$name.contentPane")
    c.setLayout(new BorderLayout() {
      override def addLayoutComponent(comp: Component, constraints: Object) {
        super.addLayoutComponent(comp, if (constraints == null) BorderLayout.CENTER else constraints)
      }
    })
    c
  }
}