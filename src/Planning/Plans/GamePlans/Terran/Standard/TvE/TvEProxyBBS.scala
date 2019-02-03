package Planning.Plans.GamePlans.Terran.Standard.TvE

import Information.Geography.Types.Zone
import Lifecycle.With
import Macro.Architecture.Blueprint
import Macro.Architecture.Heuristics.PlacementProfiles
import Macro.BuildRequests.{BuildRequest, Get}
import Planning.Predicates.Compound.And
import Planning.UnitCounters.UnitCountExactly
import Planning.UnitMatchers.UnitMatchWorkers
import Planning.Plans.Army.Attack
import Planning.Plans.Basic.{Do, NoPlan}
import Planning.Plans.Compound._
import Planning.Plans.GamePlans.GameplanTemplate
import Planning.Plans.Macro.Automatic.Pump
import Planning.Plans.Macro.Build.ProposePlacement
import Planning.Plans.Macro.BuildOrders.Build
import Planning.Predicates.Milestones.UnitsAtLeast
import Planning.Predicates.Reactive.EnemyBasesAtLeast
import Planning.Predicates.Strategy.Employing
import Planning.{Plan, ProxyPlanner}
import ProxyBwapi.Races.Terran
import Strategery.Strategies.Terran.TvE.TvEProxyBBS

class TvEProxyBBS extends GameplanTemplate {
  
  override val activationCriteria = new Employing(TvEProxyBBS)
  
  override val aggression: Double = 1.5
  
  lazy val proxyZone: Option[Zone] = ProxyPlanner.proxyAutomaticAggressive
  
  override def scoutPlan: Plan = NoPlan()
  
  override def attackPlan: Plan = new Parallel(
    new Attack,
    new Trigger(
      new UnitsAtLeast(1, Terran.Marine, complete = false),
      new Attack(UnitMatchWorkers, UnitCountExactly(2))))
  
  override def workerPlan: Plan = NoPlan()
  override def supplyPlan: Plan = NoPlan()
  
  override def buildOrder: Seq[BuildRequest] = Vector(
    Get(Terran.CommandCenter),
    Get(8, Terran.SCV),
    Get(2, Terran.Barracks),
    Get(Terran.SupplyDepot),
    Get(9, Terran.SCV),
    Get(Terran.Marine))
  
  override def buildPlans: Seq[Plan] = Vector(
    new Do(() => With.blackboard.maxFramesToSendAdvanceBuilder = Int.MaxValue),
    new ProposePlacement{
      override lazy val blueprints = Vector(
        new Blueprint(this, building = Some(Terran.Barracks), preferZone = proxyZone, respectHarvesting = Some(false), placement = Some(PlacementProfiles.proxyBuilding)),
        new Blueprint(this, building = Some(Terran.Barracks), preferZone = proxyZone, respectHarvesting = Some(false), placement = Some(PlacementProfiles.proxyBuilding)))
    },
    new Pump(Terran.Marine),
    new Build(Get(10, Terran.SCV)),
    new If(
      new And(
        new UnitsAtLeast(9, Terran.SCV),
        new EnemyBasesAtLeast(1)),
      new Parallel(
        new ProposePlacement {
          override lazy val blueprints = Vector(
            new Blueprint(this, building = Some(Terran.Bunker), preferZone = With.geography.enemyBases.headOption.map(_.zone), respectHarvesting = Some(false)),
            new Blueprint(this, building = Some(Terran.Bunker), preferZone = With.geography.enemyBases.headOption.map(_.zone), respectHarvesting = Some(false)),
            new Blueprint(this, building = Some(Terran.Bunker), preferZone = With.geography.enemyBases.headOption.map(_.zone), respectHarvesting = Some(false)),
            new Blueprint(this, building = Some(Terran.Bunker), preferZone = With.geography.enemyBases.headOption.map(_.zone), respectHarvesting = Some(false))
          )
        },
        new Pump(Terran.Bunker, 3, 1)
      ))
  )
}