package Planning.Plans.GamePlans.Zerg.ZvE

import Lifecycle.With
import Macro.Architecture.Blueprint
import Macro.Architecture.Heuristics.PlacementProfiles
import Macro.BuildRequests.{BuildRequest, GetAtLeast}
import Planning.Composition.UnitMatchers.UnitMatchOr
import Planning.Plan
import Planning.Plans.Army.Attack
import Planning.Plans.Compound._
import Planning.Plans.GamePlans.GameplanModeTemplate
import Planning.Plans.Macro.Automatic.TrainContinuously
import Planning.Plans.Macro.BuildOrders.Build
import Planning.Plans.Macro.Expanding.{BuildGasPumps, RequireMiningBases}
import Planning.Plans.Predicates.Economy.{GasAtMost, MineralsAtLeast}
import Planning.Plans.Predicates.Employing
import Planning.Plans.Predicates.Matchup.EnemyIsZerg
import Planning.Plans.Predicates.Milestones.{EnemiesAtLeast, UnitsAtLeast, UnitsAtMost}
import Planning.Plans.Predicates.Reactive.EnemyBasesAtLeast
import Planning.Plans.Scouting.Scout
import ProxyBwapi.Races.{Terran, Zerg}
import Strategery.Strategies.Zerg.NinePoolMuta

class NinePoolMuta extends GameplanModeTemplate {
  
  override val activationCriteria: Plan = new Employing(NinePoolMuta)
  
  override def buildOrder: Seq[BuildRequest] = Vector(
    GetAtLeast(9, Zerg.Drone),
    GetAtLeast(1, Zerg.SpawningPool),
    GetAtLeast(10, Zerg.Drone),
    GetAtLeast(1, Zerg.Extractor),
    GetAtLeast(2, Zerg.Overlord),
    GetAtLeast(11, Zerg.Drone),
    GetAtLeast(6, Zerg.Zergling))
  
  override def blueprints: Seq[Blueprint] = Vector(
    new Blueprint(this, building = Some(Zerg.CreepColony), requireZone = Some(With.geography.ourMain.zone),    placement = Some(PlacementProfiles.hugTownHall)),
    new Blueprint(this, building = Some(Zerg.CreepColony), requireZone = Some(With.geography.ourNatural.zone), placement = Some(PlacementProfiles.hugTownHall)),
    new Blueprint(this, building = Some(Zerg.CreepColony), requireZone = Some(With.geography.ourNatural.zone), placement = Some(PlacementProfiles.hugTownHall)))
  
  override def defaultScoutPlan: Plan = new If(
    new Not(new EnemiesAtLeast(1, UnitMatchOr(Zerg.Spire, Zerg.Mutalisk, Zerg.Hydralisk))),
    new Scout(3) { scouts.get.unitMatcher.set(Zerg.Overlord) })
  
  override def defaultAttackPlan: Plan = new Trigger(
    new Or(
      new EnemyBasesAtLeast(2),
      new UnitsAtLeast(1, Zerg.Mutalisk),
      new Not(new EnemyIsZerg)),
    new Attack)
  
  override def buildPlans: Seq[Plan] = Vector(
    new If(
      new UnitsAtMost(0, Zerg.Lair),
      new Do(() => {
        With.blackboard.gasTargetRatio = 3.0 / 9.0
        With.blackboard.gasLimitFloor = 100
        With.blackboard.gasLimitCeiling = 100
      }),
      new If(
        new UnitsAtMost(0, Zerg.Spire),
        new Do(() => {
          With.blackboard.gasTargetRatio = 2.0 / 9.0
          With.blackboard.gasLimitFloor = 200
          With.blackboard.gasLimitCeiling = 200
        }),
        new Do(() => {
          With.blackboard.gasTargetRatio = (if (With.self.gas > With.self.minerals) 1.0 else 3.0) / 9.0
          With.blackboard.gasLimitFloor = 100
          With.blackboard.gasLimitCeiling = 300
        }))),
    new Build(
      GetAtLeast(8, Zerg.Drone),
      GetAtLeast(1, Zerg.Lair)),
    new TrainContinuously(Zerg.SunkenColony),
    new If(
      new UnitsAtMost(0, Zerg.Spire),
      new Parallel(
        new If(
          new Not(new EnemyIsZerg),
          new TrainContinuously(Zerg.Drone, 16)),
        new TrainContinuously(Zerg.Zergling))),
    new Build(GetAtLeast(1, Zerg.Spire)),
    new If(
      new UnitsAtMost(1, Zerg.Extractor),
      new TrainContinuously(Zerg.Mutalisk, 100, 2),
      new TrainContinuously(Zerg.Mutalisk)),
    new If(
      new And(
        new Or(
          new EnemiesAtLeast(1, Terran.Vulture),
          new EnemiesAtLeast(1, Terran.Factory)),
        new UnitsAtMost(1, Zerg.SunkenColony)),
      new Build(GetAtLeast(1, Zerg.CreepColony))),
    new If(
      new And(
        new UnitsAtLeast(1, Zerg.Spire),
        new UnitsAtMost(0, Zerg.SunkenColony),
        new EnemyIsZerg),
      new Build(GetAtLeast(1, Zerg.CreepColony))),
    new If(
      new Or(
        new MineralsAtLeast(350),
        new Not(new EnemyIsZerg)),
      new RequireMiningBases(2)),
    new If(
      new And(
        new MineralsAtLeast(150),
        new GasAtMost(100),
        new UnitsAtLeast(2, Zerg.Larva)),
      new If(
        new And(
          new UnitsAtMost(24, Zerg.Drone),
          new Check(() =>
            With.units.countOurs(Zerg.Drone) / 9 <
            With.geography.ourBases.size)),
        new TrainContinuously(Zerg.Drone),
        new Parallel(new TrainContinuously(Zerg.Zergling)))),
    new If(
      new MineralsAtLeast(300),
      new BuildGasPumps),
    new If(
      new GasAtMost(99),
      new TrainContinuously(Zerg.Hatchery, 8, 1))
  )
}
