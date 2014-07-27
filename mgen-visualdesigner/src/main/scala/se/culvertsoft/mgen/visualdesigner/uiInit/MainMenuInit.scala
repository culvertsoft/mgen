package se.culvertsoft.mgen.visualdesigner.uiInit

import javax.swing.JMenu
import javax.swing.JMenuBar
import se.culvertsoft.mgen.visualdesigner.Actions
import se.culvertsoft.mgen.visualdesigner.control.Controller

object MainMenuInit {

  def mkMainMenu(implicit controller: Controller): JMenuBar = {

    // Main menu object
    val menuBar = new JMenuBar()

    // File menu
    val fileMenu = new JMenu("File")
    fileMenu.add(Actions.MainMenu.File.NEW)
    fileMenu.add(Actions.MainMenu.File.LOAD)
    fileMenu.add(Actions.MainMenu.File.SAVE)
    fileMenu.add(Actions.MainMenu.File.SAVE_AS)
    fileMenu.addSeparator()

    fileMenu.add(Actions.MainMenu.File.EXIT)

    // Edit menu
    val editMenu = new JMenu("Edit")
    editMenu.add(Actions.MainMenu.Edit.UNDO)
    editMenu.add(Actions.MainMenu.Edit.REDO)
    editMenu.addSeparator()
    editMenu.add(Actions.MainMenu.Edit.CUT)
    editMenu.add(Actions.MainMenu.Edit.COPY)
    editMenu.add(Actions.MainMenu.Edit.PASTE)
    editMenu.addSeparator()
    editMenu.add(Actions.MainMenu.Edit.FIND_BY_NAME)
    editMenu.add(Actions.MainMenu.Edit.FIND_SELECTED)
    editMenu.addSeparator()
    editMenu.add(Actions.MainMenu.Edit.SELECT_ALL)
    editMenu.add(Actions.MainMenu.Edit.DESELECT_ALL)
    editMenu.addSeparator()
    editMenu.add(Actions.MainMenu.Edit.RENAME)
    editMenu.add(Actions.MainMenu.Edit.DELETE)

    // View menu
    val viewMenu = new JMenu("View")
    viewMenu.add(Actions.MainMenu.View.GO_UP)
    viewMenu.add(Actions.MainMenu.View.GO_BACK)
    viewMenu.add(Actions.MainMenu.View.GO_FORWARD)
    viewMenu.addSeparator()
    viewMenu.add(Actions.MainMenu.View.SET_VIEW_ROOT)
    viewMenu.add(Actions.MainMenu.View.RESET_VIEW_ROOT)
    viewMenu.add(Actions.MainMenu.View.TOGGLE_FULL_SCREEN)
    viewMenu.addSeparator()
    viewMenu.add(Actions.MainMenu.View.ICONS_ONLY_VIEW)
    viewMenu.addSeparator()
    viewMenu.add(Actions.MainMenu.View.REBUILD_VIEW)

    // Tools menu
    val toolsMenu = new JMenu("Tools")
    toolsMenu.add(Actions.MainMenu.Tools.NEW_MODULE)
    toolsMenu.add(Actions.MainMenu.Tools.NEW_TYPE)
    toolsMenu.add(Actions.MainMenu.Tools.NEW_ENUM)
    toolsMenu.add(Actions.MainMenu.Tools.NEW_FIELD)
    toolsMenu.addSeparator()
    toolsMenu.addSeparator()
    toolsMenu.add(Actions.MainMenu.Tools.ALIGN_Y_TOP_BTN)
    toolsMenu.add(Actions.MainMenu.Tools.ALIGN_Y_CENTER_BTN)
    toolsMenu.add(Actions.MainMenu.Tools.ALIGN_Y_BOTTOM_BTN)
    toolsMenu.add(Actions.MainMenu.Tools.ALIGN_X_LEFT_BTN)
    toolsMenu.add(Actions.MainMenu.Tools.ALIGN_X_CENTER_BTN)
    toolsMenu.add(Actions.MainMenu.Tools.ALIGN_X_RIGHT_BTN)
    toolsMenu.addSeparator()
    toolsMenu.add(Actions.MainMenu.Tools.SPREAD_EQUAL_Y)
    toolsMenu.add(Actions.MainMenu.Tools.SPREAD_EQUAL_X)
    toolsMenu.addSeparator()
    toolsMenu.add(Actions.MainMenu.Tools.RESIZE_EQUAL_X)
    toolsMenu.add(Actions.MainMenu.Tools.RESIZE_EQUAL_Y)
    toolsMenu.addSeparator()
    toolsMenu.add(Actions.MainMenu.Tools.SPACE_OUT_Y)
    toolsMenu.add(Actions.MainMenu.Tools.SPACE_OUT_X)
    toolsMenu.addSeparator()
    toolsMenu.add(Actions.MainMenu.Tools.LAY_OUT)
    toolsMenu.add(Actions.MainMenu.Tools.LAY_OUT_AND_RESIZE)
    toolsMenu.addSeparator()
    toolsMenu.addSeparator()
    toolsMenu.add(Actions.MainMenu.Tools.GENERATE)

    // Settings menu
    val settingsMenu = new JMenu("Settings")
    settingsMenu.add(Actions.MainMenu.Settings.GENERATION)

    // Help menu
    val helpMenu = new JMenu("Help")
    helpMenu.add(Actions.MainMenu.About.HELP)
    helpMenu.add(Actions.MainMenu.About.ABOUT)

    // Add all menus
    menuBar.add(fileMenu)
    menuBar.add(editMenu)
    menuBar.add(viewMenu)
    menuBar.add(toolsMenu)
    menuBar.add(settingsMenu)
    menuBar.add(helpMenu)

    menuBar

  }
}