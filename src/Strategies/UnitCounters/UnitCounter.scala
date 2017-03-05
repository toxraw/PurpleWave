package Strategies.UnitCounters

import Types.UnitInfo.FriendlyUnitInfo

trait UnitCounter {
  
  def continue(units:Iterable[FriendlyUnitInfo]):Boolean
  def accept(units:Iterable[FriendlyUnitInfo]):Boolean
  
}
