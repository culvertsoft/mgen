package se.culvertsoft.mgen.visualdesigner.classlookup

import scala.collection.JavaConversions.asScalaBuffer
import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.UserTypeRef

/** returns the best hit of current availible tyoes **/
object String2Type {
   def apply(searchString: String, onlySimpleType: Boolean = false, parentModule: Option[Module])(implicit model: Model): Option[FieldType] = {
      if (!onlySimpleType) {
         //look for exact hit in parent module.
         for (parentModule <- parentModule) {
            for (found <- parentModule.getTypes().find(_.getName() == searchString)) {
               return Option(new UserTypeRef(found.getId()))
            }
         }
      }
      
      return FieldTypeLookup.getClosestMatch(searchString, onlySimpleType, parentModule, model).map(_._1)
   }
}

