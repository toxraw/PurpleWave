package Planning.Plans.Zerg.GamePlans

import Information.Geography.Types.Zone
import Lifecycle.With
import Macro.Architecture.Blueprint
import Macro.BuildRequests.RequestAtLeast
import Planning.Composition.UnitMatchers.{UnitMatchType, UnitMatchWorkers}
import Planning.Plans.Army.Attack
import Planning.Plans.Compound.{If, _}
import Planning.Plans.Macro.Automatic.{Gather, TrainContinuously}
import Planning.Plans.Macro.Build.ProposePlacement
import Planning.Plans.Macro.BuildOrders.{Build, FollowBuildOrder}
import Planning.Plans.Macro.Milestones.UnitsAtLeast
import Planning.Plans.Scouting.{FindEnemyBase, FoundEnemyBase}
import Planning.ProxyPlanner
import ProxyBwapi.Races.Zerg

class Zerg9Hatch9PoolProxy extends Parallel {
  
  override def onUpdate() {
    With.blackboard.maxFramesToSendAdvanceBuilder = Int.MaxValue
    super.onUpdate()
  }
  
  protected def proxyZone: Option[Zone] = {
    if (With.geography.startLocations.size == 2) {
      ProxyPlanner.proxyEnemyNatural
    }
    else {
      ProxyPlanner.proxyMiddleBase
    }
  }
  
  children.set(Vector(
    new ProposePlacement { override lazy val blueprints = Vector(new Blueprint(this, building = Some(Zerg.Hatchery), zone = proxyZone)) },
    new Build(
      RequestAtLeast(9,   Zerg.Drone),
      RequestAtLeast(2,   Zerg.Hatchery),
      RequestAtLeast(1,   Zerg.SpawningPool),
      RequestAtLeast(2,   Zerg.Overlord),
      RequestAtLeast(16,  Zerg.Zergling),
      RequestAtLeast(3,   Zerg.Hatchery),
      RequestAtLeast(3,   Zerg.Overlord),
      RequestAtLeast(34,  Zerg.Zergling),
      RequestAtLeast(4,   Zerg.Overlord),
      RequestAtLeast(50,  Zerg.Zergling)),
    new TrainContinuously(Zerg.Hatchery),
    new If(
      new And(
        new Not(new FoundEnemyBase),
        new UnitsAtLeast(2, UnitMatchType(Zerg.SpawningPool), complete = false)
      ),
      new FindEnemyBase { scouts.get.unitMatcher.set(UnitMatchWorkers) }),
    new Attack,
    new FollowBuildOrder,
    new Gather
  ))
}