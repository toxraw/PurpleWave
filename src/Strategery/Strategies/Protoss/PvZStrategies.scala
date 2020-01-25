package Strategery.Strategies.Protoss

import Information.Intelligenze.Fingerprinting.Fingerprint
import Lifecycle.With
import Strategery.Strategies.Strategy
import Strategery.{MapGroups, StarCraftMap}
import bwapi.Race

abstract class PvZStrategy extends Strategy {
  override def ourRaces: Iterable[Race] = Vector(Race.Protoss)
  override def enemyRaces: Iterable[Race] = Vector(Race.Zerg)
}

abstract class PvZFFEOpening extends PvZStrategy {
  override def mapsBlacklisted: Iterable[StarCraftMap] = MapGroups.badForWalling ++ MapGroups.tooShortForFFE
  override def choices: Iterable[Iterable[Strategy]] = Vector(
    ProtossChoices.pvzMidgameTransitioningFromTwoBases
  )
}
abstract class PvZ2GateOpening extends PvZStrategy {
  override def choices: Iterable[Iterable[Strategy]] = Vector(
    ProtossChoices.pvzOpenersTransitioningFrom2Gate
  )
}
object PvZ1BaseForgeTech extends PvZStrategy {
  override def allowedVsHuman: Boolean = false
  override def responsesWhitelisted: Iterable[Fingerprint] = Seq(With.fingerprints.fourPool, With.fingerprints.ninePool, With.fingerprints.ninePoolGas)
  override def choices: Iterable[Iterable[Strategy]] = Vector(Seq(PvZMidgameNeoBisu))
}
object PvZ4GatePlusOne extends PvZStrategy {
  override def choices: Iterable[Iterable[Strategy]] = Vector(Seq(PvZMidgame5GateGoon, PvZMidgame5GateGoonReaver, PvZMidgameBisu, PvZMidgameNeoBisu, PvZMidgameNeoNeoBisu, PvZMidgame4Gate2Archon))
}
object PvZ4GateGoon extends PvZStrategy {
  override def choices: Iterable[Iterable[Strategy]] = Vector(Seq(PvZMidgame5GateGoon, PvZMidgame5GateGoonReaver))
}
object PvZProxy2Gate extends PvZ2GateOpening {
  override def mapsBlacklisted: Iterable[StarCraftMap] = MapGroups.badForProxying
  override def responsesBlacklisted = Iterable(With.fingerprints.fourPool, With.fingerprints.ninePool, With.fingerprints.tenHatch)
}
object PvZ2Gate1012 extends PvZ2GateOpening {
  override def choices: Iterable[Iterable[Strategy]] = Iterable(Iterable(PvZ4GatePlusOne, PvZ4GateGoon))
}
object PvZ2Gate910 extends PvZ2GateOpening {
  override def choices: Iterable[Iterable[Strategy]] = Iterable(Iterable(PvZ4GatePlusOne, PvZ4GateGoon))
}
object PvZFFEConservative extends PvZFFEOpening {
  override def responsesWhitelisted: Iterable[Fingerprint] = Vector(With.fingerprints.fourPool)
  override def allowedVsHuman: Boolean = false
}
object PvZFFEEconomic     extends PvZFFEOpening
object PvZGatewayFE       extends PvZFFEOpening {
  override def minimumGamesVsOpponent: Int = 2
  override def responsesWhitelisted = Seq(With.fingerprints.twelveHatch, With.fingerprints.tenHatch)
  override def responsesBlacklisted = Seq(With.fingerprints.fourPool, With.fingerprints.ninePool, With.fingerprints.overpool, With.fingerprints.twelvePool)
}

object PvZLateGameTemplar extends PvZStrategy
object PvZLateGameReaver  extends PvZStrategy
object PvZLateGameCarrier extends PvZStrategy

object PvZMidgame4Gate2Archon         extends PvZStrategy { override def choices: Iterable[Iterable[Strategy]] = Seq(Seq(PvZLateGameTemplar)) }
object PvZMidgame5GateGoon            extends PvZStrategy { override def choices: Iterable[Iterable[Strategy]] = Seq(Seq(PvZLateGameTemplar)) }
object PvZMidgame5GateGoonReaver      extends PvZStrategy { override def choices: Iterable[Iterable[Strategy]] = Seq(Seq(PvZLateGameReaver))  }
object PvZMidgameCorsairReaverZealot  extends PvZStrategy { override def choices: Iterable[Iterable[Strategy]] = Seq(Seq(PvZLateGameReaver, PvZLateGameCarrier)) }
object PvZMidgameCorsairReaverGoon    extends PvZStrategy { override def choices: Iterable[Iterable[Strategy]] = Seq(Seq(PvZLateGameReaver, PvZLateGameCarrier)) }
object PvZMidgameBisu                 extends PvZStrategy { override def choices: Iterable[Iterable[Strategy]] = Seq(Seq(PvZLateGameTemplar)) }
object PvZMidgameNeoBisu              extends PvZStrategy { override def choices: Iterable[Iterable[Strategy]] = Seq(Seq(PvZLateGameTemplar)) }
object PvZMidgameNeoNeoBisu           extends PvZStrategy { override def choices: Iterable[Iterable[Strategy]] = Seq(Seq(PvZLateGameTemplar)) }
