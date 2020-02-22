package Information.Intelligenze.Fingerprinting.TerranStrategies

import Information.Intelligenze.Fingerprinting.Fingerprint
import Information.Intelligenze.Fingerprinting.Generic.GameTime
import Lifecycle.With

class FingerprintWallIn extends Fingerprint {
  override protected def investigate: Boolean = With.frame < GameTime(3, 0)() && With.geography.bases.exists(b => b.zone.walledIn && (b.owner.isEnemy || b.isNaturalOf.exists(_.owner.isEnemy)))
  override protected def sticky: Boolean = true
}