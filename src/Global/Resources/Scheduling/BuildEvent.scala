package Global.Resources.Scheduling

import Startup.With
import Types.Buildable.Buildable

class BuildEvent(
  val buildable:Buildable,
  val frameStart:Int,
  val frameEnd:Int) extends Comparable[BuildEvent] {
  
  override def toString: String = buildable.toString
  
  def formatTime(time:Int):String = {
    val relativeTime = time - With.game.getFrameCount
    if (relativeTime < 0) "Now" else relativeTime.toString
  }
  
  //Not strictly necessary, but helps keep the simulation more stable
  override def compareTo(other: BuildEvent): Int = {
    val start = frameStart.compareTo(other.frameStart)
    if (start != 0) return start
    return frameEnd.compareTo(other.frameEnd)
  }
}