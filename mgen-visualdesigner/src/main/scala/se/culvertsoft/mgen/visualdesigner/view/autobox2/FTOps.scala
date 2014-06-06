package se.culvertsoft.mgen.visualdesigner.view.autobox2

import se.culvertsoft.mgen.visualdesigner.model.ArrayType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef
import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.ListOrArrayType
import se.culvertsoft.mgen.visualdesigner.model.ListType
import se.culvertsoft.mgen.visualdesigner.model.MapType
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.SimpleType
import se.culvertsoft.mgen.visualdesigner.model.NoType

object FTOps {

   def traverse(t: FieldType)(fContinue: FTChildParent => Boolean) {
      def traverse(parent: FieldType, child: FieldType): Boolean = {
         fContinue(FTChildParent(parent, child)) &&
            (child match {
               case child: MapType => child.hasKeyType() && traverse(child, child.getKeyType()) &&
                  child.hasValueType() && traverse(child, child.getValueType())
               case child: ListOrArrayType =>
                  child.hasElementType() && traverse(child, child.getElementType())
               case _ => true
            })
      }
      traverse(null, t)
   }

   def lastNode(t: FieldType): FTChildParent = {
      var out: FTChildParent = null
      traverse(t) { cp =>
         out = cp
         true
      }
      out
   }

   def complete(base: FieldType, t: FieldType) {
      base match {
         case m: MapType =>
            if (!m.hasKeyType())
               m.setKeyType(t.asInstanceOf[SimpleType])
            else if (!m.hasValueType())
               m.setValueType(t)
            else if (!isComplete(m.getValueType()))
               complete(m.getValueType(), t)
         case l: ListOrArrayType =>
            if (!l.hasElementType())
               l.setElementType(t)
            else if (!isComplete(l.getElementType()))
               complete(l.getElementType(), t)
      }
   }

   def isComplete(t: FieldType): Boolean = {
      t match {
         case t: SimpleType => true
         case t: MapType => t.hasKeyType() && isComplete(t.getKeyType()) && t.hasValueType() && isComplete(t.getValueType())
         case t: ListOrArrayType => t.hasElementType() && isComplete(t.getElementType())
         case t: CustomTypeRef => true
         case t: NoType => true
      }
   }

   def removeLast(_t: FieldType): FieldType = {
      val t = _t.deepCopy()
      t match {
         case t: MapType =>
            if (t.hasValueType()) {
               val x = removeLast(t.getValueType())
               if (x != null)
                  t.setValueType(x)
               else
                  t.unsetValueType()
               t
            } else if (t.hasKeyType()) {
               val x = removeLast(t.getKeyType()).asInstanceOf[SimpleType]
               if (x != null)
                  t.setKeyType(x)
               else
                  t.unsetKeyType()
               t
            } else {
               null
            }
         case t: ListOrArrayType =>
            if (t.hasElementType()) {
               val x = removeLast(t.getElementType())
               if (x != null)
                  t.setElementType(x)
               else
                  t.unsetElementType()
               t
            } else {
               null
            }
         case _ =>
            null
      }
   }

   def toString(t: FieldType, toStringFunc: FieldType => String)(implicit model: Model): String = {
      t match {
         case t: MapType =>
            if (t.hasKeyType()) {
               if (t.hasValueType()) {
                  if (isComplete(t.getValueType())) {
                     s"map[${toString(t.getKeyType(), toStringFunc)}, ${toString(t.getValueType(), toStringFunc)}]"
                  } else {
                     s"map[${toString(t.getKeyType(), toStringFunc)}, ${toString(t.getValueType(), toStringFunc)}"
                  }
               } else {
                  s"map[${toString(t.getKeyType(), toStringFunc)}, "
               }
            } else {
               "map["
            }
         case t: ListType =>
            if (t.hasElementType()) {
               if (isComplete(t.getElementType())) {
                  s"list[${toString(t.getElementType(), toStringFunc)}]"
               } else {
                  s"list[${toString(t.getElementType(), toStringFunc)}"
               }
            } else {
               "list["
            }
         case t: ArrayType =>
            if (t.hasElementType()) {
               if (isComplete(t.getElementType())) {
                  s"array[${toString(t.getElementType(), toStringFunc)}]"
               } else {
                  s"array[${toString(t.getElementType(), toStringFunc)}"
               }
            } else {
               "array["
            }
         case t => toStringFunc(t)
      }
   }

}
