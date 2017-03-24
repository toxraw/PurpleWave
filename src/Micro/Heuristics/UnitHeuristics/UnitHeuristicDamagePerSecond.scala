package Micro.Heuristics.UnitHeuristics
import Micro.Intentions.Intention
import ProxyBwapi.UnitInfo.UnitInfo

object UnitHeuristicDamagePerSecond extends UnitHeuristic{
  
  override def evaluate(intent: Intention, candidate: UnitInfo): Double = {
    
    Math.max(candidate.unitClass.groundDps, candidate.unitClass.airDps)
    
  }
  
}