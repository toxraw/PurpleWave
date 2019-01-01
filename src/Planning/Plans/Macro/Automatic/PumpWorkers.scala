package Planning.Plans.Macro.Automatic

import Lifecycle.With

class PumpWorkers(oversaturate: Boolean = false, cap: Int = 85) extends Pump(With.self.workerClass) {
  
  protected def builderCount: Int = {
    if (With.self.isTerran)
      4
    else if (With.self.isProtoss)
      1
    else
      2
  }
  override def maxDesirable: Int = Math.min(
    cap,
    {
      var sum = (if (oversaturate) 18 else 0)
      With.geography.ourBases.foreach(base => {
        sum += builderCount + 2 * base.minerals.size + 3 * base.gas.size
      })
      sum
    })
}
