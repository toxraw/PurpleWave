package Planning.Plans.GamePlans.Protoss.Standard.PvZ

import Lifecycle.With
import Macro.Architecture.Blueprint
import Macro.Architecture.Heuristics.PlacementProfiles
import Macro.BuildRequests.Get
import Planning.Plan
import Planning.Plans.Army.{Aggression, Attack}
import Planning.Plans.Basic.NoPlan
import Planning.Plans.Compound._
import Planning.Plans.GamePlans.GameplanModeTemplate
import Planning.Plans.GamePlans.Protoss.ProtossBuilds
import Planning.Plans.GamePlans.Protoss.Situational.DefendZealotsAgainst4Pool
import Planning.Plans.Macro.Automatic._
import Planning.Plans.Macro.Build.ProposePlacement
import Planning.Plans.Macro.BuildOrders._
import Planning.Plans.Macro.Expanding.RequireMiningBases
import Planning.Plans.Scouting.ScoutOn
import Planning.Predicates.Compound.{And, Latch}
import Planning.Predicates.Economy.{GasAtLeast, MineralsAtLeast}
import Planning.Predicates.Milestones._
import Planning.Predicates.Reactive.SafeAtHome
import Planning.Predicates.Strategy.{Employing, EnemyStrategy}
import Planning.UnitMatchers.UnitMatchWarriors
import ProxyBwapi.Races.{Protoss, Zerg}
import Strategery.Strategies.Protoss.{PvZ4Gate99, PvZ4GateDragoonAllIn}

class PvZ4Gate extends GameplanModeTemplate {
  
  override val activationCriteria     = new Employing(PvZ4GateDragoonAllIn)
  override val completionCriteria     = new Latch(new MiningBasesAtLeast(2))
  override val scoutExpansionsAt      = 55
  override def buildOrder             = ProtossBuilds.OpeningTwoGate1012
  override def defaultWorkerPlan      = NoPlan()
  override def defaultScoutPlan       = new ScoutOn(Protoss.Pylon)
  override def defaultPlacementPlan   = new ProposePlacement {
    override lazy val blueprints = Vector(
      new Blueprint(this, building = Some(Protoss.Pylon),   placement = Some(PlacementProfiles.hugTownHall)),
      new Blueprint(this, building = Some(Protoss.Gateway), placement = Some(PlacementProfiles.hugTownHall)),
      new Blueprint(this, building = Some(Protoss.Gateway), placement = Some(PlacementProfiles.hugTownHall)),
      new Blueprint(this, building = Some(Protoss.Pylon),   placement = Some(PlacementProfiles.backPylon)))
}
  override def defaultAggressionPlan  = new If(
    new UnitsAtMost(8, UnitMatchWarriors, complete = true),
    new Aggression(1.0),
    new If(
      new UnitsAtMost(10, UnitMatchWarriors, complete = true),
      new Aggression(1.2),
      new If(
        new UnitsAtMost(15, UnitMatchWarriors, complete = true),
        new Aggression(1.4),
        new Aggression(2.0))))
  
  override def defaultAttackPlan: Plan =
    new If(
      new EnemyStrategy(With.fingerprints.fourPool),
      new If(
        new UnitsAtLeast(6, UnitMatchWarriors, complete = true),
        super.defaultAttackPlan),
      new If(
        new Or(
          new EnemiesAtMost(0, UnitMatchWarriors),
          new UnitsAtLeast(4, UnitMatchWarriors, complete = true)),
        new Attack,
        super.defaultAttackPlan))

  class RespectMutalisks extends Or(
    new EnemyHasShown(Zerg.Lair),
    new EnemyHasShown(Zerg.Spire),
    new EnemyHasShown(Zerg.Mutalisk))

  override def buildPlans = Vector(
    new DefendZealotsAgainst4Pool,
    new CapGasAt(200),
    new If(
      new EnemyHasShownCloakedThreat,
      new Build(
        Get(1, Protoss.Assimilator),
        Get(1, Protoss.CyberneticsCore),
        Get(1, Protoss.RoboticsFacility),
        Get(1, Protoss.Observatory),
        Get(2, Protoss.Observer))),
    new If(
      new RespectMutalisks,
      new Build(
        Get(1, Protoss.Assimilator),
        Get(1, Protoss.CyberneticsCore),
        Get(1, Protoss.DragoonRange))),
  
    new Trigger(
      new Or(
        new MineralsAtLeast(800),
        new UnitsAtLeast(24, UnitMatchWarriors),
        new EnemiesAtLeast(4, Zerg.SunkenColony, complete = true)),
      new RequireMiningBases(2)),
  
    new If(
      new GasAtLeast(150),
      new UpgradeContinuously(Protoss.DragoonRange)),

    new FlipIf(
      new And(
        new UnitsAtLeast(1, Protoss.Assimilator, complete = true),
        new SafeAtHome),
      new FlipIf(
        new UnitsAtLeast(6, UnitMatchWarriors),
        new Parallel(
          new PumpMatchingRatio(Protoss.Dragoon, 0, 20, Seq(Enemy(Zerg.Mutalisk, 1.0), Enemy(Zerg.Lurker, 1.0))),
          new PumpMatchingRatio(Protoss.Zealot, 0, 20, Seq(Enemy(Zerg.Zergling, 0.3))),
          new If(
            new And(
              new GasAtLeast(50),
              new UnitsAtLeast(1, Protoss.CyberneticsCore, complete = true),
              new UpgradeComplete(Protoss.DragoonRange, 1, Protoss.DragoonRange.upgradeFrames(1))),
            new Pump(Protoss.Dragoon)),
          new Pump(Protoss.Zealot)),
        new PumpWorkers),
      new Build(
        Get(1, Protoss.CyberneticsCore), // Of course, the Assimilator SHOULD go first but then we mine too much gas from it
        Get(1, Protoss.Assimilator),
        Get(4, Protoss.Gateway))),
    new If(
      new UnitsAtLeast(4, Protoss.Gateway, complete = true),
      new RequireMiningBases(2))
  )
}

class PvZ4Gate99 extends PvZ4Gate {
  override val activationCriteria = new Employing(PvZ4Gate99)
  override def defaultScoutPlan   = new ScoutOn(Protoss.Gateway, quantity = 2)
  override def buildOrder         = ProtossBuilds.OpeningTwoGate99
}