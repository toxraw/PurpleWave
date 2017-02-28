package Strategies.PositionFinders

import Startup.With
import Utilities.Cache
import Utilities.Enrichment.EnrichUnitType._
import Utilities.Enrichment.EnrichPosition._
import bwapi.{TilePosition, UnitType}

class PositionSimpleBuilding(
  val buildingType:UnitType)
    extends PositionFinder {
  
  val _cache = new Cache[Option[TilePosition]] { duration = 24 * 2; setCalculator(() => _recalculate) }
  override def find: Option[TilePosition] = _cache.get
  
  def _recalculate: Option[TilePosition] = {
    val startPosition = With.geography.home.toTilePosition
    
    if (buildingType.isRefinery) {
      val geysers = With.units.neutral.filter(_.isGas)
      if (geysers.isEmpty) return None
      return Some(geysers.minBy(_.tilePosition.getDistance(startPosition)).tilePosition)
    }
    else if (buildingType.isTownHall) {
      val basePositions = With.geography.basePositions
      if (basePositions.isEmpty) return None
      return Some(basePositions.minBy(_.distanceSquared(startPosition)))
    }
    
    val maxMargin = if (buildingType == UnitType.Protoss_Pylon) 4 else 0
  
    var output:Option[TilePosition] = None
    (0 to maxMargin).reverse.foreach(margin =>
      output = output.orElse(
        With.architect.placeBuilding(
        buildingType,
        startPosition,
        margin = margin,
        searchRadius = 50,
        exclusions = With.geography.ourHarvestingAreas)))
    
    if (output == None) {
      With.logger.warn("Failed to place a " ++ buildingType.toString ++ " near " ++ startPosition.toString)
    }
    
    output
  }
}
