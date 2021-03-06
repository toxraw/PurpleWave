package Micro.Actions.Combat.Decisionmaking

import Micro.Actions.Action
import Micro.Actions.Commands.Move
import ProxyBwapi.Races.{Protoss, Terran, Zerg}
import ProxyBwapi.UnitInfo.FriendlyUnitInfo
import Utilities.ByOption

object Follow extends Action {
  override def allowed(unit: FriendlyUnitInfo): Boolean = (
    ! unit.matchups.threats.exists(_.unitClass.dealsRadialSplashDamage)
    && unit.isAny(
      Terran.Battlecruiser,
      Terran.Wraith,
      Terran.Valkyrie,
      Protoss.Carrier,
      Protoss.Corsair,
      Protoss.Scout,
      Zerg.Mutalisk)
    && ! unit.agent.leader().contains(unit))

  override protected def perform(unit: FriendlyUnitInfo): Unit = {
    val maybeLeader = unit.agent.leader()
    unit.agent.toTravel = maybeLeader.map(_.pixelCenter).orElse(unit.agent.toTravel)
    maybeLeader
      .withFilter(leader =>
        unit.pixelDistanceCenter(leader) < Seq(256, ByOption.min(unit.matchups.threats.view.map(_.pixelsToGetInRange(unit).toInt)).getOrElse(0)).max
        && ( ! unit.isCarrier() || leader.matchups.threatsInRange.forall(unit.matchups.threatsInRange.contains))
      )
      .foreach(leader => {
        if (unit.matchups.targetsInRange.isEmpty
          && unit.matchups.framesOfSafety > 24
          && unit.pixelDistanceCenter(leader) > 64) {
          Move.delegate(unit)
        }
        leader.agent.leadFollower(unit)
      })
  }
}
