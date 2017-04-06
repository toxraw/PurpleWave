package Information.Grids.Construction

import Information.Grids.ArrayTypes.AbstractGridBoolean
import ProxyBwapi.Races.Protoss
import ProxyBwapi.UnitInfo.FriendlyUnitInfo
import Lifecycle.With
import Mathematics.Positions.Point
import Utilities.EnrichPosition._

abstract class AbstractGridPsi extends AbstractGridBoolean {
  
  private var lastPylons:Set[FriendlyUnitInfo] = Set.empty
  
  override def update(): Unit = {
    
    val newPylons = pylons
    if (newPylons == lastPylons) return
    lastPylons = newPylons
    
    reset()
    pylons.foreach(pylon =>
      psiPoints.foreach(point =>
        set(pylon.tileTopLeft.add(point), true)))
  }
  
  val psiPoints:Array[Point]
  def pylons:Set[FriendlyUnitInfo] = With.units.ours.filter(unit => unit.alive && unit.complete && unit.unitClass == Protoss.Pylon)
}
