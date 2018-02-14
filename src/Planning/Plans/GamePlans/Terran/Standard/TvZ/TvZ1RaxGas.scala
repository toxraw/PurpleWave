package Planning.Plans.GamePlans.Terran.Standard.TvZ

import Macro.BuildRequests.RequestAtLeast
import Planning.Composition.Latch
import Planning.Composition.UnitMatchers.UnitMatchWarriors
import Planning.Plan
import Planning.Plans.Compound.{And, FlipIf}
import Planning.Plans.GamePlans.GameplanModeTemplate
import Planning.Plans.GamePlans.Terran.Situational.TvZPlacement
import Planning.Plans.Information.{Employing, SafeAtHome}
import Planning.Plans.Macro.Automatic.TrainContinuously
import Planning.Plans.Macro.BuildOrders.Build
import Planning.Plans.Macro.Expanding.RequireMiningBases
import Planning.Plans.Macro.Milestones.{MiningBasesAtLeast, UnitsAtLeast}
import ProxyBwapi.Races.Terran
import Strategery.Strategies.Terran.TvZ.TvZEarly1RaxGas

class TvZ1RaxGas extends GameplanModeTemplate {
  
  override val activationCriteria: Plan = new Employing(TvZEarly1RaxGas)
  override val completionCriteria: Plan = new Latch(new MiningBasesAtLeast(2))
  
  override def defaultPlacementPlan: Plan = new TvZPlacement
  
  override val buildOrder = Vector(
    RequestAtLeast(1,   Terran.CommandCenter),
    RequestAtLeast(9,   Terran.SCV),
    RequestAtLeast(1,   Terran.SupplyDepot),
    RequestAtLeast(11,  Terran.SCV),
    RequestAtLeast(1,   Terran.Barracks),
    RequestAtLeast(12,  Terran.SCV),
    RequestAtLeast(1,   Terran.Refinery),
    RequestAtLeast(13,  Terran.SCV),
    RequestAtLeast(1,   Terran.Marine),
    RequestAtLeast(14,  Terran.SCV),
    RequestAtLeast(2,   Terran.SupplyDepot),
    RequestAtLeast(2,   Terran.Marine),
    RequestAtLeast(1,   Terran.Factory))
  
  override def buildPlans: Seq[Plan] = Vector(
    new TrainContinuously(Terran.Vulture),
    new TrainContinuously(Terran.Marine),
    new FlipIf(
      new And(
        new SafeAtHome,
        new UnitsAtLeast(4, UnitMatchWarriors)),
      new Build(RequestAtLeast(4, Terran.Barracks)),
      new RequireMiningBases(2))
  )
}