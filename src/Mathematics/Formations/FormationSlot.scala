package Mathematics.Formations

import Mathematics.Points.Pixel
import ProxyBwapi.Races.Terran
import ProxyBwapi.UnitClasses.UnitClass
import ProxyBwapi.UnitInfo.UnitInfo

class FormationSlot(unit: UnitInfo) {
  val unitClass: UnitClass = unit.unitClass
  val idealPixels : Double = range(unit)
  
  var pixelAfter: Pixel = _
  
  private def range(unit: UnitInfo): Double = {
    if (unit.isSiegeTankUnsieged() && unit.player.hasTech(Terran.SiegeMode))
      Terran.SiegeTankSieged.effectiveRangePixels
    else
      unit.effectiveRangePixels
  }
}
