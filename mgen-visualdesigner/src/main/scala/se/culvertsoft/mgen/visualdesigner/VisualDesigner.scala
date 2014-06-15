package se.culvertsoft.mgen.visualdesigner

import java.awt.AWTEvent
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import EntityFactory.mkModel
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.SwingUtilities
import javax.swing.ToolTipManager
import javax.swing.UIManager
import net.miginfocom.swing.MigLayout
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.images.MkImgIcon
import se.culvertsoft.mgen.visualdesigner.uiInit.MainMenuInit
import se.culvertsoft.mgen.visualdesigner.uiInit.PanelsInit
import se.culvertsoft.mgen.visualdesigner.uiInit.ToolbarsInit
import se.culvertsoft.mgen.visualdesigner.util.ConsolePipe
import se.culvertsoft.mgen.visualdesigner.view.AddressField
import se.culvertsoft.mgen.visualdesigner.view.PackageExplorer
import java.awt.Frame

object VisualDesigner {

  def main(args: Array[String]) {

    val cmdLineArgs = se.culvertsoft.mgen.compiler.MGen.parseKeyValuePairs(args)

    var controller: Controller = null

    SwingUtilities.invokeLater(new Runnable() {
      override def run() {

        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel")
        ToolTipManager.sharedInstance().setInitialDelay(200)
        ToolTipManager.sharedInstance().setDismissDelay(999999999)

        // Configure main gui areas
        val window = PanelsInit.mkWindow()
        val leftPanel = PanelsInit.mkContentPane("leftPanel")
        val rightPanelBase = PanelsInit.mkContentPane("rightPanelBase")
        val rightPanel = PanelsInit.mkContentPane("rightPanelBase")
        val dashBoard = PanelsInit.mkContentPane("drawArea")
        val rightTopPanel = new JPanel { setLayout(new MigLayout("insets 0 0 0 0")) }

        // Configure console
        val consolePanel = PanelsInit.mkTextPanel()
        val msgConsole = new ConsolePipe(consolePanel)
        msgConsole.redirectOut(Color.BLACK, System.out)
        msgConsole.redirectErr(Color.RED, System.err)

        // Configure splitters
        val leftRightPanelSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, rightPanelBase)
        val rightPanelSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, rightPanel, consolePanel)
        rightPanelBase.add(rightPanelSplitter)
        window.add(leftRightPanelSplitter, BorderLayout.CENTER)

        // Configure status bar
        val statusBar = new JLabel()
        statusBar.setPreferredSize(new Dimension(200, 20))
        statusBar.setText("    Welcome to MGen Visual Designer! This is the status bar where various information will me posted.")
        window.add(statusBar, BorderLayout.PAGE_END)

        // Configure model and controller
        val model = mkModel()
        controller = new Controller(model, dashBoard, window)

        // Configure view helpers
        val pkgExplorer = new PackageExplorer(controller, leftPanel)
        val addressField = new AddressField(controller)

        // Configure toolbars
        val leftPanelTopToolBar = ToolbarsInit.mkLeftPanelTopToolBar(controller)
        val rightPanelTopToolbar = ToolbarsInit.mkRightPanelTopToolbar(controller)
        val rightPanelLeftToolbar = ToolbarsInit.mkRightPanelLeftToolBar(controller)

        // Add toolbars and helpers to main panels
        leftPanel.add(leftPanelTopToolBar, BorderLayout.PAGE_START)
        rightPanel.add(rightPanelLeftToolbar, BorderLayout.WEST)
        rightPanel.add(dashBoard, BorderLayout.CENTER)
        rightPanel.add(rightTopPanel, BorderLayout.NORTH)
        rightTopPanel.add(rightPanelTopToolbar, "growx, wrap")
        rightTopPanel.add(addressField, "growx")
        leftPanel.add(pkgExplorer)

        // Finally configure window
        window.setJMenuBar(MainMenuInit.mkMainMenu(controller));
        window.setIconImage(MkImgIcon.small("drag-mode-16.png").getImage());
        window.setLocation(50, 50)
        window.addWindowListener(new WindowAdapter() {
          override def windowClosing(e: WindowEvent) {
            controller.saveMgr.closeApplication()
          }
        })
        window.setVisible(true)
        window.setExtendedState(window.getExtendedState() | Frame.MAXIMIZED_BOTH)

        // Configure splitter sizes
        leftRightPanelSplitter.setResizeWeight(0.1)
        rightPanelSplitter.setResizeWeight(0.9)
        leftRightPanelSplitter.setDividerLocation(200)
        rightPanelSplitter.setDividerLocation(window.getHeight() - 200)

        // Configure our AWT event filter 
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
          override def dispatchEvent(event: AWTEvent) {
            try {
              if (controller.filterGlobalEvent(event)) {
                super.dispatchEvent(event)
              }
            } catch {
              case e: Exception =>
                e.printStackTrace()
            }
          }
        })

      }
    })

    SwingUtilities.invokeLater(new Runnable() {
      override def run() {

        // Print something fun in the console
        println(" --- Welcome to MGen Visual Designer ---")
        println(" --- Happy modeling! --- ")

        // Load a default model
        if (cmdLineArgs.contains("project"))
          controller.saveMgr.load(new File(cmdLineArgs("project")))

      }
    })

  }

}
