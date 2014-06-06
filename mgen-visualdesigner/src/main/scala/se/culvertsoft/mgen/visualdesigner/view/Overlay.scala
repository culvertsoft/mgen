package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Rectangle

import javax.swing.JComponent

class Overlay(_component: JComponent, _position: => Rectangle) {
   def component() = _component
   def position() = _position
   def reposition() { _component.setBounds(position()) }
}
