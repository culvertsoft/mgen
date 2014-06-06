package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Color
import java.awt.Dimension

import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.control.ControllerListener
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.view.autobox2.AutoBoxListener
import se.culvertsoft.mgen.visualdesigner.view.autobox2.EntityAutoBoxItem
import se.culvertsoft.mgen.visualdesigner.view.autobox2.SelectViewRootAutoBox2

class AddressField(controller: Controller) extends JPanel {
  setLayout(new MigLayout("insets 3 3 5 3"))

  val prefix = "Viewing: "
  val label = new JLabel(prefix)
  val selectBtn = new SelectViewRootAutoBox2(controller) {
    addObserver(new AutoBoxListener[EntityAutoBoxItem]() {
      override def finishedItem(t: EntityAutoBoxItem) {
        controller.model.getEntity(t.id) match {
          case Some(entity) => controller.viewMgr.setViewRoot(entity)
          case _ =>
        }
      }
    })

    override def getToolTipText(): String = {
      "Type or select view root"
    }

  }

  val textField = new JTextField("?")
  textField.setEnabled(false)
  textField.setDisabledTextColor(Color.BLACK)
  textField.setPreferredSize(new Dimension(99999, selectBtn.getPreferredSize().height))

  add(label, "gapleft 10, align right")
  add(textField, "gapleft 2, grow")
  add(selectBtn, "gapleft 2")

  controller.addObserver(new ControllerListener() {
    override def onViewRootChanged() {
      textField.setText(" " + getAddressString(controller.viewMgr.root))
    }
  })

  def getAddressString(entity: Entity): String = {
    controller.model.parentOf(entity) match {
      case Some(parent) => getAddressString(parent) + " -> " + entity.getName()
      case _ => entity.getName()
    }
  }

}