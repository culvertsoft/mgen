package se.culvertsoft.mgen.javapack.test

import java.io.FileInputStream

import org.junit.Test

import gameworld.types.ClassRegistry
import gameworld.types.basemodule2.AppConfigarion
import gameworld.types.basemodule2.Grade
import se.culvertsoft.mgen.javapack.serialization.JsonReader

class AppConfigTest {

  def readConfig(filePath: String) = {
    new JsonReader(new FileInputStream(filePath), new ClassRegistry)
      .readObject(classOf[AppConfigarion])
  }

  @Test
  def testReadConfig() {

    val configWithDiff = readConfig("src/test/resources/TestConfigDiff.txt")
    assert(configWithDiff.hasDifficulty())
    assert(!configWithDiff.hasAi_threads())
    assert(!configWithDiff.hasCpu_threshold())
    assert(!configWithDiff.hasHost_game())

    val configWithAll = readConfig("src/test/resources/TestConfigAll.txt")

    assert(configWithAll.hasDifficulty())
    assert(configWithAll.hasAi_threads())
    assert(configWithAll.hasCpu_threshold())
    assert(configWithAll.hasHost_game())

    assert(configWithAll.getDifficulty() == Grade.HIGH)
    assert(configWithAll.getAi_threads() == 4)
    assert(configWithAll.getHost_game() == true)
    assert(math.abs(configWithAll.getCpu_threshold() - 0.9) < 1e-5)

  }

}