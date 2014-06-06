package se.culvertsoft.mgen.visualdesigner.window

import javax.swing.JPanel
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout
import se.culvertsoft.mgen.visualdesigner.util.MkActionListener
import se.culvertsoft.mgen.visualdesigner.util.MkDocumentListener
import java.awt.Dimension

abstract class SettingsRow(key: String, value: String) extends JPanel {
  setLayout(new MigLayout("insets 0 0 0 0, wrap 2"))
  
  val keyTF = new JTextField(key)
  keyTF.getDocument().addDocumentListener(MkDocumentListener{
    onChange()
  })
  keyTF.setPreferredSize(new Dimension(1000, 10))
  
  val valueTF = new JTextField(value)
  valueTF.getDocument().addDocumentListener(MkDocumentListener{
    onChange()
  })
  valueTF.setPreferredSize(new Dimension(1000, 10))
  
  add(keyTF, "growx")
  add(valueTF, "growx")
  
  def onChange()

  def getKey() = keyTF.getText()
  def getValue() = valueTF.getText()
}
