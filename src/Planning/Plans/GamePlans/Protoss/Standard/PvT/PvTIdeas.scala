package Planning.Plans.GamePlans.Protoss.Standard.PvT

import Lifecycle.With
import Macro.BuildRequests.GetAtLeast
import Planning.Composition.UnitMatchers.{UnitMatchCustom, UnitMatchOr, UnitMatchWarriors}
import Planning.Plans.Army.{Attack, ConsiderAttacking}
import Planning.Plans.Compound.{If, _}
import Planning.Plans.Macro.Automatic.{MatchingRatio, TrainContinuously, TrainMatchingRatio}
import Planning.Plans.Macro.BuildOrders.Build
import Planning.Plans.Predicates.Economy.{GasAtLeast, GasAtMost, MineralsAtLeast}
import Planning.Plans.Predicates.Employing
import Planning.Plans.Predicates.Milestones._
import Planning.Plans.Predicates.Reactive.{EnemyBasesAtLeast, EnemyBio}
import ProxyBwapi.Races.{Protoss, Terran}
import Strategery.Strategies.Protoss.{PvT1015Expand, PvT13Nexus, PvTEarly1015GateGoonDT, PvTEarly1GateStargateTemplar}

object PvTIdeas {
  
  class AttackWithDarkTemplar extends If(
    new Or(
      new EnemyUnitsNone(Protoss.Observer),
      new EnemyBasesAtLeast(3)),
    new Attack(Protoss.DarkTemplar))
  
  class AttackWithScouts extends Attack(Protoss.Scout)
  
  class AttackWithCarrierFleet extends Trigger(
    new UnitsAtLeast(4, Protoss.Carrier),
    initialAfter = new Attack(Protoss.Carrier))
  
  class PriorityAttacks extends Parallel(
    new AttackWithDarkTemplar,
    new AttackWithScouts,
    new AttackWithCarrierFleet)
  
  class AttackRespectingMines extends If(
    new Or(
      new Employing(PvT1015Expand),
      new Employing(PvTEarly1015GateGoonDT),
      new Employing(PvTEarly1GateStargateTemplar),
      new IfOnMiningBases(3),
      new EnemyBio,
      new Not(new EnemyHasShown(Terran.Vulture)),
      new UnitsAtLeast(1, UnitMatchCustom((unit) => unit.is(Protoss.Observer) && With.framesSince(unit.frameDiscovered) > 24 * 10), complete = true),
      new UnitsAtLeast(20, UnitMatchWarriors, complete = true)),
    new ConsiderAttacking)
  
  class EmergencyBuilds extends Parallel(
    new If(
      new And(new Employing(PvT13Nexus), new EnemiesAtLeast(1, Terran.Marine), new UnitsAtMost(0, Protoss.CyberneticsCore, complete = true)),
      new Parallel(
        new TrainContinuously(Protoss.Zealot),
        new Build(GetAtLeast(2, Protoss.Gateway)))))
  
  class TrainMinimumDragoons extends TrainMatchingRatio(
    Protoss.Dragoon, 1, 20,
    Seq(
      MatchingRatio(Terran.Vulture, 0.6),
      MatchingRatio(Terran.Wraith, 0.5)))
  
  class TrainDarkTemplar extends If(
    new UnitsAtMost(0, UnitMatchOr(Protoss.Arbiter, Protoss.ArbiterTribunal)),
    new If(
      new And(
        new EnemyUnitsAtMost(5, Terran.Vulture),
        new EnemyUnitsNone(Terran.ScienceVessel),
        new EnemyUnitsNone(UnitMatchCustom((unit) => unit.is(Terran.MissileTurret) && unit.zone.owner.isNeutral))),
      new TrainContinuously(Protoss.DarkTemplar, 3)))
  
  private class TrainObservers extends If(
    new UnitsAtLeast(24, UnitMatchWarriors),
    new TrainContinuously(Protoss.Observer, 4),
    new If(
      new UnitsAtLeast(18, UnitMatchWarriors),
      new TrainContinuously(Protoss.Observer, 3),
      new If(
        new UnitsAtLeast(12, UnitMatchWarriors),
        new TrainContinuously(Protoss.Observer, 2),
        new If(
          new UnitsAtLeast(3, UnitMatchWarriors),
          new TrainContinuously(Protoss.Observer, 1)))))
  
  class TrainReaversAgainstBio extends TrainMatchingRatio(Protoss.Reaver, 0, 5, Seq(MatchingRatio(Terran.Marine, 1.0/6.0)))
  
  class TrainHighTemplarWithSpareGas extends If(
    new GasAtLeast(800),
    new TrainContinuously(Protoss.HighTemplar, maximumConcurrently = 1))
  
  class TrainHighTemplarAgainstBio extends If(
    new EnemyBio,
    new TrainMatchingRatio(Protoss.HighTemplar, 1, 6, Seq(MatchingRatio(Terran.Marine, 1.0/5.0))))
  
  class TrainScouts extends If(
    new And(
      new EnemyUnitsAtMost(0, Terran.Goliath),
      new EnemyUnitsAtMost(6, Terran.Marine),
      new EnemyUnitsAtMost(8, Terran.MissileTurret),
      new UnitsExactly(0, Protoss.FleetBeacon),
      new UnitsExactly(0, Protoss.ArbiterTribunal),
      new Employing(PvTEarly1GateStargateTemplar)),
    new TrainContinuously(Protoss.Scout, 5))
  
  class TrainZealotsOrDragoons extends FlipIf(
    new Or(
      new And(
        new MineralsAtLeast(600),
        new GasAtMost(200)),
      new And(
        new UnitsAtLeast(12, Protoss.Dragoon),
        new UpgradeComplete(Protoss.ZealotSpeed, withinFrames = Protoss.ZealotSpeed.upgradeFrames.head._2))),
    new TrainContinuously(Protoss.Dragoon),
    new TrainContinuously(Protoss.Zealot))
  
  class TrainArmy extends Parallel(
    new TrainDarkTemplar,
    new TrainReaversAgainstBio,
    new TrainObservers,
    new TrainMinimumDragoons,
    new TrainHighTemplarAgainstBio,
    new FlipIf(
      new Check(() => With.units.countOurs(Protoss.Carrier) >= Math.max(8, 4 * With.units.countOurs(Protoss.Arbiter))),
      new TrainContinuously(Protoss.Carrier),
      new TrainContinuously(Protoss.Arbiter)),
    new TrainHighTemplarWithSpareGas,
    new TrainScouts,
    new TrainZealotsOrDragoons)
  
  class GetObserversForCloakedWraiths extends If(
    new EnemyHasShownWraithCloak,
    new Parallel(
      new Build(
        GetAtLeast(1, Protoss.RoboticsFacility),
        GetAtLeast(1, Protoss.Observatory)),
      new PvTIdeas.TrainObservers))
}

