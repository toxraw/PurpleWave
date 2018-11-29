package Information.Grids.Movement

import Information.Grids.ArrayTypes.AbstractGridInt
import Lifecycle.With
import Mathematics.Points.Tile
import Mathematics.Shapes.Spiral

class GridGroundDistance(origin: Tile) extends AbstractGridInt {

  override def onInitialization(): Unit = {
    Spiral.points.view.map(origin.add).find(p => With.grids.walkable.get(origin)).foreach(start => {
      var distance = 0
      var openSize = 0
      val tilesA = new Array[Int](tiles.size)
      val tilesB = new Array[Int](tiles.size)
      var open = tilesA
      val width = With.mapTileWidth
      def expand(iTile: Int): Unit = {
        if (iTile % width > 0) {
          open(openSize) = iTile - 1
          openSize += 1
        }
        if (iTile % width < width - 1) {
          open(openSize) = iTile + 1
          openSize += 1
        }
        if (iTile >= width) {
          open(openSize) = iTile - width
          openSize+= 1
        }
        if (iTile + width < tiles.size) {
          open(openSize) = iTile + width
          openSize+= 1
        }
      }
      def explore(iTile: Int): Unit = {
        if (get(iTile) == defaultValue && valid(iTile) && With.grids.walkable.get(iTile)) {
          set(iTile, distance)
          expand(iTile)
        }
      }

      explore(start.i)
      distance = 1
      while(openSize > 0) {
        val next = open
        val nextSize = openSize
        open = if (open == tilesA) tilesB else tilesA
        openSize = 0
        for (nextTile <- 0 until nextSize) explore(next(nextTile))
        distance += 1
      }
    })
  }
  override def defaultValue: Int = Int.MaxValue
}
