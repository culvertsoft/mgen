package se.culvertsoft.mgen.javapack.test

import org.junit.Test
import scala.collection.JavaConversions._
import se.culvertsoft.testmodule.ClassRegistry

class BasicReadWrite {

  val registry = new ClassRegistry

  @Test
  def canCreateAllTypes() {
    assert(registry.entries.nonEmpty)
    for (e <- registry.entries) {
      val instance = e.construct()
      assert(instance != null)
      assert(instance == instance)
    }
  }

  @Test
  def canCopyAllTypes() {
    for (e <- registry.entries) {
      val instance = e.construct()
      val instance2 = instance.deepCopy()
      assert(instance == instance2)
    }
  }

  @Test
  def canWriteReadAllTypes() {
  }

}