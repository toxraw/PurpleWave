package Plans.Compound

class Parallel extends AbstractAll {
  
  description.set("Do in parallel")
  
  override def onFrame() = getChildren.foreach(_.onFrame())
}