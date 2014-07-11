package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D

import scala.collection.JavaConversions.asScalaBuffer
import scala.language.reflectiveCalls

import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.ScrollPaneConstants
import javax.swing.SwingConstants
import net.miginfocom.swing.MigLayout
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.NoType
import se.culvertsoft.mgen.visualdesigner.model.UserTypeRef
import se.culvertsoft.mgen.visualdesigner.view.autobox2.AutoBoxListener
import se.culvertsoft.mgen.visualdesigner.view.autobox2.FieldTypeAutoBoxItem
import se.culvertsoft.mgen.visualdesigner.view.autobox2.SuperTypeAutoComboBox2

object ClassView {
  val BG_COLOR = new Color(210, 210, 210)
}

class ClassView(override val entity: CustomType, controller: Controller)
  extends ScrollableView(entity, controller)
  with BackGrounded
  with Bordered
  with Labeled
  with Selectable
  with SelectionBoxable
  with Inheritable
  with Movable
  with Resizeable
  with CursorChanging {

  override def onEntityModified(child: Entity, validate: Boolean, parent: Option[Entity]) {
    super.onEntityModified(child, validate, parent)

    if (child eq entity) {
      nameLabel.setText(nameLabel.getText())
      nameLabel.updateTooltipText()
      superTypeComboBox.onNewSuperType(entity.getSuperType())
      if (validate)
        super.validate()
      super.repaint()
    }

    if (entity.hasSuperType()) {
      if (controller.model.existsReference(entity.getSuperType(), child)) {
        superTypeComboBox.update()
      }
    }
  }

  val abstractCheckBox = new JCheckBox() {
    addMouseListener(selectMouseListenerWOFocus)
  }

  val nameLabel = new JLabel {
    override def getText(): String = entity.getName()
    addMouseListener(selectMouseListenerWFocus)
    addMouseListener(moveByMouseListener)
    addMouseListener(renameDblClickMouseListener)
    override def getMinimumSize(): Dimension = {
      new Dimension(30, super.getMinimumSize().height)
    }
    def updateTooltipText() {
      setToolTipText(s"class name: ${entity.getName}")
    }
    updateTooltipText()
    setHorizontalAlignment(SwingConstants.CENTER)
  }

  val colonLabel = new JLabel(":") {
    addMouseListener(selectMouseListenerWFocus)
    addMouseListener(moveByMouseListener)
    addMouseListener(renameDblClickMouseListener)
  }

  val superTypeComboBox = new SuperTypeAutoComboBox2(entity, controller) {
    addExternalMouseListener(selectMouseListenerWOFocus)
    addMouseListener(renameDblClickMouseListener)
    override def getMinimumSize(): Dimension = {
      new Dimension(0, super.getMinimumSize().height)
    }
    addObserver(new AutoBoxListener[FieldTypeAutoBoxItem]() {
      override def finishedItem(item: FieldTypeAutoBoxItem) {
        println("Finished super")
        item.fieldType match {
          case fieldType: UserTypeRef =>
            val t = controller.model.getEntity(fieldType.getId()).get.asInstanceOf[CustomType]
            controller.changeSuperType(entity, t)
          case fieldType: NoType =>
            controller.removeSuperTypeOf(entity)
        }
      }
    })
    /* override def getPreferredSize(): Dimension = {
         new Dimension(80, super.getMinimumSize().height)
      }*/
    setMaximumSize(new Dimension(9999, colonLabel.getPreferredSize().height))

  }

  override def desiredViewComplexity(): ViewComplexity = {
    if (ClassView.this.width < 100 || controller.viewMgr.isIconOverrideActive) {
      VIEW_COMPLEXITY_SIMPLE
    } else {
      VIEW_COMPLEXITY_COMPLEX
    }
  }

  override def onViewComplexityChange(complexity: ViewComplexity) {
    complexity match {
      case VIEW_COMPLEXITY_SIMPLE => setSimple()
      case _ => setComplex()
    }
  }

  //superTypeComboBox.setMaximumSize(new Dimension(width() / 3, superTypeComboBox.getPreferredSize().height))
  superTypeComboBox.setOpaque(false)
  abstractCheckBox.setOpaque(false)
  abstractCheckBox.setToolTipText("abstract")

  scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)

  val layout = new ClassOrEnumViewLayout(this, () => entity.getFields(), controller)
  innerPanel().setLayout(layout)

  resetViewComplexity()

  /**
   * ***************************************************************************
   *
   *
   * 							METHODS
   *
   * **************************************************************************
   */

  def setSimple() {
    if (labelPanel != null) {
      labelPanel.removeAll()
      labelPanel.setLayout(new MigLayout("center, insets 2 0 0 0"))
      labelPanel.add(nameLabel, "align center")
      labelPanel.validate()
      labelPanel.repaint()
    }
  }

  def setComplex() {
    if (labelPanel != null) {
      labelPanel.removeAll()
      labelPanel.setOpaque(false)
      labelPanel.setLayout(new MigLayout("center, insets 2 0 0 0"))
      //labelPanel.add(abstractCheckBox, " width 5%, gapleft 5")
      labelPanel.add(nameLabel, "gapleft 5")
      labelPanel.add(colonLabel, "gapleft 3")
      labelPanel.add(superTypeComboBox, "gapleft 5, width 30%" /*, width 30%, wrap, "gapleft 10, width 30%!"*/ )
      labelPanel.validate()
      labelPanel.repaint()
    }
  }

  override def labelWidth(i: Int = labelTextWidth): Int = {
    width - 1
  }

  override def labelText(): String = {
    entity.getName()
  }

  override def labelBackgroundColor(): Color = {
    ClassView.BG_COLOR
  }

  override def backgroundColor(): Color = {
    ClassView.BG_COLOR
  }

  override def resizeableAtCorners(): Boolean = {
    true
  }

  override def resizeableEW(): Boolean = {
    true
  }

  override def resizeableNS(): Boolean = {
    false
  }

  override def initRename() {
    controller.renameSelection()
  }

  override protected def drawScrollpaneBorder(g: Graphics2D) {
    drawBorder(g)
  }

  override protected def drawInnerPanelComponent(g: Graphics2D) {
    drawBackground(g)
  }

  override def setBounds(x: Int, y: Int, width: Int, height: Int) {
    /*   if (superTypeComboBox != null)
         superTypeComboBox.setMaximumSize(new Dimension(width / 4, superTypeComboBox.getPreferredSize().height))
  */ super.setBounds(x, y, width, height)
    innerPanel.setBounds(innerPanel.getX(), innerPanel.getY(), width, innerPanel.getHeight())
  }

  override def drawLabelText(g: Graphics2D) {

  }

  override def rescaleChildren() {
    // Currently does nothing
  }

}
