package se.culvertsoft.mgen.visualdesigner.util

import java.awt.Dimension
import java.awt.Point

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.Project

object LayOutEntities {

  import se.culvertsoft.mgen.visualdesigner.util.AwtMath._

  val DEFAULT_WALL_OFFSET_X = 80
  val DEFAULT_WALL_OFFSET_Y = 120

  val DEFAULT_SPACING_X = 80
  val DEFAULT_SPACING_Y = 200

  val P0 = new Point(DEFAULT_WALL_OFFSET_X, DEFAULT_WALL_OFFSET_Y)

  val DEFAULT_MODULE_WIDTH = 700
  val DEFAULT_MODULE_HEIGHT = 900
  val DEFAULT_MODULE_WIDTH_PADDED = DEFAULT_MODULE_WIDTH + DEFAULT_SPACING_X
  val DEFAULT_MODULE_HEIGHT_PADDED = DEFAULT_MODULE_HEIGHT + DEFAULT_SPACING_Y
  val DEFAULT_MODULE_SIZE = new Dimension(DEFAULT_MODULE_WIDTH, DEFAULT_MODULE_HEIGHT)
  val DEFAULT_MODULE_SIZE_PADDED = new Dimension(DEFAULT_MODULE_WIDTH_PADDED, DEFAULT_MODULE_HEIGHT_PADDED)

  val DEFAULT_CLASS_WIDTH = 275
  val DEFAULT_CLASS_HEIGHT = 375
  val DEFAULT_CLASS_WIDTH_PADDED = DEFAULT_CLASS_WIDTH + DEFAULT_SPACING_X
  val DEFAULT_CLASS_HEIGHT_PADDED = DEFAULT_CLASS_HEIGHT + DEFAULT_SPACING_Y
  val DEFAULT_CLASS_SIZE = new Dimension(DEFAULT_CLASS_WIDTH, DEFAULT_CLASS_HEIGHT)
  val DEFAULT_CLASS_SIZE_PADDED = new Dimension(DEFAULT_CLASS_WIDTH_PADDED, DEFAULT_CLASS_HEIGHT_PADDED)

  private def modules(parent: Entity, model: Model, allowResize: Boolean): Point = {

    val modules = parent match {
      case parent: Project => parent.getModules()
      case parent: Module => parent.getSubmodules()
    }

    var x = P0.x

    for (m <- modules) {

      module(m, model, allowResize)

      m.getPlacementMutable()
        .setX(x)
        .setY(P0.y)

      if (allowResize) {
        m.getPlacementMutable()
          .setWidth(DEFAULT_MODULE_WIDTH)
          .setHeight(DEFAULT_MODULE_HEIGHT)
      }

      x += m.getPlacement.getWidth + DEFAULT_SPACING_X
    }

    new Point(P0.x + DEFAULT_MODULE_WIDTH_PADDED * modules.size(), P0.y)

  }

  private def classes(xOffset: Double, module: Module, model: Model, allowResize: Boolean) {

    if (allowResize) {
      for (c <- module.getTypes()) {
        c.getPlacementMutable()
          .setWidth(DEFAULT_CLASS_WIDTH)
          .setHeight(DEFAULT_CLASS_HEIGHT)
      }
    }

    new TopLayoutNode(xOffset, module, model)
  }

  private def module(module: Module, model: Model, allowResize: Boolean) {
    val sizeOccupiedBySubModules = modules(module, model, allowResize)
    classes(sizeOccupiedBySubModules.x, module, model, allowResize)
  }

  private def model(model: Model, allowResize: Boolean) {
    model.updateCache()
    modules(model.project, model, allowResize)
  }

  private def project(project: Project, model: Model, allowResize: Boolean) {
    modules(project, model, allowResize)
  }

  def apply(entity: Entity, model: Model, allowResize: Boolean) {
    entity match {
      case entity: Project => project(entity, model, allowResize)
      case entity: Module => module(entity, model, allowResize)
      case _ =>
    }
  }

}