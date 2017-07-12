package Planning.Plans.Information.Scenarios

import Lifecycle.With
import Planning.Plan
import ProxyBwapi.Races.Zerg

class WeAreBeing4Pooled extends Plan {
  
  var conditionsMet = false
  
  override def isComplete: Boolean = conditionsMet
  
  override def onUpdate(): Unit = {
    if (With.frame > 24 * 60 * 3) {
      conditionsMet = false
      return
    }
    
    // 5 pool spawning pool finishes about 1:35 == 95 seconds
    // Let's add a bit of buffer to our detection.
    conditionsMet = conditionsMet || With.units.enemy.exists(_.is(Zerg.Zergling))
    conditionsMet = conditionsMet || (With.units.enemy.exists(u => u.is(Zerg.Drone) && u.pixelCenter.zone.owner.isUs) && With.frame < 24 * 60 * 2)
    conditionsMet = conditionsMet || With.units.enemy.exists(u => u.is(Zerg.SpawningPool) && (u.complete || u.lastSeen + u.remainingBuildFrames < 24 * 120))
  }
}
