package Planning.Plans.GamePlans.Protoss

import Macro.BuildRequests.{BuildRequest, RequestUnitAtLeast, RequestUpgrade}
import Planning.Composition.UnitMatchers.{UnitMatchType, UnitMatchWarriors}
import Planning.Plans.Army.{Attack, Defend}
import Planning.Plans.Compound.{And, IfThenElse, Parallel}
import Planning.Plans.Information.{FindExpansions, FlyoverEnemyBases, ScoutAt}
import Planning.Plans.Macro.Automatic.{BuildEnoughPylons, TrainContinuously, TrainProbesContinuously}
import Planning.Plans.Macro.BuildOrders.ScheduleBuildOrder
import Planning.Plans.Macro.Reaction.EnemyThreatensMutalisks
import Planning.Plans.Macro.UnitCount.{SupplyAtLeast, UnitsAtLeast, UnitsExactly}
import ProxyBwapi.Races.Protoss

class ProtossVsZerg extends Parallel {
  
  description.set("Protoss vs Zerg")
  
  val _twoBase = Vector[BuildRequest] (
    new RequestUnitAtLeast(2,   Protoss.Assimilator),
    new RequestUnitAtLeast(4,   Protoss.Gateway),
    new RequestUnitAtLeast(1,   Protoss.Forge),
    new RequestUnitAtLeast(1,   Protoss.CitadelOfAdun),
    new RequestUpgrade(         Protoss.ZealotSpeed),
    new RequestUpgrade(         Protoss.GroundDamage, 1),
    new RequestUnitAtLeast(1,   Protoss.TemplarArchives),
    
    new RequestUnitAtLeast(3,   Protoss.Nexus),
    new RequestUnitAtLeast(1,   Protoss.RoboticsFacility),
    new RequestUnitAtLeast(4,   Protoss.PhotonCannon),
    new RequestUnitAtLeast(3,   Protoss.Assimilator),
    new RequestUpgrade(         Protoss.DragoonRange),
    new RequestUnitAtLeast(1,   Protoss.RoboticsSupportBay),
    new RequestUnitAtLeast(8,   Protoss.Gateway),
    
    new RequestUnitAtLeast(4,   Protoss.Nexus),
    new RequestUnitAtLeast(2,   Protoss.RoboticsFacility),
    new RequestUnitAtLeast(4,   Protoss.Assimilator),
    new RequestUnitAtLeast(1,   Protoss.TemplarArchives),
    new RequestUnitAtLeast(2,   Protoss.Forge),
    new RequestUnitAtLeast(7,   Protoss.PhotonCannon),
    new RequestUpgrade(         Protoss.GroundDamage, 2),
    new RequestUpgrade(         Protoss.GroundArmor, 2),
    new RequestUnitAtLeast(8,   Protoss.Gateway),
    
    new RequestUnitAtLeast(5,   Protoss.Nexus),
    new RequestUnitAtLeast(10,  Protoss.Gateway),
    new RequestUnitAtLeast(5,   Protoss.Assimilator),
    new RequestUpgrade(         Protoss.GroundDamage, 3),
    new RequestUpgrade(         Protoss.GroundArmor, 3),
    
    new RequestUnitAtLeast(6,   Protoss.Nexus),
    new RequestUnitAtLeast(12,  Protoss.Gateway),
    new RequestUnitAtLeast(6,   Protoss.Assimilator),
    
    new RequestUnitAtLeast(7,   Protoss.Nexus),
    new RequestUnitAtLeast(7,   Protoss.Assimilator),
    
    new RequestUnitAtLeast(8,   Protoss.Nexus),
    new RequestUnitAtLeast(8,   Protoss.Assimilator)
  )
  
  children.set(Vector(
    new ScheduleBuildOrder(ProtossBuilds.TwoGate99),
    new IfThenElse(
      new UnitsExactly(0, UnitMatchType(Protoss.CyberneticsCore)),
      new ScheduleBuildOrder(ProtossBuilds.TwoGate99Zealots)
    ),
    new BuildEnoughPylons,
    new TrainProbesContinuously,
    new TrainContinuously(Protoss.DarkTemplar, 1),
    new IfThenElse(
      new EnemyThreatensMutalisks,
      new Parallel(
        new ScheduleBuildOrder(ProtossBuilds.TechCorsairs),
        new ScheduleBuildOrder(ProtossBuilds.TechDragoons),
        new ScheduleBuildOrder(Vector(RequestUpgrade(Protoss.AirDamage))),
        new TrainContinuously(Protoss.Corsair, 12),
        new TrainContinuously(Protoss.Dragoon)
      ),
      new TrainContinuously(Protoss.Corsair, 4)
    ),
    new TrainContinuously(Protoss.DarkTemplar, 3),
    new TrainContinuously(Protoss.Reaver, 3),
    new IfThenElse(
      new And(
        new UnitsAtLeast(1, UnitMatchType(Protoss.CyberneticsCore)),
        new UnitsAtLeast(1, UnitMatchType(Protoss.Assimilator))
      ),
      new IfThenElse (
        new UnitsAtLeast(8, UnitMatchType(Protoss.Zealot)),
        new TrainContinuously(Protoss.Dragoon),
        new TrainContinuously(Protoss.Zealot)
      ),
      new TrainContinuously(Protoss.Zealot)
    ),
    new ScheduleBuildOrder(ProtossBuilds.TechCorsairs),
    new ScheduleBuildOrder(ProtossBuilds.TakeNatural),
    new ScheduleBuildOrder(_twoBase),
    new ScoutAt(10),
    new IfThenElse(
      new SupplyAtLeast(80),
      new FindExpansions
    ),
    new FlyoverEnemyBases,
    new Attack { attackers.get.unitMatcher.set(UnitMatchType(Protoss.Corsair)) },
    new IfThenElse(
      new UnitsAtLeast(10, UnitMatchWarriors),
      new Attack,
      new Defend
    )
  ))
}