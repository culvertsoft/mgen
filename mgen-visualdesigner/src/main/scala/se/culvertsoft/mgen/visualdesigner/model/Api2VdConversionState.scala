package se.culvertsoft.mgen.visualdesigner.model

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

import ModelConversion.ApiModule
import ModelConversion.ApiProject
import ModelConversion.ApiUserDefinedType
import ModelConversion.VdModule
import ModelConversion.VdProject
import ModelConversion.VdUserDefinedType

class Api2VdConversionState {

  private val projects = new HashMap[String, VdProject] // absoluteFilePath->Project
  private val modules = new HashMap[String, VdModule] // absoluteFilePath->Module
  private val types = new HashMap[ApiUserDefinedType, VdUserDefinedType]
  private val projectsSplitDone = new HashSet[String] // absoluteFilePath

  private val _unlinkedFieldTypes = new ArrayBuffer[UserTypeRef]
  private val _unlinkedTypes = new ArrayBuffer[VdUserDefinedType]

  def converted(apiProject: ApiProject): Option[VdProject] = {
    projects.get(apiProject.absoluteFilePath)
  }

  def converted(apiModule: ApiModule): Option[VdModule] = {
    modules.get(apiModule.absoluteFilePath)
  }

  def converted(apiType: ApiUserDefinedType): Option[VdUserDefinedType] = {
    types.get(apiType)
  }

  def markConverted(apiProject: ApiProject, vdProject: VdProject) {
    projects.put(apiProject.absoluteFilePath, vdProject)
  }

  def markConverted(apiModule: ApiModule, vdModule: VdModule) {
    modules.put(apiModule.absoluteFilePath, vdModule)
  }

  def markConverted(apiType: ApiUserDefinedType, vdType: VdUserDefinedType) {
    types.put(apiType, vdType)
  }

  def isModuleSplitDone(p: VdProject): Boolean = {
    projectsSplitDone.contains(p.getFilePath.getAbsolute)
  }

  def markProjectModulesSplitDone(p: VdProject) {
    projectsSplitDone += p.getFilePath.getAbsolute
  }

  def getLinked(apiType: ApiUserDefinedType): VdUserDefinedType = {
    types.get(apiType).getOrElse(throw new RuntimeException(s"Could not convert ${apiType.shortName}"))
  }

  def addUnlinked(t: UserTypeRef) {
    _unlinkedFieldTypes += t
  }

  def addUnlinked(c: VdUserDefinedType) {
    _unlinkedTypes += c
  }

  def unlinkedFieldTypes() = {
    _unlinkedFieldTypes
  }

  def unlinkedClasses() = {
    _unlinkedTypes
  }

}