package se.culvertsoft.mgen.visualdesigner.view.autobox2

import se.culvertsoft.mgen.visualdesigner.classlookup.Type2String
import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.ListOrArrayType
import se.culvertsoft.mgen.visualdesigner.model.MapType
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.SimpleType
import se.culvertsoft.mgen.visualdesigner.control.Controller

case class FieldTypeAutoBoxItem(val fieldType: FieldType, controller: Controller) extends AutoBoxItem {

  override def deepCopy(): FieldTypeAutoBoxItem = {
    FieldTypeAutoBoxItem(fieldType.deepCopy(), controller)
  }

  override def toString(): String = {
    implicit val model = controller.model
    FTOps.toString(fieldType, Type2String.apply)
  }

  override def tooltipString(): String = {
    implicit val model = controller.model
    FTOps.toString(fieldType, Type2String.long)
  }

  override def removeLast(): FieldTypeAutoBoxItem = {
    val x = FTOps.removeLast(fieldType)
    if (x != null)
      FieldTypeAutoBoxItem(x, controller)
    else
      null
  }

  override def completeWith(t: AutoBoxItem): FieldTypeAutoBoxItem = {
    complete(t.asInstanceOf[FieldTypeAutoBoxItem].fieldType)
  }

  override def isComplete(): Boolean = {
    FTOps.isComplete(fieldType)
  }

  override def matches(s: String): Boolean = {
    s.toLowerCase().startsWith(toString().toLowerCase())
  }

  private def complete(t: FieldType): FieldTypeAutoBoxItem = {
    val out = fieldType.deepCopy()
    FTOps.complete(out, t)
    FieldTypeAutoBoxItem(out, controller)
  }

  private def isComplete(t: FieldType): Boolean = {
    t match {
      case t: SimpleType => true
      case t: MapType => t.hasKeyType() && isComplete(t.getKeyType()) && t.hasValueType() && isComplete(t.getValueType())
      case t: ListOrArrayType => t.hasElementType() && isComplete(t.getElementType())
    }
  }

  override def idString(): String = "<no_id>"

}
