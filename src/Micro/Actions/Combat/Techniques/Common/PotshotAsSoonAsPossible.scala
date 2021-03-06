package Micro.Actions.Combat.Techniques.Common

import Micro.Actions.Action
import Micro.Actions.Combat.Targeting.Target
import Micro.Actions.Combat.Tactics.Potshot.PotshotTargetFilter
import Micro.Actions.Commands.Attack
import ProxyBwapi.UnitInfo.FriendlyUnitInfo
import Utilities.ByOption

object PotshotAsSoonAsPossible extends Action {
  
  override def allowed(unit: FriendlyUnitInfo): Boolean = (
    unit.canMove
    && unit.canAttack
  )
  
  override protected def perform(unit: FriendlyUnitInfo): Unit = {
    val targets               = unit.matchups.targets.filter(PotshotTargetFilter.legal(unit, _))
    val minFramesToGetInRange = ByOption.min(targets.map(unit.framesToGetInRange)).getOrElse(0)
    val framesBeforeShooting  = unit.framesToBeReadyForAttackOrder
    
    if (targets.isEmpty) return
  
    if (minFramesToGetInRange >= framesBeforeShooting) {
      Target.delegate(unit)
      Attack.delegate(unit)
    }
  }
}
