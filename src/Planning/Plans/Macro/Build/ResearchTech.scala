package Planning.Plans.Macro.Build

import Lifecycle.With
import Micro.Agency.Intention
import Planning.Plan
import Planning.ResourceLocks.{LockCurrencyForTech, LockUnits}
import Planning.UnitCounters.UnitCountOne
import Planning.UnitPreferences.UnitPreferIdle
import ProxyBwapi.Techs.Tech
import Utilities.ByOption

class ResearchTech(tech: Tech) extends Plan {

  val techerClass = tech.whatResearches
  val currency = new LockCurrencyForTech(tech)
  val techers = new LockUnits {
    unitCounter.set(UnitCountOne)
    unitMatcher.set(techerClass)
    unitPreference.set(UnitPreferIdle)
  }
  
  description.set("Tech " + tech)
  
  override def isComplete: Boolean = With.self.hasTech(tech)
  
  override def onUpdate() {
    if (isComplete) return
  
    // Don't even stick a projected expenditure in the queue if we're this far out.
    if ( ! With.units.existsOurs(techerClass)) return
    
    currency.framesPreordered = Math.max(
      ByOption.max(techers.units.map(_.remainingOccupationFrames)).getOrElse(0),
      With.projections.unit(techerClass))
    currency.acquire(this)
    currency.isSpent = With.units.ours.exists(techer => techer.teching && techer.techingType == tech)
    if ( ! currency.satisfied) return
  
    techers.acquire(this)
    techers.units.foreach(_.agent.intend(this, new Intention { toTech = Some(tech) }))
  }
}
