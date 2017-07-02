package Information.Geography.Calculations

import Lifecycle.With
import Mathematics.Clustering
import Mathematics.Points.{Tile, TileRectangle}
import Mathematics.Shapes.Circle
import ProxyBwapi.Races.Protoss
import ProxyBwapi.UnitInfo.ForeignUnitInfo
import Utilities.EnrichPixel._

import scala.collection.JavaConverters._
import scala.collection.mutable

object BaseFinder {
  
  def calculate: Iterable[Tile] = {
  
    //Find base positions
    val allHalls = clusteredResourcePatches.flatMap(bestTownHallTile).to[mutable.Set] ++ With.game.getStartLocations.asScala.map(new Tile(_))
    removeConflictingBases(allHalls)
  }
  
  private def clusteredResourcePatches: Iterable[Iterable[ForeignUnitInfo]] = {
    Clustering.group[ForeignUnitInfo](
      resourcePatches,
      32 * 12,
      limitRegion = true,
      (unit) => unit.pixelCenter).values
  }
  
  private def resourcePatches = {
    With.units.neutral.filter(unit => unit.unitClass.isGas || unit.unitClass.isMinerals && unit.initialResources > With.configuration.blockerMineralThreshold)
  }
  
  private def bestTownHallTile(resources: Iterable[ForeignUnitInfo]): Option[Tile] = {
    val centroid = resources.map(_.pixelCenter).centroid
    val centroidTile = centroid.tileIncluding
    val altitude = With.game.getGroundHeight(centroidTile.bwapi)
    val searchRadius = 10
    val candidates =
      Circle
        .points(searchRadius)
        .map(centroidTile.add)
        .filter(tile => isLegalTownHallTile(tile) && altitude == With.game.getGroundHeight(tile.bwapi))
    if (candidates.isEmpty) return None
    Some(candidates.minBy(_.topLeftPixel.add(64, 48).pixelDistanceFast(centroid)))
  }
  
  private def isLegalTownHallTile(candidate: Tile): Boolean = {
    if ( ! candidate.valid) return false
    val buildingArea = Protoss.Nexus.tileArea.add(candidate)
    val exclusions =
      resourcePatches
        .map(resourcePatch => TileRectangle(
          resourcePatch.tileTopLeft.subtract(3, 3),
          resourcePatch.tileTopLeft.add(3, 3).add(resourcePatch.unitClass.tileSize)))
    
    buildingArea.tiles.forall(tile => With.game.isBuildable(tile.bwapi)) &&
      ! resourcePatches.view.map(resourcePatch => resourcePatch.tileArea.expand(3, 3)).exists(buildingArea.intersects)
  }
  
  private def removeConflictingBases(halls: mutable.Set[Tile]): Iterable[Tile] = {
  
    val basesToRemove = new mutable.HashSet[Tile]
    
    //Remove conflicting/overlapping positions
    //O(n^3) but we only do it once
    halls.foreach(hall =>
      if ( ! basesToRemove.contains(hall)) {
        val conflictingHalls =
          Vector(hall) ++
          halls
            .diff(basesToRemove)
            .filter(otherHall => otherHall != hall && otherHall.tileDistanceSlow(hall) < 8)
        
        //Take the hall which is closest to a geyser
        val preferredHall = conflictingHalls
          .sortBy(conflictingHallTile => With.geography.startLocations.exists(_ == conflictingHallTile))
          .sortBy(hall => With.units.neutral
            .filter(_.unitClass.isGas)
            .map(_.pixelDistanceFast(hall.pixelCenter))
            .min)
        
        basesToRemove -- conflictingHalls.filterNot(_ == preferredHall)
      })
    
    halls.diff(basesToRemove)
  }
}
