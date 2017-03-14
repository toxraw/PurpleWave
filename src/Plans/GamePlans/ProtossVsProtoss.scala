package Plans.GamePlans

import Plans.Army.{Attack, DefendChoke}
import Plans.Compound.{IfThenElse, JustOnce, Parallel}
import Plans.Defense.DefeatWorkerHarass
import Plans.Information.ScoutAt
import Plans.Macro.Automatic._
import Plans.Macro.Build.{FollowBuildOrder, ScheduleBuildOrder}
import Plans.Macro.UnitCount.UnitCountAtLeast
import Strategies.UnitMatchers.UnitMatchWarriors
import Types.Buildable.{Buildable, BuildableUnit, BuildableUpgrade}
import bwapi.{UnitType, UpgradeType}

class ProtossVsProtoss extends Parallel {
  
  description.set("Protoss vs Protoss")
  
  //http://wiki.teamliquid.net/starcraft/4_Gate_Goon_(vs._Protoss)
  val _fourGateGoons = List[Buildable] (
    new BuildableUnit(UnitType.Protoss_Nexus),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Pylon), //8
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Gateway), //10
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Pylon), //12
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Zealot), //13
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Assimilator), //16
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Cybernetics_Core), //17
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Zealot), //18
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Pylon), //22
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Dragoon), //23
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUpgrade(UpgradeType.Singularity_Charge), //26
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Dragoon), //27
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Probe),
    new BuildableUnit(UnitType.Protoss_Gateway), //31
    new BuildableUnit(UnitType.Protoss_Gateway), //31
    new BuildableUnit(UnitType.Protoss_Gateway), //31
    new BuildableUnit(UnitType.Protoss_Dragoon), //31
    new BuildableUnit(UnitType.Protoss_Pylon), //33
    new BuildableUnit(UnitType.Protoss_Dragoon), //33
    new BuildableUnit(UnitType.Protoss_Dragoon),
    new BuildableUnit(UnitType.Protoss_Dragoon),
    new BuildableUnit(UnitType.Protoss_Dragoon),
    new BuildableUnit(UnitType.Protoss_Pylon), //33
    new BuildableUnit(UnitType.Protoss_Dragoon), //33
    new BuildableUnit(UnitType.Protoss_Dragoon),
    new BuildableUnit(UnitType.Protoss_Dragoon),
    new BuildableUnit(UnitType.Protoss_Dragoon),
    new BuildableUnit(UnitType.Protoss_Nexus)
  )
  
  
  children.set(List(
    new JustOnce { child.set(new ScheduleBuildOrder { buildables.set(_fourGateGoons) }) },
    new BuildPylonsContinuously,
    new BuildWorkersContinuously,
    new TrainContinuously(UnitType.Protoss_Scout),
    new TrainContinuously(UnitType.Protoss_Zealot),
    new ScheduleBuildOrder { buildables.set(MassScoutLateGame.build) },
    new FollowBuildOrder,
    new DefeatWorkerHarass,
    new ScoutAt(20),
    new IfThenElse {
      predicate.set(new UnitCountAtLeast { quantity.set(6); unitMatcher.set(UnitMatchWarriors) })
      whenFalse.set(new DefendChoke)
      whenTrue.set(new Attack)
    },
    new GatherGas,
    new GatherMinerals))
}