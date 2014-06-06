package se.culvertsoft.mgen.visualdesigner.view.autobox2

import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef
import se.culvertsoft.mgen.visualdesigner.model.NoType
import se.culvertsoft.mgen.visualdesigner.model.EntityId
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase

class SuperTypeAutoComboBox2(t: CustomType, controller: Controller)
  extends AutoBox[FieldTypeAutoBoxItem](
    true,
    FieldTypeAutoBoxItem(FieldTypes.getSuperType(t)(controller.model), controller),
    FieldTypeAutoBoxItem(new NoType, controller)) {
  import FieldTypes._

  override def getToolTipText(): String = {
    "super " + super.getToolTipText()
  }

  def reallyAddItem(item: FieldTypeAutoBoxItem): Boolean = {
    item match {
      case FieldTypeAutoBoxItem(e: CustomTypeRef, _) => controller.model.isInheritanceAllowed(t.getId(), e.getId())
      case _ => true
    }
  }

  private val noTypeItem = FieldTypeAutoBoxItem(new NoType, controller)

  override def updateSuggestions() {

    val keepSelected = if (selected().isDefined) selected().get.deepCopy() else null

    cmpModel.removeAllElements()
    cmpModel.addElement(noTypeItem)
    controller.model.foreach(_.child match {
      case e: CustomType =>
        val t = FieldTypeAutoBoxItem(new CustomTypeRef(e.getId()), controller)
        if (reallyAddItem(t)) {
          if (cmpModel.getIndexOf(t) == -1) {
            cmpModel.addElement(t)
          }
        }
      case _ =>
    })

    if (keepSelected != null) {
      cmpDocument.setText(keepSelected.toString())
      cmpModel.setSelectedItem(keepSelected)
    }
  }

  def onNewSuperType(id: EntityIdBase) {
    id match { // it may be null
      case id: EntityIdBase  => super.select(FieldTypeAutoBoxItem(new CustomTypeRef(id), controller))
      case _ => super.select(FieldTypeAutoBoxItem(new NoType, controller))
    }

  }

}