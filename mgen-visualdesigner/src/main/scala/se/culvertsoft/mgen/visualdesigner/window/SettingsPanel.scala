package se.culvertsoft.mgen.visualdesigner.window

import java.util.HashMap
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.ArrayBuffer
import javax.swing.JPanel
import net.miginfocom.swing.MigLayout
import javax.swing.JButton
import se.culvertsoft.mgen.visualdesigner.util.MkActionListener
import javax.swing.JLabel

class SettingsPanel(map: HashMap[String, String]) extends JPanel {
  setLayout(new MigLayout("wrap 1"))
  add(new JLabel("Settings"))
  val addButton = new JButton("Add Setting")
  addButton.addActionListener(MkActionListener {
    createSettingsRow("", "")
  })
  add(addButton)
  
  val settingsRows = ArrayBuffer[SettingsRow]()

  for ((key, value) <- map) {
    createSettingsRow(key, value)
  }

  def createSettingsRow(key: String, value: String) {
    val s = new SettingsRow(key, value) {
      override def onChange() {
        updateMap()
      }
    }
    settingsRows += s
    add(s)
    revalidate()
  }

  def updateMap() {
    map.clear()
    settingsRows.map { row =>
      if ("" != row.getKey) {
        map.put(row.getKey, row.getValue)
      }
    }
    revalidate()
  }
}