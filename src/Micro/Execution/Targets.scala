package Micro.Execution

import Lifecycle.With
import ProxyBwapi.Races.Zerg
import ProxyBwapi.UnitInfo.UnitInfo

object Targets {
  
  val ineligibleClasses = Vector(Zerg.Larva, Zerg.Egg)
  
  def get(state: ActionState): Vector[UnitInfo] = {
    if ( ! state.unit.canAttackThisSecond) return Vector.empty
    if (state.unit.battle.isEmpty && state.unit.unitClass.isWorker) return Vector.empty //Performance shortcut
    With.units.inTileRadius(
      state.unit.tileIncludingCenter,
      With.configuration.battleMarginTiles)
      .filter(target => valid(state, target))
      .toVector
  }
  
  def valid(state: ActionState, target: UnitInfo): Boolean = {
    target.likelyStillThere &&
      With.frame - target.lastSeen < 24 * 60 &&
      target.isEnemyOf(state.unit) &&
      state.unit.canAttackThisSecond(target) &&
      ! ineligibleClasses.contains(target.unitClass)
  }
  
  def inRange(state: ActionState, target: UnitInfo): Boolean = {
    state.unit.inRangeToAttackFast(target, With.latency.framesRemaining)
  }
}