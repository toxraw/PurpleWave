package Planning.Predicates.Economy

import Lifecycle.With
import Planning.Predicate

class GasAtLeast(value: Int) extends Predicate {
  
  override def isComplete: Boolean = With.self.gas >= value
  
}
