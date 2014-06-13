package se.culvertsoft.mgen.visualdesigner.model

import java.util.ArrayList
import scala.annotation.tailrec
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.reflect.ClassTag
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import scala.collection.mutable.HashSet

class Model(val project: Project) {
  
  private val cache = new java.util.HashMap[EntityIdBase, Entity]

  def traverse(f: ChildParent => Boolean) = {
    project.traverse(f)
  }

  def foreach[A](f: ChildParent => A) = {
    traverse { cp =>
      f(cp)
      true
    }
  }

  def findFirst(f: ChildParent => Boolean): Option[ChildParent] = {
    foreach { x =>
      if (f(x)) {
        return Some(x)
      }
    }
    return None
  }

  def findLast(f: ChildParent => Boolean): Option[ChildParent] = {
    var out: Option[ChildParent] = None
    foreach { cp =>
      if (f(cp)) {
        out = Some(cp)
      }
    }
    out
  }

  def findEach(f: ChildParent => Boolean): Seq[ChildParent] = {
    val out = new ArrayBuffer[ChildParent]
    foreach { cp =>
      if (f(cp)) {
        out += cp
      }
    }
    out
  }

  def findEach[T <: Entity: ClassTag](f: T => Boolean = { t: T => true }): Seq[T] = {
    val out = new ArrayBuffer[T]
    foreach(_.child match {
      case c: T if (f(c)) => out += c
      case _ =>
    })
    out
  }

  def clearCaches() {
    cache.clear()
  }

  override def equals(x: Any): Boolean = {
    if (x == null) return false
    if (!(x.getClass() == classOf[Model])) return false
    if (x.asInstanceOf[Model] eq this) return true
    val otherModel = x.asInstanceOf[Model]
    return project == otherModel.project
  }

  def getEntity(id: EntityIdBase): Option[Entity] = {

    val cached = cache.get(id)

    if (cached != null) {
      return Some(cached)
    } else {
      findFirst(_.child.getId() == id) match {
        case Some(cp) =>
          cache.put(id, cp.child)
          Some(cp.child)
        case _ =>
          None
      }
    }

  }

  def getCustomType(id: EntityIdBase): Option[CustomType] = {
    getEntity(id) match {
      case x @ Some(e: CustomType) => x.asInstanceOf[Option[CustomType]]
      case _ => None
    }
  }

  def parentOf(entity: Entity): Option[Entity] = {
    if (entity.hasParent()) {
      getEntity(entity.getParent())
    } else {
      None
    }
  }

  def parentOf(id: EntityIdBase): Option[Entity] = {
    getEntity(id).flatMap(parentOf)
  }

  def superTypeOf(entity: CustomType): Option[CustomType] = {
    if (entity.hasSuperType()) {
      getEntity(entity.getSuperType()).map(_.asInstanceOf[CustomType])
    } else {
      None
    }
  }

  def superTypeOf(entityId: EntityIdBase): Option[CustomType] = {
    getEntity(entityId) match {
      case Some(entity: CustomType) => superTypeOf(entity)
      case _ => None
    }
  }

  def subtypesOf(entity: CustomType): Seq[CustomType] = {
    if (entity.hasSubTypes()) {
      entity
        .getSubTypes()
        .flatMap(getEntity)
        .map(_.asInstanceOf[CustomType])
    } else {
      Nil
    }
  }

  def isRootProject(entity: Entity): Boolean = {
    entity eq project
  }

  @tailrec
  final def exists(entity: Entity): Boolean = {
    if (isRootProject(entity)) {
      true
    } else {
      parentOf(entity) match {
        case None => false
        case Some(parent) => exists(parent)
      }
    }
  }

  def exists(id: EntityIdBase): Boolean = {
    getEntity(id).map(exists).getOrElse(false)
  }

  def removeFromCache(id: EntityIdBase) {
    cache.remove(id)
  }

  def removeFromCache(entity: Entity) {
    removeFromCache(entity.getId())
  }

  def deepCopy(): Model = {
    new Model(project.deepCopy())
  }

  final def superTypesOf(subType: CustomType): List[CustomType] = {
    superTypeOf(subType) match {
      case Some(superType) => superType :: superTypesOf(superType)
      case _ => Nil
    }
  }

  @tailrec
  final def foreachParentOf(typeId: EntityIdBase)(f: Entity => Unit) {
    parentOf(typeId) match {
      case Some(parent) =>
        f(parent)
        foreachParentOf(parent.getId())(f)
      case _ =>
    }
  }

  @tailrec
  final def foreachSuperTypeOf(subTypeId: EntityIdBase)(f: CustomType => Unit) {
    superTypeOf(subTypeId) match {
      case Some(superType) =>
        f(superType)
        if (superType.hasSuperType()) {
          foreachSuperTypeOf(superType.getId())(f)
        }
      case _ => Nil
    }
  }

  def isInheritanceAllowed(subTypeId: EntityIdBase, superTypeId: EntityIdBase): Boolean = {
    if (subTypeId == superTypeId)
      return false
    foreachSuperTypeOf(superTypeId) { t =>
      if (t.getId() == subTypeId || t.getId() == superTypeId) {
        return false
      }
    }
    return true
  }

  def attachSubType(subType: CustomType, superType: CustomType) {

    // Ensure subtype is not already in this chain
    if (!isInheritanceAllowed(subType.getId(), superType.getId())) {
      throw new RuntimeException(s"Illegal inheritance (self inheritance): Cannot set supertype of ${subType.getName()} to ${superType.getName()}")
    }

    detachSuperTypeOf(subType)

    subType.setSuperType(superType.getId())
    if (!superType.hasSubTypes())
      superType.setSubTypes(new ArrayList)
    superType.getSubTypesMutable().add(subType.getId())
  }

  def detachSubType(subType: CustomType, superType: CustomType) {
    subType.unsetSuperType()
    if (superType.hasSubTypes())
      superType.getSubTypesMutable().remove(subType.getId())
  }

  def detachSuperTypeOf(subType: CustomType) {
    if (subType.hasSuperType()) {
      for (superType <- getEntity(subType.getSuperType())) {
        detachSubType(subType, superType.asInstanceOf[CustomType])
      }
    }
  }

  def detachSubTypesOf(superType: CustomType) {
    if (superType.hasSubTypes()) {
      for (subTypeId <- superType.getSubTypes().reverse) {
        for (subType <- getEntity(subTypeId)) {
          detachSubType(subType.asInstanceOf[CustomType], superType)
        }
      }
    }
  }

  def attachParent(child: Entity, parent: Entity) {

    detachParentOf(child)

    child.setParent(parent.getId())
    if (!parent.firstLevelChildren().contains(child)) {
      parent.add(child)
    }
  }

  def detachParent(child: Entity, parent: Entity) {
    child.unsetParent()
    parent.remove(child)
  }

  def detachParentOf(child: Entity) {
    if (child.hasParent()) {
      getEntity(child.getParent()) foreach { parent =>
        detachParent(child, parent)
      }
    }
  }

  def updateCache() {
    cache.clear()

    // Add all of this project
    foreach { cp =>
      cache.put(cp.child.getId(), cp.child)
    }

  }

  private def foreachReferencedClass[A](t: FieldType)(f: CustomType => A) {
    def foreachReferencedClass(fieldType: FieldType) {
      fieldType match {
        case fieldType: CustomTypeRef =>
          getCustomType(fieldType.getId()) foreach f
        case fieldType: GenericType =>
          fieldType match {
            case fieldType: ListOrArrayType =>
              foreachReferencedClass(fieldType.getElementType())
            case fieldType: MapType =>
              foreachReferencedClass(fieldType.getKeyType())
              foreachReferencedClass(fieldType.getValueType())
          }
        case _ =>
      }
    }
    foreachReferencedClass(t)
  }

  private def isChildClass(
    potentialChild: CustomType,
    potentialParent: Entity): Boolean = {

    foreachParentOf(potentialChild.getId()) { parent =>
      if (parent.getId() == potentialParent.getId()) {
        return true
      }
    }
    return false
  }

  def foreachReferencedClass[A](from: CustomTypeField)(f: CustomType => A) {
    foreachReferencedClass(from.getType())(f)
  }

  def foreachReferencedClass[A](t: CustomType)(f: CustomType => A) {
    import scala.collection.JavaConversions._
    val superType = if (t.hasSuperType()) new CustomTypeRef(t.getSuperType()) else null
    val subTypes = if (t.hasSubTypes()) t.getSubTypes().map(id => new CustomTypeRef(id)) else null
    val fields = if (t.hasFields()) t.getFields().map(_.getType()) else null

    if (superType != null) {
      foreachReferencedClass(superType)(f)
    }

    if (subTypes != null) {
      for (subType <- subTypes) {
        foreachReferencedClass(subType)(f)
      }
    }

    if (fields != null) {
      for (field <- fields) {
        foreachReferencedClass(field)(f)
      }
    }

  }

  def existsReference(from: FieldType, to: Entity): Boolean = {
    foreachReferencedClass(from) { c =>
      if (c.getId() == to.getId() || isChildClass(c, to)) {
        return true
      }
    }
    return false
  }

  def existsReference(id: EntityIdBase, to: Entity): Boolean = {
    getEntity(id) match {
      case Some(t: CustomType) => existsReference(new CustomTypeRef(id), to)
      case None => false
      case _ => ???
    }
  }
  
}
