package Planning.Plans.Army

import Information.Geography.Types.Zone
import Lifecycle.With
import Micro.Intent.Intention
import Planning.Composition.Property
import Planning.Composition.ResourceLocks.LockUnits
import Planning.Composition.UnitCounters.{UnitCountCombat, UnitCountOne}
import Planning.Composition.UnitMatchers.{UnitMatchAnd, UnitMatchDetectors, UnitMatchMobile, UnitMatchWarriors}
import Planning.Composition.UnitPreferences.UnitPreferClose
import Planning.Plan
import ProxyBwapi.UnitInfo.UnitInfo

class ControlZone(zone: Zone) extends Plan {
  
  val fighters = new Property[LockUnits](new LockUnits)
  fighters.get.unitMatcher.set(UnitMatchWarriors)
  fighters.get.unitPreference.set(UnitPreferClose(zone.centroid.pixelCenter))
  
  val detectors = new Property[LockUnits](new LockUnits)
  detectors.get.unitPreference.set(fighters.get.unitPreference.get)
  detectors.get.unitMatcher.set(UnitMatchAnd(UnitMatchDetectors, UnitMatchMobile))
  detectors.get.unitCounter.set(UnitCountOne)
  
  override def onUpdate() {
    
    val enemies = With.units.enemy.filter(threateningZone)
    val ourBase = zone.bases.find(_.owner.isUs)
    
    val threats = enemies.filter(threat => threat.canAttackThisSecond && ! threat.unitClass.isWorker)
    if (threats.nonEmpty) {
      fighters.get.unitCounter.set(new UnitCountCombat(enemies, alwaysAccept = ourBase.isDefined))
      fighters.get.acquire(this)
      
      if (fighters.get.satisfied) {
        val target = enemies
          .map(_.pixelCenter)
          .minBy(_.pixelDistanceFast(ourBase.map(_.heart).getOrElse(zone.centroid).pixelCenter))
  
        fighters.get.units.foreach(fighter => With.executor.intend(new Intention(this, fighter) {
          toTravel = Some(target)
        }))
  
        val cloakedThreats = threats.filter(threat => threat.cloaked || threat.burrowed)
        if (cloakedThreats.nonEmpty) {
          detectors.get.acquire(this)
          detectors.get.units.foreach(detector => With.executor.intend(new Intention(this, detector) {
            toTravel = Some(cloakedThreats.minBy(_.pixelDistanceFast(zone.centroid.pixelCenter)).pixelCenter)
            canCower = true
          }))
        }
      }
      
    }
    
    // TODO: If there's no threat, answer likely ones
  }
  
  def threateningZone(unit: UnitInfo): Boolean = {
    unit.pixelCenter.zone == zone                   ||
    unit.targetPixel.exists(_.zone == zone)         ||
    unit.target.exists(_.pixelCenter.zone == zone)  ||
    zone.edges.map(_.centerPixel).exists(unit.pixelDistanceFast(_) < With.configuration.battleMarginPixels)
  }
}
