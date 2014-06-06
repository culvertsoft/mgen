package se.culvertsoft.mgen.visualdesigner.view;

import java.awt.BorderLayout
import java.awt.Component
import java.awt.Container
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import javax.swing.InputMap
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeNode
import javax.swing.tree.TreePath
import se.culvertsoft.mgen.visualdesigner.Icons
import se.culvertsoft.mgen.visualdesigner.classlookup.Type2String
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.control.ControllerListener
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.EntityId
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.Project
import se.culvertsoft.mgen.visualdesigner.util.OperationStatus
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase

class PackageExplorer(

  private val controller: Controller,
  private val container: Container) extends JPanel with ControllerListener {
  private val root = new DefaultMutableTreeNode();
  private val treeModel = new DefaultTreeModel(root);
  private val renderer = new TreeRenderer();
  private val tree = new JTree(treeModel);
  private val id2node = new HashMap[EntityIdBase, DefaultMutableTreeNode]
  private val id2entity = new HashMap[EntityIdBase, Entity]
  private val mouseInputStatus = new OperationStatus

  initialize()
  controller.addObserver(this)

  case class StrEntity(val entity: Entity) {
    override def toString(): String = {
      entity match {
        case entity: CustomTypeField =>
          val nameStr = entity.getName()
          val typeStr = Type2String(entity.getType())(controller.model)
          val flagsStr =
            if (entity.hasFlags() && entity.getFlags().nonEmpty)
              s"{${entity.getFlags().map(_.trim()).mkString(", ")}}"
            else
              ""

          s"$nameStr: $typeStr  $flagsStr"
        case _ => entity.getName()
      }
    }
  }

  def initialize() {

    val scrollpane = new JScrollPane(tree)

    setLayout(new BorderLayout());
    add(scrollpane, BorderLayout.CENTER);

    tree.setCellRenderer(renderer);
    tree.addTreeSelectionListener(new TreeSelectionListener() {

      override def valueChanged(e: TreeSelectionEvent) {

        if (!controller.isTriggeringObservers) {

          mouseInputStatus active {

            controller.bulkOperation {

              controller.deselectAll(null)
              val selectionPaths = tree.getSelectionPaths()

              if (selectionPaths != null) {
                val selectedEntities = selectionPaths
                  .map(_.getLastPathComponent().asInstanceOf[DefaultMutableTreeNode])
                  .map(_.getUserObject().asInstanceOf[StrEntity])

                for (strEntity <- selectedEntities.filter(_ != null)) {
                  controller.viewMgr.getView(strEntity.entity) match {
                    case selectable: Selectable => selectable.setSelected(true)
                    case _ =>
                  }
                }
              }

            }

            controller.triggerObservers(_.onSelectionChanged(controller.selectedEntities(), None))

          }
        }
      }

    })

    for (
      condition <- List(
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
        JComponent.WHEN_FOCUSED,
        JComponent.WHEN_IN_FOCUSED_WINDOW)
    ) {

      def clearIM(inputMap: InputMap) {

        if (inputMap == null)
          return

        if (inputMap.getParent() != null) {
          clearIM(inputMap.getParent())
        }

        val keyStrokes = inputMap.keys();
        if (keyStrokes != null) {
          for (keyStroke <- keyStrokes) {
            inputMap.get(keyStroke) match {
              case "copy" => inputMap.remove(keyStroke)
              case "cut" => inputMap.remove(keyStroke)
              case "paste" => inputMap.remove(keyStroke)
              case _ =>
            }
          }
        }

      }

      clearIM(tree.getInputMap(condition))

    }

  }

  def updateSelection(nodes: Seq[DefaultMutableTreeNode]) {
    if (!mouseInputStatus.isActive) {
      val nodePaths = nodes.map(node =>
        new TreePath(node.getPath().asInstanceOf[Array[Object]]));

      if (nodePaths == null || nodePaths.isEmpty)
        tree.clearSelection()
      else
        tree.setSelectionPaths(nodePaths.toArray)
    }
  }

  def updateSelection() {
    if (!mouseInputStatus.isActive) {
      updateSelection(controller.selectedEntities().map(x => id2node(x.getId())))
    }
  }

  def expand(parentNode: DefaultMutableTreeNode) {
    if (!controller.isBulkOperationActive) {
      if (!mouseInputStatus.isActive) {
        if (parentNode != null) {
          val parentPath = new TreePath(parentNode.getPath().asInstanceOf[Array[Object]]);
          tree.expandPath(parentPath);
        } else {
          tree.expandRow(0);
        }
      }
      tree.updateUI()
    }
  }

  def getChildren(node: TreeNode): Seq[TreeNode] = {
    val out = new ArrayBuffer[TreeNode]
    val n = node.getChildCount()
    for (i <- 0 until n) {
      out += node.getChildAt(i)
    }
    out
  }

  def reAddChildrenSorted(parent: Entity) {

    val parentNode = id2node(parent.getId())
    parentNode.removeAllChildren()

    parent.firstLevelChildren().foreach { ec =>
      for (childNode <- id2node.get(ec.getId())) {
        parentNode.add(childNode)
      }
    }

  }

  override def onEntityAdded(child: Entity, parent: Entity) {
    if (parent != null) {
      val parentNode = id2node(parent.getId())
      val childNode = id2node.getOrElseUpdate(child.getId(), new DefaultMutableTreeNode(new StrEntity(child)));
      id2node.put(child.getId(), childNode)
      id2entity.put(child.getId(), child)
      reAddChildrenSorted(parent)
      expand(parentNode)
    } else {
      val usrObj = new StrEntity(child)
      root.setUserObject(usrObj)
      id2node.put(child.getId(), root)
      id2entity.put(child.getId(), child)
      expand(root);
    }
  }

  override def onChildrenReordered(parent: Entity) {
    reAddChildrenSorted(parent)
    if (!controller.isBulkOperationActive) {
      tree.updateUI()
    }
  }

  override def onEntityTransferred(child: Entity, newParent: Entity, oldParent: Entity) {
    val oldParentNode = id2node(oldParent.getId())
    val childNode = id2node(child.getId())
    oldParentNode.remove(childNode)
    expand(oldParentNode);
    onEntityAdded(child, newParent)
  }

  override def onEntityDeleted(child: Entity, parent: Option[Entity]) {
    val childNode = id2node(child.getId())
    parent foreach { parent =>
      val oldParentNode = id2node(parent.getId())
      oldParentNode.remove(childNode)
      expand(oldParentNode);
    }
  }

  override def onSelectionChanged(selection: Seq[Entity], focused: Option[Entity]) {
    val selectedNodes = selection.map(x => id2node(x.getId()))
    updateSelection(selectedNodes)
  }

  override def onEntityModified(child: Entity, validate: Boolean = false, parent: Option[Entity] = None) {
    if (!controller.isBulkOperationActive) {
      tree.updateUI()
    }
  }

  override def onModelCleared() {
    root.setUserObject(null)
    id2node.clear()
    id2entity.clear()
    root.removeAllChildren()
    if (!controller.isBulkOperationActive)
      treeModel.reload()
  }

  override def onModelModified(isPreview: Boolean) {
    if (!controller.isBulkOperationActive && !isPreview) {
      expand(root)
    }
  }

  class TreeRenderer extends DefaultTreeCellRenderer {

    override def getTreeCellRendererComponent(
      tree: JTree,
      value: Object,
      selected: Boolean,
      expanded: Boolean,
      leaf: Boolean,
      row: Int,
      hasFocus: Boolean): Component = {
      super.getTreeCellRendererComponent(
        tree,
        value,
        selected,
        expanded,
        leaf,
        row,
        hasFocus);

      val strEntity = value.asInstanceOf[DefaultMutableTreeNode].getUserObject().asInstanceOf[StrEntity]

      if (strEntity != null) {
        strEntity.entity match {
          case project: Project => setIcon(Icons.TreeView.Dash.PROJECT_ICON)
          case module: Module => setIcon(Icons.TreeView.Dash.MODULE_ICON)
          case cls: CustomType => setIcon(Icons.TreeView.Dash.CLASS_ICON)
          case field: CustomTypeField => setIcon(Icons.TreeView.Dash.FIELD_ICON)
        }

      }

      return this;
    }

  }

  override def onViewRootChanged() {
    val node = id2node(controller.viewMgr.root.getId)
    expand(node)
  }

}
