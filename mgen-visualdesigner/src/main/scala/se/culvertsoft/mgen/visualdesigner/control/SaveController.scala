package se.culvertsoft.mgen.visualdesigner.control

import java.io.File

import scala.collection.JavaConversions.asScalaBuffer
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter
import se.culvertsoft.mgen.compiler.Output
import se.culvertsoft.mgen.compiler.defaultparser.DefaultParser
import se.culvertsoft.mgen.compiler.defaultparser.FileUtils
import se.culvertsoft.mgen.compiler.defaultparser.Project2Xml
import se.culvertsoft.mgen.visualdesigner.EntityFactory
import se.culvertsoft.mgen.visualdesigner.ClassRegistry
import se.culvertsoft.mgen.visualdesigner.model.FilePath
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.ModelConversion
import se.culvertsoft.mgen.visualdesigner.model.Module

class SaveController(controller: Controller, window: JFrame) extends SubController(controller) {

  val classRegistry = new ClassRegistry

  private var _currentSelectDirectory: Option[File] = None
  private var _currentSaveFile: Option[File] = None
  private var _currentSavedModel: Option[Model] = None

  val saveFileChooser = new JFileChooser(currentSaveLoadDir)
  val loadFileChooser = new JFileChooser(currentSaveLoadDir)
  val xmlFilter = new FileNameExtensionFilter(".xml (mgen compiler)", "xml")
  saveFileChooser.setAcceptAllFileFilterUsed(false)
  saveFileChooser.addChoosableFileFilter(xmlFilter)
  loadFileChooser.setAcceptAllFileFilterUsed(false)
  loadFileChooser.addChoosableFileFilter(xmlFilter)
  saveFileChooser.setFileFilter(xmlFilter)
  loadFileChooser.setFileFilter(xmlFilter)

  controller.addObserver(new ModelChangeListener() {
    override def onModelModified(isPreview: Boolean) {
      if (!controller.isBulkOperationActive) {
        updateWindowTitle()
      }
    }
  })

  /**
   * ***********************************************************
   *
   *
   * 					METHODS
   *
   * **********************************************************
   */

  def isFileType(file: File, ext: String): Boolean = {
    file != null && file.getName().toLowerCase().endsWith(s".$ext")
  }

  def isXmlFile(file: File): Boolean = {
    isFileType(file, "xml")
  }

  def isXmlFile(file: Option[File]): Boolean = {
    file.map(isXmlFile).getOrElse(false)
  }

  def appendExtension(file: File, filter: FileNameExtensionFilter): File = {
    val ext = filter.getExtensions().head
    if (file.getName().toLowerCase().endsWith(s".$ext")) {
      file
    } else {
      new File(s"${file.getPath()}.$ext")
    }
  }

  def confirmOverwrite(): Boolean = {

    val saveQuestionReply = controller.viewMgr.showConfirmDialog(
      "That file already exists, do you want to overwrite it?",
      "Confirm overwrite",
      JOptionPane.YES_NO_CANCEL_OPTION,
      JOptionPane.QUESTION_MESSAGE)

    saveQuestionReply match {
      case JOptionPane.YES_OPTION => true
      case _ => false
    }
  }

  def saveAs(): Boolean = {

    saveFileChooser.setCurrentDirectory(currentSaveLoadDir)

    val returnVal = controller.viewMgr.showSaveDialog(saveFileChooser)
    if (returnVal == JFileChooser.APPROVE_OPTION) {

      val filter = saveFileChooser.getFileFilter().asInstanceOf[FileNameExtensionFilter]

      val file = appendExtension(saveFileChooser.getSelectedFile(), filter)
      if (file.exists() && !confirmOverwrite()) {
        false
      } else {

        resetSave()
        _currentSaveFile = Some(file)
        _currentSelectDirectory = Some(file)

        saveToCurrentFile()

        true
      }

    } else {
      println("User aborted save file selection")
      false
    }
  }

  def getCurrentSaveFilePath(): String = {
    _currentSaveFile match {
      case Some(file) => file.getAbsolutePath()
      case _ => "unsaved"
    }
  }

  def currentSaveLoadDir(): File = {
    _currentSelectDirectory.getOrElse(null)
  }

  def loadFromFile() {

    def loadFunc {

      loadFileChooser.setCurrentDirectory(currentSaveLoadDir)

      val returnVal = controller.viewMgr.showOpenDialog(loadFileChooser)

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        load(loadFileChooser.getSelectedFile())
      } else {
        println("User aborted load file selection")
      }
    }

    if (isModified()) {
      checkSaveBeforeContinue(
        "Exit",
        "Do you want to save your project before loading a new file?",
        loadFunc)

    } else {
      loadFunc
    }
  }

  def isModified(): Boolean = {
    _currentSavedModel match {
      case Some(model) => model != controller.model
      case _ => true
    }
  }

  def load(file: File): Boolean = {

    _currentSelectDirectory = Some(file)

    println(s"Loading file: $file")

    val optLoadedModel =
      if (isXmlFile(file)) {
        tryLoadFromXml(file)
      } else {
        throw new RuntimeException(s"Tried to load unknown file type $file")
      }

    optLoadedModel match {

      case Success(model) =>
        controller.setModel(model, true)
        _currentSavedModel = Some(model.deepCopy())
        _currentSaveFile = Some(file)

        controller.viewMgr.configureViewOnLoadedNewFile()
        println(s"Loaded project file file: $file")

      case Failure(failure) =>
        System.err.println(s"Could not load project file: $file")
        failure.printStackTrace()

    }

    updateWindowTitle()
    controller.triggerObservers(_.onNewModel())
    return true
  }

  def save(): Boolean = {
    _currentSaveFile match {
      case Some(file) if (isModified()) => saveToCurrentFile()
      case Some(file) => true
      case None => saveAs()
    }
  }

  private def updateWindowTitle() {
    if (!controller.isBulkOperationActive) {
      window.setTitle("MGen Visual Designer:    " + getCurrentSaveFilePath() + (if (isModified()) " * " else ""))
    }
  }

  private def saveToCurrentFile(): Boolean = {

    _currentSaveFile match {
      case Some(file) if (isModified()) =>

        val project = controller.model.project
        val newAbsPath = file.getCanonicalPath()
        val oldAbsPath = project.getFilePath().getAbsolute()

        println(s"Saving model to: $newAbsPath")

        if (!isXmlFile(file))
          throw new RuntimeException("Trying to save with unknown file extension")

        if (FileUtils.directoryOf(newAbsPath) != FileUtils.directoryOf(oldAbsPath)) {

          project.getFilePath().setWritten("").setAbsolute(newAbsPath)

          val saveDir = new FilePath("", FileUtils.directoryOf(newAbsPath))
          def setSaveDirOfModules(ms: Seq[Module]) {
            for (m <- ms) {
              setSaveDirOfModules(m.getSubmodules())
              m.setSaveDir(saveDir)
            }
          }

          setSaveDirOfModules(project.getModules())
        }

        val apiProject = ModelConversion.vd2Api(controller.model)
        apiProject.setFilePath(file.getPath())
        apiProject.setAbsoluteFilePath(file.getCanonicalPath())

        val sources = Project2Xml(apiProject)

        Output.write(sources)

        _currentSavedModel = Some(controller.model.deepCopy())
        updateWindowTitle()

        true

      case Some(file) => true
      case None => false
    }

  }

  def checkSaveBeforeContinue(
    title: String,
    message: String,
    onContinue: => Unit = {},
    onCancel: => Unit = {}) {

    if (isModified()) {

      val saveQuestionReply = controller.viewMgr.showConfirmDialog(
        message,
        title,
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE)

      saveQuestionReply match {
        case JOptionPane.YES_OPTION => { if (save) onContinue }
        case JOptionPane.NO_OPTION => onContinue
        case _ => onCancel
      }

    } else {
      onContinue
    }

  }

  def resetSave() {
    _currentSaveFile = None
    _currentSavedModel = None
  }

  def startNewProject() {
    checkSaveBeforeContinue(
      "Confirm new project",
      "Do you want to save your changes before starting a new project?",
      forceStartNewProject())
  }

  def forceStartNewProject() {
    resetSave()
    controller.setModel(EntityFactory.mkModel(), true)
    controller.triggerObservers(_.onNewModel())
  }

  def closeApplication() {

    if (isModified()) {

      checkSaveBeforeContinue(
        "Exit",
        "Do you want to save your project before exiting?",
        System.exit(0))

    } else if (controller.viewMgr.showConfirmDialog(
      "Are you sure you want to exit?",
      "Exit",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
      System.exit(0)
    }
  }

  private def tryLoadFromXml(file: File): Try[Model] = {

    Try {

      val parser = new DefaultParser(true)

      val settings = new java.util.HashMap[String, String]
      settings.put("project", file.getPath())

      val project = parser.parse(settings)

      println(s"Loading xml project: ${project.name}")

      import scala.collection.JavaConversions.asScalaBuffer
      if (project.dependencies.nonEmpty) {
        System.err.println("WARNING: Project contains dependencies. This feature is not yet supported by MGenVisualDesigner.")
      }

      println(s"Converting xml project to MGenVisualDesigner format")

      ModelConversion.api2Vd(project)

    }

  }

}