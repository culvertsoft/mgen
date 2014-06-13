package se.culvertsoft.mgen.visualdesigner.model

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

import ModelConversion.ApiCustomType
import ModelConversion.ApiModule
import ModelConversion.ApiProject
import ModelConversion.VdClass
import ModelConversion.VdModule
import ModelConversion.VdProject

class Api2VdConversionState {

  private val projects = new HashMap[String, VdProject] // absoluteFilePath->Project
  private val modules = new HashMap[String, VdModule] // absoluteFilePath->Module
  private val types = new HashMap[ApiCustomType, VdClass]
  private val projectsSplitDone = new HashSet[String] // absoluteFilePath

  private val _unlinkedFieldTypes = new ArrayBuffer[CustomTypeRef]
  private val _unlinkedClasses = new ArrayBuffer[VdClass]

  def converted(apiProject: ApiProject): Option[VdProject] = {
    projects.get(apiProject.absoluteFilePath)
  }

  def converted(apiModule: ApiModule): Option[VdModule] = {
    modules.get(apiModule.absoluteFilePath)
  }

  def converted(apiType: ApiCustomType): Option[VdClass] = {
    types.get(apiType)
  }

  def markConverted(apiProject: ApiProject, vdProject: VdProject) {
    projects.put(apiProject.absoluteFilePath, vdProject)
  }

  def markConverted(apiModule: ApiModule, vdModule: VdModule) {
    modules.put(apiModule.absoluteFilePath, vdModule)
  }

  def markConverted(apiType: ApiCustomType, vdType: VdClass) {
    types.put(apiType, vdType)
  }

  def isModuleSplitDone(p: VdProject): Boolean = {
    projectsSplitDone.contains(p.getFilePath.getAbsolute)
  }

  def markProjectModulesSplitDone(p: VdProject) {
    projectsSplitDone += p.getFilePath.getAbsolute
  }

  def getLinked(apiType: ApiCustomType): VdClass = {
    types.get(apiType).getOrElse(throw new RuntimeException(s"Could not convert ${apiType.name}"))
  }

  def addUnlinked(t: CustomTypeRef) {
    _unlinkedFieldTypes += t
  }

  def addUnlinked(c: VdClass) {
    _unlinkedClasses += c
  }

  def unlinkedFieldTypes() = {
    _unlinkedFieldTypes
  }

  def unlinkedClasses() = {
    _unlinkedClasses
  }

}