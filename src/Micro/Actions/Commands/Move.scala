package Micro.Actions.Commands

import Lifecycle.With
import Micro.Actions.Action
import ProxyBwapi.UnitInfo.FriendlyUnitInfo

object Move extends Action {
  
  override def allowed(unit: FriendlyUnitInfo): Boolean = {
    unit.canMove &&
    unit.agent.toTravel.isDefined
  }
  
  override def perform(unit: FriendlyUnitInfo) {
    val pixelToMove = unit.agent.toTravel.get
    
    if (unit.agent.shouldEngage
      && With.reaction.agencyAverage > 12
      && ! unit.unitClass.isWorker
      && unit.canAttack) {
      With.commander.attackMove(unit, pixelToMove)
    }
    else if (unit.agent.ride.isDefined
      && ( ! unit.agent.shouldEngage
        || unit.matchups.threatsInRange.nonEmpty
        || unit.framesToTravelTo(pixelToMove) > unit.unitClass.groundDamageCooldown * 3)) {
      With.commander.rightClick(unit, unit.agent.ride.get)
    }
    else {
      With.commander.move(unit, pixelToMove)
    }
    unit.agent.setRideGoal(pixelToMove)
  }
}
