package Strategery.Strategies.Options.Protoss.PvZ

import Strategery.Strategies.Strategy
import bwapi.Race

object MidgameCorsairCarrier extends Strategy {
  
  override def ourRaces    : Iterable[Race] = Vector(Race.Random, Race.Protoss)
  override def enemyRaces  : Iterable[Race] = Vector(Race.Random, Race.Zerg)
}