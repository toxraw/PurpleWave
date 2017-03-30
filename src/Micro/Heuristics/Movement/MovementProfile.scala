package Micro.Heuristics.Movement

import Debugging.Visualization.Colors
import Micro.Heuristics.TileHeuristics._

class MovementProfile(
  var preferTravel      : Double = 0,
  var preferSpot        : Double = 0,
  var preferSitAtRange  : Double = 0,
  var preferTarget      : Double = 0,
  var preferMobility    : Double = 0,
  var preferHighGround  : Double = 0,
  var preferMoving      : Double = 0,
  var preferRandom      : Double = 0,
  var avoidDamage       : Double = 0,
  var avoidTraffic      : Double = 0,
  var avoidVision       : Double = 0,
  var avoidDetection    : Double = 0) {
  
  def heuristics: Iterable[WeightedMovementHeuristic] =
    List(
      new WeightedMovementHeuristic(TileHeuristicDestinationApproximate,  preferTravel,       Colors.MediumGreen),
      new WeightedMovementHeuristic(TileHeuristicDestinationExact,        preferSpot,         Colors.BrightGreen),
      new WeightedMovementHeuristic(TileHeuristicEnemyAtMaxRange,         preferSitAtRange,   Colors.MediumRed),
      new WeightedMovementHeuristic(TileHeuristicInRangeOfTarget,         preferTarget,       Colors.BrightBlue),
      new WeightedMovementHeuristic(TileHeuristicMobility,                preferMobility,     Colors.MediumOrange),
      new WeightedMovementHeuristic(TileHeuristicHighGround,              preferHighGround,   Colors.DarkBlue),
      new WeightedMovementHeuristic(TileHeuristicKeepMoving,              preferMoving,       Colors.MediumBlue),
      new WeightedMovementHeuristic(TileHeuristicRandom,                  preferRandom,       Colors.DarkGray),
      new WeightedMovementHeuristic(TileHeuristicExposureToDamage,        -avoidDamage,       Colors.NeonRed),
      new WeightedMovementHeuristic(TileHeuristicTraffic,                 -avoidTraffic,      Colors.BrightYellow),
      new WeightedMovementHeuristic(TileHeuristicEnemyVision,             -avoidVision,       Colors.MediumGray),
      new WeightedMovementHeuristic(TileHeuristicEnemyDetection,          -avoidDetection,    Colors.BrightGray)
    )
}
