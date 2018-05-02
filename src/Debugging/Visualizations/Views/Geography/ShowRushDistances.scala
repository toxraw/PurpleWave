package Debugging.Visualizations.Views.Geography

import Debugging.Visualizations.Rendering.DrawScreen
import Debugging.Visualizations.Views.View
import Lifecycle.With
import Mathematics.PurpleMath

object ShowRushDistances extends View {
  
  /*
  Known distances:
  Andromeda: 4940 - 5404 - 6083
  Benzene: 6140? 6191?
  Heartbreak: 5066
  Icarus: 4492 - 4900 - 5278
  La Mancha: 4441 - 4950 - 5715
  
  Neo Moon Glaive: 4687 - 4750 - 4846
  Tau Cross: 5318 - 5538
  Roadrunner: 4567 - 4858 - 5402
  Python: 4096 -  4458 - 5312
  */
  override def renderScreen() {
    val x = 5
    val y = 2 * With.visualization.lineHeightSmall
    val distances = With.geography.rushDistances
    DrawScreen.table(
      x,
      y,
      Vector(
        Vector(
          "Rush : ",
          distances.min.toInt.toString,
          PurpleMath.mean(distances).toInt.toString,
          distances.max.toInt.toString
        )))
  }
  
}
