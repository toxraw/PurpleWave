package Planning.Plans.Macro.Expanding

import Debugging.English
import Lifecycle.With
import Macro.BuildRequests.GetAtLeast
import Planning.Composition.Property
import Planning.Plan
import ProxyBwapi.Races.Zerg

class RequireBases(basesInitial: Int = 1) extends Plan {
  
  override def toString: String = "Require " + basesDesired.get + English.pluralize(" bases", basesDesired.get)
  
  val basesDesired = new Property(basesInitial)
  
  protected def basesNow: Int = {
    With.geography.ourBases.size
  }
  
  override def onUpdate() {
    val basesAll    = With.geography.ourBases.size
    val goal        = basesDesired.get + basesAll - basesNow
    
    if (goal > 0) {
      With.scheduler.request(this, GetAtLeast(goal, With.self.townHallClass))
    }
  }
}

