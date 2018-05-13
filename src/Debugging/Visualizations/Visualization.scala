package Debugging.Visualizations

import Debugging.Visualizations.Views.Battles.{ShowBattleDetails, ShowBattleSummary, ShowClustering}
import Debugging.Visualizations.Views.Economy.{ShowEconomy, ShowScheduler}
import Debugging.Visualizations.Views.Fun._
import Debugging.Visualizations.Views.Geography._
import Debugging.Visualizations.Views.Micro._
import Debugging.Visualizations.Views.Performance.{ShowPerformanceDetails, ShowPerformanceSummary, ShowReactionTime}
import Debugging.Visualizations.Views.Planning._
import Debugging.Visualizations.Views.{ShowClock, View}
import Lifecycle.With

import scala.collection.mutable
import scala.util.Random

class Visualization {
  
  //////////////
  // Settings //
  //////////////
  
  var views = mutable.ArrayBuffer(
    // Evergreen views
    ShowRushDistances,
    ShowClock,
    ShowStrategy,
    ShowUnitsAll,
    ShowUnitsFriendly,
    ShowUnitsForeign,
    ShowExplosions,
    ShowBattleSummary,
    ShowSquads
  )
  
  var enabled   : Boolean = _
  var screen    : Boolean = _
  var map       : Boolean = _
  var happy     : Boolean = _
  var textOnly  : Boolean = _
  
  //////////////
  
  val lineHeightSmall = 9
  
  def toggle(view: View) {
    if (views.contains(view)) {
      views -= view
    }
    else {
      views += view
    }
  }
  
  def tryToggle(viewName: String): Boolean = {
    val matches = knownViews.filter(_.name.toLowerCase.contains(viewName.toLowerCase))
    val matched = matches.size == 1
    if (matched) {
      toggle(matches.head)
    }
    matched
  }
  
  def render() {
    requireInitialization()
    if ( ! enabled) return
    if (With.tasks.tasks.exists(_.totalRuns == 0)) return
    With.game.setTextSize(bwapi.Text.Size.Enum.Small)
    
    if (happy) {
      ShowHappyVision.render()
    }
    else if (textOnly) {
      ShowTextOnly.render()
    }
    else {
      if (map) {
        views.foreach(_.renderMap())
      }
      if (screen) {
        views.foreach(_.renderScreen())
      }
      
    }
  }
  
  private var initialized = false
  private def requireInitialization() {
    if (initialized) return
    initialized = true
    enabled = With.configuration.visualize
    screen  = With.configuration.visualizeScreen
    map     = With.configuration.visualizeMap
    var random = Random.nextDouble()
    random -= With.configuration.visualizationProbabilityHappyVision
    if (random < 0) {
      happy = true
    }
    else {
      random -= With.configuration.visualizationProbabilityTextOnly
      if (random < 0) {
        textOnly = true
      }
    }
  }
  
  lazy val knownViews: Vector[View] = Vector[View](
    ShowBattleSummary,
    ShowBattleDetails,
    ShowEconomy,
    ShowScheduler,
    ShowBlackScreen,
    ShowBulletsAsHearts,
    ShowExplosions,
    ShowHappyUnits,
    ShowHappyVision,
    ShowHistory,
    ShowIntelligence,
    ShowTextOnly,
    ShowTextOnlyUnits,
    ShowArchitecturePlacements,
    ShowArchitectureHeuristics,
    ShowArchitecturePaths,
    ShowBases,
    ShowClustering,
    ShowDesire,
    ShowGroundskeeper,
    ShowGrids,
    ShowMobility,
    ShowTechniques,
    ShowUnitsAll,
    ShowUnitsForeign,
    ShowUnitsFriendly,
    ShowPerformanceDetails,
    ShowPerformanceSummary,
    ShowPlans,
    ShowReactionTime,
    ShowResources,
    ShowRushDistances,
    ShowSquads,
    ShowStrategy,
    ShowStrategyEvaluations,
    ShowStrategiesInterest,
    ShowZoneBorderTiles,
    ShowZones,
    ShowZoneLabels,
    ShowZonePathDemo
  )
}
