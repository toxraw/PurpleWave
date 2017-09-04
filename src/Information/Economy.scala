package Information

import Information.Geography.Types.Base
import Lifecycle.With
import Performance.CacheFrame
import ProxyBwapi.UnitInfo.FriendlyUnitInfo

class Economy {
  
  private val incomePerFrameMinerals  = 0.041
  private val incomePerFrameGas       = 0.068
  
  //Should start at 50, of course but this -30 offsets the effect of starting workers all being far from minerals
  var ourEstimatedTotalMinerals = 20.0
  var ourEstimatedTotalGas = 0.0
  
  var _lastFrame = 0
  def update() {
    val frameDiff = With.framesSince(_lastFrame)
    ourEstimatedTotalMinerals += frameDiff * ourIncomePerFrameMinerals
    ourEstimatedTotalGas      += frameDiff * ourIncomePerFrameGas
    _lastFrame = With.frame
  }
  
  def ourActualTotalMinerals    : Integer = With.self.gatheredMinerals
  def ourActualTotalGas         : Integer = With.self.gatheredGas
  
  def ourIncomePerFrameMinerals : Double = ourIncomePerFrameMineralsCache.get
  def ourIncomePerFrameGas      : Double = ourIncomePerFrameGasCache.get
  
  private val ourIncomePerFrameMineralsCache  = new CacheFrame(() => ourActiveMiners.size   * incomePerFrameMinerals)
  private val ourIncomePerFrameGasCache       = new CacheFrame(() => ourActiveDrillers.size * incomePerFrameGas)
  
  def genericIncomePerFrameMinerals (miners:Int, bases:Int): Double = Math.min(miners, bases * 16)  * incomePerFrameMinerals
  def genericIncomePerFrameGas      (miners:Int, bases:Int): Double = Math.min(miners, bases * 3)   * incomePerFrameGas
  
  def ourActiveGatherers  : Traversable[FriendlyUnitInfo] = ourActiveGatherersCache.get
  def ourActiveMiners     : Traversable[FriendlyUnitInfo] = ourActiveMinersCache.get
  def ourActiveDrillers   : Traversable[FriendlyUnitInfo] = ourActiveDrillersCache.get
  
  private val ourActiveGatherersCache = new CacheFrame(() => With.geography.ourBases.flatten(ourActiveGatherers))
  private val ourActiveMinersCache    = new CacheFrame(() => With.geography.ourBases.flatten(ourActiveMiners))
  private val ourActiveDrillersCache  = new CacheFrame(() => With.geography.ourBases.flatten(ourActiveDrillers))
  
  def ourActiveGatherers(base: Base): Traversable[FriendlyUnitInfo] = {
    base.units.toSeq.flatMap(_.friendly).filter(unit => unit.agent.toGather.exists(_.base.contains(base)) && base.harvestingArea.contains(unit.tileIncludingCenter))
  }
  
  def ourActiveMiners(base:Base): Traversable[FriendlyUnitInfo] = {
    ourActiveGatherers(base).filter(_.gatheringMinerals).take(base.minerals.size * 2)
  }
  
  def ourActiveDrillers(base:Base): Traversable[FriendlyUnitInfo] = {
    ourActiveGatherers(base).filter(_.gatheringGas).take(base.gas.count(_.isOurs) * 3)
  }
}
