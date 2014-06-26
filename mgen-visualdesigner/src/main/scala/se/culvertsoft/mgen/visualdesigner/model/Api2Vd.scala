package se.culvertsoft.mgen.visualdesigner.model

import scala.collection.JavaConversions.asJavaCollection
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable.HashMap

import ModelConversion.ApiArrayType
import ModelConversion.ApiBoolType
import ModelConversion.ApiCustomType
import ModelConversion.ApiField
import ModelConversion.ApiFloat32Type
import ModelConversion.ApiFloat64Type
import ModelConversion.ApiGenerator
import ModelConversion.ApiInt16Type
import ModelConversion.ApiInt32Type
import ModelConversion.ApiInt64Type
import ModelConversion.ApiInt8Type
import ModelConversion.ApiListType
import ModelConversion.ApiMapType
import ModelConversion.ApiModule
import ModelConversion.ApiProject
import ModelConversion.ApiStringType
import ModelConversion.VdClass
import ModelConversion.VdEntity
import ModelConversion.VdField
import ModelConversion.VdGenerator
import ModelConversion.VdModule
import ModelConversion.VdProject
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.compiler.defaultparser.FileUtils
import se.culvertsoft.mgen.visualdesigner.EntityFactory
import se.culvertsoft.mgen.visualdesigner.model.ModelConversion.VdModule
import se.culvertsoft.mgen.visualdesigner.util.LayOutEntities

case class UnlinkedId(val apiType: ModelConversion.ApiCustomType) extends EntityId

object Api2Vd {
  import ModelConversion._

  def apply(apiProject: ApiProject): Model = {
    val state = new Api2VdConversionState
    val out = new Model(cvtProject(apiProject, None, state))
    out.updateCache()

    linkTypes(state, out, apiProject)

    LayOutEntities(out.project, out, true)

    out
  }

  def toJava[T](seq: Iterable[T]) = new java.util.ArrayList(seq)

  def toJava[K, V](map: java.util.Map[K, V]) = new java.util.HashMap[K, V](map)

  def cvtProject(apiProject: ApiProject, parent: Option[VdProject], state: Api2VdConversionState): VdProject = {

    state.converted(apiProject) match {
      case Some(done) =>
        done

      case _ =>

        val vdProject = EntityFactory.mkProject(apiProject.name, getFilePath(apiProject))
        state.markConverted(apiProject, vdProject)

        parent.foreach { parent =>
          vdProject.setParent(parent.getId())
          parent.getDependencies().add(vdProject)
        }

        // Set FilePath
        vdProject.setFilePath(getFilePath(apiProject))

        // Convert settings
        vdProject.setSettings(toJava(apiProject.settings))

        // Convert generators
        vdProject.setGenerators(toJava(apiProject.generators().map(cvtGenerator)))

        // Convert dependencies
        vdProject.setDependencies(toJava(apiProject.dependencies.map(cvtProject(_, Some(vdProject), state))))

        // Convert Modules
        vdProject.setModules(toJava(apiProject.modules().map(cvtModule(_, vdProject, state))))

        // Split modules
        splitModules(vdProject, state)

        vdProject

    }

  }

  /**
   * What this does is that it splits the single api style full.module.name
   * into vd modules (grandparent, parent, child).
   */
  def splitModules(project: VdProject, state: Api2VdConversionState) {

    val writtenFilePath = project.getFilePath().getWritten()
    val absoluteFilePath = project.getFilePath().getAbsolute()

    if (state.isModuleSplitDone(project))
      return
    state.markProjectModulesSplitDone(project)

    def splitModules(source: Seq[VdModule]): java.util.ArrayList[VdModule] = {

      // Storage for this level
      val lkUp = new HashMap[String, VdModule]

      val writtenDir = FileUtils.directoryOf(writtenFilePath).trim()
      val absoluteDir = FileUtils.directoryOf(absoluteFilePath).trim()

      // Merge this level
      for (m <- source.sortBy(_.getName)) {

        val moduleNameParts = m.getName().split('.')
        val part0 = moduleNameParts(0)

        if (moduleNameParts.size == 1) {

          lkUp.put(part0, m)

        } else {

          val parent = lkUp.getOrElseUpdate(part0, EntityFactory.mkModule(part0, m.getSaveDir))
          m.setName(m.getName.substring(parent.getName.length + 1))
          parent.getSubmodules().add(m)
          m.setParent(parent.getId())
        }
      }

      // Merge the next level
      for (m <- lkUp.values) {
        m.setSubmodules(splitModules(m.getSubmodules()))
      }

      toJava(lkUp.values)
    }

    def mergeProjectModules(project: VdProject) {
      project.setModules(splitModules(project.getModules))
    }

    // Start top down, with dependencies first
    project.getDependencies foreach mergeProjectModules
    mergeProjectModules(project)

  }

  def cvtModule(apiModule: ApiModule, parent: VdEntity, state: Api2VdConversionState): VdModule = {

    state.converted(apiModule) match {
      case Some(done) =>
        done

      case _ =>
        val vdModule = EntityFactory.mkModule(apiModule.path, getSaveDir(apiModule))
        state.markConverted(apiModule, vdModule)

        vdModule.setParent(parent.getId)
        parent match {
          case p: VdProject => p.getModules().add(vdModule)
          case m: VdModule => m.getSubmodules().add(vdModule)
        }
        vdModule.setSaveDir(getSaveDir(apiModule))
        vdModule.setSettings(toJava(apiModule.settings()))
        vdModule.setTypes(toJava(apiModule.types.values.map(cvtClass(_, vdModule, state))))
        vdModule
    }
  }

  def cvtClass(apiClass: ApiCustomType, parent: VdModule, state: Api2VdConversionState): VdClass = {
    val cls = EntityFactory.mkClass(apiClass.name)
    state.markConverted(apiClass, cls)

    if (apiClass.hasSuperType) {
      cls.setSuperType(new UnlinkedId(apiClass.superType.asInstanceOf[ApiCustomType]))
      state.addUnlinked(cls)
    }

    parent.getTypes().add(cls)
    cls.setParent(parent.getId)

    cls.setFields(toJava(apiClass.fields().map(cvtField(_, cls, state))))

    cls
  }

  def cvtField(apiField: ApiField, parent: VdClass, state: Api2VdConversionState): VdField = {
    val fld = EntityFactory.mkField(apiField.name)
    fld.setFlags(toJava(apiField.flags()))
    fld.setParent(parent.getId)
    parent.getFields().add(fld)
    fld.setType(cvtFieldType(apiField, state))
    fld
  }

  def cvtGenerator(apiGenerator: ApiGenerator): VdGenerator = {
    val out = new VdGenerator
    out.setClassRegistryPath(apiGenerator.getGeneratorSettings().get("classregistry_path"))
    out.setGeneratorClassName(apiGenerator.getGeneratorClassPath())
    out.setGeneratorJarFileFolder("") // TODO: No such field?
    out.setName(apiGenerator.getGeneratorName())
    out.setOutputFolder(apiGenerator.getGeneratorSettings().get("output_path"))
    val settings = new java.util.HashMap[String, String]
    settings.putAll(apiGenerator.getGeneratorSettings())
    settings.remove("generator_class_path")
    settings.remove("output_path")
    settings.remove("classregistry_path")
    out.setSettings(settings)
    out
  }

  def cvtFieldType(apiField: ApiField, state: Api2VdConversionState): FieldType = {
    def cv(t: Type): FieldType = {
      t match {
        case t: ApiBoolType => new BoolType
        case t: ApiInt8Type => new Int8Type
        case t: ApiInt16Type => new Int16Type
        case t: ApiInt32Type => new Int32Type
        case t: ApiInt64Type => new Int64Type
        case t: ApiFloat32Type => new Float32Type
        case t: ApiFloat64Type => new Float64Type
        case t: ApiStringType => new StringType
        case t: ApiListType => new ListType(cv(t.elementType))
        case t: ApiArrayType => new ArrayType(cv(t.elementType))
        case t: ApiMapType => new MapType(cv(t.keyType).asInstanceOf[SimpleType], cv(t.valueType))
        case t: ApiCustomType =>
          val fld = new CustomTypeRef(new UnlinkedId(t))
          state.addUnlinked(fld)
          fld
      }
    }
    cv(apiField.typ)
  }

  def linkTypes(state: Api2VdConversionState, model: Model, apiProject: ApiProject) {

    for (c <- state.unlinkedClasses) {
      val id = c.getSuperType().asInstanceOf[UnlinkedId]
      val superType = state.getLinked(id.apiType)
      superType.getSubTypes().add(c.getId())
      c.setSuperType(superType.getId)
    }

    for (f <- state.unlinkedFieldTypes) {
      val id = f.getId().asInstanceOf[UnlinkedId]
      f.setId(state.getLinked(id.apiType).getId)
    }

  }

  def getFilePath(apiProject: ApiProject): FilePath = {
    new FilePath(apiProject.filePath(), apiProject.absoluteFilePath())
  }

  def getSaveDir(apiModule: ApiModule): FilePath = {
    new FilePath(FileUtils.directoryOf(apiModule.filePath()), FileUtils.directoryOf(apiModule.absoluteFilePath()))
  }

  private def extractSaveDir(apiModule: ApiModule): FilePath = {
    val absoluteDir = FileUtils.directoryOf(apiModule.absoluteFilePath)
    val nToDrop = apiModule.absoluteFilePath.length - absoluteDir.length
    val writtenDir = apiModule.filePath.dropRight(nToDrop)
    new FilePath(writtenDir, absoluteDir)
  }

}