package Information.Fingerprinting.TerranStrategies

import Information.Fingerprinting.Fingerprint
import Information.Fingerprinting.Generic.{FingerprintAnd, FingerprintNot, GameTime}
import Lifecycle.With
import ProxyBwapi.Races.Terran

class FingerprintSiegeExpand extends FingerprintAnd(
  new FingerprintNot(With.fingerprints.oneRaxFE),
  new FingerprintNot(With.fingerprints.fourteenCC),
  new Fingerprint {
    override protected def investigate: Boolean = {
      With.enemies.exists(e => {
        val siege =
          (With.unitsShown(e, Terran.SiegeTankUnsieged)
          + With.unitsShown(e, Terran.SiegeTankSieged)) > 0
        val expand = (
          With.unitsShown(e, Terran.CommandCenter) > 1
          || With.units.enemy.exists(u => u.player == e && u.isCommandCenter() && ! With.geography.startLocations.contains(u.tileTopLeft))
          || e.bases.size > 1)
        siege && expand
      })
    }
    override val sticky: Boolean = true
    override protected def lockAfter: Int = GameTime(7, 0)()
  })
