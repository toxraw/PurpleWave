package Micro.Actions.Scouting

import Micro.Actions.Action
import Micro.Actions.Commands.Attack
import ProxyBwapi.UnitInfo.FriendlyUnitInfo

object KnockKnock extends Action {

  override def allowed(unit: FriendlyUnitInfo): Boolean = (
    unit.agent.canScout
    && (unit.zone != unit.agent.destination.zone || unit.agent.destination.zone.edges.exists(_.contains(unit.pixelCenter)))
    && unit.matchups.threats.size == 1
    && unit.matchups.threats.forall(threat =>
      unit.canAttack(threat)
      && threat.unitClass.isWorker
        && unit.totalHealth > Math.min(11, threat.totalHealth)))

  override protected def perform(unit: FriendlyUnitInfo): Unit = {
    unit.agent.toAttack = unit.matchups.threats.headOption
    Attack.delegate(unit)
  }
}
