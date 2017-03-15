package Planning.Plans.Macro.Build

import Planning.Plan
import Startup.With
import Macro.Buildables.Buildable
import Performance.Caching.CacheFrame

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class FollowBuildOrder extends Plan {
  
  description.set("Follow a build order")
  
  private val plans = new mutable.HashMap[Buildable, ListBuffer[Plan]]
  private var queue:Iterable[Buildable] = List.empty
  
  override def getChildren: Iterable[Plan] = getChildrenCache.get
  private val getChildrenCache = new CacheFrame[Iterable[Plan]](() => getChildrenRecalculate)
  private def getChildrenRecalculate: Iterable[Plan] = {
    val indexByBuild = new mutable.HashMap[Buildable, Int]
    plans.keys.foreach(build => indexByBuild.put(build, 0))
    queue.map(build => {
      val output = plans(build)(indexByBuild(build))
      indexByBuild(build) += 1
      output
    })
  }
  
  override def onFrame() {
    //Remove complete plans
    plans.values.foreach(plans => plans.indices.foreach(i =>
      while (i < plans.size && plans(i).isComplete) plans.remove(i)))
  
    //Add plans to match number of builds we need
    queue = With.scheduler.queueOptimized.filter(_.frameStart <= With.frame).map(_.buildable)
    val buildsNeeded = queue.groupBy(x => x).map(group => (group._1, group._2.size))
    buildsNeeded.keys.foreach(build => {
      if ( ! plans.contains(build)) {
        plans.put(build, new ListBuffer[Plan])
      }
      while (plans(build).size < buildsNeeded(build)) {
        plans(build).append(buildPlan(build))
      }
      //Consider removing excess plans
    })
    
    getChildren.foreach(_.onFrame())
  }
  
  private def buildPlan(buildable:Buildable):Plan = {
    if (buildable.unitOption.nonEmpty) {
      val unitType = buildable.unitOption.get
      if (unitType.isBuilding) {
        return new BuildBuilding(unitType)
      } else {
        return new TrainUnit(unitType)
      }
    }
    if (buildable.techOption.nonEmpty) {
      return new ResearchTech(buildable.techOption.get)
    }
    if (buildable.upgradeOption.nonEmpty) {
      return new ResearchUpgrade(buildable.upgradeOption.get, buildable.upgradeLevel)
    }
    
    throw new Exception("Tried to build a Buildable that doesn't specify any unit, tech, or research")
  }
}