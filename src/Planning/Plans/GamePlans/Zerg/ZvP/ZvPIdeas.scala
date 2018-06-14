package Planning.Plans.GamePlans.Zerg.ZvP

import Lifecycle.With
import Macro.BuildRequests.Get
import Planning.Plans.Army.Aggression
import Planning.Plans.Compound.{If, _}
import Planning.Plans.Macro.Automatic.Pump
import Planning.Plans.Macro.BuildOrders.{Build, BuildOrder}
import Planning.Plans.Macro.Expanding.RequireMiningBases
import Planning.Predicates.Economy.{GasAtLeast, MineralsAtLeast}
import Planning.Predicates.Milestones._
import Planning.Predicates.Reactive.SafeAtHome
import Planning.Predicates.Strategy.EnemyStrategy
import ProxyBwapi.Races.Zerg

object ZvPIdeas {
  
  class ShouldDoSpeedlingAllIn extends EnemyStrategy(
    With.fingerprints.cannonRush,
    With.fingerprints.proxyGateway)
  
  class DoSpeedlingAllIn extends Parallel(
    new Aggression(1.2),
    new BuildOrder(Get(10, Zerg.Zergling)),
    new If(
      new Or(
        new GasAtLeast(100),
        new UpgradeComplete(Zerg.ZerglingSpeed, 1, Zerg.ZerglingSpeed.upgradeFrames(1))),
      new Do(() => { With.blackboard.gasLimitFloor = 0; With.blackboard.gasLimitCeiling = 0 })),
    new FlipIf(
      new SafeAtHome,
      new Pump(Zerg.Zergling),
      new Pump(Zerg.Drone, 9)),
    new Build(
      Get(1, Zerg.Extractor),
      Get(Zerg.ZerglingSpeed)),
    new RequireMiningBases(3),
    new If(
      new MineralsAtLeast(400),
      new RequireMiningBases(4)))
  
  class OneBaseProtoss extends EnemyStrategy(
    With.fingerprints.cannonRush,
    With.fingerprints.proxyGateway,
    With.fingerprints.twoGate,
    With.fingerprints.oneGateCore)
  
  class TwoBaseProtoss extends EnemyStrategy(
    With.fingerprints.nexusFirst,
    With.fingerprints.forgeFe,
    With.fingerprints.gatewayFe)
}