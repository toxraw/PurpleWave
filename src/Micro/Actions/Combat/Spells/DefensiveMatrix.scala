package Micro.Actions.Combat.Spells

import Lifecycle.With
import ProxyBwapi.Races.Terran
import ProxyBwapi.Techs.Tech
import ProxyBwapi.UnitClass.UnitClass
import ProxyBwapi.UnitInfo.{FriendlyUnitInfo, UnitInfo}

object DefensiveMatrix extends TargetedSpell {
  
  override protected def casterClass    : UnitClass = Terran.ScienceVessel
  override protected def tech           : Tech      = Terran.DefensiveMatrix
  override protected def aoe            : Boolean   = false
  override protected def castRangeTiles : Int       = 10
  override protected def thresholdValue : Double    = Terran.Goliath.subjectiveValue
  
  override def additionalConditions(unit: FriendlyUnitInfo): Boolean = {
    ! With.self.hasTech(Terran.Irradiate) &&
    ! With.self.hasTech(Terran.EMP)
  }
  
  override protected def valueTarget(target: UnitInfo): Double = {
    if (target.unitClass.isBuilding)      return 0.0
    if (target.isEnemy)                   return 0.0
    if (target.defensiveMatrixPoints > 0) return 0.0
  
    val dangerFrames  = 128.0
    val dangerBonus   = dangerFrames / Math.max(dangerFrames, target.matchups.framesToLiveDiffused)
    val output        = dangerBonus * target.subjectiveValue
    output
  }
}
