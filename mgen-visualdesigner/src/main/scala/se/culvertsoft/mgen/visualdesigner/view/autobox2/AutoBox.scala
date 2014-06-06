package se.culvertsoft.mgen.visualdesigner.view.autobox2

import java.awt.Dimension
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseListener

import scala.reflect.ClassTag
import scala.util.Failure
import scala.util.Try

import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.ToolTipManager
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import javax.swing.plaf.metal.MetalComboBoxUI
import javax.swing.text.JTextComponent
import se.culvertsoft.mgen.visualdesigner.HotKey
import se.culvertsoft.mgen.visualdesigner.HotKey.toStroke
import se.culvertsoft.mgen.visualdesigner.util.Observable
import se.culvertsoft.mgen.visualdesigner.util.OperationStatus
import se.culvertsoft.mgen.visualdesigner.view.MkAction

abstract class AutoBox[ItemType <: AutoBoxItem: ClassTag](
  removeArrow: Boolean,
  item0: ItemType,
  fallbackItem: ItemType)
  extends JComboBox[ItemType]
  with Observable[AutoBoxListener[ItemType]] {

  private val inhitibInput = new OperationStatus
  private val layingOut = new OperationStatus
  private val focusCyclingToReOpenList = new OperationStatus

  private var _cancelled = false
  private var _finished: ItemType = item0.deepCopy().asInstanceOf[ItemType]
  private var _build: ItemType = null.asInstanceOf[ItemType]
  private var _lastSelected: ItemType = null.asInstanceOf[ItemType]

  if (removeArrow) {
    // Hack to get around the bullshit of the jcombobox button
    // Create it to avoid nullptr exceptions in the superclass,
    // but don't add it
    setUI(new MetalComboBoxUI() {
      override def createArrowButton(): JButton = {
        new JButton() {
          override def getWidth(): Int = 0
        }
      }
      override def installComponents() {
        arrowButton = createArrowButton();
        if (comboBox.isEditable())
          addEditor();
        comboBox.add(currentValuePane);
      }
    })
  }

  val cmpModel = new AutoBoxModel(this)
  val cmpEditor = getEditor().getEditorComponent().asInstanceOf[JTextComponent]
  val cmpDocument = new AutoBoxDocument(this, cmpModel)
  val im = cmpEditor.getInputMap()
  val am = cmpEditor.getActionMap()

  setModel(cmpModel)
  setRenderer(new AutoBoxRenderer)
  cmpEditor.setDocument(cmpDocument)

  im.put(HotKey(KeyEvent.VK_ESCAPE), "cancel")
  am.put("cancel", MkAction(escCancel()))

  def escCancel() {
    if (!isPopupVisible())
      triggerObservers(_.requestFocusUp())
    cancel()
  }

  cmpEditor.addKeyListener(new KeyAdapter() {
    override def keyPressed(e: KeyEvent) {
      if (!isPopupVisible() && e.getKeyCode() == KeyEvent.VK_ENTER) {
        cmpEditor.transferFocus()
        return
      }
      if (!isPopupVisible() && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
        setPopupVisible(true)
      }
    }
  })

  val editBorder = cmpEditor.getBorder()

  setEditorGfx(false)

  cmpEditor.addFocusListener(new FocusAdapter() {
    override def focusGained(e: FocusEvent) {
      if (!isPopupVisible())
        setPopupVisible(true)
      if (!isFocusCyclingToReopenList()) {
        setEditorGfx(true)
        triggerObservers(_.focusGained(e))
      }
      focusCyclingToReOpenList.disable()
    }
    override def focusLost(e: FocusEvent) {
      if (!isFocusCyclingToReopenList()) {
        setEditorGfx(false)
        if (build != null && !isComplete())
          cancel()
      }
    }
  })

  addPopupMenuListener(new PopupMenuListener() {

    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
      inhibitInput {
        updateSuggestions()
      }
      if (build == null || isComplete()) {
        cmpDocument.highlightCompletedText(0)
      }
    }

    override def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {
      handleFinalInput()
    }

    override def popupMenuCanceled(e: PopupMenuEvent) {
      cancel()
    }

  })

  cmpDocument.setText(finished.toString())
  cmpDocument.highlightCompletedText(0)

  ToolTipManager.sharedInstance().registerComponent(this)

  setEditable(true)

  /**
   * ******************************************************
   *
   *
   * 							METHODS
   *
   * ******************************************************
   */

  def hideEditorGfx(): Boolean = {
    true
  }

  def setEditorGfx(state: Boolean) {
    if (hideEditorGfx) {
      if (state) {
        cmpEditor.setOpaque(true)
        cmpEditor.setBorder(editBorder)
      } else {
        cmpEditor.setOpaque(false)
        cmpEditor.setBorder(null)
      }
    }
  }

  def select(item: ItemType) {
    _lastSelected = item
    _finished = item
    cmpModel.setSelectedItem(item)
    cmpDocument.setText(item.toString())
    cmpDocument.highlightCompletedText(0)
    setToolTipText(getToolTipText)
  }
  
  def update() {
    select(finished)
  }

  def handleFinalInput() {

    safe {

      if (isCancelled()) {
        _cancelled = false
        _build = null.asInstanceOf[ItemType]
        cmpDocument.setText(finished.toString())
        cmpDocument.highlightCompletedText(0)
        return
      }

      if (build != null) {
        if (!build.isComplete() && selected().isDefined) {
          _build = build.completeWith(selected().get.deepCopy().asInstanceOf[ItemType]).asInstanceOf[ItemType]
        }
      } else {
        if (selected().isDefined) {
          _build = selected().get.deepCopy().asInstanceOf[ItemType]
        }
      }

      if (isComplete()) {
        _finished = build.deepCopy().asInstanceOf[ItemType]
        _build = null.asInstanceOf[ItemType]
        cmpDocument.setText(_finished.toString())
        cmpDocument.highlightCompletedText(0)
        triggerObservers(_.finishedItem(finished))
      } else {
        cmpModel.setSelectedItem(fallbackItem.deepCopy().asInstanceOf[ItemType])
        cmpDocument.highlightCompletedText(cmpDocument.getLength())
        if (cmpEditor.hasFocus()) {
          focusCyclingToReOpenList.enable()
          cmpEditor.transferFocus()
          cmpEditor.requestFocusInWindow()
        }
      }
    }

    setToolTipText(getToolTipText())
  }

  def setLastSelected(o: ItemType) {
    _lastSelected = o
  }

  def finished() = {
    _finished
  }

  def lastSelected() = {
    _lastSelected
  }

  def build() = {
    _build
  }

  def isFocusCyclingToReopenList(): Boolean = {
    focusCyclingToReOpenList.isActive()
  }

  def isComplete(): Boolean = {
    build != null && build.isComplete()
  }

  override def doLayout() {
    layingOut active {
      super.doLayout();
    }
  }

  override def getSize(): Dimension = {
    val dim = super.getSize();
    if (!layingOut.isActive())
      dim.width = Math.max(dim.width, getPreferredSize().width);
    return dim;
  }

  def addExternalMouseListener(l: MouseListener) {
    cmpEditor.addMouseListener(l)
    // if (_arrowBtn != null)
    //      _arrowBtn.addMouseListener(l)
  }

  def cancel() {
    _cancelled = true
    _build = null.asInstanceOf[ItemType]
    _lastSelected = finished().deepCopy().asInstanceOf[ItemType]
    inhitibInput.disable()
    setSelectedItem(finished())
    cmpDocument.setText(finished.toString())
    setPopupVisible(false)
  }

  def isCancelled(): Boolean = {
    _cancelled
  }

  def isInputInhibited(): Boolean = {
    inhitibInput.isActive()
  }

  override def getToolTipText(): String = {
    s"type: ${finished.tooltipString}"
  }

  def safe(f: => Unit) {
    Try {
      f
    } match {
      case Failure(err) =>
        err.printStackTrace()
        cmpDocument.revert()
      case _ =>
    }
  }

  def setBuild(b: ItemType) {
    _build = b
  }

  def inhibitInput(f: => Unit) {
    inhitibInput active {
      f
    }
  }

  def selected(): Option[ItemType] = {
    getSelectedItem() match {
      case st: ItemType => Some(st)
      case _ => None
    }
  }

  def getSelectedCore(): ItemType = {
    if (build != null)
      build.deepCopy().asInstanceOf[ItemType]
    else if (selected().isDefined)
      selected().get.deepCopy().asInstanceOf[ItemType]
    else
      finished.deepCopy().asInstanceOf[ItemType]
  }

  // checks if str1 starts with str2 - ignores case
  def startsWithIgnoreCase(str1: String, str2: String): Boolean = {
    str1.toUpperCase().startsWith(str2.toUpperCase())
  }

  /**
   * **********************************************************************
   *
   *
   * 						ABSTRACT METHODS
   *
   * *********************************************************************
   */

  def updateSuggestions()

}