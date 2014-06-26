package se.culvertsoft.mgen.visualdesigner.model

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions

case class ChildParent(child: Entity, parent: Entity)

class RichEntity(base: Entity) {

  def clear() { throw new RuntimeException(s"Cannot call clear() on $this") }

  def traverse(parent: Entity, f: ChildParent => Boolean) { throw new RuntimeException(s"Cannot call traverse(..) on $this") }

  def moveChildUp(child: Entity) { throw new RuntimeException(s"Cannot call moveChildUp(..) on $this") }
  def moveChildDown(child: Entity) { throw new RuntimeException(s"Cannot call moveChildUp(..) on $this") }

  def canBeParentOf(e: Entity): Boolean = { throw new RuntimeException(s"Cannot call canBeParentOf(..,..) on $this") }

  def add(e: Project) { throw new RuntimeException(s"Cannot add $e to $this") }
  def add(e: Module) { throw new RuntimeException(s"Cannot add $e to $this") }
  def add(e: CustomType) { throw new RuntimeException(s"Cannot add $e to $this") }
  def add(e: CustomTypeField) { throw new RuntimeException(s"Cannot add $e to $this") }

  def remove(e: Project) { throw new RuntimeException(s"Cannot transferAway $e from $this") }
  def remove(e: Module) { throw new RuntimeException(s"Cannot transferAway $e from $this") }
  def remove(e: CustomType) { throw new RuntimeException(s"Cannot transferAway $e from $this") }
  def remove(e: CustomTypeField) { throw new RuntimeException(s"Cannot transferAway $e from $this") }

  final def traverse(f: ChildParent => Boolean) { traverse(null, f) }
  final def foreach[A](f: ChildParent => A) { traverse({ x: ChildParent => f(x); true }) }
  final def foreachFirstLevelChild[A](f: ChildParent => A) { traverse { x: ChildParent => if (x.parent != null) f(x); x.parent == null } }
  final def firstLevelChildren(): Seq[Entity] = { val out = new ArrayBuffer[Entity]; foreachFirstLevelChild(out += _.child); out }
  final def numFirstLevelChildren(): Int = { var out = 0; foreachFirstLevelChild(_ => out += 1); out }
  final def allChildren(): Seq[Entity] = { val out = new ArrayBuffer[Entity]; foreach(cp => if (cp.parent != null) out += cp.child); out }

  final def contains(e: Entity): Boolean = {
    findFirst(_.child.getId() == e.getId()).isDefined
  }

  final def containsAtFirstLevel(e: Entity): Boolean = {
    foreachFirstLevelChild { cp =>
      if (cp.child eq e) {
        return true
      }
    }
    return false
  }

  final def findFirst(f: ChildParent => Boolean): Option[ChildParent] = {
    foreach { cp =>
      if (f(cp)) {
        return Some(cp)
      }
    }
    return None
  }

  final def findLast(f: ChildParent => Boolean): Option[ChildParent] = {
    var out: Option[ChildParent] = None
    foreach { cp =>
      if (f(cp)) {
        out = Some(cp)
      }
    }
    return out
  }

  def add(e: Entity) {
    e match {
      case e: Project => add(e)
      case e: Module => add(e)
      case e: CustomType => add(e)
      case e: CustomTypeField => add(e)
    }
  }

  def remove(e: Entity) {
    e match {
      case e: Project => remove(e)
      case e: Module => remove(e)
      case e: CustomType => remove(e)
      case e: CustomTypeField => remove(e)
    }
  }

}

class RichProject(base: Project) extends RichEntity(base) {
  import ModelOps._

  override def clear() {
    base.getModules().clear()
  }

  override def traverse(parent: Entity, f: ChildParent => Boolean) {
    if (f(ChildParent(base, parent))) {
      for (d <- base.getDependencies()) {
        d.traverse(base, f)
      }
      for (m <- base.getModules()) {
        m.traverse(base, f)
      }
    }
  }

  override def canBeParentOf(e: Entity): Boolean = {
    e match {
      case e: CustomType => false
      case _ => true
    }
  }

  override def add(e: Project) { base.getDependencies().add(e) }
  override def add(e: Module) { base.getModules().add(e) }
    
  override def remove(e: Project) { base.getDependencies().remove(e) }
  override def remove(e: Module) { base.getModules().remove(e) }

  override def moveChildUp(child: Entity) {
    child match {
      case module: Module =>
        val iprev = base.getModules().indexWhere(_ eq module)
        if (iprev >= 1) {
          base.getModules().remove(iprev)
          base.getModules().insert(iprev - 1, module)
        }
    }
  }

  override def moveChildDown(child: Entity) {
    child match {
      case module: Module =>
        val iprev = base.getModules().indexWhere(_ eq module)
        if (iprev < (base.getModules().size() - 1)) {
          base.getModules().remove(iprev)
          base.getModules().insert(iprev + 1, module)
        }
    }
  }

}

class RichModule(base: Module) extends RichEntity(base) {
  import ModelOps._

  override def clear() {
    base.getSubmodules().clear()
    base.getTypes().clear()
  }

  override def traverse(parent: Entity, f: ChildParent => Boolean) {
    if (f(ChildParent(base, parent))) {
      for (m <- base.getSubmodules()) {
        m.traverse(base, f)
      }
      for (t <- base.getTypes()) {
        t.traverse(base, f)
      }
    }
  }

  override def canBeParentOf(e: Entity): Boolean = {
    e match {
      case e: Project => false
      case _ => true
    }
  }

  override def add(e: Module) { base.getSubmodules().add(e) }
  override def add(e: CustomType) { base.getTypes().add(e) }
  override def remove(e: Module) { base.getSubmodules().remove(e) }
  override def remove(e: CustomType) { base.getTypes().remove(e) }

  override def moveChildUp(child: Entity) {
    child match {
      case module: Module =>
        val iprev = base.getSubmodules().indexWhere(_ eq module)
        if (iprev >= 1) {
          base.getSubmodules().remove(iprev)
          base.getSubmodules().insert(iprev - 1, module)
        }
      case typ: CustomType =>
        val iprev = base.getTypes().indexWhere(_ eq typ)
        if (iprev >= 1) {
          base.getTypes().remove(iprev)
          base.getTypes().insert(iprev - 1, typ)
        }
    }
  }

  override def moveChildDown(child: Entity) {
    child match {
      case module: Module =>
        val iprev = base.getSubmodules().indexWhere(_ eq module)
        if (iprev < (base.getSubmodules().size() - 1)) {
          base.getSubmodules().remove(iprev)
          base.getSubmodules().insert(iprev + 1, module)
        }
      case typ: CustomType =>
        val iprev = base.getTypes().indexWhere(_ eq typ)
        if (iprev < (base.getTypes().size() - 1)) {
          base.getTypes().remove(iprev)
          base.getTypes().insert(iprev + 1, typ)
        }
    }
  }

}

class RichCustomType(base: CustomType) extends RichEntity(base) {
  import ModelOps._

  override def clear() {
    base.getFields().clear()
  }

  override def traverse(parent: Entity, f: ChildParent => Boolean) {
    if (f(ChildParent(base, parent))) {
      for (field <- base.getFields()) {
        field.traverse(base, f)
      }
    }
  }

  override def add(e: CustomTypeField) {
    base.getFields().add(e)
  }

  override def remove(e: CustomTypeField) {
    base.getFields().remove(e)
  }

  override def canBeParentOf(e: Entity): Boolean = {
    e match {
      case field: CustomTypeField => true
      case _ => false
    }
  }

  override def moveChildUp(child: Entity) {
    child match {
      case field: CustomTypeField =>
        val iprev = base.getFields().indexWhere(_ eq field)
        if (iprev >= 1) {
          base.getFields().remove(iprev)
          base.getFields().insert(iprev - 1, field)
        }
    }
  }

  override def moveChildDown(child: Entity) {
    child match {
      case field: CustomTypeField =>
        val iprev = base.getFields().indexWhere(_ eq field)
        if (iprev < (base.getFields().size() - 1)) {
          base.getFields().remove(iprev)
          base.getFields().insert(iprev + 1, field)
        }
    }
  }

}

class RichCustomTypeField(base: CustomTypeField) extends RichEntity(base) {

  override def clear() {
  }

  override def traverse(parent: Entity, f: ChildParent => Boolean) {
    if (f(ChildParent(base, parent))) {
    }
  }

  override def canBeParentOf(e: Entity): Boolean = {
    false
  }

  override def moveChildUp(child: Entity) {
  }

  override def moveChildDown(child: Entity) {
  }

}

object ModelOps {

  implicit def toRichCustomType(base: Entity): RichEntity = {
    base match {
      case base: Project => new RichProject(base)
      case base: Module => new RichModule(base)
      case base: CustomType => new RichCustomType(base)
      case base: CustomTypeField => new RichCustomTypeField(base)
    }
  }

}