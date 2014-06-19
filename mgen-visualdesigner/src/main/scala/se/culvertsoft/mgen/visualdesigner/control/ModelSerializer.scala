package se.culvertsoft.mgen.visualdesigner.control

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import se.culvertsoft.mgen.javapack.classes.MGenBase
import se.culvertsoft.mgen.javapack.serialization.JsonReader
import se.culvertsoft.mgen.javapack.serialization.JsonWriter
import se.culvertsoft.mgen.visualdesigner.ClassRegistry
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.Project

class ModelSerializer {

   private val bos = new ByteArrayOutputStream
   private val classRegistry = new ClassRegistry
   private val writer = new JsonWriter(bos, classRegistry)

   def serialize(model: Model): Array[Byte] = {
      serializeAny(model.project)
   }

   def deSerialize(bytes: Array[Byte]): Model = {
      val project = deSerializeAny(bytes).asInstanceOf[Project]
      new Model(project)
   }

   def serializeAny(o: MGenBase): Array[Byte] = {
      writer.writeMGenObject(o)
      val bytes = bos.toByteArray()
      bos.reset()
      bytes
   }

   def deSerializeAny(bytes: Array[Byte]): MGenBase = {
      val bis = new ByteArrayInputStream(bytes)
      val reader = new JsonReader(bis, classRegistry)
      reader.readMGenObject()
   }

}