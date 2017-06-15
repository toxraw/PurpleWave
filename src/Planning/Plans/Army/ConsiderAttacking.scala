package Planning.Plans.Army

import Lifecycle.With
import Planning.Composition.ResourceLocks.LockUnits
import Planning.Composition.UnitMatchers.UnitMatchWarriors
import Planning.Composition.{Property, UnitCountEverything}
import Planning.Plans.Compound.{If, IfThenElse}
import Planning.Plans.Information.{FindEnemyBase, FoundEnemyBase}
import Planning.Yolo

class ConsiderAttacking
  extends IfThenElse(
    new If(() =>
      Yolo.active
      || With.battles.global.estimation.weGainValue
      || With.battles.global.estimation.weSurvive)) {
  
  val attackers = new Property[LockUnits](new LockUnits {
    unitMatcher.set(UnitMatchWarriors)
    unitCounter.set(UnitCountEverything)
  })
  
  val attack = new Attack
  attack.controllers.inherit(attackers)
    
  val scout = new FindEnemyBase
  scout.scouts.inherit(attackers)
  
  val attackOrScout = new IfThenElse(new FoundEnemyBase, attack, scout)
  whenTrue.set(attackOrScout)
}