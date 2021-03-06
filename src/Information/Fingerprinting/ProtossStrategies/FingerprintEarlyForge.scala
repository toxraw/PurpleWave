package Information.Fingerprinting.ProtossStrategies

import Information.Fingerprinting.Generic._
import ProxyBwapi.Races.Protoss

class FingerprintEarlyForge extends FingerprintOr(
  new FingerprintCompleteBy(Protoss.Forge,        GameTime(4, 0)),
  new FingerprintCompleteBy(Protoss.PhotonCannon, GameTime(5, 30))) {
  
  override val sticky = true
}
