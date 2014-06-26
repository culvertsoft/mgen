package se.culvertsoft.mgen.visualdesigner.util

import java.awt.Point
import scala.collection.JavaConversions.asScalaBuffer
import LayOutEntities.DEFAULT_SPACING_X
import LayOutEntities.DEFAULT_SPACING_Y
import LayOutEntities.DEFAULT_WALL_OFFSET_X
import LayOutEntities.DEFAULT_WALL_OFFSET_Y
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase

abstract class LayoutNode(model: Model, module: Module) {
  def parent(): Option[LayoutNode]
  def children(): Seq[LayoutNode]
  def width(): Double
  def height(): Double
  def placeAt(x: Double, y: Double)
  def name(): String

  private val thisModulesIds = module.getTypes.map(_.getId).toSet

  def isInThisModule(t: EntityIdBase): Boolean = {
    thisModulesIds.contains(t)
  }
}

class TopLayoutNode(xOffset: Double, module: Module, model: Model) extends LayoutNode(model, module) {

  import LayOutEntities._

  val children =
    module
      .getTypes()
      .filter(e => !e.hasSuperType || !isInThisModule(e.getSuperType()))
      .map(new ClassLayoutNode(_, module, model, Some(this)))

  val parent: Option[LayoutNode] = None

  val widthOfChildren = if (children.nonEmpty) children.map(_.width).sum else 0.0
  val heightOfChildren = if (children.nonEmpty) children.map(_.height).max else 0.0
  val width: Double = widthOfChildren
  val height: Double = heightOfChildren
  val halfWidth = width / 2

  placeAt(xOffset + DEFAULT_WALL_OFFSET_X + halfWidth, DEFAULT_WALL_OFFSET_Y)

  def placeAt(xCtr: Double, yCtr: Double) {

    var x = xCtr - halfWidth
    for (c <- children) {
      x += c.halfWidth
      c.placeAt(x, yCtr)
      x += c.halfWidth
    }
  }

  def name(): String = module.getName()

}

class ClassLayoutNode(
  val clas: CustomType,
  val module: Module,
  val model: Model,
  val parent: Option[LayoutNode]) extends LayoutNode(model, module) {

  import LayOutEntities._

  private var _pos = new Point(0, 0)

  val children: Seq[ClassLayoutNode] =
    clas
      .getSubTypes().filter(isInThisModule)
      .map(id => model.getEntity(id).get.asInstanceOf[CustomType])
      .filter(_.getParent() == module.getId())
      .map(new ClassLayoutNode(_, module, model, Some(this)))

  val widthOfChildren = if (children.nonEmpty) children.map(_.width).sum else 0.0
  val heightOfChildren = if (children.nonEmpty) children.map(_.height).max else 0.0
  val depthOfChildren: Int = if (children.nonEmpty) children.map(_.depthOfChildren + 1).max else 0
  val widthOfTopClass = clas.getPlacement().getWidth() + DEFAULT_SPACING_X
  val heightOfTopClass = clas.getPlacement().getHeight()
  val width: Double = math.max(widthOfTopClass, widthOfChildren)
  val height: Double = heightOfChildren + heightOfTopClass
  val halfWidth = width / 2

  def placeAt(xCtr: Double, yCtr: Double) {

    clas.getPlacement()
      .setX((xCtr - clas.getPlacement().getWidth() / 2).toInt)
      .setY(yCtr.toInt)

    var x = xCtr - halfWidth
    for (c <- children) {
      x += c.halfWidth
      c.placeAt(x, yCtr + heightOfTopClass + DEFAULT_SPACING_Y)
      x += c.halfWidth
    }

  }

  def name(): String = clas.getName()

}