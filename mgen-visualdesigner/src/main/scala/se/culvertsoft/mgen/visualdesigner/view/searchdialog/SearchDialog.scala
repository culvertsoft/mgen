package se.culvertsoft.mgen.visualdesigner.view.searchdialog

import java.awt.event.KeyEvent
import java.util.regex.Pattern

import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.JTextField
import javax.swing.border.EmptyBorder
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import net.miginfocom.swing.MigLayout
import se.culvertsoft.mgen.visualdesigner.HotKey
import se.culvertsoft.mgen.visualdesigner.HotKey.toStroke
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.EnumEntry
import se.culvertsoft.mgen.visualdesigner.model.EnumType
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.Project
import se.culvertsoft.mgen.visualdesigner.util.MkActionListener
import se.culvertsoft.mgen.visualdesigner.view.MkAction
import se.culvertsoft.mgen.visualdesigner.view.Selectable
import se.culvertsoft.mgen.visualdesigner.view.autobox2.AutoBoxListener
import se.culvertsoft.mgen.visualdesigner.view.autobox2.Entity2String
import se.culvertsoft.mgen.visualdesigner.view.autobox2.EntityAutoBoxItem
import se.culvertsoft.mgen.visualdesigner.view.autobox2.FindEntityAutoComboBox2

trait FocusFordwardingTab {
  def forwardFocus()
}

class FilterPanel extends JPanel {
  setLayout(new MigLayout)
  val projectsCheckbox = new JCheckBox("project", false) { setToolTipText("Include projects in the search") }
  val moduleCheckbox = new JCheckBox("module", true) { setToolTipText("Include modules in the search") }
  val enumCheckbox = new JCheckBox("enum", true) { setToolTipText("Include enums in the search") }
  val classCheckbox = new JCheckBox("class", true) { setToolTipText("Include classes in the search") }
  val fieldCheckbox = new JCheckBox("field", false) { setToolTipText("Include fields in the search") }
  add(projectsCheckbox)
  add(moduleCheckbox)
  add(enumCheckbox)
  add(classCheckbox)
  add(fieldCheckbox)
}

class ByNamePanel(controller: Controller) extends JPanel with FocusFordwardingTab {
  setLayout(new MigLayout)

  val label = new JLabel("Pattern:")
  val textField = new JTextField
  val findNextBtn = new JButton("Find Next") { addActionListener(MkActionListener(next())) }
  val findPreviousBtn = new JButton("Find Previous") { addActionListener(MkActionListener(previous())) }
  val filterPanel = new FilterPanel

  val textFieldIM = textField.getInputMap()
  val textFieldAM = textField.getActionMap()

  textFieldIM.put(HotKey(KeyEvent.VK_ENTER), "next")
  textFieldAM.put("next", MkAction(next()))

  add(label, "align right")
  add(textField, "width 130::, growx, wrap")
  add(findNextBtn)
  add(findPreviousBtn, "wrap")
  add(filterPanel, "span 2")

  /**
   * **************************************************************
   *
   *
   * 						METHODS
   *
   * *************************************************************
   */

  def isAllowed(e: Entity): Boolean = {
    e match {
      case e: CustomTypeField => filterPanel.fieldCheckbox.isSelected()
      case e: EnumEntry => filterPanel.fieldCheckbox.isSelected()
      case e: EnumType => filterPanel.enumCheckbox.isSelected()
      case e: CustomType => filterPanel.classCheckbox.isSelected()
      case e: Module => filterPanel.moduleCheckbox.isSelected()
      case e: Project => filterPanel.projectsCheckbox.isSelected()
      case _ => false
    }
  }

  def findMatchingItems(entityStringifyer: Entity => String)(matchFcn: (String, String) => Boolean): Seq[Entity] = {
    controller.model.findEach[Entity](e => {
      if (isAllowed(e)) {
        val stringified = entityStringifyer(e)
        val stringPattern = textField.getText()
        if (stringPattern != null && matchFcn(stringPattern, stringified)) {
          true
        } else {
          false
        }
      } else {
        false
      }
    })
  }

  def shortNameOf(e: Entity): String = Entity2String.short(e.getId(), controller)
  def longNameOf(e: Entity): String = Entity2String.long(e.getId(), controller)

  def exact(pattern: String, name: String): Boolean = pattern == name
  def exactNoCase(pattern: String, name: String): Boolean = exact(pattern.toLowerCase(), name.toLowerCase())
  def contains(pattern: String, name: String): Boolean = name.contains(pattern)
  def containsNoCase(pattern: String, name: String): Boolean = contains(pattern.toLowerCase(), name.toLowerCase())

  def exactShortName(): Seq[Entity] = findMatchingItems(shortNameOf)(exact)
  def noCaseShortName(): Seq[Entity] = findMatchingItems(shortNameOf)(exactNoCase)
  def partialMatchShortName(): Seq[Entity] = findMatchingItems(shortNameOf)(contains)
  def partialMatchNoCaseShortName(): Seq[Entity] = findMatchingItems(shortNameOf)(containsNoCase)

  def exactLongName(): Seq[Entity] = findMatchingItems(longNameOf)(exact)
  def noCaseLongName(): Seq[Entity] = findMatchingItems(longNameOf)(exactNoCase)
  def partialMatchLongName(): Seq[Entity] = findMatchingItems(longNameOf)(contains)
  def partialMatchNoCaseLongName(): Seq[Entity] = findMatchingItems(longNameOf)(containsNoCase)

  def regexpMatch(stringiFyer: Entity => String): Seq[Entity] = {
    val text = textField.getText()
    if (text != null) {
      val pattern = Pattern.compile(text)
      findMatchingItems(stringiFyer) { (_, stringified) =>
        val matcher = pattern.matcher(stringified)
        matcher.matches
      }
    } else {
      Nil
    }
  }

  def regexpMatchShortName(): Seq[Entity] = {
    regexpMatch(shortNameOf)
  }

  def regexpMatchLongName(): Seq[Entity] = {
    regexpMatch(longNameOf)
  }

  def findMatches(): Seq[Entity] = {

    val testFcns = List(
      () => exactShortName,
      () => exactLongName,
      () => noCaseShortName,
      () => noCaseLongName,
      () => partialMatchShortName,
      () => partialMatchLongName,
      () => partialMatchNoCaseShortName,
      () => partialMatchNoCaseLongName,
      () => regexpMatchShortName,
      () => regexpMatchLongName)

    val results = testFcns.toStream.map(_())

    results.find(_.nonEmpty).getOrElse(Nil)

  }

  def firstSelectedAmong(es: Seq[Entity]): Option[Int] = {
    es
      .map(controller.viewMgr.view)
      .zipWithIndex
      .find(_._1 match {
        case e: Selectable => e.isSelected()
        case _ => false
      })
      .map(_._2)
  }

  def step(delta: Int) {

    val matches = findMatches()

    if (matches.nonEmpty) {
      firstSelectedAmong(matches) match {
        case Some(prevIndex) =>
          val newIndex = (prevIndex + matches.size + delta) % matches.size
          controller.viewMgr.find(matches(newIndex))
        case _ =>
          val newIndex = if (delta > 0) 0 else matches.size - 1
          controller.viewMgr.find(matches(newIndex))
      }
    }
  }

  def next() {
    step(1)
  }

  def previous() {
    step(-1)
  }

  override def forwardFocus() {
    textField.requestFocusInWindow()
  }

}

class FromListPanel(controller: Controller) extends JPanel with FocusFordwardingTab {
  setLayout(new MigLayout)

  val label = new JLabel("Select:")
  val filterPanel = new FilterPanel
  val typeBox = new FindEntityAutoComboBox2(controller) {
    override def allowFields(): Boolean = filterPanel.fieldCheckbox.isSelected()
    override def allowClasses(): Boolean = filterPanel.classCheckbox.isSelected()
    override def allowModules(): Boolean = filterPanel.moduleCheckbox.isSelected()
    override def allowProjects(): Boolean = filterPanel.projectsCheckbox.isSelected()

    addObserver(new AutoBoxListener[EntityAutoBoxItem]() {
      override def finishedItem(item: EntityAutoBoxItem) {
        controller.model.getEntity(item.id) match {
          case Some(entity) => controller.viewMgr.find(entity)
          case _ =>
        }
      }
    })

    override def setLastSelected(o: EntityAutoBoxItem) {
      super.setLastSelected(o)
    }

  }

  add(label, "align right")
  add(typeBox, "width 130::, growx, wrap")
  add(filterPanel, "span 2")

  /**
   * **************************************************************
   *
   *
   * 						METHODS
   *
   * *************************************************************
   */

  override def forwardFocus() {
    typeBox.requestFocusInWindow()
  }

}

class SearchDialog(controller: Controller) extends JDialog {

  // From list panel
  val tabbedPane = new JTabbedPane
  val searchByNameTab = new ByNamePanel(controller)
  val selectFromListTab = new FromListPanel(controller)

  // Finish adding components to top level
  tabbedPane.addTab("By Name", searchByNameTab)
  tabbedPane.addTab("From List", selectFromListTab)

  tabbedPane.addChangeListener(new ChangeListener() {
    override def stateChanged(e: ChangeEvent) {
      forwardFocus()
    }
  })

  // Top level configuration
  setTitle("Search for class, module or project")
  add(tabbedPane)
  setResizable(false)

  // Required to display at correct size
  getContentPane().asInstanceOf[JPanel].setBorder(new EmptyBorder(5, 0, 0, 0))
  pack()

  def exit() {
    this.setVisible(false)
  }

  def forwardFocus() {
    val tab =
      tabbedPane
        .getComponent(tabbedPane.getSelectedIndex())
        .asInstanceOf[FocusFordwardingTab]
    tab.forwardFocus()
  }

}