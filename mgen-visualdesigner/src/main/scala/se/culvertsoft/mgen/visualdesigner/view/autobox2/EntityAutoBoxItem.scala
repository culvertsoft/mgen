package se.culvertsoft.mgen.visualdesigner.view.autobox2

import se.culvertsoft.mgen.visualdesigner.classlookup.Type2String
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.EntityId
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase

object Entity2String {

  def short(id: EntityIdBase, controller: Controller): String = {
    implicit val model = controller.model
    model.getEntity(id) match {
      case Some(entity: CustomTypeField) =>
        controller.model.parentOf(entity) match {
          case Some(clas) => s"${clas.getName()}.${entity.getName()}"
          case _ => s"<unknown_class>.${entity.getName()}"
        }
      case Some(entity) =>
        entity.getName()
      case _ => ""
    }
  }

  def long(id: EntityIdBase, controller: Controller): String = {
    implicit val model = controller.model
    model.getEntity(id) match {
      case Some(entity) =>
        Type2String.getClassPath(entity)
      case _ => ""
    }
  }

}

case class EntityAutoBoxItem(val id: EntityIdBase, controller: Controller) extends AutoBoxItem {

  override def deepCopy(): EntityAutoBoxItem = {
    EntityAutoBoxItem(id.deepCopy(), controller)
  }

  override def toString(): String = {
    Entity2String.short(id, controller)
  }

  override def tooltipString(): String = {
    Entity2String.long(id, controller)
  }

  override def removeLast(): EntityAutoBoxItem = {
    null
  }

  override def completeWith(t: AutoBoxItem): AutoBoxItem = {
    t.deepCopy()
  }

  override def isComplete(): Boolean = {
    true
  }

  override def matches(s: String): Boolean = {
    s.toLowerCase().startsWith(toString().toLowerCase())
  }

  override def idString(): String = id.toString()

}