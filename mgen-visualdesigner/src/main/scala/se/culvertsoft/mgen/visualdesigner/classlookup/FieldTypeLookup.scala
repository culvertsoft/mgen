package se.culvertsoft.mgen.visualdesigner.classlookup

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.collectionAsScalaIterable

import se.culvertsoft.mgen.visualdesigner.MGenClassRegistry
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef
import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.PrimitiveType
import se.culvertsoft.mgen.visualdesigner.model.SimpleType
import se.culvertsoft.mgen.visualdesigner.util.Util

object FieldTypeLookup {
   
   def search(
      searchString: String,
      onlySimpleType: Boolean,
      model: Model): Seq[(FieldType, Int)] = {
      search(searchString, onlySimpleType, model, None)
   }

   def search(
      searchString: String, onlySimpleType: Boolean,
      model: Model, parentModule: Option[Module]): Seq[(FieldType, Int)] = {

      val cr = new MGenClassRegistry()

      val fieldTypes = model.findEach[CustomType]().map(x => new CustomTypeRef(x.getId()))

      val fieldTypesInOwnPackage = parentModule
         .map(_.getTypes().toSeq)
         .getOrElse(Nil)
         .map(x => new CustomTypeRef(x.getId()))

      val simpleTypes = cr.entries()
         .filter(e => classOf[SimpleType].isAssignableFrom(e.cls()))
         .filter { e => e.cls() != classOf[SimpleType] && e.cls() != classOf[PrimitiveType] }
         .map(_.construct())
         .map(_.asInstanceOf[SimpleType])

      def getLeven(types: Iterable[FieldType], weightOffset: Int): Seq[(FieldType, Int)] = {
         types.map(x => (x, Util.levenshtein(Type2String(x)(model), searchString) + weightOffset)).toSeq
      }
         
      
      val res = getLeven(fieldTypes, 2) ++ getLeven(fieldTypesInOwnPackage, 1) ++ getLeven(simpleTypes, 0)
      res.sortBy(_._2)
   }

   def getClosestMatch(searchString: String, onlySimpleType: Boolean = false, parentModule: Option[Module], model: Model): Option[(FieldType, Int)] = {
      return search(searchString, onlySimpleType, model, parentModule).headOption
   }
}
