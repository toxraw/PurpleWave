package Micro.Actions.Basic

import Micro.Actions.Action
import ProxyBwapi.UnitInfo.FriendlyUnitInfo

object Shove extends Action {
  
  override protected def allowed(unit: FriendlyUnitInfo): Boolean = {
    ! unit.flying &&
    unit.canMoveThisFrame &&
    unit.matchups.threats.nonEmpty
  }
  
  override protected def perform(unit: FriendlyUnitInfo): Unit = {
    unit.matchups.allies.foreach(_.action.shove(unit))
  }
}