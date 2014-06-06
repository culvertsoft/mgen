package se.culvertsoft.mgen.visualdesigner.window

import java.awt.BorderLayout
import java.awt.Dimension

import scala.collection.JavaConversions.asScalaBuffer

import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants
import net.miginfocom.swing.MigLayout
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.Generator
import se.culvertsoft.mgen.visualdesigner.util.MkActionListener

class GeneratorWindow(controller: Controller) {

  val dialog = new JDialog(controller.viewMgr.getWindow, "Generator Settings");
  dialog.setModal(true)

  val content = new JPanel() {
    override def getPreferredSize(): Dimension = {
      return new Dimension(getScroller().getViewport().getSize().width, super.getPreferredSize.height)
    }
  }

  content.setLayout(new MigLayout("wrap 1"))
  val generators = controller.settingsMgr.getGenerators

  val scroller = new JScrollPane(content) {
    getVerticalScrollBar().setUnitIncrement(10)
    setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
  }

  def getScroller(): JScrollPane = {
    scroller
  }

  dialog.getContentPane().add(scroller, BorderLayout.CENTER);

  def createGeneratorPanel(generator: Generator): GeneratorPanel = {
    return new GeneratorPanel(generator) {
      override def onDelete() {
        generators.remove(generator)
        GeneratorWindow.this.remove(this)
        updateAndSaveGenerators
      }
      override def onUpdate(){
        updateAndSaveGenerators
      }
    }
  }

  def remove(g: GeneratorPanel) {
    content.remove(g)
    content.repaint()
    content.validate()
  }

  val button = new JButton("Add Generator")
  button.addActionListener(MkActionListener {
    val g = new Generator("", "", "", "", "", new java.util.HashMap[String, String])
    generators.add(g)
    content.add(createGeneratorPanel(g))
    content.validate()
    content.revalidate()
    updateAndSaveGenerators
  })
  content.add(button)

  generators.foreach { generator =>
    content.add(createGeneratorPanel(generator))
  }

  dialog.pack()
  dialog.setSize(800, 768)
  import se.culvertsoft.mgen.visualdesigner.util.AwtMath._
  val mousePos = controller.mouseInputMgr.mousePos.onScreen
  dialog.setLocation(mousePos)
  dialog.setVisible(true)

  def updateAndSaveGenerators(){
    controller.settingsMgr.setGenerators(generators)
  }
  
}