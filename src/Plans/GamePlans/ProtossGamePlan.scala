package Plans.GamePlans

import Plans.Allocation.LockUnits
import Plans.Army.{Attack, ControlPosition}
import Plans.Compound.{AllParallel, While}
import Plans.Defense.DefeatWorkerHarass
import Plans.Information.ScoutAt
import Plans.Macro.Automatic._
import Plans.Macro.Build.{FollowBuildOrder, ScheduleBuildOrder}
import Plans.Macro.UnitCount.UnitCountAtLeast
import Strategies.PositionFinders.PositionChoke
import Strategies.UnitCounters.UnitCountAll
import Strategies.UnitMatchers.UnitMatchWarriors
import Types.Buildable.{Buildable, BuildableUnit, BuildableUpgrade}
import bwapi.{UnitType, UpgradeType}

class ProtossGamePlan extends AllParallel {
  
  val _buildOrder = List[Buildable] (
    new BuildableUnit(UnitType.Protoss_Nexus),
    new BuildableUnit(UnitType.Protoss_Pylon),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUnit(UnitType.Protoss_Pylon),
    new BuildableUnit(UnitType.Protoss_Cybernetics_Core),
    new BuildableUnit(UnitType.Protoss_Assimilator),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUpgrade(UpgradeType.Singularity_Charge),
    new BuildableUnit(UnitType.Protoss_Nexus),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUnit(UnitType.Protoss_Assimilator),
    new BuildableUnit(UnitType.Protoss_Nexus),
    new BuildableUnit(UnitType.Protoss_Forge),
    new BuildableUpgrade(UpgradeType.Protoss_Ground_Weapons, 1),
    new BuildableUnit(UnitType.Protoss_Citadel_of_Adun),
    new BuildableUpgrade(UpgradeType.Leg_Enhancements),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUnit(UnitType.Protoss_Gateway),
    new BuildableUpgrade(UpgradeType.Protoss_Ground_Armor, 1),
    new BuildableUnit(UnitType.Protoss_Templar_Archives),
    new BuildableUpgrade(UpgradeType.Protoss_Ground_Weapons, 2),
    new BuildableUpgrade(UpgradeType.Protoss_Ground_Weapons, 3),
    new BuildableUpgrade(UpgradeType.Protoss_Ground_Armor, 2),
    new BuildableUpgrade(UpgradeType.Protoss_Ground_Armor, 3)
  )
  
  children.set(List(
    new BuildSupplyContinuously,
    new BuildWorkersContinuously,
    new BuildGatewayUnitsContinuously,
    new ScheduleBuildOrder { this.buildables.set(_buildOrder) },
    new FollowBuildOrder,
    new DefeatWorkerHarass,
    new ScoutAt(20),
    new While {
      predicate.set(new UnitCountAtLeast { quantity.set(5); unitMatcher.set(UnitMatchWarriors) })
      action.set(new Attack)
    },
    new ControlPosition {
      position.set(PositionChoke)
      units.set(new LockUnits {
        unitMatcher.set(UnitMatchWarriors)
        unitCounter.set(UnitCountAll)
      })
    },
    new GatherGas,
    new GatherMinerals
  ))
}