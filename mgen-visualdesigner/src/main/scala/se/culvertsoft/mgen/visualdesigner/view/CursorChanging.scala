package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Component
import java.awt.Cursor
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

import se.culvertsoft.mgen.visualdesigner.util.Asof.AsofOps

trait CursorChanging {
   self: AbstractView =>
   import Graphics2DOps._
   import Labeled._

   val cursorChange_btn = createEnterExitCursorChangeListener(Cursor.HAND_CURSOR)
   val cursorChange_N_RESIZE_CURSOR = createEnterExitCursorChangeListener(Cursor.N_RESIZE_CURSOR)
   val cursorChange_NE_RESIZE_CURSOR = createEnterExitCursorChangeListener(Cursor.NE_RESIZE_CURSOR)
   val cursorChange_E_RESIZE_CURSOR = createEnterExitCursorChangeListener(Cursor.E_RESIZE_CURSOR)
   val cursorChange_SE_RESIZE_CURSOR = createEnterExitCursorChangeListener(Cursor.SE_RESIZE_CURSOR)
   val cursorChange_S_RESIZE_CURSOR = createEnterExitCursorChangeListener(Cursor.S_RESIZE_CURSOR)
   val cursorChange_SW_RESIZE_CURSOR = createEnterExitCursorChangeListener(Cursor.SW_RESIZE_CURSOR)
   val cursorChange_W_RESIZE_CURSOR = createEnterExitCursorChangeListener(Cursor.W_RESIZE_CURSOR)
   val cursorChange_NW_RESIZE_CURSOR = createEnterExitCursorChangeListener(Cursor.NW_RESIZE_CURSOR)


   this.ifIs[Resizeable with Selectable](x => {
	   x.RESIZE_SQUARE_N.addMouseListener(cursorChange_N_RESIZE_CURSOR)
	   x.RESIZE_SQUARE_NE.addMouseListener(cursorChange_NE_RESIZE_CURSOR)
	   x.RESIZE_SQUARE_E.addMouseListener(cursorChange_E_RESIZE_CURSOR)
	   x.RESIZE_SQUARE_SE.addMouseListener(cursorChange_SE_RESIZE_CURSOR)
	   x.RESIZE_SQUARE_S.addMouseListener(cursorChange_S_RESIZE_CURSOR)
	   x.RESIZE_SQUARE_SW.addMouseListener(cursorChange_SW_RESIZE_CURSOR)
	   x.RESIZE_SQUARE_W.addMouseListener(cursorChange_W_RESIZE_CURSOR)
	   x.RESIZE_SQUARE_NW.addMouseListener(cursorChange_NW_RESIZE_CURSOR)
   })

   def createEnterExitCursorChangeListener(id: Int): MouseAdapter = {
      new MouseAdapter() {
         override def mouseEntered(e: MouseEvent) {
            if (isRootView())
               controller.mouseInputMgr.setCursor(Cursor.DEFAULT_CURSOR)
            else
               controller.mouseInputMgr.setCursor(id)
         }
         override def mouseExited(e: MouseEvent) {
            controller.mouseInputMgr.setCursor(Cursor.DEFAULT_CURSOR)
         }
      }
   }

}
