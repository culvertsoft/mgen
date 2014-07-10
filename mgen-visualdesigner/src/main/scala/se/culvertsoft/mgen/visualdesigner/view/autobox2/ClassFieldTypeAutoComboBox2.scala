package se.culvertsoft.mgen.visualdesigner.view.autobox2

import FieldTypes.genericTypeTemplates
import FieldTypes.simpleTypes
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomType

import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.Int8Type
import se.culvertsoft.mgen.visualdesigner.model.MapType
import se.culvertsoft.mgen.visualdesigner.model.UserTypeRef

class ClassFieldTypeAutoComboBox2(t: FieldType, controller: Controller)
  extends AutoBox[FieldTypeAutoBoxItem](
    true,
    FieldTypeAutoBoxItem(t, controller),
    FieldTypeAutoBoxItem(new Int8Type, controller)) {
  import FieldTypes._

  override def updateSuggestions() {
    val prevSelected = getSelectedCore()
    if (build != null) {
      val last = FTOps.lastNode(build.fieldType)
      last.child match {
        case t: MapType => updateSuggestions(t.hasKeyType(), t.hasKeyType(), true)
        case t => updateSuggestions(true, true, true)
      }
    } else {
      updateSuggestions(true, true, true)
    }
    setSelectedItem(prevSelected)
  }

  def updateSuggestions(
    allowCustom: Boolean,
    allowGeneric: Boolean,
    allowSimple: Boolean) {

    val prevBuild = if (build != null) build.deepCopy() else null
    val prevSelected = if (selected().isDefined) selected().get.deepCopy() else null

    cmpModel.removeAllElements()

    // Always Add simple types
    if (allowSimple) {
      for (t <- simpleTypes.map(FieldTypeAutoBoxItem(_, controller))) {
        cmpModel.addElement(t)
      }
    }

    if (allowCustom) {
      controller.model.foreach(_.child match {
        case e: CustomType =>
          val t = FieldTypeAutoBoxItem(new UserTypeRef(e.getId()), controller)
          cmpModel.addElement(t)
        case _ =>
      })
    }

    if (allowGeneric) {
      for (t <- genericTypeTemplates().map(FieldTypeAutoBoxItem(_, controller))) {
        cmpModel.addElement(t)
      }
    }

    if (prevSelected != null) {
      cmpModel.setSelectedItem(prevSelected)
    }

    if (prevBuild != null) {

      cmpDocument.setText(prevBuild.toString())
      setBuild(prevBuild)
    } else if (prevSelected != null) {

      cmpDocument.setText(prevSelected.toString())
    }

  }

}