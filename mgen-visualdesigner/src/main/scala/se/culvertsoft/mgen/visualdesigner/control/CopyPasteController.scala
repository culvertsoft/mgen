package se.culvertsoft.mgen.visualdesigner.control

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.util.ArrayList
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import se.culvertsoft.mgen.visualdesigner.EntityFactory
import se.culvertsoft.mgen.visualdesigner.model.ArrayType
import se.culvertsoft.mgen.visualdesigner.model.ChildParent
import se.culvertsoft.mgen.visualdesigner.model.ClipboardContents
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.EntityId
import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.ListType
import se.culvertsoft.mgen.visualdesigner.model.MapType
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.Project
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase

class CopyPasteController(controller: Controller) extends SubController(controller) {

  private val modelSerializer = new ModelSerializer

  def checkSelectionForCopy(): Boolean = {

    val parents = selectedEntities()
      .map(_.getParent())
      .distinct

    if (selectedEntities.exists(_.isInstanceOf[Project])) {
      controller.viewMgr.popupPreconditionFailed("Cannot copy Projects. Can only copy modules, classes or fields.")
    } else if (parents.isEmpty) {
      controller.viewMgr.popupPreconditionFailed("No items selected. Need to select one or more items with the same parent.")
    } else if (parents.size > 1) {
      controller.viewMgr.popupPreconditionFailed("Invalid items selected. Need to select one or more items with the same parent.")
    } else {
      true
    }
  }

  def copySelectionToClipBoard(): Boolean = {
    if (checkSelectionForCopy()) {
      val clipboardData = new ClipboardContents().setItems(new ArrayList(selectedEntities()))
      val serialized = modelSerializer.serializeAny(clipboardData)
      val toClipboard = new StringSelection(new String(serialized))
      val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
      clipboard.setContents(toClipboard, null)
      true
    } else {
      false
    }
  }

  def cutSelectionToClipBoard() {
    if (copySelectionToClipBoard()) {
      controller.entityAddMgr.deleteSelection(false)
    }
  }

  def pasteFromClipBoard() {

    val allSelected = selectedEntities()

    if (allSelected.size == 1) {
      doPaste(allSelected(0))
    } else if (allSelected.isEmpty) {
      controller.viewMgr.popupPreconditionFailed("No items selected to paste into. Need to select exactly one item to paste clipboard into.")
    } else {
      controller.viewMgr.popupPreconditionFailed("Too many items selected to paste into. Need to select exactly one item to paste clipboard into.")
    }

  }

  private def doPaste(tgt: Entity) {

    getClipboardContents() foreach { text =>

      val o = modelSerializer.deSerializeAny(text.getBytes())

      o match {
        case clipboardContents: ClipboardContents => doPaste2(clipboardContents.getItems(), tgt)
        case entity: Entity => doPaste2(Seq(entity), tgt)
        case _ => // Nothing we can handle
      }

    }

    def doPaste2(topLevelEntities: Seq[Entity], tgt: Entity) {

      if (topLevelEntities.isEmpty)
        return

      val inPaste = new ArrayBuffer[ChildParent]()

      for (e <- topLevelEntities) {
        e.foreach { cp =>
          inPaste += cp
        }
      }

      // Find ids of all entities in paste
      val idsInPaste = inPaste.map(_.child.getId()).toSet

      // Find and sort all referenced ids in paste (parents, supertypes, subtypes) into:
      // -> internal
      // -> external
      val allIds = new HashSet[EntityIdBase]
      val intIds = new HashSet[EntityIdBase]
      val extIds = new HashSet[EntityIdBase]
      for (cp <- inPaste) {
        val id = cp.child.getId()
        if (idsInPaste.contains(id)) {
          intIds += id
        } else {
          extIds += id
        }
        allIds += id
      }

      // Find replacements for internal id duplicates with current model
      val idReplacements = new HashMap[EntityIdBase, EntityIdBase]
      for (id <- intIds) {
        if (controller.model.exists(id)) {
          idReplacements.put(id, EntityFactory.nextId())
        }
      }

      // Remove all external subtype references in the paste
      for (e <- inPaste) {
        e.child match {
          case t: CustomType if (t.hasSubTypes()) =>
            val newSubTypes = t.getSubTypes().filter(intIds.contains)
            t.setSubTypes(new ArrayList(newSubTypes))
          case _ =>
        }
      }

      // Apply id replacements
      // -> own
      // -> parents
      // -> supertype
      // -> suptypes
      // -> fieldtypes
      for (cp <- inPaste) {

        val e = cp.child
        val id = e.getId()

        // Replace own id if needed
        if (idReplacements.contains(id)) {
          e.setId(idReplacements(id))
        }

        // Replace parent id if needed
        if (e.hasParent() && idReplacements.contains(e.getParent())) {
          e.setParent(idReplacements(e.getParent()))
        }

        e match {
          case e: Project =>
          case e: Module =>
          case e: CustomType =>

            // Replace super type id if needed
            if (e.hasSuperType()) {
              if (idReplacements.contains(e.getSuperType())) {
                e.setSuperType(idReplacements(e.getSuperType()))
              } else {
                controller.model.superTypeOf(e).get.getSubTypesMutable().add(e.getId())
              }
            }

            // Replace sub type ids if needed
            if (e.hasSubTypes()) {
              val oldSubTypes = e.getSubTypes()
              val newSubTypes = oldSubTypes map { id =>
                if (idReplacements.contains(id)) {
                  idReplacements(id)
                } else {
                  id
                }
              }
              e.setSubTypes(new ArrayList(newSubTypes))
            }

          // Replace field type references as needed
          case e: CustomTypeField =>
            def replace(t: FieldType): FieldType = {
              t match {
                case t: CustomTypeRef if (idReplacements.contains(t.getId())) => new CustomTypeRef(idReplacements(t.getId()))
                case t: ListType => new ListType(replace(t.getElementType()))
                case t: ArrayType => new ArrayType(replace(t.getElementType()))
                case t: MapType => new MapType(t.getKeyType(), replace(t.getValueType()))
                case _ => t
              }
            }
            e.setType(replace(e.getType()))
        }
      }

      // Add to model
      controller.bulkOperation {
        for (eTopLevel <- topLevelEntities) {
          if (tgt.canBeParentOf(eTopLevel)) {
            tgt.add(eTopLevel)
            eTopLevel.setParent(tgt.getId())
            eTopLevel.foreach { cp =>
              val parent = if (cp.parent != null) cp.parent else tgt
              val child = cp.child

              if (!parent.containsAtFirstLevel(child))
                parent.add(child)
              child.setParent(parent.getId())

              controller.entityAddMgr.add(child, parent)
            }
          }
        }

        controller.deselectAll(null)
        for (e <- topLevelEntities) {
          controller.select(e, false)
        }
      }

      controller.triggerObservers(_.onModelModified())

    }

  }

  def getClipboardContents(): Option[String] = {

    val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
    val contents = clipboard.getContents(null);
    val hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor)

    if (hasTransferableText) {
      Some(contents.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String])
    } else {
      None
    }

  }
}