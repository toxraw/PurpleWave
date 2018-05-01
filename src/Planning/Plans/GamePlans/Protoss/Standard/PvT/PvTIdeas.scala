package Planning.Plans.GamePlans.Protoss.Standard.PvT

import Lifecycle.With
import Macro.BuildRequests.{RequestAtLeast, RequestTech}
import Planning.Composition.Latch
import Planning.Composition.UnitMatchers.{UnitMatchCustom, UnitMatchOr, UnitMatchWarriors}
import Planning.Plans.Army.{Attack, ConsiderAttacking}
import Planning.Plans.Compound.{If, _}
import Planning.Plans.Macro.Automatic.TrainContinuously
import Planning.Plans.Macro.BuildOrders.Build
import Planning.Plans.Predicates.Economy.{GasAtMost, MineralsAtLeast}
import Planning.Plans.Predicates.Milestones._
import Planning.Plans.Predicates.Reactive.{EnemyBasesAtLeast, EnemyBio}
import Planning.Plans.Predicates.{Employing, SafeToAttack}
import ProxyBwapi.Races.{Protoss, Terran}
import Strategery.Strategies.Protoss.{PvT13Nexus, PvTEarly1015GateGoonDT, PvTEarly1GateStargateTemplar, PvTEarly4Gate}

object PvTIdeas {
  
  class AttackWithDarkTemplar extends If(
    new Or(
      new EnemyUnitsNone(Protoss.Observer),
      new EnemyBasesAtLeast(3)),
    new Attack { attackers.get.unitMatcher.set(Protoss.DarkTemplar) })
  
  class AttackWithScouts extends Attack { attackers.get.unitMatcher.set(Protoss.Scout) }
  
  class AttackWithCarrierFleet extends Trigger(
    new UnitsAtLeast(4, Protoss.Carrier),
    initialAfter = new Attack { attackers.get.unitMatcher.set(Protoss.Carrier) })
  
  class PriorityAttacks extends Parallel(
    new AttackWithDarkTemplar,
    new AttackWithScouts,
    new AttackWithCarrierFleet)
  
  class AttackRespectingMines extends If(
    new And(
      new Or(
        new SafeToAttack,
        new UnitsAtLeast(10, UnitMatchWarriors, complete = true)),
      new Or(
        new Employing(PvTEarly1015GateGoonDT),
        new Employing(PvTEarly4Gate),
        new Employing(PvTEarly1GateStargateTemplar),
        new IfOnMiningBases(3),
        new Not(new EnemyHasShown(Terran.Vulture)),
        new UnitsAtLeast(1, UnitMatchCustom((unit) => unit.is(Protoss.Observer) && With.framesSince(unit.frameDiscovered) > 24 * 10), complete = true),
        new UnitsAtLeast(20, UnitMatchWarriors, complete = true))),
    new ConsiderAttacking)
  
  class EmergencyBuilds extends Parallel(
    new If(
      new And(new Employing(PvT13Nexus), new EnemyUnitsAtLeast(1, Terran.Marine), new UnitsAtMost(0, Protoss.CyberneticsCore, complete = true)),
      new Parallel(
        new TrainContinuously(Protoss.Zealot),
        new Build(RequestAtLeast(2, Protoss.Gateway))))
  )
  
  class TrainScouts extends If(
    new And(
      new EnemyUnitsAtMost(0, Terran.Goliath),
      new EnemyUnitsAtMost(6, Terran.Marine),
      new EnemyUnitsAtMost(8, Terran.MissileTurret),
      new UnitsExactly(0, Protoss.FleetBeacon),
      new UnitsExactly(0, Protoss.ArbiterTribunal),
      new Employing(PvTEarly1GateStargateTemplar)),
    new TrainContinuously(Protoss.Scout, 5))
  
  class TrainDarkTemplar extends If(
    new UnitsAtMost(0, UnitMatchOr(Protoss.Arbiter, Protoss.ArbiterTribunal)),
    new If(
      new And(
        new EnemyUnitsAtMost(3, Terran.Vulture),
        new EnemyUnitsNone(Terran.ScienceVessel),
        new EnemyUnitsNone(UnitMatchCustom((unit) => unit.is(Terran.MissileTurret) && unit.zone.owner.isNeutral))),
      new TrainContinuously(Protoss.DarkTemplar, 3),
      new TrainContinuously(Protoss.DarkTemplar, 1)))
  
  private class IfCloakedThreats_Observers extends If(
    new Or(
      new Check(() => With.units.enemy.exists(u => u.is(Terran.Vulture) && u.spiderMines > 0)),
      new EnemyHasShown(Terran.SpiderMine),
      new EnemyHasShownWraithCloak),
    new Build(
      RequestAtLeast(1, Protoss.Pylon),
      RequestAtLeast(1, Protoss.Gateway),
      RequestAtLeast(1, Protoss.Assimilator),
      RequestAtLeast(1, Protoss.CyberneticsCore),
      RequestAtLeast(1, Protoss.RoboticsFacility),
      RequestAtLeast(1, Protoss.Observatory)))
  
  class TrainZealotsOrDragoons extends FlipIf(
    new Or(
      new And(
        new MineralsAtLeast(600),
        new GasAtMost(200)),
      new And(
        new UnitsAtLeast(10, Protoss.Dragoon),
        new UpgradeComplete(Protoss.ZealotSpeed, withinFrames = Protoss.ZealotSpeed.upgradeFrames.head._2))),
    new TrainContinuously(Protoss.Dragoon),
    new TrainContinuously(Protoss.Zealot, 30, 5))
  
  private class TrainObserversScalingWithArmy extends If(
    new UnitsAtLeast(1, UnitMatchWarriors),
    new If(
      new UnitsAtLeast(12, UnitMatchWarriors),
      new If(
        new UnitsAtLeast(24, UnitMatchWarriors),
        new TrainContinuously(Protoss.Observer, 3)),
      new TrainContinuously(Protoss.Observer, 2)),
    new TrainContinuously(Protoss.Observer, 1))
  
  class TrainObservers extends If(
    new EnemyHasShownWraithCloak,
    new TrainObserversScalingWithArmy,
    new If(
      new EnemyHasShown(Terran.SpiderMine),
      new TrainObserversScalingWithArmy,
      new TrainContinuously(Protoss.Observer, 1)))
  
  class TrainHighTemplar extends If(
    new And(
      new Or(
        new And(
          new HaveGasPumps(3),
          new EnemyUnitsAtLeast(12, Terran.Goliath)),
        new And(
          new HaveGasPumps(2),
          new EnemyBio))),
      new Parallel(
        new If(
          new UnitsAtLeast(1, Protoss.HighTemplar),
          new Build(RequestTech(Protoss.PsionicStorm))),
        new TrainContinuously(Protoss.HighTemplar, 5, 1)))
    
  class TrainArmy extends Parallel(
    new TrainObservers,
    new TrainContinuously(Protoss.Arbiter),
    new If(          new EnemyBio,  new TrainContinuously(Protoss.Reaver, 4)),
    new If(new Latch(new EnemyBio), new TrainContinuously(Protoss.Reaver, 2)),
    new TrainContinuously(Protoss.Carrier),
    new TrainDarkTemplar,
    new TrainHighTemplar,
    new TrainScouts,
    new TrainZealotsOrDragoons)
  
  class GetObserversForCloakedWraiths extends If(
    new EnemyHasShownWraithCloak,
    new Parallel(
      new Build(
        RequestAtLeast(1, Protoss.RoboticsFacility),
        RequestAtLeast(1, Protoss.Observatory)),
      new PvTIdeas.TrainObservers))
}

