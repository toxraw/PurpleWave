package Macro.Allocation

import Geometry.TileRectangle
import Planning.Plan

import scala.collection.mutable

class Reservations {
  
  private val requestsUpdated = new mutable.HashSet[Plan]
  val requestedAreas = new mutable.HashMap[Plan, TileRectangle]
  
  def onFrame() {
    requestedAreas.keySet.diff(requestsUpdated).foreach(remove)
    requestsUpdated.clear()
  }
  
  def request(plan:Plan, area:TileRectangle): Boolean = {
    
    //For now, just do first-come, first-served
    
    requestedAreas.remove(plan)
    val approved = requestedAreas.values.forall( ! _.intersects(area))
    
    if (approved) {
      requestedAreas.put(plan, area)
      requestsUpdated.add(plan)
    }
    
    return approved
  }
  
  def available(rectangle:TileRectangle):Boolean = {
    ! requestedAreas.values.exists(_.intersects(rectangle))
  }
  
  def remove(plan:Plan) {
    requestsUpdated.remove(plan)
    requestedAreas.remove(plan)
  }
}
