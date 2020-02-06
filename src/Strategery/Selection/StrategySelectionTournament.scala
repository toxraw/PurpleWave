package Strategery.Selection

import Lifecycle.With
import Strategery.Plasma
import Strategery.Strategies.Strategy

object StrategySelectionTournament extends StrategySelectionPolicy {
  
  def chooseBest(topLevelStrategies: Iterable[Strategy], expand: Boolean = true): Iterable[Strategy] = {
    if (Plasma.matches) {
      return StrategySelectionGreedy.chooseBest(topLevelStrategies, expand)
    }
    
    val enemyName = With.configuration.playbook.enemyName
    val opponent =
      Opponents.all.find(_.matches(enemyName)).orElse(
        Opponents.all.find(_.matchesLoosely(enemyName)).orElse(
          Opponents.all.find(_.matchesVeryLoosely(enemyName))))
    
    if (opponent.isEmpty) {
      With.logger.warn("Didn't find opponent plan for " + enemyName)
      return StrategySelectionGreedy.chooseBest(topLevelStrategies, expand)
    }
    
    opponent
      .map(_.policy.chooseBest(topLevelStrategies))
      .getOrElse(StrategySelectionGreedy.chooseBest(topLevelStrategies, expand))
  }
}
