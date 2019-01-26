package Planning.Plans.GamePlans.Zerg.ZvT

import Macro.BuildRequests.{BuildRequest, Get}
import Planning.{Plan, Predicate}
import Planning.Plans.Army.{AllIn, Attack, EjectScout}
import Planning.Plans.Compound.If
import Planning.Plans.GamePlans.GameplanTemplate
import Planning.Plans.Macro.Automatic.Pump
import Planning.Plans.Macro.Expanding.{BuildGasPumps, RequireBases}
import Planning.Predicates.Milestones.EnemiesAtLeast
import Planning.Predicates.Strategy.Employing
import ProxyBwapi.Races.{Terran, Zerg}
import Strategery.Strategies.Zerg.ZvT2HatchLingBustMuta

class ZvT2HatchLingBustMuta extends GameplanTemplate {

  override val activationCriteria: Predicate = new Employing(ZvT2HatchLingBustMuta)

  override def attackPlan: Plan = new Attack

  // Based on Effort vs. Flash's 1-1-1:
  // https://www.youtube.com/watch?v=3sb47YGI7l8&feature=youtu.be&t=2280
  // https://docs.google.com/spreadsheets/d/1m6nU6FewJBC2LGQX_DPuo4PqzxH8hF3bazp8T6QlqRs/edit#gid=1166229923
  override def buildOrder: Seq[BuildRequest] = Seq(
    Get(9, Zerg.Drone),
    Get(2, Zerg.Overlord),
    Get(12, Zerg.Drone),
    Get(2, Zerg.Hatchery),
    Get(Zerg.Extractor),
    Get(Zerg.SpawningPool),
    Get(17, Zerg.Drone),
    Get(Zerg.ZerglingSpeed),
    Get(Zerg.Lair),
    Get(4, Zerg.Zergling),
    Get(3, Zerg.Overlord),
    Get(20, Zerg.Zergling),
    Get(Zerg.Spire),
    Get(26, Zerg.Zergling)
  )

  override def buildPlans: Seq[Plan] = Seq(
    new If(
      new EnemiesAtLeast(1, Terran.Vulture),
      new AllIn),
    new EjectScout,

    new Pump(Zerg.Mutalisk),
    new Pump(Zerg.Drone, 20),
    new RequireBases(3),
    new Pump(Zerg.Drone, 24),
    new BuildGasPumps
  )
}
