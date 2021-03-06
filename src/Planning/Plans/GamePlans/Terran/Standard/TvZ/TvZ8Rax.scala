package Planning.Plans.GamePlans.Terran.Standard.TvZ

import Information.Fingerprinting.Generic.GameTime
import Lifecycle.With
import Macro.BuildRequests.{BuildRequest, Get}
import Planning.Plans.Army.Attack
import Planning.Plans.Compound.{If, Or, Parallel}
import Planning.Plans.GamePlans.GameplanTemplate
import Planning.Plans.GamePlans.Protoss.Situational.DefendFightersAgainstRush
import Planning.Plans.GamePlans.Terran.Situational.RepairBunker
import Planning.Plans.GamePlans.Terran.Standard.TvZ.TvZIdeas.TvZFourPoolEmergency
import Planning.Plans.Macro.Automatic.Pump
import Planning.Plans.Macro.BuildOrders.Build
import Planning.Plans.Macro.Expanding.RequireMiningBases
import Planning.Plans.Macro.Terran.{BuildBunkersAtEnemyNatural, BuildBunkersAtNatural}
import Planning.Plans.Scouting.ScoutAt
import Planning.Predicates.Compound._
import Planning.Predicates.Milestones._
import Planning.Predicates.Strategy.{Employing, EnemyStrategy, StartPositionsAtLeast}
import Planning.UnitCounters.{UnitCountExactly, UnitCountExcept}
import Planning.{Plan, Predicate}
import ProxyBwapi.Races.{Terran, Zerg}
import Strategery.Strategies.Terran.TvZ8Rax

class TvZ8Rax extends GameplanTemplate {

  override val activationCriteria: Predicate = new Employing(TvZ8Rax)
  override val completionCriteria: Predicate = new Latch(new MiningBasesAtLeast(2))

  class CanBunkerRush extends Or(
    new Check(() => With.geography.enemyBases.exists(_.units.exists(u => u.isFriendly && u.is(Terran.Bunker)))),
    new And(
      new FrameAtMost(GameTime(3, 15)()),
      new EnemiesAtMost(0, Zerg.SunkenColony),
      new EnemiesAtMost(8, Zerg.Zergling),
      new EnemyStrategy(With.fingerprints.twelveHatch)))

  override def attackPlan: Plan = new If(
    new EnemyStrategy(With.fingerprints.twelveHatch),
    new Parallel(
      new Attack,
      new If(
        new CanBunkerRush,
        new If(
          new UnitsAtMost(0, Terran.Bunker, complete = true),
          new Attack(Terran.SCV, new UnitCountExcept(8, Terran.SCV)),
          new Attack(Terran.SCV, UnitCountExactly(2))))))

  override def initialScoutPlan: Plan = new If(
    new Not(new EnemyStrategy(With.fingerprints.twelveHatch, With.fingerprints.fourPool)),
    new If(
      new StartPositionsAtLeast(3),
      new ScoutAt(10, scoutCount = 2),
      new ScoutAt(10)))

  override def buildOrder: Seq[BuildRequest] = Seq(
    Get(8, Terran.SCV),
    Get(Terran.Barracks),
    Get(9, Terran.SCV),
    Get(Terran.SupplyDepot),
    Get(10, Terran.SCV))

  override def emergencyPlans: Seq[Plan] = Seq(
    new TvZFourPoolEmergency,
  )

  override def buildPlans: Seq[Plan] = Seq(
    new RepairBunker,
    new DefendFightersAgainstRush,

    new If(
      new CanBunkerRush,
      new BuildBunkersAtEnemyNatural(1)),

    new Pump(Terran.Marine),

    new TvZIdeas.TvZ1RaxExpandVs9Pool,

    new If(
      new EnemyStrategy(With.fingerprints.fourPool),
      new Build(Get(3, Terran.Barracks))),

    new If(
      new EnemyStrategy(With.fingerprints.tenHatch),
      new BuildBunkersAtNatural(1)),
    new RequireMiningBases(2),
  )
}