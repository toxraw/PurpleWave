package Planning.UnitPreferences

import ProxyBwapi.UnitInfo.FriendlyUnitInfo

trait UnitPreference {
  def apply(unit: FriendlyUnitInfo): Double
}
