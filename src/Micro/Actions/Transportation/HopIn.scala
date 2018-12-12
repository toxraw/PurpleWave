package Micro.Actions.Transportation

import Lifecycle.With
import Micro.Actions.Action
import ProxyBwapi.UnitInfo.FriendlyUnitInfo

object HopIn extends Action {

  override def allowed(unit: FriendlyUnitInfo): Boolean = (
    unit.canMove
    && unit.agent.ride.isDefined
    && unit.agent.shouldHopIn
  )

  override protected def perform(unit: FriendlyUnitInfo): Unit = {
    unit.agent.ride.foreach(With.commander.rightClick(unit, _))
  }
}
