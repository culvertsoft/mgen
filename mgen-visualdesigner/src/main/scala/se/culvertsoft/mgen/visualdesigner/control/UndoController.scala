package se.culvertsoft.mgen.visualdesigner.control

import se.culvertsoft.mgen.visualdesigner.model.Model

class UndoController(controller: Controller) extends SubController(controller) {

  private val undoBuffer = new UndoBuffer[Model](controller.model.deepCopy, _ => 1000)
  private var _undoing = false

  resetUndoBuffer()

  controller.addObserver(new ModelChangeListener() {
    override def onModelModified(isPreview: Boolean) {
      if (!controller.isBulkOperationActive && !isPreview) {
        updateUndoBuffer()
      }
    }
  })

  private def undoing(f: => Unit) {
    _undoing = true
    try {
      f
    } finally {
      _undoing = false
    }
  }

  private def updateUndoBuffer() {

    if (!controller.isBulkOperationActive && !isUndoing) {

      val state = controller.model()
      val previous = undoBuffer.optPrevious
      val future = undoBuffer.optFuture

      if (previous.isDefined && previous.get == state) {
        return
      }

      /*   if (future.isDefined && future.get == state) {
            return
         }*/

      undoBuffer.pushState(state.deepCopy(), true)

    }

  }

  def isUndoing(): Boolean = {
    _undoing
  }

  def undo(prevAttemptState: Model = null) {

    undoing {

      controller.viewMgr.keepScrollbarPos {

        undoBuffer.goBack(controller.model) match {
          case Some(prevState) =>
            val prevModel = prevState
            if (prevModel.project != controller.model.project) {
              controller.setModel(prevModel.deepCopy(), false)
            } else {
              if (!(prevState eq prevAttemptState))
                undo(prevState)
            }
          case _ =>
        }

      }

    }

  }

  def redo() {

    undoing {

      controller.viewMgr.keepScrollbarPos {

        undoBuffer.goForward(controller.model) match {
          case Some(prevState) =>
            val prevModel = prevState
            if (prevModel.project != controller.model.project) {
              controller.setModel(prevModel.deepCopy(), false)
            } else {
              redo()
            }
          case _ =>
        }

      }

    }

  }

  def resetUndoBuffer() {
    undoBuffer.clear(controller.model.deepCopy)
    updateUndoBuffer()
  }

}