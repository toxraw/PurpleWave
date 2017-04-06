package Micro.Actions
import Lifecycle.With
import Micro.Intent.Intention

object Attack extends Action {
  
  override def perform(intent: Intention): Boolean = {
    val willingToFight = intent.desireToFight > 0.5
    if (
      intent.unit.canAttackThisFrame &&
        intent.toAttack.isDefined &&
        (willingToFight ||
          (intent.toAttack.exists(intent.unit.inRangeToAttack)
            && ! intent.unit.flying))) {
      With.commander.attack(intent, intent.toAttack.get)
      return true
    }
  
    false
  }
}