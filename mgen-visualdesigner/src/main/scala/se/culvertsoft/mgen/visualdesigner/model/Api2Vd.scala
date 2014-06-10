package se.culvertsoft.mgen.visualdesigner.model

import java.util.IdentityHashMap

import scala.collection.JavaConversions.asJavaCollection
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

import ModelConversion.ApiArrayType
import ModelConversion.ApiBoolType
import ModelConversion.ApiClass
import ModelConversion.ApiCustomType
import ModelConversion.ApiEntity
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
import ModelConversion.VdModel
import ModelConversion.VdModule
import ModelConversion.VdProject
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.visualdesigner.EntityFactory
import se.culvertsoft.mgen.visualdesigner.util.LayOutEntities

class Api2VdConversionState {
  import ModelConversion._

  var rootApi: ApiProject = null
  var rootVd: VdProject = null

  val api2vd_nonDeps = new IdentityHashMap[ApiEntity, VdEntity]
  val api2vd_deps = new IdentityHashMap[ApiEntity, VdEntity]

  val vd2api_nonDeps = new IdentityHashMap[VdEntity, ApiEntity]
  val vd2api_deps = new IdentityHashMap[VdEntity, ApiEntity]

  def add(eApi: ApiEntity, eVd: VdEntity, isDependency: Boolean) {

    if (rootApi == null) {
      rootApi = eApi.asInstanceOf[ApiProject]
      rootVd = eVd.asInstanceOf[VdProject]
    }

    if (isDependency) {
      api2vd_deps.put(eApi, eVd)
      vd2api_deps.put(eVd, eApi)
    } else {
      api2vd_nonDeps.put(eApi, eVd)
      vd2api_nonDeps.put(eVd, eApi)
    }
  }

  def isDependency(eApi: ApiEntity): Boolean = {
    api2vd_deps.containsKey(eApi)
  }

  def isDependency(eVd: VdEntity): Boolean = {
    vd2api_deps.containsKey(eVd)
  }

  def getVd(eApi: ApiEntity): VdEntity = {
    if (isDependency(eApi)) {
      api2vd_deps.get(eApi)
    } else {
      api2vd_nonDeps.get(eApi)
    }
  }

  def getApi(eVd: VdEntity): ApiEntity = {
    if (isDependency(eVd)) {
      vd2api_deps.get(eVd)
    } else {
      vd2api_nonDeps.get(eVd)
    }
  }

}

object Api2Vd {
  import ModelConversion._

  def apply(apiProject: ApiProject)(implicit cache: HashMap[String, Model] = new HashMap[String, Model]): Model = {

    cache.getOrElseUpdate(apiProject.filePath, {

      // Create conversion state (lookup pairs)
      val state = buildApi2VdObjLkUp(apiProject)

      // Create our output model
      val model = new Model(state.rootVd)

      // Add each entity to its parent (except root ofc)
      addToModel(state, model, apiProject)
      model.updateCache()

      // Merge modules
      mergeModules(model)

      // Add/load dependencies
      loadDependencies(model, apiProject)

      // Add settings
      addSettings(state, model, apiProject)

      // Root project specifics
      if (apiProject.isRoot()) {

        // Link field types
        linkTypes(state, model, apiProject)

        // Add generators
        addGenerators(state, model, apiProject)

        // Make Sizes and placement more useful :P
        LayOutEntities(model.project, model, true)

      }

      model

    })
  }

  def loadDependencies(model: VdModel, apiProject: ApiProject)(implicit cache: HashMap[String, Model]) {
    val deps = new ArrayBuffer[(String, VdProject)]
    foreach(apiProject) { (eApi, eApiParent, isDependency) =>
      eApi match {
        case eApi: ApiProject if (isDependency) => deps += ((eApi.filePath, apply(eApi).project))
        case _ =>
      }
    }

    for (pathDep <- deps) {
      model.loadDependency(pathDep._1, pathDep._2)
    }
  }

  def mergeModules(model: VdModel)(implicit done: HashSet[EntityIdBase] = new HashSet[EntityIdBase]) {

    def mergeModules(source: Seq[VdModule]): java.util.ArrayList[VdModule] = {

      // Storage for this level
      val lkUp = new HashMap[String, VdModule]

      // Merge this level
      for (m <- source.sortBy(_.getName)) {

        val moduleNameParts = m.getName().split('.')
        val part0 = moduleNameParts(0)

        if (moduleNameParts.size == 1) {
          lkUp.put(part0, m)
        } else {
          val trg = lkUp.getOrElseUpdate(part0, EntityFactory.mkModule(part0))
          m.setName(m.getName.substring(trg.getName.length + 1))
          trg.getSubmodulesMutable().add(m)
          m.setParent(trg.getId())
        }
      }

      // Merge the next level
      for (m <- lkUp.values) {
        m.setSubmodules(mergeModules(m.getSubmodules()))
      }

      new java.util.ArrayList(lkUp.values)
    }

    def mergeProjectModules(project: VdProject) {
      project.setModules(mergeModules(project.getModules))
    }

    // Start top down, with dependencies first
    model.loadedDepdendencies.values foreach mergeProjectModules
    mergeProjectModules(model.project)
  }

  def addGenerators(state: Api2VdConversionState, model: Model, apiProject: ApiProject) {
    val apiProject = state.rootApi
    val vdProject = state.rootVd
    val srcGenerators = apiProject.generators()
    vdProject.setGenerators(new java.util.ArrayList(srcGenerators.map(apiGen2Vd)))
  }

  def apiGen2Vd(apiGenerator: ApiGenerator): VdGenerator = {
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

  def linkTypes(state: Api2VdConversionState, model: Model, apiProject: ApiProject) {

    foreach(apiProject) { (eApi, eApiParent, isDependency) =>

      val eVd = state.getVd(eApi)
      val eVdParent = state.getVd(eApiParent)

      if (!(eApi eq apiProject)) {

        eApi match {

          case apiField: ApiField =>
            val vdField = eVd.asInstanceOf[VdField]
            vdField.setFlags(new java.util.ArrayList(apiField.flags().filter(_.nonEmpty)))
            vdField.setType(apiType2Vd(apiField, state, model, apiProject))

          case apiClass: ApiClass =>
            val apiParentModule = eApiParent.asInstanceOf[ApiModule]
            val vdClass = eVd.asInstanceOf[VdClass]
            val vdParentModule = eVdParent.asInstanceOf[VdModule]

            if (apiClass.hasSuperType()) {
              val subType = vdClass
              val superType = state.getVd(apiClass.superType()).asInstanceOf[VdClass]
              model.attachSubType(subType, superType)
            }

          case _ =>

        }

      }

    }
  }

  def apiType2Vd(apiField: ApiField, state: Api2VdConversionState, model: Model, apiProject: ApiProject): FieldType = {
    def cvtType(t: Type): FieldType = {
      t match {
        case t: ApiBoolType => new BoolType
        case t: ApiInt8Type => new Int8Type
        case t: ApiInt16Type => new Int16Type
        case t: ApiInt32Type => new Int32Type
        case t: ApiInt64Type => new Int64Type
        case t: ApiFloat32Type => new Float32Type
        case t: ApiFloat64Type => new Float64Type
        case t: ApiStringType => new StringType
        case t: ApiListType => new ListType(cvtType(t.elementType))
        case t: ApiArrayType => new ArrayType(cvtType(t.elementType))
        case t: ApiMapType => new MapType(cvtType(t.keyType).asInstanceOf[SimpleType], cvtType(t.valueType))
        case t: ApiCustomType => new CustomTypeRef(state.getVd(t).getId())
      }
    }
    cvtType(apiField.typ)
  }

  def addSettings(state: Api2VdConversionState, model: Model, apiProject: ApiProject) {

    foreach(apiProject) { (eApi, eApiParent, isDependency) =>

      val eVd = state.getVd(eApi)

      if (!(eApi eq apiProject)) {
        eApi match {
          case apiProject: ApiProject =>
            val vdProject = eVd.asInstanceOf[VdProject]
            vdProject.setSettings(new java.util.HashMap(apiProject.settings()))
          case apiModule: ApiModule =>
            val vdModule = eVd.asInstanceOf[VdModule]
            vdModule.setSettings(new java.util.HashMap(apiModule.settings()))
          case _ =>
        }
      }
    }
  }

  def addToModel(state: Api2VdConversionState, model: Model, apiProject: ApiProject) {

    foreach(apiProject) { (eApi, eApiParent, isDependency) =>

      val eVd = state.getVd(eApi)
      val eVdParent = state.getVd(eApiParent)

      if (!(eApi eq apiProject)) {

        eApi match {

          case apiField: ApiField =>
            val vdField = eVd.asInstanceOf[VdField]
            val vdParentClass = eVdParent.asInstanceOf[VdClass]

            vdParentClass.getFieldsMutable().add(vdField)
            vdField.setParent(vdParentClass.getId())

          case apiClass: ApiClass =>
            val vdClass = eVd.asInstanceOf[VdClass]
            val vdParentModule = eVdParent.asInstanceOf[VdModule]

            vdParentModule.getTypesMutable().add(vdClass)
            vdClass.setParent(vdParentModule.getId())

          case apiProject: ApiProject =>
            val vdProject = eVd.asInstanceOf[VdProject]
            val vdParentProject = eVdParent.asInstanceOf[VdProject]

            vdParentProject.getDependenciesMutable().add(apiProject.filePath)
            vdProject.setParent(vdParentProject.getId())

            model.loadDependency(apiProject.filePath, vdProject)

          case apiModule: ApiModule =>
            val vdModule = eVd.asInstanceOf[VdModule]
            val vdParentProject = eVdParent.asInstanceOf[VdProject]

            vdParentProject.getModulesMutable().add(vdModule)
            vdModule.setParent(vdParentProject.getId())

        }

      }

    }
  }

  def buildApi2VdObjLkUp(apiProject: ApiProject): Api2VdConversionState = {

    val state = new Api2VdConversionState

    foreach(apiProject) { (eApi, eApiParent, isDependency) =>

      val eVd = eApi match {
        case eApi: ApiField =>
          val parentClass = eApiParent.asInstanceOf[ApiClass]
          EntityFactory.mkField(eApi.name)
        case eApi: ApiClass =>
          val parentModule = eApiParent.asInstanceOf[ApiModule]
          EntityFactory.mkClass(eApi.name)
        case eApi: ApiProject =>
          val dependentProject = eApiParent.asInstanceOf[ApiProject]
          EntityFactory.mkProject(eApi.name)
        case eApi: ApiModule =>
          EntityFactory.mkModule(eApi.path)
      }

      if (isDependency)
        replaceDependencyId(eVd, eApi, eApiParent)

      state.add(eApi, eVd, isDependency)

    }

    state

  }

  def replaceDependencyId(eVd: VdEntity, eApi: ApiEntity, eApiParent: ApiEntity) {
    eApi match {
      case eApi: ApiProject => eVd.setId(EntityFactory.mkId(eApi.name()))
      case eApi: ApiModule => eVd.setId(EntityFactory.mkId(eApi.path()))
      case eApi: ApiClass => eVd.setId(EntityFactory.mkId(eApi.fullName()))
      case eApi: ApiField => eVd.setId(EntityFactory.mkId(s"${eApiParent.asInstanceOf[ApiClass].name}.${eApi.name}"))
    }
  }

  def foreach(e: ApiEntity, p: ApiEntity = null, dep: Boolean = false)(implicit f: (ApiEntity, ApiEntity, Boolean) => Unit) {
    f(e, p, dep)
    e match {
      case e: ApiProject =>
        e.dependencies foreach { foreach(_, e, true) }
        e.modules foreach { foreach(_, e, dep) }
      case e: ApiModule =>
        e.types.values foreach { foreach(_, e, dep) }
      case e: ApiClass =>
        e.fields foreach { foreach(_, e, dep) }
      case e: ApiField =>
    }
  }

}