package Planning.Plans.GamePlans.Protoss.Standard.PvZ

import Macro.BuildRequests.{GetAtLeast, GetTech, GetUpgrade}
import Planning.Plan
import Planning.Plans.Army.Attack
import Planning.Plans.Compound._
import Planning.Plans.GamePlans.GameplanModeTemplate
import Planning.Plans.Macro.BuildOrders.{Build, BuildOrder}
import Planning.Plans.Macro.Expanding.{BuildGasPumps, RequireMiningBases}
import Planning.Plans.Predicates.Employing
import Planning.Plans.Predicates.Milestones._
import ProxyBwapi.Races.Protoss
import Strategery.Strategies.Protoss.PvZMidgameCorsairSpeedlot

class ProtossVsZergSpeedlotTemplar extends GameplanModeTemplate {
  
  override val activationCriteria = new Employing(PvZMidgameCorsairSpeedlot)
  override def aggression: Double = 0.85
  
  override def defaultAttackPlan: Plan = new Parallel(
    new Attack(Protoss.Corsair),
    new Attack(Protoss.DarkTemplar),
    super.defaultAttackPlan
  )
  
  class AddPriorityTech extends Parallel(
    new If(
      new UnitsAtLeast(1, Protoss.HighTemplar),
      new Build(GetTech(Protoss.PsionicStorm))),
      new If(
        new UnitsAtLeast(1, Protoss.Dragoon),
        new Build(GetUpgrade(Protoss.DragoonRange))))
  
  class AddTech extends Parallel(
    new Build(
      GetAtLeast(1, Protoss.Gateway),
      GetAtLeast(1, Protoss.Assimilator),
      GetAtLeast(1, Protoss.CyberneticsCore)),
    new IfOnMiningBases(2,
      new Parallel(
      new Build(
        GetAtLeast(1, Protoss.Forge),
        GetAtLeast(1, Protoss.Stargate)),
      new BuildGasPumps,
      new BuildOrder(
        GetAtLeast(1, Protoss.CitadelOfAdun),
        GetUpgrade(Protoss.AirDamage),
        GetUpgrade(Protoss.GroundDamage),
        GetUpgrade(Protoss.ZealotSpeed),
        GetAtLeast(1, Protoss.TemplarArchives),
        GetUpgrade(Protoss.DragoonRange),
        GetTech(Protoss.PsionicStorm),
        GetAtLeast(4, Protoss.Gateway),
        GetAtLeast(1, Protoss.RoboticsFacility),
        GetAtLeast(1, Protoss.Observatory),
        GetAtLeast(6, Protoss.Gateway)))),
    
    new IfOnMiningBases(3,
      new Build(
        GetAtLeast(5, Protoss.Gateway),
        GetAtLeast(2, Protoss.Forge),
        GetUpgrade(Protoss.HighTemplarEnergy),
        GetAtLeast(1, Protoss.RoboticsSupportBay),
        GetUpgrade(Protoss.ScarabDamage))))
  
  override def emergencyPlans: Seq[Plan] = Seq(
    new PvZIdeas.ReactToLurkers,
    new PvZIdeas.ReactToMutalisks
  )
  
  override def buildPlans: Seq[Plan] = Vector(
    new PvZIdeas.AddEarlyCannons,
    new PvZIdeas.TakeSafeNatural,
    new Trigger(
      new UnitsAtLeast(6, Protoss.Gateway),
      new Parallel(
        new PvZIdeas.TakeSafeThirdBase,
        new PvZIdeas.TakeSafeFourthBase)),
    new AddPriorityTech,
    new PvZIdeas.TrainAndUpgradeArmy,
    new AddTech,
    new PvZIdeas.AddGateways,
    new RequireMiningBases(3)
  )
}
