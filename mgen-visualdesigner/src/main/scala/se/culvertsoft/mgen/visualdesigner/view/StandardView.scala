package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Container

import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.Entity

abstract class StandardView(
   entity: Entity,
   _controller: Controller,
   _overlaySizePad: Int) extends AbstractView(entity, _controller, _overlaySizePad) {

   val focusHelper = new FocusHelper(this, controller)

   innerPanel.add(focusHelper)

   override def hasFocus(): Boolean = {
      focusHelper.hasFocus()
   }

   override def requestFocus() {
      focusHelper.requestFocus()
   }

   override def beAddedToSwingParent(parent: Container) = {
      parent.add(boundingComponent, 0)
      if (!isRoot())
         parent.add(overlayPanel, 0)
   }

   override def beRemovedFromSwingParent(parent: Container) = {
      parent.remove(overlayPanel)
      parent.remove(boundingComponent)
   }

}