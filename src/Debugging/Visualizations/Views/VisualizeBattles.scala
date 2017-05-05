package Debugging.Visualizations.Views

import Debugging.Visualizations.{Colors, Visualization}
import Debugging.Visualizations.Rendering.{DrawMap, DrawScreen}
import Information.Battles.BattleTypes.Battle
import Information.Battles.Estimation.{BattleEstimationCalculationState, BattleEstimationResult}
import Information.Battles.TacticsTypes.{Tactics, TacticsOptions}
import Lifecycle.With
import Mathematics.Pixels.Pixel
import Planning.Yolo
import ProxyBwapi.Players.PlayerInfo
import Utilities.EnrichPixel._
import bwapi.Color

import scala.collection.mutable.ArrayBuffer

object VisualizeBattles {
  
  private val graphMargin         = Pixel(2, 2)
  private val graphAreaStart      = Pixel(5,  18)
  private val graphDimensions     = Pixel(90, 180 + 2 * Visualization.lineHeightSmall)
  private val healthGraphStart    = graphAreaStart.add(graphMargin).add(0, Visualization.lineHeightSmall)
  private val healthGraphEnd      = graphAreaStart.add(graphDimensions.x, 90 + Visualization.lineHeightSmall).subtract(graphMargin)
  private val positionGraphStart  = graphAreaStart.add(0, 90 + Visualization.lineHeightSmall * 3).add(graphMargin)
  private val positionGraphEnd    = graphAreaStart.add(graphDimensions).subtract(graphMargin)
  private val tableHeader0        = Pixel(95, 18)
  private val tableHeader1        = tableHeader0.add(125, 0)
  private val tableStart0         = tableHeader0.add(0, 25)
  private val tableStart1         = tableHeader1.add(0, 25)
  private val army0               = Pixel(438, 18)
  private val army1               = Pixel(521, 18)
  private val army2               = Pixel(589, 18)
  private val yolo                = Pixel(5, 5)
  private val tacticsRanks        = Pixel(225, 18)
  
  def render() {
    With.game.drawTextScreen(army0.bwapi, "Overall:")
    With.game.drawTextScreen(army1.bwapi, "+" + With.battles.global.estimation.costToEnemy.toInt)
    With.game.drawTextScreen(army2.bwapi, "-" + With.battles.global.estimation.costToUs.toInt)
    With.battles.local.foreach(drawBattle)
    val localBattles = With.battles.local.filter(_.happening)
    if (localBattles.nonEmpty) {
      val battle      = localBattles.minBy(battle => battle.focus.pixelDistanceSquared(With.viewport.center))
      val tactics     = battle.bestTactics
      val estimation  = battle.estimation(tactics)
      estimation.foreach(drawEstimationReport)
      drawTacticsReport(battle)
    }
    if (Yolo.enabled && With.frame / 24 % 2 == 0) {
      With.game.drawTextScreen(yolo.bwapi, "YOLO")
    }
  }
  
  private def drawBattle(battle:Battle) {
    val ourColor            = With.self.colorDark
    val enemyColor          = With.enemies.head.colorDark
    val neutralColor        = Color.Black
    val topLeft             = (battle.us.units ++ battle.enemy.units).map(_.pixelCenter).minBound.subtract(16, 16)
    val bottomRight         = (battle.us.units ++ battle.enemy.units).map(_.pixelCenter).maxBound.add(16, 16)
    val winnerStrengthColor = if (battle.estimation.costToEnemy >=  battle.estimation.costToUs) ourColor else enemyColor
    DrawMap.circle  (battle.focus,          8,                      neutralColor)
    DrawMap.circle  (battle.us.vanguard,    8,                      ourColor)
    DrawMap.circle  (battle.enemy.vanguard, 8,                      enemyColor)
    DrawMap.line    (battle.focus,          battle.us.vanguard,     ourColor)
    DrawMap.line    (battle.focus,          battle.enemy.vanguard,  enemyColor)
    DrawMap.box     (topLeft,               bottomRight,            neutralColor)
    DrawMap.labelBox(
      Vector(battle.estimation.netCost.toInt.toString),
      battle.focus.add(24, 0),
      drawBackground = true,
      backgroundColor = winnerStrengthColor)
  }
  
  private def drawEstimationReport(estimation:BattleEstimationResult) {
    With.game.setTextSize(bwapi.Text.Size.Enum.Large)
    With.game.drawTextScreen(tableHeader0.bwapi, "+" + estimation.costToEnemy.toInt)
    With.game.drawTextScreen(tableHeader1.bwapi, "-" + estimation.costToUs.toInt)
    With.game.setTextSize(bwapi.Text.Size.Enum.Small)
    
    if (estimation.statesUs.isEmpty) return
    
    With.game.drawBoxScreen(graphAreaStart.bwapi,     graphAreaStart.add(graphDimensions).bwapi,  Color.Black, true)
    With.game.drawBoxScreen(healthGraphStart.bwapi,   healthGraphEnd.bwapi,                       Colors.DarkGray)
    With.game.drawBoxScreen(positionGraphStart.bwapi, positionGraphEnd.bwapi,                     Colors.DarkGray)
    
    With.game.drawTextScreen(healthGraphStart   .subtract(0, Visualization.lineHeightSmall).bwapi, "Health:")
    With.game.drawTextScreen(positionGraphStart .subtract(0, Visualization.lineHeightSmall).bwapi, "Position:")
    
    val allStates = estimation.statesUs ++ estimation.statesEnemy
    val healthMax = Math.max(estimation.statesUs.head.avatar.totalHealth, estimation.statesEnemy.head.avatar.totalHealth)
    val xMin      = allStates.map(state => state.x - state.spread).min
    val xMax      = allStates.map(state => state.x + state.spread).max
    
    drawHealthGraph(estimation.statesUs,     healthMax, With.self)
    drawHealthGraph(estimation.statesEnemy,  healthMax, With.enemies.head)
    
    drawPositionGraph(estimation.statesUs,     xMin, xMax, With.self)
    drawPositionGraph(estimation.statesEnemy,  xMin, xMax, With.enemies.head)
  }
  
  def drawHealthGraph(
    states    : ArrayBuffer[BattleEstimationCalculationState],
    valueMax  : Double,
    player    : PlayerInfo) {
  
    val xScale  = (healthGraphEnd.x - healthGraphStart.x) / states.size.toDouble
    val yScale  = (healthGraphEnd.y - healthGraphStart.y) / valueMax
  
    var i = 0
    while (i < states.size - 1) {
      val xBefore = healthGraphStart.x + xScale * i
      val xAfter  = healthGraphStart.x + xScale * (i + 1)
      val yBefore = healthGraphEnd.y - (states(i  ).avatar.totalHealth - states(i  ).damage) * yScale
      val yAfter  = healthGraphEnd.y - (states(i+1).avatar.totalHealth - states(i+1).damage) * yScale
      With.game.drawLineScreen(xBefore.toInt, yBefore.toInt, xAfter.toInt, yAfter.toInt, player.colorNeon)
      i += 1
    }
  }
  
  def drawPositionGraph(
    states  : ArrayBuffer[BattleEstimationCalculationState],
    xMin    : Double,
    xMax    : Double,
    player  : PlayerInfo) {
    
    val colorMedium = player.colorMedium
    val colorNeon   = player.colorNeon
    val xScale      = (positionGraphEnd.x - positionGraphStart.x) / states.size.toDouble
    val yScale      = (positionGraphEnd.y - positionGraphStart.y) / (xMax - xMin)
    
    var i = 0
    while (i < states.size - 1) {
      val xStart    = positionGraphStart.x + (xScale *  i     ).toInt
      val xEnd      = positionGraphStart.x + (xScale * (i + 1)).toInt
      val yMiddle0  = positionGraphStart.y + (yScale * (graphAreaStart.y + states(i  ).x - xMin)).toInt
      val yMiddle1  = positionGraphStart.y + (yScale * (graphAreaStart.y + states(i+1).x - xMin)).toInt
      val ySpread0  = (yScale * states(i  ).spread).toInt
      val ySpread1  = (yScale * states(i+1).spread).toInt
      With.game.drawLineScreen(xStart, yMiddle0 - ySpread0, xEnd, yMiddle1 - ySpread1, colorMedium)
      With.game.drawLineScreen(xStart, yMiddle0 + ySpread0, xEnd, yMiddle1 + ySpread1, colorMedium)
      With.game.drawLineScreen(xStart, yMiddle0,            xEnd, yMiddle1,            colorNeon)
      i += 1
    }
  }
  
  private def getMove(tactics:TacticsOptions):String = {
    if      (tactics.has(Tactics.Movement.Charge))  "Charge"
    else if (tactics.has(Tactics.Movement.Flee))    "Flee"
    else                                            "-"
  }
  
  private def getFocus(tactics:TacticsOptions):String = {
    if      (tactics.has(Tactics.Focus.Air))    "Air"
    else if (tactics.has(Tactics.Focus.Ground)) "Ground"
    else                                        "-"
  }
  
  private def getWounded(tactics:TacticsOptions):String = {
    if (tactics.has(Tactics.Wounded.Flee))  "Flee"
    else                                    "-"
  }
  
  private def getWorkers(tactics:TacticsOptions):String = {
    if      (tactics.has(Tactics.Workers.FightAll))   "Fight (All)"
    else if (tactics.has(Tactics.Workers.FightHalf))  "Fight (Half)"
    else if (tactics.has(Tactics.Workers.Flee))       "Flee"
    else                                              "-"
  }
  
  private def drawTacticsReport(battle:Battle) {
    drawTacticsReport(battle.bestTactics,           tableStart0, With.self.name)
    drawTacticsReport(battle.enemy.tacticsApparent, tableStart1, With.enemies.head.name)
    
    if (With.configuration.visualizeBattleTacticsRanks) {
      With.game.drawTextScreen(
        tacticsRanks.bwapi,
        battle.rankedTactics
          .zipWithIndex
          .map(pair => "#" + (pair._2 + 1) + " " + pair._1)
          .mkString("\n"))
    }
  }
  
  private def drawTacticsReport(tactics: TacticsOptions, origin:Pixel, playerName:String) {
    DrawScreen.table(
      origin.x,
      origin.y,
      Vector(
        Vector(playerName),
        Vector(""),
        Vector("Move:",     getMove(tactics)),
        Vector("Focus:",    getFocus(tactics)),
        Vector("Workers:",  getWorkers(tactics)),
        Vector("Wounded:",  getWounded(tactics))
      ))
  }
}

