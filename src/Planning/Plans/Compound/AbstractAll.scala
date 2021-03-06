package Planning.Plans.Compound

import Planning.{Plan, Property}

abstract class AbstractAll(initialChildren: Plan*) extends Plan {
  val children = new Property[Seq[Plan]](initialChildren.toVector)
  
  override def isComplete: Boolean = getChildren.forall(_.isComplete)
  
  final override def getChildren: Iterable[Plan] = children.get
}
