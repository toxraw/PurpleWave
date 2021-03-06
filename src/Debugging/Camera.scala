package Debugging

import Lifecycle.With
import Mathematics.Points.{Pixel, SpecificPoints, Tile, TileRectangle}
import ProxyBwapi.Races.{Protoss, Terran}
import ProxyBwapi.UnitInfo.UnitInfo
import Utilities.ByOption
import bwapi.MouseButton

class Camera {
  
  private var tweenFrom   : Pixel     = SpecificPoints.middle
  private var focus       : Pixel     = With.self.startTile.pixelCenter
  private var focusUnit   : UnitInfo  = _
  
  private val refocusLimit      = 96
  private var focusFrame        = -240
  private val tweenFrames       = 24
  private var impatienceFrames  = 24 * 10
  
  var obscurityByUnit   : Map[UnitInfo, Double] = Map.empty
  var visibleArea       : TileRectangle         = TileRectangle(Tile(0, 0), Tile(0, 0))
  var visibleUnits      : Set[UnitInfo]         = Set.empty
  var enabled           : Boolean               = true

  private def totalInterest(unit: UnitInfo): Double = {
    val interestBattle    = if (unit.battle.exists(_.enemy.units.exists(u => u.canAttack && ! u.unitClass.isWorker))) 10.0 else 1.0
    val obscurityUnit     = obscurityByUnit(unit)
    val interestActivity  = if (unit.training || unit.upgrading || unit.teching || unit.moving || unit.attacking) 3.0 else 1.0
    interestBattle * obscurityUnit * interestActivity
  }

  def onFrame() {
    if ( ! With.configuration.debugging) { return }

    // Enable autocamera until we interact with the screen
    val mousePosition = new Pixel(With.game.getMousePosition)
    if (mousePosition.x > 10 && mousePosition.x < 630 && mousePosition.y > 10 && mousePosition.y < 470) {
      enabled = enabled && ! With.game.getMouseState(MouseButton.M_LEFT)
      enabled = enabled && ! With.game.getMouseState(MouseButton.M_MIDDLE)
      enabled = enabled && ! With.game.getMouseState(MouseButton.M_RIGHT)
    }

    if ( ! enabled) { return }

    val interestGain  = 1.0 / impatienceFrames
    val interestDecay = 2 * interestGain

    obscurityByUnit = With.units.ours
      .map(unit => {
        var interest = obscurityByUnit.getOrElse(unit, 1.0)
        if (visibleUnits.contains(unit)) interest -= 5 else interest += 1
        interest = Math.max(1.0, interest)
        (unit, interest)
      }).toMap
    
    val eligibleUnits       = With.units.ours.filterNot(_.isAny(Protoss.Interceptor, Protoss.Scarab, Terran.SpiderMine))
    val unitInterests       = With.units.ours.map(unit => (unit, totalInterest(unit)))
    val mostInterestingUnit = ByOption.maxBy(unitInterests)(_._2)
    
    if (mostInterestingUnit.isDefined) {
      focusOn(mostInterestingUnit.get._1)
    }
  
    if (focusUnit != null && focusUnit.battle.isDefined) {
      setCameraSpeed(With.configuration.cameraDynamicSpeedSlowest)
    }
    else {
      setCameraSpeed(With.configuration.cameraDynamicSpeedFastest)
    }
    
    tween()
  
    visibleArea   = TileRectangle(With.viewport.start.tileIncluding, With.viewport.start.pixel.add(Pixel(640, 400)).tileIncluding)
    visibleUnits  = With.units.inTileRectangle(visibleArea).filter(_.visible).toSet
  }
  
  def focusOn(unit: UnitInfo) {
    if (With.framesSince(focusFrame) < refocusLimit) return
    focusUnit = unit
    focusFrame = With.frame
    tweenFrom = focus
  }
  
  def setCameraSpeed(speed: Int) {
    if (With.configuration.cameraDynamicSpeed) {
      With.game.setLocalSpeed(speed)
    }
  }
  
  def tween() {
    if (focusUnit != null && focusUnit.alive) {
      val start = focusUnit.pixelCenter
      focus = if (focusUnit.battle.exists(_.enemy.units.nonEmpty)) start.project(focusUnit.battle.get.focus, 120) else start
      val tweenFraction = Math.max(0.0, Math.min(1.0, (With.framesSince(focusFrame) + 1).toDouble/tweenFrames))
      val tweenPoint    = tweenFrom.project(focus, tweenFrom.pixelDistance(focus) * tweenFraction)
      With.viewport.centerOn(tweenPoint)
    }
  }
}
