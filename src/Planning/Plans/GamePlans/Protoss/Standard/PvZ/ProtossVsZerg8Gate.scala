package Planning.Plans.GamePlans.Protoss.Standard.PvZ

import Macro.BuildRequests.{RequestAtLeast, RequestTech, RequestUpgrade}
import Planning.Composition.UnitMatchers.UnitMatchWarriors
import Planning.Plan
import Planning.Plans.Army.Aggression
import Planning.Plans.Compound._
import Planning.Plans.GamePlans.GameplanModeTemplate
import Planning.Plans.Macro.BuildOrders.Build
import Planning.Plans.Macro.Expanding.{BuildGasPumps, RequireMiningBases}
import Planning.Plans.Macro.Protoss.{BuildCannonsAtExpansions, BuildCannonsAtNatural, MeldArchons}
import Planning.Plans.Macro.Upgrades.UpgradeContinuously
import Planning.Plans.Predicates.Employing
import Planning.Plans.Predicates.Milestones._
import ProxyBwapi.Races.Protoss
import Strategery.Strategies.Protoss.PvZMidgameGatewayAttack

class ProtossVsZerg8Gate extends GameplanModeTemplate {
  
  override val activationCriteria = new Employing(PvZMidgameGatewayAttack)
    
  class GatewayTech extends Parallel(
    new Build(
      RequestAtLeast(1, Protoss.Gateway),
      RequestAtLeast(1, Protoss.Forge),
      RequestAtLeast(1, Protoss.Assimilator),
      RequestAtLeast(1, Protoss.CyberneticsCore),
      RequestAtLeast(1, Protoss.CitadelOfAdun),
      RequestUpgrade(Protoss.GroundDamage),
      RequestUpgrade(Protoss.ZealotSpeed),
      RequestUpgrade(Protoss.DragoonRange),
      RequestAtLeast(5, Protoss.Gateway),
      RequestAtLeast(2, Protoss.Assimilator),
      RequestAtLeast(1, Protoss.RoboticsFacility),
      RequestAtLeast(1, Protoss.Observatory),
      RequestAtLeast(1, Protoss.TemplarArchives),
      RequestAtLeast(8, Protoss.Gateway)),
    new BuildGasPumps)
      
  class LateTech extends Parallel(
    new Build(RequestTech(Protoss.PsionicStorm)),
    new UpgradeContinuously(Protoss.GroundArmor),
    new UpgradeContinuously(Protoss.ObserverSpeed))
  
  override def defaultArchonPlan: Plan = new If(
    new TechComplete(Protoss.PsionicStorm),
    new MeldArchons(49) { override def maximumTemplar = 8 },
    new MeldArchons
  )
  
  override def emergencyPlans: Seq[Plan] = Seq(
    new PvZIdeas.ReactToLurkers,
    new PvZIdeas.ReactToMutalisks
  )
  
  override def buildPlans: Seq[Plan] = Vector(
    new Trigger(
      new UnitsAtLeast(5, Protoss.Gateway, complete = true),
      new Aggression(0.9),
      new Aggression(0.5)),
    new PvZIdeas.TakeSafeNatural,
    new PvZIdeas.AddEarlyCannons,
    new BuildCannonsAtExpansions(5),
    new If(
      new UnitsAtLeast(3, Protoss.Nexus),
      new BuildCannonsAtNatural(2)),
    new If(
      new UnitsAtLeast(40, UnitMatchWarriors),
      new Parallel(
        new PvZIdeas.TakeSafeThirdBase,
        new PvZIdeas.TakeSafeFourthBase,
        new LateTech)),
    new PvZIdeas.TrainAndUpgradeArmy,
    new GatewayTech,
    new PvZIdeas.AddGateways,
    new If(
      new UnitsAtLeast(25,  UnitMatchWarriors),
      new RequireMiningBases(3))
  )
}