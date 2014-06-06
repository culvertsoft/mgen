package se.culvertsoft.mgen.visualdesigner.view

object ZoomLevels {
   val levels = Array(
      0.100,
      0.125,
      0.150,
      0.175,
      0.200,
      0.225,
      0.250,
      0.275,
      0.300,
      0.350,
      0.400,
      0.450,
      0.500,
      0.600,
      0.700,
      0.800,
      0.900,
      1.000,
      1.250,
      1.500,
      1.750,
      2.000,
      2.250,
      2.500,
      2.750,
      3.000)

   def findClosestIndex(d: Double): Int = {
      val currentLevel = levels.minBy(s => math.abs(s - d))
      levels.indexOf(currentLevel)
   }

}
