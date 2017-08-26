package Micro.Squads.Goals

import Mathematics.Points.Pixel
import Micro.Agency.Intention
import Micro.Squads.Squad

class SquadDrop(pixel: Pixel) extends SquadGoal {
  
  override def toString: String = "Drop on " + pixel.zone.name
  
  def update(squad: Squad) {
    val transports  = squad.recruits.filter   (_.isTransport)
    val passengers  = squad.recruits.filterNot(_.isTransport).filter( ! _.flying )
        
    squad.recruits.foreach(_.agent.intend(squad.client, new Intention {
      toTravel = Some(pixel)
    }))
    
    if (transports.nonEmpty) {
      passengers.foreach(passenger => passenger.agent.lastIntent.toTravel = Some(transports.minBy(_.pixelDistanceFast(passenger)).pixelCenter))
    }
  }
  
  override def acceptsHelp: Boolean = false
  override def requiresAntiGround : Boolean = true
  override def requiresTransport  : Boolean = true
}