package Geometry

import Startup.With
import bwapi.{Position, TilePosition}

object Positions {
  def middle = new Position(
    With.mapWidth * 32 / 2,
    With.mapHeight * 32 / 2)
  
  def tileMiddle = new TilePosition(
      With.mapWidth / 2,
      With.mapHeight / 2)
}
