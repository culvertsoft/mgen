package se.culvertsoft.mgen.visualdesigner.window

import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout
import se.culvertsoft.mgen.visualdesigner.model.Generator
import se.culvertsoft.mgen.visualdesigner.util.MkActionListener
import se.culvertsoft.mgen.visualdesigner.util.MkDocumentListener
import javax.swing.JFileChooser
import javax.swing.BorderFactory
import java.awt.Color
abstract class GeneratorPanel(generator: Generator) extends JPanel() {
  setLayout(new MigLayout("inset 10 10 10 10"))
  setBorder(BorderFactory.createLineBorder(Color.black));
  val generatorClassName = createTextFieldWithLabel("Generator Class Name", generator.getGeneratorClassName, generator.setGeneratorClassName)
  val name = createTextFieldWithLabel("Name", generator.getName, generator.setName)
  val classRegistryPath = createTextFieldWithLabel("Class Registry Path", generator.getClassRegistryPath, generator.setClassRegistryPath)
  val generatorPath = createTextFieldWithLabel("Generator Path", generator.getGeneratorJarFileFolder, generator.setGeneratorJarFileFolder, true)
  val outputFolder = createTextFieldWithLabel("Output Folder", generator.getOutputFolder, generator.setOutputFolder, true)
  val settings = new SettingsPanel(generator.getSettingsMutable())

  val delButton = new JButton("Remove Generator")
  delButton.addActionListener(MkActionListener {
    onDelete()
  })

  add(generatorClassName, "growx, wrap")
  add(name, "growx, wrap")
  add(classRegistryPath, "growx, wrap")
  add(generatorPath, "growx, wrap")
  add(outputFolder, "growx, wrap")
  add(settings, "span 2, wrap")
  add(delButton)
  
  revalidate()
  
  def createTextFieldWithLabel(title: String, get: () => String, set: String => Unit, fileBrowser: Boolean = false): JPanel = {
    val pane = new JPanel()
    pane.setLayout(new MigLayout("inset 0 0 0 0"))
    val label = new JLabel(title)
    val textField = new JTextField(get()) {
      setPreferredSize(new Dimension(999999, 10))
      getDocument().addDocumentListener(MkDocumentListener {
        set(getText())
        onUpdate()
      })
    }

    pane.add(label, "width 200px!")

    if (fileBrowser) {
      val fileBrowserButton = new JButton("Select")
      fileBrowserButton.addActionListener(MkActionListener {
        val chooser = new JFileChooser();
        chooser.setDialogTitle("Select target directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
        val returnVal = chooser.showOpenDialog(getParent())
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          val text = chooser.getSelectedFile().getAbsolutePath()
          textField.setText(text)
          set(text)
          onUpdate()
        }
      })

      pane.add(textField, "growx")
      pane.add(fileBrowserButton)
    } else {
      pane.add(textField, "growx")
    }
    return pane
  }

  def onDelete()
  def onUpdate()
}