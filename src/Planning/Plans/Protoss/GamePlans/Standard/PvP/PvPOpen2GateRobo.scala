package Planning.Plans.Protoss.GamePlans.Standard.PvP

import Lifecycle.With
import Macro.Architecture.Blueprint
import Macro.BuildRequests.{RequestAtLeast, RequestUpgrade}
import Planning.Composition.UnitMatchers.UnitMatchWarriors
import Planning.Plan
import Planning.Plans.Army.{ConsiderAttacking, DefendZones, EscortSettlers}
import Planning.Plans.Compound._
import Planning.Plans.GamePlans.Mode
import Planning.Plans.Information.Employing
import Planning.Plans.Information.Reactive.EnemyBasesAtLeast
import Planning.Plans.Macro.Automatic.{RequireSufficientSupply, TrainContinuously, TrainWorkersContinuously}
import Planning.Plans.Macro.Build.ProposePlacement
import Planning.Plans.Macro.BuildOrders.{Build, BuildOrder, RequireBareMinimum}
import Planning.Plans.Macro.Expanding.RequireMiningBases
import Planning.Plans.Macro.Milestones.{EnemyUnitsAtLeast, UnitsAtLeast}
import Planning.Plans.Protoss.Situational.Blueprinter
import Planning.Plans.Scouting.Scout
import ProxyBwapi.Races.Protoss
import Strategery.Strategies.Protoss.PvP.PvP2GateRoboObs

class PvPOpen2GateRobo extends Mode {
  
  override val activationCriteria: Plan = new Employing(PvP2GateRoboObs)
  
  override val completionCriteria: Plan = new Or(
    new EnemyBasesAtLeast(2),
    new UnitsAtLeast(2, Protoss.Nexus),
    new UnitsAtLeast(40, UnitMatchWarriors))
  
  private class ProposeCannonsAtExpanion extends ProposePlacement {
    override lazy val blueprints: Iterable[Blueprint] = Blueprinter.pylonsAndCannonsAtNatural(this, 1, 3)
  }
  
  children.set(Vector(
    new Do(() => With.blackboard.gasBankSoftLimit = 450),
    new RequireBareMinimum,
    new If(
      new EnemyUnitsAtLeast(1, Protoss.DarkTemplar),
      new Build(RequestAtLeast(1, Protoss.Observer))),
    new BuildOrder(
      // http://wiki.teamliquid.net/starcraft/2_Gate_Reaver_(vs._Protoss)
      // We get gas/core faster because of mineral locking + later scout
      RequestAtLeast(8,   Protoss.Probe),
      RequestAtLeast(1,   Protoss.Pylon),             // 8
      RequestAtLeast(10,  Protoss.Probe),
      RequestAtLeast(1,   Protoss.Gateway),           // 10
      RequestAtLeast(11,  Protoss.Probe),
      RequestAtLeast(1,   Protoss.Assimilator),       // 11
      RequestAtLeast(13,  Protoss.Probe),
      RequestAtLeast(1,   Protoss.Zealot),            // 13
      RequestAtLeast(14,  Protoss.Probe),
      RequestAtLeast(2,   Protoss.Pylon),             // 16 = 14 + Z
      RequestAtLeast(16,  Protoss.Probe),
      RequestAtLeast(1,   Protoss.CyberneticsCore),   // 18 = 16 + Z
      RequestAtLeast(17,  Protoss.Probe),
      RequestAtLeast(2,   Protoss.Zealot),            // 19 = 17 + Z
      RequestAtLeast(18,  Protoss.Probe),
      RequestAtLeast(3,   Protoss.Pylon),             // 22 = 18 + ZZ
      RequestAtLeast(19,  Protoss.Probe),
      RequestAtLeast(1,   Protoss.Dragoon),           // 23 = 19 + ZZ
      RequestAtLeast(20,  Protoss.Probe),
      RequestAtLeast(2,   Protoss.Gateway),           // 26 = 20 + ZZ + D
      RequestAtLeast(21,  Protoss.Probe),
      RequestAtLeast(2,   Protoss.Dragoon),           // 27 = 21 + ZZ + D
      RequestAtLeast(22,  Protoss.Probe),
      RequestAtLeast(3,   Protoss.Pylon),
      RequestUpgrade(Protoss.DragoonRange)),
    
    new Trigger(new UnitsAtLeast(1, Protoss.Reaver), new RequireMiningBases(2)),
    
    new RequireSufficientSupply,
    new TrainWorkersContinuously(oversaturate = true),
    new TrainContinuously(Protoss.Reaver),
    new TrainContinuously(Protoss.Dragoon),
    new Build(
      RequestAtLeast(1, Protoss.RoboticsFacility),
      RequestAtLeast(1, Protoss.Observatory),
      RequestAtLeast(1, Protoss.RoboticsSupportBay),
      RequestAtLeast(1, Protoss.Observer),
      RequestAtLeast(3, Protoss.Gateway),
      RequestAtLeast(2, Protoss.Nexus)),
      
    new If(new UnitsAtLeast(1, Protoss.CyberneticsCore), new Scout),
  
    new DefendZones,
    new EscortSettlers,
    new Trigger(
      new UnitsAtLeast(2, Protoss.Reaver, complete = true),
      initialAfter = new ConsiderAttacking)
  ))
}