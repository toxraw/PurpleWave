package Planning.Plans.GamePlans.Protoss.Situational

import Lifecycle.With
import Planning.UnitMatchers.{UnitMatchAnd, UnitMatchComplete, UnitMatchWorkers}
import ProxyBwapi.Races.{Protoss, Zerg}

class DefendFFEWithProbesAgainst4Pool extends DefendFFEWithProbes {
  
  override def probeCount: Int = {
    val zerglings           = Seq(4, With.units.countEnemy(Zerg.Zergling), 8 - With.units.countEver(Zerg.Zergling)).max
    val cannonsComplete     = With.units.countOurs(UnitMatchAnd(Protoss.PhotonCannon, UnitMatchComplete))
    val cannonsIncomplete   = With.units.countOurs(Protoss.PhotonCannon) - cannonsComplete
    val workerCount         = With.units.countOurs(UnitMatchWorkers)
    val workersToMine       = if (cannonsIncomplete == 2) 0 else if (cannonsIncomplete == 1) 3 else 6
    val workersDesired      = if (cannonsComplete >= 5) 0 else Math.min(workerCount - workersToMine, zerglings * 4 - cannonsComplete * 3)
    workersDesired
  }
}
