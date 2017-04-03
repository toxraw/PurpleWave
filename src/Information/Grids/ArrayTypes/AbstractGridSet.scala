package Information.Grids.ArrayTypes

import bwapi.TilePosition

import scala.collection.mutable

abstract class AbstractGridSet[T] extends AbstractGridArray[mutable.HashSet[T]] {
  
  private val empty = Array.fill(width * height)(defaultValue)
  
  override protected var values: Array[mutable.HashSet[T]] = Array.fill(width * height)(defaultValue)
  override def defaultValue: mutable.HashSet[T] = mutable.HashSet.empty
  override def repr(value: mutable.HashSet[T]): String  = value.size.toString
  
  override def reset() {
    values = empty.clone()
  }
  
  override def update() {
    reset()
    getUnits.foreach(item => getTiles(item).foreach(tile => get(tile).add(item)))
  }
  
  protected def getTiles(item: T): Iterable[TilePosition]
  protected def getUnits: Iterable[T]
}