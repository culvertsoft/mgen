package se.culvertsoft.mgen.visualdesigner.control

import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

import scala.collection.JavaConversions.asScalaBuffer
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.Output
import se.culvertsoft.mgen.compiler.defaultparser.DefaultParser
import se.culvertsoft.mgen.compiler.defaultparser.FileUtils
import se.culvertsoft.mgen.compiler.defaultparser.Project2Xml
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter
import se.culvertsoft.mgen.visualdesigner.EntityFactory
import se.culvertsoft.mgen.visualdesigner.MGenClassRegistry
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.ModelConversion
import se.culvertsoft.mgen.visualdesigner.model.Project
import se.culvertsoft.mgen.visualdesigner.util.Util

class SaveController(controller: Controller, window: JFrame) extends SubController(controller) {

  val classRegistry = new MGenClassRegistry

  private var _currentSelectDirectory: Option[File] = None
  private var _currentSaveFile: Option[File] = None
  private var _currentSavedModel: Option[Model] = None

  val saveFileChooser = new JFileChooser(currentSaveLoadDir)
  val loadFileChooser = new JFileChooser(currentSaveLoadDir)
  val xmlFilter = new FileNameExtensionFilter(".xml (mgen compiler/human readable)", "xml");
  val jsonFilter = new FileNameExtensionFilter(".json (visual designer optimized)", "json");
  val xmlOrJsonFilter = new FileNameExtensionFilter(".xml (mgen compiler), .json(visual designer optimized)", "xml", "json");
  saveFileChooser.setAcceptAllFileFilterUsed(false)
  saveFileChooser.addChoosableFileFilter(jsonFilter)
  saveFileChooser.addChoosableFileFilter(xmlFilter)
  loadFileChooser.setAcceptAllFileFilterUsed(false)
  loadFileChooser.addChoosableFileFilter(xmlOrJsonFilter)

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

  def isJsonFile(file: File): Boolean = {
    isFileType(file, "json")
  }

  def isXmlFile(file: Option[File]): Boolean = {
    file.map(isXmlFile).getOrElse(false)
  }

  def isJsonFile(file: Option[File]): Boolean = {
    file.map(isJsonFile).getOrElse(false)
  }

  def appendExtension(file: File, filter: FileNameExtensionFilter): File = {
    val ext = filter.getExtensions().head
    if (file.getName().toLowerCase().endsWith(s".$ext")) {
      file
    } else {
      new File(s"${file.getPath()}.$ext")
    }
  }

  controller.addObserver(new ModelChangeListener() {
    override def onModelModified(isPreview: Boolean) {
      if (!controller.isBulkOperationActive) {
        updateWindowTitle()
      }
    }
  })

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

    if (isXmlFile(_currentSaveFile)) {
      saveFileChooser.setFileFilter(xmlFilter)
    } else if (isJsonFile(_currentSaveFile)) {
      saveFileChooser.setFileFilter(jsonFilter)
    }

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
      if (isJsonFile(file)) {
        tryLoadFromJson(file)
      } else if (isXmlFile(file)) {
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
        System.err.println(s"Could not load project file: $file");
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

        println("Saving model to : " + file.getAbsolutePath)

        val bos = new ByteArrayOutputStream

        if (isXmlFile(file)) {

          val apiProject = ModelConversion.vd2Api(controller.model)
          apiProject.setFilePath(file.getName)

          val sources = Project2Xml(apiProject)
          val dir = FileUtils.directoryOf(file.getPath)
          val sourceWithFullPath = sources map { s => new GeneratedSourceFile(dir, s.fileName, s.sourceCode) }

          Output.write(sourceWithFullPath)

        } else if (isJsonFile(file)) {

          val writer = new JsonWriter(bos, classRegistry, true)
          writer.writeMGenObject(controller.model.project)

          if (file.exists)
            file.delete()

          file.createNewFile()

          Util.manageFileOut(file)(_.write(bos.toByteArray))

        } else {
          throw new RuntimeException("Trying to save with unknown file extension")
        }

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
      System.exit(0);
    }
  }

  private def tryLoadFromJson(file: File): Try[Model] = {

    Try {
      Util.manage(new BufferedInputStream(new FileInputStream(file))) { fileInputStream =>

        val jsonReader = new JsonReader(fileInputStream, classRegistry)
        val model = new Model(jsonReader.readMGenObject().asInstanceOf[Project])

        model
      }
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