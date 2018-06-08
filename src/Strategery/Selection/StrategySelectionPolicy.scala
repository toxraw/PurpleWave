package Strategery.Selection

import Strategery.Strategies.Strategy

trait StrategySelectionPolicy {
  def chooseBest(topLevelStrategies: Iterable[Strategy]): Iterable[Strategy]
}