package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Component

object ClampPos {

  def apply(toClamp: Component, within: Component, pad: Int = 20) {

    val bounds = toClamp.getBounds()
    val window = within.getBounds()

    val dx =
      if (bounds.x < (window.x + pad)) {
        window.x + pad - bounds.x
      } else if ((bounds.x + bounds.width + pad) > (window.x + window.width)) {
        (window.x + window.width) - (pad + bounds.x + bounds.width)
      } else {
        0
      }

    val dy =
      if (bounds.y < (window.y + pad)) {
        window.y + pad - bounds.y
      } else if ((bounds.y + bounds.height + pad) > (window.y + window.height)) {
        (window.y + window.height) - (pad + bounds.y + bounds.height)
      } else {
        0
      }

    toClamp.setLocation(bounds.x + dx, bounds.y + dy)
  }

}