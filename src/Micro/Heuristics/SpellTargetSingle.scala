package Micro.Heuristics

import ProxyBwapi.UnitInfo.{FriendlyUnitInfo, UnitInfo}

object SpellTargetSingle {
  
  def chooseTarget(
    caster              : FriendlyUnitInfo,
    searchRadiusPixels  : Double,
    minimumValue        : Double,
    evaluate            : (UnitInfo, FriendlyUnitInfo) => Double): Option[UnitInfo] = {
    
    val targets = caster.matchups.others.filter(t => t.pixelDistanceCenter(caster) <= searchRadiusPixels && t.visible && ! t.effectivelyCloaked)
    
    if (targets.isEmpty) {
      return None
    }
    
    val valueByTarget = targets.map(t => (t, evaluate(t, caster))).toMap
    
    val best = valueByTarget.maxBy(_._2)
    
    if (best._2 >= minimumValue)
      Some(best._1)
    else
      None
  }
}
