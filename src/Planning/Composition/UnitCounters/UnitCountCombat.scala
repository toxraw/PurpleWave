package Planning.Composition.UnitCounters

import Information.Battles.Estimations.{AvatarBuilder, Estimation, EstimateAvatar}
import ProxyBwapi.UnitInfo.{FriendlyUnitInfo, UnitInfo}

class UnitCountCombat(
  val enemies       : Iterable[UnitInfo],
  val alwaysAccept  : Boolean,
  val overkill      : Double = 2.0) extends UnitCounter {
  
  val builder = new AvatarBuilder
  var lastEstimation: Estimation = _
  enemies.foreach(builder.addUnit)
  
  /////////////////
  // UnitCounter //
  /////////////////
  
  override def continue(units: Iterable[FriendlyUnitInfo]): Boolean = {
    units.foreach(builder.addUnit)
    lastEstimation = EstimateAvatar.calculate(builder)
    ! isSufficient
  }
  
  override def accept(units: Iterable[FriendlyUnitInfo]): Boolean = {
    units.foreach(builder.addUnit)
    lastEstimation = EstimateAvatar.calculate(builder)
    isAcceptable
  }
  
  //////////
  // Guts //
  //////////
  
  private def isSufficient: Boolean = {
    weKillThemAndSurvive || weOverkill
  }
  
  private def isAcceptable: Boolean = {
    alwaysAccept || isSufficient
  }
  
  private def weKillThemAndSurvive: Boolean = {
    lastEstimation.enemyDies && lastEstimation.weSurvive
  }
  
  private def weOverkill: Boolean = {
    lastEstimation.costToEnemy * overkill > lastEstimation.costToUs
  }
}
