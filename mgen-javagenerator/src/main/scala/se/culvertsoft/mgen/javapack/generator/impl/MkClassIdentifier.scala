package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.mapAsScalaMap

import Alias.instantiate
import Alias.name
import Alias.typeIdStr
import Alias.typeIdStr16BitBase64
import Alias.typeIdStr16bit
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import se.culvertsoft.mgen.javapack.generator.JavaConstants.clsRegistryClsString

object MkClassIdentifier {

  def apply(referencedModules: Seq[Module], packagePath: String)(implicit txtBuffer: SuperStringBuffer): String = {

    txtBuffer.clear()

    MkPackage(packagePath)

    MkClassStart("ClassIdentifier", null)

    // prot unhandled(MGenBase o) {
    // }    
    // prot handleNull(MGenBase o) {
    // }
    //
    // prot handle(T1 o) {
    //   if T1HasSuper
    //      handle((SUPER)o)
    //   else
    //      unhandle(o)
    // }
    
    // pub handle(MGenBase o)
    //  if (o != null)
    //  --> switch(o....)
    //      --> if (o.typeId == T::_TYPE_ID)
    // 			--> handle((T)o)
    //      --> handleUnknown(o)
    //  else
    //      --> handleNull(o)

    MkClassEnd()

    
    txtBuffer.toString()
  }
}