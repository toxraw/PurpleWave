package Planning.Plans.GamePlans.Zerg.ZvE

import Lifecycle.With
import Macro.BuildRequests.{BuildRequest, GetAtLeast, GetTech, GetUpgrade}
import Planning.Plan
import Planning.Plans.Army.Attack
import Planning.Plans.Compound.{Do, If, NoPlan, Parallel}
import Planning.Plans.GamePlans.GameplanModeTemplate
import Planning.Plans.Macro.Automatic.TrainContinuously
import Planning.Plans.Macro.BuildOrders.Build
import Planning.Plans.Macro.Expanding.{BuildGasPumps, RequireMiningBases}
import Planning.Plans.Predicates.Milestones.{UnitsAtLeast, UnitsAtMost}
import ProxyBwapi.Races.Zerg

class OneHatchLurker extends GameplanModeTemplate {
  
  override def buildOrder: Seq[BuildRequest] = Vector(
    GetAtLeast(9, Zerg.Drone),
    GetAtLeast(1, Zerg.SpawningPool),
    GetAtLeast(10, Zerg.Drone),
    GetAtLeast(1, Zerg.Extractor),
    GetAtLeast(2, Zerg.Overlord),
    GetAtLeast(11, Zerg.Drone),
    GetAtLeast(6, Zerg.Zergling))
  
  override def defaultScoutPlan: Plan = NoPlan()
  
  override def defaultAttackPlan: Plan = new If(
    new UnitsAtLeast(1, Zerg.Lurker),
    new Attack,
    super.defaultAttackPlan
  )
  
  override def buildPlans: Seq[Plan] = Vector(
    new If(
      new UnitsAtMost(0, Zerg.Lair),
      new Do(() => {
        With.blackboard.gasTargetRatio = 3.0 / 9.0
        With.blackboard.gasLimitFloor = 100
        With.blackboard.gasLimitCeiling = 150
      }),
      new Do(() => {
        With.blackboard.gasTargetRatio = 3.0 / 9.0
        With.blackboard.gasLimitFloor = 175
        With.blackboard.gasLimitCeiling = 250
      })),
    new Build(
      GetAtLeast(9, Zerg.Drone),
      GetAtLeast(1, Zerg.Lair),
      GetAtLeast(11, Zerg.Drone),
      GetAtLeast(1, Zerg.HydraliskDen)),
    new If(
      new UnitsAtMost(0, Zerg.HydraliskDen),
      new TrainContinuously(Zerg.Zergling)),
    new If(
      new UnitsAtMost(4, Zerg.Lurker),
      new Parallel(
        new Build(GetTech(Zerg.LurkerMorph)),
        new TrainContinuously(Zerg.Lurker),
        new TrainContinuously(Zerg.Hydralisk, 4, 2),
        new RequireMiningBases(2),
        new TrainContinuously(Zerg.Zergling),
        new Build(GetUpgrade(Zerg.ZerglingSpeed))),
      new Parallel(
        new RequireMiningBases(2),
        new Build(GetAtLeast(1, Zerg.Spire)),
        new TrainContinuously(Zerg.Mutalisk),
        new TrainContinuously(Zerg.Drone))),
    new BuildGasPumps,
    new TrainContinuously(Zerg.Hatchery, 5, 1)
  )
}
