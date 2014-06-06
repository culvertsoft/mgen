package se.culvertsoft.mgen.visualdesigner.view.autobox2

import scala.reflect.ClassTag

import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase
import se.culvertsoft.mgen.visualdesigner.model.EntityId
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.Project

abstract class FindEntityAutoComboBox2(controller: Controller)
  extends AutoBox[EntityAutoBoxItem](
    false,
    EntityAutoBoxItem(new EntityId(0L, 0L), controller),
    EntityAutoBoxItem(new EntityId(0L, 0L), controller)) {
  import FieldTypes._

  def allowFields(): Boolean
  def allowClasses(): Boolean
  def allowModules(): Boolean
  def allowProjects(): Boolean

  private def addAll[T <: Entity: ClassTag]() {
    controller.model.foreach(_.child match {
      case e: T =>
        val t = EntityAutoBoxItem(e.getId(), controller)
        cmpModel.addElement(t)
      case _ =>
    })
  }

  override def hideEditorGfx(): Boolean = {
    false
  }

  override def updateSuggestions() {

    val keepSelected = if (selected().isDefined) selected().get.deepCopy() else null

    cmpModel.removeAllElements()

    if (allowFields) {
      addAll[CustomTypeField]()
    }

    if (allowClasses) {
      addAll[CustomType]()
    }

    if (allowModules) {
      addAll[Module]()
    }

    if (allowProjects) {
      addAll[Project]()
    }

    if (keepSelected != null) {
      cmpModel.setSelectedItem(keepSelected)
      cmpDocument.setText(keepSelected.toString())
    }

  }

}