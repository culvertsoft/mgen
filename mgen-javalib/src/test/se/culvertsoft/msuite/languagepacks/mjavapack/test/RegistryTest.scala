package se.culvertsoft.msuite.languagepacks.mjavapack.test

import scala.collection.JavaConversions.asScalaSet
import scala.collection.JavaConversions.collectionAsScalaIterable

import org.junit.Test

class RegistryTest {

   @Test
   def checkRegistry() {

      val classRegistry = new gameworld.types.MGenClassRegistry

      val entries = classRegistry.entries()
      val names = classRegistry.registeredClassNames()
      val hashes16bit = classRegistry.registered16bitHashes()
      val hashes32bit = classRegistry.registered32bitHashes()
      val classes = classRegistry.registeredClasses()

      assert(entries.nonEmpty)
      assert(names.size == entries.size)
      assert(hashes16bit.size == entries.size)
      assert(hashes32bit.size == entries.size)
      assert(classes.size == entries.size)

      assert(entries.toSeq.distinct.size == names.size)
      assert(names.toSeq.distinct.size == names.size)
      assert(hashes16bit.toSeq.distinct.size == names.size)
      assert(hashes32bit.toSeq.distinct.size == names.size)
      assert(classes.toSeq.distinct.size == names.size)

   }

}

