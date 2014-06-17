package se.culvertsoft.mgen.visualdesigner.uiInit

import java.awt.Dimension

import javax.swing.JToolBar
import javax.swing.SwingConstants
import se.culvertsoft.mgen.visualdesigner.Actions
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.view.ScaleSetBox

object ToolbarsInit {

  def mkRightPanelBottomToolbar(implicit controller: Controller): JToolBar = {

    val toolbar = new JToolBar()
    toolbar.setFloatable(true)

    toolbar
  }

  def mkRightPanelTopToolbar(implicit controller: Controller): JToolBar = {

    val toolbar = new JToolBar()
    toolbar.setFloatable(false)

    toolbar.addSeparator()

    val cb = new ScaleSetBox(controller)
    cb.setMaximumSize(new Dimension(75, 30))
    toolbar.add(cb)

    toolbar.addSeparator()

    toolbar.add(Actions.DashBoard.Top.ALIGN_Y_TOP_BTN)
    toolbar.add(Actions.DashBoard.Top.ALIGN_Y_CENTER_BTN)
    toolbar.add(Actions.DashBoard.Top.ALIGN_Y_BOTTOM_BTN)

    toolbar.addSeparator()

    toolbar.add(Actions.DashBoard.Top.ALIGN_X_LEFT_BTN)
    toolbar.add(Actions.DashBoard.Top.ALIGN_X_CENTER_BTN)
    toolbar.add(Actions.DashBoard.Top.ALIGN_X_RIGHT_BTN)

    toolbar.addSeparator()

    toolbar.add(Actions.DashBoard.Top.SPREAD_EQUAL_Y)
    toolbar.add(Actions.DashBoard.Top.SPREAD_EQUAL_X)

    toolbar.addSeparator()

    toolbar.add(Actions.DashBoard.Top.RESIZE_EQUAL_Y)
    toolbar.add(Actions.DashBoard.Top.RESIZE_EQUAL_X)

    toolbar.addSeparator()

    toolbar.add(Actions.DashBoard.Top.SPACE_OUT_Y)
    toolbar.add(Actions.DashBoard.Top.SPACE_OUT_X)

    toolbar.addSeparator()

    toolbar.add(Actions.DashBoard.Top.LAY_OUT)

    toolbar.addSeparator()
    toolbar.addSeparator()
    toolbar.addSeparator()

    toolbar.add(Actions.DashBoard.Top.GENERATE)

    toolbar

  }

  def mkLeftPanelTopToolBar(implicit controller: Controller): JToolBar = {

    val toolbar = new JToolBar()
    toolbar.addSeparator()

    toolbar.add(Actions.TreeView.MOVE_UP)
    toolbar.add(Actions.TreeView.MOVE_DOWN)
    toolbar.add(Actions.TreeView.RENAME)
    toolbar.add(Actions.TreeView.FIND_SELECTED)
    toolbar.setFloatable(false)

    toolbar

  }

  def mkRightPanelLeftToolBar(implicit controller: Controller): JToolBar = {

    val toolbar = new JToolBar()
    toolbar.setOrientation(SwingConstants.VERTICAL)
    toolbar.setFloatable(false)

    toolbar.add(Actions.DashBoard.Left.GO_UP)
    toolbar.add(Actions.DashBoard.Left.GO_BACK)
    toolbar.add(Actions.DashBoard.Left.GO_FORWARD)
    toolbar.add(Actions.DashBoard.Left.SET_VIEW_ROOT)
    toolbar.add(Actions.DashBoard.Left.MAXIMIZE)
    toolbar.add(Actions.DashBoard.Left.RESET_VIEW_ROOT)

    toolbar.addSeparator()
    toolbar.addSeparator()

    toolbar.add(Actions.DashBoard.Left.NEW_MODULE)
    toolbar.add(Actions.DashBoard.Left.NEW_TYPE)
    toolbar.add(Actions.DashBoard.Left.NEW_FIELD)
    toolbar.addSeparator()

    toolbar.addSeparator()
    toolbar.addSeparator()

    toolbar
  }

}