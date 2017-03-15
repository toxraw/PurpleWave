package Planning.Plans.GamePlans

import Planning.Plans.Army.Attack
import Planning.Plans.Compound.Parallel
import Planning.Plans.Information.ScoutAt
import Planning.Plans.Macro.Automatic._
import Planning.Plans.Macro.Build.ScheduleBuildOrder
import Macro.BuildRequests.{BuildRequest, RequestUnitAnother, RequestUnitAnotherOne, RequestUpgrade}
import bwapi.{UnitType, UpgradeType}

class ProtossVsTerran extends Parallel {
  
  description.set("Protoss vs Terran")
  
  //Via http://wiki.teamliquid.net/starcraft/14_Nexus_(vs._Terran)
  val _14Nexus = List[BuildRequest] (
    new RequestUnitAnotherOne(UnitType.Protoss_Nexus), //0
    new RequestUnitAnother(8, UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Pylon), //8
    new RequestUnitAnother(6, UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Nexus), //14
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway), //14
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Assimilator), //15
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Cybernetics_Core), //17
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway), //17
    new RequestUnitAnotherOne(UnitType.Protoss_Zealot), //17
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Pylon), //21
    new RequestUnitAnotherOne(UnitType.Protoss_Dragoon), //21
    new RequestUnitAnotherOne(UnitType.Protoss_Dragoon), //23
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUpgrade(UpgradeType.Singularity_Charge), //25
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Probe),
    new RequestUnitAnotherOne(UnitType.Protoss_Pylon), //27
    new RequestUnitAnotherOne(UnitType.Protoss_Dragoon), //27
    new RequestUnitAnotherOne(UnitType.Protoss_Dragoon), //29
    new RequestUnitAnotherOne(UnitType.Protoss_Probe), //31
    new RequestUnitAnotherOne(UnitType.Protoss_Probe), //32
    new RequestUnitAnotherOne(UnitType.Protoss_Pylon), //33
    new RequestUnitAnotherOne(UnitType.Protoss_Probe), //31
    new RequestUnitAnotherOne(UnitType.Protoss_Probe), //32
    new RequestUnitAnotherOne(UnitType.Protoss_Dragoon), //35
    new RequestUnitAnotherOne(UnitType.Protoss_Dragoon) //37
  )
  
  val _lateGame = List[BuildRequest] (
    new RequestUnitAnotherOne(UnitType.Protoss_Nexus),
    new RequestUnitAnotherOne(UnitType.Protoss_Assimilator),
    new RequestUnitAnotherOne(UnitType.Protoss_Robotics_Facility),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Robotics_Support_Bay),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Assimilator),
    new RequestUnitAnotherOne(UnitType.Protoss_Citadel_of_Adun),
    new RequestUpgrade(UpgradeType.Leg_Enhancements),
    new RequestUnitAnotherOne(UnitType.Protoss_Nexus),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Forge),
    new RequestUpgrade(UpgradeType.Protoss_Ground_Weapons, 1),
    new RequestUnitAnotherOne(UnitType.Protoss_Templar_Archives),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Nexus),
    new RequestUpgrade(UpgradeType.Protoss_Ground_Weapons, 2),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Nexus),
    new RequestUpgrade(UpgradeType.Protoss_Ground_Weapons, 3),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway),
    new RequestUnitAnotherOne(UnitType.Protoss_Gateway)
  )
  
  children.set(List(
    new ScheduleBuildOrder { buildables.set(_14Nexus) },
    new BuildWorkersContinuously,
    new TrainContinuously(UnitType.Protoss_Reaver),
    new TrainContinuously(UnitType.Protoss_Dragoon),
    new TrainContinuously(UnitType.Protoss_Zealot),
    new ScheduleBuildOrder { buildables.set(_lateGame) },
    new ScoutAt(28),
    new Attack
  ))
}