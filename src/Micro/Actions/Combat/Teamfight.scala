package Micro.Actions.Combat

import Micro.Actions.Action
import Micro.Execution.ActionState
import Planning.Yolo

object Teamfight extends Action {
  
  override def allowed(state: ActionState): Boolean = {
    state.canFight &&
    state.unit.battle.exists(_.happening)
  }
  
  override def perform(state: ActionState) {
    
    // TODO: When should we continue fighting losing battles?
    // How should we avoid indecision?
    
    if (state.unit.battle.exists(_.estimationGeometric.result.weGainValue) || Yolo.active) {
      Engage.consider(state)
    }
    else if (state.unit.battle.exists(_.estimationGeometric.result.weLoseValue)) {
      Disengage.consider(state)
    }
  }
}
