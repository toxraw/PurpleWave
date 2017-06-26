package Micro.Execution

import Lifecycle.With
import Micro.Actions.Idle
import Micro.Intent.Intention
import ProxyBwapi.UnitInfo.FriendlyUnitInfo

import scala.collection.mutable

class Executor {
  
  private val stateByUnit = new mutable.HashMap[FriendlyUnitInfo, ActionState]
  private var finishedExecuting = true
  
  def states: Iterable[ActionState] = stateByUnit.values
  
  def intend(intention: Intention) = {
    getState(intention.unit).intent = intention
  }
  
  def getState(unit: FriendlyUnitInfo): ActionState = {
    if ( ! stateByUnit.contains(unit)) {
      stateByUnit.put(unit, new ActionState(unit))
    }
    stateByUnit(unit)
  }
  
  //////////////
  // Batching //
  //////////////
  
  val unitQueue = new mutable.Queue[ActionState]
  
  def run() {
    if ( ! With.latency.isLastFrameOfTurn && ! finishedExecuting) return
  
    stateByUnit.keys.filterNot(_.alive).foreach(stateByUnit.remove)
    
    if (unitQueue.isEmpty) {
      unitQueue ++= stateByUnit.values.toVector.sortBy(_.lastFrame)
    }
    
    while (unitQueue.nonEmpty && With.performance.continueRunning) {
      val nextUnit = unitQueue.dequeue()
      Idle.consider(nextUnit)
    }
  
    finishedExecuting = unitQueue.isEmpty
  }
}