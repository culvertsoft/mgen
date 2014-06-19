package se.culvertsoft.msuite.languagepacks.mjavapack.test

import scala.collection.JavaConversions.collectionAsScalaIterable

import org.junit.Test

import gameworld.types.basemodule1.Creature
import gameworld.types.basemodule1.Positioning
import gameworld.types.basemodule1.VectorR3
import gameworld.types.basemodule1.World

class EqualityTest {

   @Test
   def checkEquality() {

      val classRegistry = new gameworld.types.ClassRegistry
      val entries = classRegistry.entries()

      assert(entries.size() > 5)

      for (entry <- entries) {
         val a = entry.construct()
         val b = entry.construct()
         assert(!a.eq(b))
         assert(a == b)
      }

      def mkEqWld(): World = {

         val world = new World()
         for (i <- 0 until 10) {
            val pos = new VectorR3(0 + i, 0 - i, 0 * i)
            val vel = new VectorR3(1 + i, 1 - i, 1 * i)
            val acc = new VectorR3(2 + i, 2 - i, 2 * i)
            world.getEntities().put(i, new Creature(i, new Positioning(pos, vel, acc), 10))
         }

         world
      }

      val world1 = mkEqWld()
      val world2 = mkEqWld()

      assert(world1.eq(world1))
      assert(!world1.eq(world2))
      assert(world1 == world2)

   }

   @Test
   def checkNonEquality() {

      def mkNonEqWld(offs: Int): World = {

         val world = new World()
         for (i <- 0 until 10) {
            val pos = new VectorR3(0 + i, 0 - i, 0 * i)
            val vel = new VectorR3(1 + i, 1 - i, 1 * i)
            val acc = new VectorR3(2 + i, 2 - i, 2 * i + offs)
            world.getEntities().put(i, new Creature(i, new Positioning(pos, vel, acc), 10))
         }

         world
      }
      val world1 = mkNonEqWld(1)
      val world2 = mkNonEqWld(2)

      assert(world1.eq(world1))
      assert(!world1.eq(world2))
      assert(world1 != world2)
   }

   @Test
   def checkEqualityNulled() {
      val world1 = new World()
      val world2 = new World()

      assert(world1.eq(world1))
      assert(!world1.eq(world2))
      assert(world1 == world2)

   }

}