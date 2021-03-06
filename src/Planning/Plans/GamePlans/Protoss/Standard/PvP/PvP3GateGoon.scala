package Planning.Plans.GamePlans.Protoss.Standard.PvP

import Lifecycle.With
import Macro.Architecture.Blueprint
import Macro.Architecture.Heuristics.PlacementProfiles
import Macro.BuildRequests.{BuildRequest, Get}
import Planning.Plans.Army.EjectScout
import Planning.Plans.Compound.{If, Or, Trigger}
import Planning.Plans.GamePlans.GameplanTemplate
import Planning.Plans.GamePlans.Protoss.ProtossBuilds
import Planning.Plans.GamePlans.Protoss.Standard.PvP.PvPIdeas.AttackWithDarkTemplar
import Planning.Plans.Macro.Automatic.CapGasWorkersAt
import Planning.Plans.Macro.BuildOrders.{Build, BuildOrder}
import Planning.Plans.Macro.Expanding.RequireMiningBases
import Planning.Plans.Scouting.{ScoutForCannonRush, ScoutOn}
import Planning.Predicates.Compound.{And, Not}
import Planning.Predicates.Milestones._
import Planning.Predicates.Strategy.{Employing, EnemyStrategy}
import Planning.{Plan, Predicate}
import ProxyBwapi.Races.Protoss
import Strategery.Strategies.Protoss.PvP3GateGoon

class PvP3GateGoon extends GameplanTemplate {
  
  override val activationCriteria : Predicate = new Employing(PvP3GateGoon)
  override val completionCriteria : Predicate = new MiningBasesAtLeast(2)

  override def blueprints = Vector(
    new Blueprint(this, building = Some(Protoss.Pylon),         placement = Some(PlacementProfiles.defensive), marginPixels = Some(32.0 * 10.0)),
    new Blueprint(this, building = Some(Protoss.ShieldBattery)),
    new Blueprint(this, building = Some(Protoss.Gateway),       placement = Some(PlacementProfiles.defensive), marginPixels = Some(32.0 * 12.0)),
    new Blueprint(this, building = Some(Protoss.Gateway),       placement = Some(PlacementProfiles.defensive), marginPixels = Some(32.0 * 12.0)),
    new Blueprint(this, building = Some(Protoss.Gateway),       placement = Some(PlacementProfiles.defensive), marginPixels = Some(32.0 * 12.0)),
    new Blueprint(this, building = Some(Protoss.Pylon),         placement = Some(PlacementProfiles.backPylon)),
    new Blueprint(this, building = Some(Protoss.Pylon)),
    new Blueprint(this, building = Some(Protoss.Pylon)),
    new Blueprint(this, building = Some(Protoss.Pylon), requireZone = Some(With.geography.ourNatural.zone))) // If we need emergency cannons in the natural, this is the Pylon we need done

  override def priorityAttackPlan : Plan = new AttackWithDarkTemplar
  override def attackPlan: Plan = new Trigger(
    new Or(
      new EnemyStrategy(With.fingerprints.gasSteal),
      new And(
        new UnitsAtLeast(1, Protoss.Dragoon, complete = true),
        new Not(new EnemyStrategy(With.fingerprints.twoGate, With.fingerprints.proxyGateway))),
      new UnitsAtLeast(3, Protoss.Dragoon, complete = true)),
    new PvPIdeas.AttackSafely)

  override def initialScoutPlan: Plan = new ScoutOn(Protoss.Gateway)

  override def emergencyPlans: Seq[Plan] = Vector(
    new PvPIdeas.ReactToDarkTemplarEmergencies,
    new PvPIdeas.ReactToGasSteal,
    new PvPIdeas.ReactToCannonRush,
    new PvPIdeas.ReactToProxyGateways,
    new PvPIdeas.ReactTo2Gate,
    new PvPIdeas.ReactToFFE,
    new ScoutForCannonRush)
  
  override val buildOrder: Seq[BuildRequest] = ProtossBuilds.ThreeGateGoon

  override val buildPlans = Vector(
    new If(
      new UnitsAtMost(2, Protoss.Gateway),
      new CapGasWorkersAt(2)),

    new EjectScout,
    new BuildOrder(Get(8, Protoss.Dragoon)),
    // Kind of cowardly, but if we can't be sure they're not going DT, get a Forge before expanding so we can get cannons in time if necessary
    new If(
      new Not(new EnemyStrategy(
        With.fingerprints.nexusFirst,
        With.fingerprints.twoGate,
        With.fingerprints.forgeFe,
        With.fingerprints.gatewayFe,
        With.fingerprints.dragoonRange,
        With.fingerprints.fourGateGoon,
        With.fingerprints.robo)),
      new Build(Get(Protoss.Forge))),
    new RequireMiningBases(2),
    new BuildOrder(Get(11, Protoss.Dragoon)),

    new PvPIdeas.TrainArmy,
  )
}
