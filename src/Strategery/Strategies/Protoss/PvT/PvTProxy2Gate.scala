package Strategery.Strategies.Protoss.PvT

import Planning.Plan
import Planning.Plans.Protoss.GamePlans.Specialty.Proxy2Gate
import Strategery.Strategies.Strategy
import bwapi.Race

object PvTProxy2Gate extends Strategy {
  override def gameplan   : Option[Plan]    = Some(new Proxy2Gate)
  override def ourRaces   : Iterable[Race]  = Vector(Race.Protoss)
  override def enemyRaces : Iterable[Race]  = Vector(Race.Terran)
}