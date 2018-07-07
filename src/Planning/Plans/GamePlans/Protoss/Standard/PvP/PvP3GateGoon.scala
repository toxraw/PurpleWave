package Planning.Plans.GamePlans.Protoss.Standard.PvP

import Macro.BuildRequests.BuildRequest
import Planning.Plans.Compound.FlipIf
import Planning.Plans.GamePlans.GameplanModeTemplate
import Planning.Plans.GamePlans.Protoss.ProtossBuilds
import Planning.Plans.Macro.Automatic.Pump
import Planning.Plans.Macro.Expanding.RequireMiningBases
import Planning.Plans.Scouting.ScoutOn
import Planning.Predicates.Compound.{And, Latch}
import Planning.Predicates.Milestones.{MiningBasesAtLeast, UnitsAtLeast}
import Planning.Predicates.Reactive.SafeAtHome
import Planning.Predicates.Strategy.Employing
import Planning.{Plan, Predicate}
import ProxyBwapi.Races.Protoss
import Strategery.Strategies.Protoss.PvPOpen3GateGoon

class PvP3GateGoon extends GameplanModeTemplate {
  
  override val activationCriteria : Predicate = new Employing(PvPOpen3GateGoon)
  override val completionCriteria : Predicate = new Latch(new MiningBasesAtLeast(2))
  override def defaultAttackPlan  : Plan = new PvPIdeas.AttackSafely
  override def defaultScoutPlan   : Plan = new ScoutOn(Protoss.CyberneticsCore)
  override def emergencyPlans: Seq[Plan] = Vector(
    new PvPIdeas.ReactToDarkTemplarEmergencies,
    new PvPIdeas.ReactToCannonRush,
    new PvPIdeas.ReactToProxyGateways,
    new PvPIdeas.ReactToFFE)
  
  override val buildOrder: Seq[BuildRequest] = ProtossBuilds.Opening_3GateDragoon
  
  override val buildPlans = Vector(
    new FlipIf(
      new And(
        new UnitsAtLeast(7, Protoss.Dragoon),
        new SafeAtHome),
      new Pump(Protoss.Dragoon),
      new RequireMiningBases(2))
  )
}
