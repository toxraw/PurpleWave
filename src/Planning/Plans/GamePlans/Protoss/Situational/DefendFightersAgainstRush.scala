package Planning.Plans.GamePlans.Protoss.Situational

import Information.Fingerprinting.Generic.GameTime
import Lifecycle.With
import Mathematics.PurpleMath
import Micro.Agency.Intention
import Planning.ResourceLocks.LockUnits
import Planning.UnitCounters.UnitCountExactly
import Planning.UnitMatchers.{UnitMatchWarriors, UnitMatchWorkers}
import Planning.UnitPreferences.UnitPreferClose
import Planning.{Plan, Property}
import ProxyBwapi.Races.{Protoss, Terran}
import ProxyBwapi.UnitInfo.UnitInfo

class DefendFightersAgainstRush extends Plan {
  
  val defenders = new Property[LockUnits](new LockUnits)
  defenders.get.unitMatcher.set(UnitMatchWorkers)
  
  override def onUpdate() {

    if ( ! Seq(With.fingerprints.fourPool, With.fingerprints.ninePool, With.fingerprints.fiveRax, With.fingerprints.bbs, With.fingerprints.twoRax1113).exists(_.matches)) {
      return
    }

    def inOurBase(unit: UnitInfo): Boolean = unit.zone.bases.exists(_.owner.isUs)

    val fighters          = With.units.ours .view.filter(u => u.unitClass.canMove && u.canAttack && ! u.unitClass.isWorker && inOurBase(u) && u.remainingCompletionFrames < GameTime(0, 5)())
    lazy val cannons      = With.units.ours .view.filter(u => u.aliveAndComplete && u.isAny(Terran.Bunker, Protoss.PhotonCannon))
    lazy val aggressors   = With.units.enemy.view.filter(u => u.aliveAndComplete && u.is(UnitMatchWarriors) && inOurBase(u))
    lazy val workers      = With.units.ours.view.filter(u => u.aliveAndComplete && u.unitClass.isWorker)
    lazy val threatening  = aggressors.filter(_.inPixelRadius(32 * 4).exists(n => n.isOurs && n.totalHealth < 200))
    
    if (fighters.isEmpty) {
      return
    }
    if (fighters.size > 4) {
      return
    }
    if (cannons.nonEmpty) {
      return
    }
    if (aggressors.isEmpty) {
      return
    }
    if (threatening.isEmpty) {
      return
    }
    
    val workersNeeded   = 1 + 3 * aggressors.size - 3 * fighters.filter(_.complete).map(_.unitClass.mineralValue).sum / 100.0
    val workerCap       = workers.size - 4
    val workersToFight  = PurpleMath.clamp(workersNeeded, 0, workerCap)
    val target          = fighters.minBy(fighter => aggressors.map(_.pixelDistanceEdge(fighter)).min).pixelCenter
    
    defenders.get.unitCounter.set(UnitCountExactly(workersToFight.toInt))
    defenders.get.unitPreference.set(UnitPreferClose(target))
    defenders.get.acquire(this)
    defenders.get.units.foreach(_.agent.intend(this, new Intention {
      toTravel = Some(target)
      canFlee = false
    }))
  }
}
