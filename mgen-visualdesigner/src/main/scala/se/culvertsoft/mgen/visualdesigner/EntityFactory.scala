package se.culvertsoft.mgen.visualdesigner

import java.util.ArrayList
import java.util.UUID
import se.culvertsoft.mgen.visualdesigner.model.ClassPathEntityId
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.EntityId
import se.culvertsoft.mgen.visualdesigner.model.Int32Type
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.Placement
import se.culvertsoft.mgen.visualdesigner.model.Project
import se.culvertsoft.mgen.visualdesigner.util.LayOutEntities
import se.culvertsoft.mgen.visualdesigner.model.FilePath

object EntityFactory {

  def nextId(): EntityId = {
    val uuid = UUID.randomUUID()
    new EntityId(uuid.getLeastSignificantBits(), uuid.getMostSignificantBits())
  }

  def mkModule(
    name: String,
    left: Int,
    top: Int,
    width: Int,
    height: Int,
    saveDir: FilePath): Module = {
    new Module()
      .setId(nextId())
      .setSaveDir(saveDir)
      .setName(name)
      .setPlacement(new Placement(left, top, width, height))
      .setSettings(new java.util.HashMap)
      .setSubmodules(new java.util.ArrayList)
      .setTypes(new java.util.ArrayList)
  }

  def mkField(name: String): CustomTypeField = {

    val f = new CustomTypeField
    f.setId(EntityFactory.nextId())
    f.setName(name)
    f.setType(new Int32Type)
    f.setFlags(new ArrayList)

    f
  }

  def mkModule(name: String, saveDir: FilePath): Module = {
    mkModule(
      name,
      LayOutEntities.DEFAULT_WALL_OFFSET_X,
      LayOutEntities.DEFAULT_WALL_OFFSET_Y,
      LayOutEntities.DEFAULT_MODULE_WIDTH,
      LayOutEntities.DEFAULT_MODULE_HEIGHT,
      saveDir)
  }

  def mkId(path: String): ClassPathEntityId = {
    new ClassPathEntityId(path)
  }

  def mkClass(
    name: String,
    left: Int,
    top: Int,
    width: Int,
    height: Int): CustomType = {
    new CustomType()
      .setId(nextId())
      .setName(name)
      .setFields(new java.util.ArrayList)
      .setPlacement(new Placement(left, top, width, height))
      .setSubTypes(new ArrayList)
  }

  def mkClass(name: String): CustomType = {
    mkClass(
      name,
      LayOutEntities.DEFAULT_WALL_OFFSET_X,
      LayOutEntities.DEFAULT_WALL_OFFSET_Y,
      LayOutEntities.DEFAULT_CLASS_WIDTH,
      LayOutEntities.DEFAULT_CLASS_HEIGHT)
  }

  def mkProject(name: String, filePath: FilePath): Project = {
    new Project()
      .setId(nextId())
      .setFilePath(filePath)
      .setName(name)
      .setGenerators(new java.util.ArrayList)
      .setModules(new java.util.ArrayList)
      .setSettings(new java.util.HashMap)
      .setDependencies(new java.util.ArrayList)
  }

  def mkModel(): Model = {

    new Model(mkProject("baseproject", new FilePath("baseproject.xml", "baseproject.xml")))

  }

}