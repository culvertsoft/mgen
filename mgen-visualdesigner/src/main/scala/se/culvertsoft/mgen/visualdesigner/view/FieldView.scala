package se.culvertsoft.mgen.visualdesigner.view

import java.awt.BorderLayout
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.KeyEvent
import java.util.ArrayList

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.seqAsJavaList

import javax.swing.AbstractAction
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.ToolTipManager
import javax.swing.border.EmptyBorder
import se.culvertsoft.mgen.visualdesigner.HotKey
import se.culvertsoft.mgen.visualdesigner.HotKey.toStroke
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.view.autobox2.AutoBoxListener
import se.culvertsoft.mgen.visualdesigner.view.autobox2.ClassFieldTypeAutoComboBox2
import se.culvertsoft.mgen.visualdesigner.view.autobox2.FieldTypeAutoBoxItem

object FieldView {
  val FIELD_FONT = new Font("Arial", Font.PLAIN, 10)
}

class FieldView(
  field: CustomTypeField,
  controller: Controller)
  extends StandardView(field, controller, 5)
  with BackGrounded
  with Bordered
  with Selectable
  with Movable
  with CursorChanging {
  import FieldView._

  innerPanel.setLayout(new BorderLayout())

  override def onEntityModified(child: Entity, validate: Boolean, parent: Option[Entity]) {
    if (controller.model.existsReference(field.getType(), child)) {
      typeField.update()
    }
  }

  val fieldPanel = new JPanel(new GridLayout(1, 3, 3, 3)) {
    setOpaque(false)
    setBorder(new EmptyBorder(4, 2, 5, 2))
  }

  val nameField = new EditLabel(field.getName()) {
    setFont(FIELD_FONT)
    setMargin(new Insets(1, 1, 1, 4))
    setHorizontalAlignment(SwingConstants.RIGHT)
    addFocusListener(new FocusAdapter() {
      override def focusLost(e: FocusEvent) {
        controller.rename(field, getText())
      }
    })
    override def getToolTipText(): String = {
      s"field name: ${field.getName()}"
    }
  }

  val typeField = new ClassFieldTypeAutoComboBox2(field.getType(), controller) {

    setFont(FIELD_FONT)
    addObserver(new AutoBoxListener[FieldTypeAutoBoxItem]() {
      override def requestFocusUp() {
        FieldView.this.requestFocus()
      }
      override def finishedItem(newType: FieldTypeAutoBoxItem) {
        controller.changeType(field, newType.fieldType)
      }
      override def focusGained(e: FocusEvent) {
        if (!FieldView.this.isSelected()) {
          controller.select(field, true, false)
        }
      }
    })
    override def getToolTipText(): String = {
      s"field ${super.getToolTipText()}"
    }
  }

  val flagsField = new EditLabel(field.getFlags().map(_.trim()).mkString(", ")) {
    setFont(FIELD_FONT)
    setMargin(new Insets(1, 5, 1, 1))
    setHorizontalAlignment(SwingConstants.LEFT)

    addFocusListener(new FocusAdapter() {
      override def focusLost(e: FocusEvent) {
        val text = if (getText() != null) getText() else ""
        val flags = new ArrayList(text.split(",").map(_.trim()).filter(_.nonEmpty).toList)
        controller.changeFlags(field, flags)
      }
    })
    override def getToolTipText(): String = {
      s"field flags: ${getText()}"
    }
  }

  val ttmgr = ToolTipManager.sharedInstance()

  ttmgr.registerComponent(nameField)
  ttmgr.registerComponent(typeField)
  ttmgr.registerComponent(flagsField)

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
    if (FieldView.this.width < 150 || controller.viewMgr.isIconOverrideActive) {
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
      fieldPanel.add(typeField)
      fieldPanel.add(flagsField)
      fieldPanel.validate()
      fieldPanel.repaint()
    }
  }

  def setSimple() {
    if (fieldPanel != null) {
      fieldPanel.removeAll()
      fieldPanel.add(nameField)
      fieldPanel.add(typeField)
      //fieldPanel.add(flagsField)
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

        if (!FieldView.this.isSelected()) {
          controller.select(field, true, false)
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
        FieldView.this.requestFocus()
        //transferFocus()
      }
    })

    setOpaque(false)

  }

  override def rescaleChildren() {
    // Currently does nothing
  }

}
