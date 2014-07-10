package se.culvertsoft.mgen.visualdesigner.view

import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.KeyEvent

import javax.swing.AbstractAction
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.ToolTipManager
import javax.swing.border.EmptyBorder
import se.culvertsoft.mgen.visualdesigner.HotKey
import se.culvertsoft.mgen.visualdesigner.HotKey.toStroke
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.EnumEntry

object EnumEntryView {
  val FIELD_FONT = FieldView.FIELD_FONT
}

class EnumEntryView(
  entry: EnumEntry,
  controller: Controller)
  extends StandardView(entry, controller, 5)
  with BackGrounded
  with Bordered
  with Selectable
  with Movable
  with CursorChanging {
  import EnumEntryView._

  innerPanel.setLayout(new BorderLayout())

  override def onEntityModified(child: Entity, validate: Boolean, parent: Option[Entity]) {

  }

  val fieldPanel = new JPanel(new GridLayout(1, 3, 3, 3)) {
    setOpaque(false)
    setBorder(new EmptyBorder(4, 2, 5, 2))
  }

  val nameField = new EditLabel(entry.getName()) {
    setFont(FIELD_FONT)
    setMargin(new Insets(1, 1, 1, 4))
    setHorizontalAlignment(SwingConstants.RIGHT)
    addFocusListener(new FocusAdapter() {
      override def focusLost(e: FocusEvent) {
        controller.rename(entry, getText())
      }
    })
    override def getToolTipText(): String = {
      s"enum entry name: ${entry.getName()}"
    }
  }

  val constantField = new EditLabel(entry.getConstant) {
    setFont(FIELD_FONT)
    setMargin(new Insets(1, 5, 1, 1))
    setHorizontalAlignment(SwingConstants.LEFT)

    addFocusListener(new FocusAdapter() {
      override def focusLost(e: FocusEvent) {
        controller.changeEnumEntryConstant(entry, getText())
      }
    })
    override def getToolTipText(): String = {
      s"enum entry constant: ${getText()}"
    }
  }

  val ttmgr = ToolTipManager.sharedInstance()

  ttmgr.registerComponent(nameField)
  ttmgr.registerComponent(constantField)

  innerPanel.add(fieldPanel)

  resetViewComplexity()

  /**
   * ***************************************************************
   *
   *
   * 						METHODS
   *
   * **************************************************************
   */

  override def desiredViewComplexity(): ViewComplexity = {
    if (EnumEntryView.this.width < 150 || controller.viewMgr.isIconOverrideActive) {
      VIEW_COMPLEXITY_SIMPLE
    } else {
      VIEW_COMPLEXITY_COMPLEX
    }
  }

  override def onViewComplexityChange(complexity: ViewComplexity) {
    complexity match {
      case VIEW_COMPLEXITY_COMPLEX => setComplex()
      case _ => setSimple()
    }
  }

  def setComplex() {
    if (fieldPanel != null) {
      fieldPanel.removeAll()
      fieldPanel.add(nameField)
      fieldPanel.add(constantField)
      nameField.setHorizontalAlignment(SwingConstants.RIGHT)
      fieldPanel.validate()
      fieldPanel.repaint()
    }
  }

  def setSimple() {
    if (fieldPanel != null) {
      fieldPanel.removeAll()
      fieldPanel.add(nameField)
      nameField.setHorizontalAlignment(SwingConstants.CENTER)
      fieldPanel.validate()
      fieldPanel.repaint()
    }
  }

  override protected def drawInnerPanelBorder(g: Graphics2D) { drawBorder(g) }
  override protected def drawInnerPanelComponent(g: Graphics2D) { drawBackground(g) }

  class EditLabel(resetString: => String) extends JTextField(resetString) {

    override def paintBorder(g: Graphics) {
      if (hasFocus()) {
        super.paintBorder(g)
      }
    }

    override def paintComponent(g: Graphics) {
      super.paintComponent(g)
    }

    addFocusListener(new FocusListener() {
      override def focusGained(e: FocusEvent) {

        if (!EnumEntryView.this.isSelected()) {
          controller.select(entry, true, false)
        }

        setOpaque(true)
        setSelectionStart(0)
        setSelectionEnd(getText().length())
        repaint()
      }
      override def focusLost(e: FocusEvent) {
        setOpaque(false)
        repaint()
      }
    })

    val im = getInputMap()
    val am = getActionMap()

    // Enter key
    im.put(HotKey(KeyEvent.VK_ENTER), "ok")
    am.put("ok", new AbstractAction {
      override def actionPerformed(e: ActionEvent) {
        transferFocus()
      }
    })

    // Escape key
    im.put(HotKey(KeyEvent.VK_ESCAPE), "cancel")
    am.put("cancel", new AbstractAction {
      override def actionPerformed(e: ActionEvent) {
        setText(resetString)
        EnumEntryView.this.requestFocus()
        //transferFocus()
      }
    })

    setOpaque(false)

  }

  override def rescaleChildren() {
    // Currently does nothing
  }

}
