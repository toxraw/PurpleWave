package Information.Fingerprinting.TerranStrategies

import Information.Fingerprinting.Generic.{FingerprintArrivesBy, FingerprintOr, GameTime}
import ProxyBwapi.Races.Terran

class Fingerprint2FacVultures extends FingerprintOr(
  new FingerprintArrivesBy(Terran.Vulture, GameTime(4, 35), 2),
  new FingerprintArrivesBy(Terran.Vulture, GameTime(5, 50), 4),
  new FingerprintArrivesBy(Terran.Vulture, GameTime(6, 10), 6),
)