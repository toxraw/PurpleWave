package Utilities.Enrichment

import Geometry.Positions
import bwapi.{Position, TilePosition, WalkPosition}

case object EnrichPosition {
  implicit class EnrichedPositionCollection(positions:Iterable[Position]) {
    def minBound:Position = {
      if (positions.isEmpty) return Positions.middle
      new Position(
        positions.view.map(_.getX).min,
        positions.view.map(_.getY).min)}
    def maxBound:Position = {
      if (positions.isEmpty) return Positions.middle
      new Position(
        positions.view.map(_.getX).max,
        positions.view.map(_.getY).max)}
    def centroid:Position = {
      if (positions.isEmpty) return Positions.middle
      new Position(
        positions.view.map(_.getX).sum / positions.size,
        positions.view.map(_.getY).sum / positions.size)
    }
  }
  
  implicit class EnrichedTilePositionCollection(positions:Iterable[TilePosition]) {
    def minBound:TilePosition = {
      if (positions.isEmpty) return Positions.tileMiddle
      new TilePosition(
        positions.view.map(_.getX).min,
        positions.view.map(_.getY).min)}
    def maxBound:TilePosition = {
      if (positions.isEmpty) return Positions.tileMiddle
      new TilePosition(
        positions.view.map(_.getX).max,
        positions.view.map(_.getY).max)}
    def centroid:TilePosition = {
      if (positions.isEmpty) return Positions.tileMiddle
      new TilePosition(
        positions.view.map(_.getX).sum / positions.size,
        positions.view.map(_.getY).sum / positions.size)
    }
  }
  
  implicit class EnrichedPosition(position:Position) {
    def toWalkPosition:WalkPosition = {
      new WalkPosition(position.getX / 4, position.getY / 4)
    }
    def add(otherPosition:Position):Position = {
      new Position(
        position.getX + otherPosition.getX,
        position.getY + otherPosition.getY)
    }
    def subtract(otherPosition:Position):Position = {
      new Position(
        position.getX - otherPosition.getX,
        position.getY - otherPosition.getY)
    }
    def multiply(scale:Int):Position = {
      new Position(
        scale * position.getX,
        scale * position.getY)
    }
    def divide(scale:Int):Position = {
      new Position(
        position.getX / scale,
        position.getY / scale)
    }
    def midpoint(otherPosition:Position):Position = {
      add(otherPosition).divide(2)
    }
    def distanceSquared(otherPosition:Position):Int = {
      val dx = position.getX - otherPosition.getX
      val dy = position.getY - otherPosition.getY
      dx * dx + dy * dy}
  }
  
  implicit class EnrichedTilePosition(position:TilePosition) {
    def add(otherPosition:TilePosition):TilePosition = {
      new TilePosition(
        position.getX + otherPosition.getX,
        position.getY + otherPosition.getY)
    }
    def subtract(otherPosition:TilePosition):TilePosition = {
      new TilePosition(
        position.getX - otherPosition.getX,
        position.getY - otherPosition.getY)
    }
    def multiply(scale:Int):TilePosition = {
      new TilePosition(
        scale * position.getX,
        scale * position.getY)
    }
    def divide(scale:Int):TilePosition = {
      new TilePosition(
        position.getX / scale,
        position.getY / scale)
    }
    def midpoint(otherPosition:TilePosition):TilePosition = {
      add(otherPosition).divide(2)
    }
    def distanceSquared(otherPosition:TilePosition):Int = {
      val dx = position.getX - otherPosition.getX
      val dy = position.getY - otherPosition.getY
      dx * dx + dy * dy}
    def toWalkPosition:WalkPosition = {
      new WalkPosition(position.getX * 8, position.getY * 8)
    }
  }
}
