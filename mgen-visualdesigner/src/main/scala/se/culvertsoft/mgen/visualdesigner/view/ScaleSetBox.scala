package se.culvertsoft.mgen.visualdesigner.view

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.control.ControllerListener

class ScaleSetBox(c: Controller) extends JComboBox[String] {

  private val stringScaleLevels = ZoomLevels.levels.map(x => (x * 100.0).toInt.toString() + "%")
  private val cbModel = getModel().asInstanceOf[DefaultComboBoxModel[Object]]

  stringScaleLevels foreach cbModel.addElement
  cbModel.setSelectedItem("100%")

  c.addObserver(new ControllerListener() {
    override def onScaleFactorChanged() {
      cbModel.setSelectedItem(stringScaleLevels(c.viewMgr.scaleFactorIndex))
    }
  })

  addActionListener(new ActionListener() {
    override def actionPerformed(e: ActionEvent) {
      if (!c.isTriggeringObservers()) {
        val s = getSelectedItem().toString.filterNot(_ == '%')
        val decimal = s.toDouble / 100.0
        c.viewMgr.setScaleFactor(decimal)
      }
    }
  })

}