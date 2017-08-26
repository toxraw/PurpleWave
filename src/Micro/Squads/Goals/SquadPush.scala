package Micro.Squads.Goals

import Mathematics.Points.Pixel
import Micro.Agency.Intention
import Micro.Squads.Squad

class SquadPush(pixel: Pixel) extends SquadGoal {
  
  override def toString: String = "Attack " + pixel.zone.name
  
  def update(squad: Squad) {
    squad.recruits.foreach(_.agent.intend(squad.client, new Intention {
      toTravel = Some(pixel)
    }))
  }
  
  override def requiresAntiGround : Boolean = true
  override def requiresSiege      : Boolean = true
  override def requiresSpotters   : Boolean = true
}