package Plans.Generic.Macro

import Development.{Logger, TypeDescriber}
import Startup.With
import Plans.Generic.Allocation.{PlanAcquireCurrencyForUnit, PlanAcquireUnitsExactly}
import Plans.Generic.Compound.PlanDelegateInSerial
import Types.PositionFinders.PositionSimpleBuilding
import Types.UnitMatchers.{UnitMatchType, UnitMatchTypeAbandonedBuilding}
import bwapi.UnitType

class PlanBuildBuilding(val buildingType:UnitType) extends PlanDelegateInSerial {
  
  val _positionFinder = new PositionSimpleBuilding(buildingType)
  
  val _currencyPlan = new PlanAcquireCurrencyForUnit(buildingType)
  val _builderPlan = new PlanAcquireUnitsExactly(new UnitMatchType(buildingType.whatBuilds.first), 1)
  val _recyclePlan = new PlanAcquireUnitsExactly(new UnitMatchTypeAbandonedBuilding(buildingType), 1)
  
  _children = List(_currencyPlan, _builderPlan, _recyclePlan)
  
  var _builder:Option[bwapi.Unit] = None
  var _building:Option[bwapi.Unit] = None
  var _lastOrderFrame = Integer.MIN_VALUE
  
  override def describe(): Option[String] = {
    Some(TypeDescriber.describeUnitType(buildingType))
  }
  
  override def isComplete(): Boolean = {
    _building.exists(_.isCompleted)
  }
  
  override def execute() {
    _currencyPlan.isSpent = ! _building.isEmpty
  
    if (isComplete) {
      abort()
      return
    }
  
    // Don't use the default serial execution.
    // We only want to execute the recycling plan if we don't have a builder
    //
    _currencyPlan.execute()
    if (_currencyPlan.isComplete) {
      _builderPlan.execute()
      if (_builderPlan.isComplete) {
        _builder = _builderPlan.units.headOption
        _builder.foreach(b => _building = Option.apply(b.getBuildUnit))
        if (_building.isEmpty) {
          _recyclePlan.execute()
          _building = _recyclePlan.units.headOption
        }
  
        if (_building.isEmpty) {
          _builder.foreach(_orderToBuild)
        }
        else {
          _builder.foreach(_.rightClick(_building.get))
        }
      }
    }
  }
  
  def _orderToBuild(builder:bwapi.Unit) {
    if (_lastOrderFrame < With.game.getFrameCount - 24) {
      _lastOrderFrame = With.game.getFrameCount
      
      val position = _positionFinder.find
  
      if (position.isEmpty) {
        Logger.warn("Failed to place a " ++ buildingType.toString)
      }
      else {
        val positionExplored = With.game.isExplored(position.get)
        
        if (positionExplored) {
          builder.build(buildingType, position.get)
        } else {
          builder.move(position.get.toPosition)
        }
      }
    }
  }
}
