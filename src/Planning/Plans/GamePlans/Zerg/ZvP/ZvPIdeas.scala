package Planning.Plans.GamePlans.Zerg.ZvP

import Planning.Plans.Army.{Attack, Hunt}
import Planning.Plans.Compound.{If, _}
import Planning.Plans.Macro.Automatic._
import Planning.Predicates.Milestones._
import ProxyBwapi.Races.{Protoss, Zerg}

object ZvPIdeas {

  class PumpScourgeAgainstAir extends Parallel(
    new If(
      new Or(
        new EnemyHasShown(Protoss.Stargate),
        new EnemyHasShown(Protoss.Corsair),
        new EnemyHasShown(Protoss.Scout),
        new EnemyHasShown(Protoss.Shuttle)),
      new PumpRatio(Zerg.Scourge, 0, 8, Seq(Flat(2), Enemy(Protoss.Corsair, 2), Enemy(Protoss.Scout, 3)))),
    new PumpRatio(Zerg.Scourge, 0, 24, Seq(Enemy(Protoss.Carrier, 6))))

  class AttackPlans extends Parallel(
    new Hunt(Zerg.Scourge, Protoss.Shuttle),
    new Hunt(Zerg.Scourge, Protoss.Corsair),
    new Hunt(Zerg.Scourge, Protoss.Carrier),
    new Hunt(Zerg.Scourge, Protoss.Scout),
    new Hunt(Zerg.Scourge, Protoss.Stargate),
    new Hunt(Zerg.Mutalisk, Protoss.HighTemplar),
    new Hunt(Zerg.Mutalisk, Protoss.Shuttle),
    new Hunt(Zerg.Mutalisk, Protoss.Reaver),
    new Attack
  )
}
