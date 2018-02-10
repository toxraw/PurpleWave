package Micro.Actions.Basic

import Lifecycle.With
import Micro.Actions.Action
import Micro.Actions.Combat.Decisionmaking.Fight
import Micro.Actions.Combat.Tactics.Potshot
import ProxyBwapi.UnitInfo.FriendlyUnitInfo

object Gather extends Action {
  
  override def allowed(unit: FriendlyUnitInfo): Boolean = {
    unit.agent.toGather.isDefined
  }
  
  override def perform(unit: FriendlyUnitInfo) {
  
    Potshot.consider(unit)
    val resource = unit.agent.toGather.get
    if ( ! unit.agent.toGather.exists(_.zone == unit.zone)) {
      if (unit.matchups.threats.exists(_.framesBeforeAttacking(unit) < 36)) {
        Fight.consider(unit)
      }
    }
    
    With.commander.gather(unit, unit.agent.toGather.get)
  }
}
