package Strategery.Strategies.Protoss.FFA

import Planning.Plan
import Planning.Plans.GamePlans.Protoss.Standard.FFA.ProtossHuntersFFAFFEScoutReaver
import Strategery.{Hunters, StarCraftMap}
import Strategery.Strategies.Strategy
import bwapi.Race

object ProtossHuntersFFAFFEScoutReaver extends Strategy {
  
  override def gameplan: Option[Plan] = {  Some(new ProtossHuntersFFAFFEScoutReaver) }
  
  override def ourRaces: Iterable[Race] = Vector(Race.Protoss)
  override def ffa = true
  
  override def mapsWhitelisted: Option[Iterable[StarCraftMap]] = Some(Vector(Hunters))
}
