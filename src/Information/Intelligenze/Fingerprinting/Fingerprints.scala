package Information.Intelligenze.Fingerprinting

import Information.Intelligenze.Fingerprinting.Generic.GameTime
import Information.Intelligenze.Fingerprinting.ProtossStrategies._
import Information.Intelligenze.Fingerprinting.TerranStrategies._
import Information.Intelligenze.Fingerprinting.ZergStrategies._
import Lifecycle.With

import scala.collection.mutable

class Fingerprints {
  
  def update() {
    if (With.enemies.exists(_.isUnknownOrTerran)) {
      fiveRax
      bbs
      twoRax1113
      twoFac
      twoFacVultures
      threeFac
      threeFacVultures
      siegeExpand
      oneRaxFE
      fourteenCC
    }
    if (With.enemies.exists(_.isUnknownOrProtoss)) {
      gatewayFirst
      earlyForge
      proxyGateway
      cannonRush
      twoGate
      oneGateCore
      fourGateGoon
      nexusFirst
      forgeFe
      gatewayFe
      dtRush
    }
    if (With.enemies.exists(_.isUnknownOrZerg)) {
      fourPool
      ninePool
      overpool
      tenHatch
      twelvePool
      twelveHatch
    }
    if (With.frame < GameTime(10, 0)()) {
      all.foreach(_.update())
    }
  }

  val all: mutable.ArrayBuffer[Fingerprint] = new mutable.ArrayBuffer[Fingerprint]

  private def addFingerprint(fingerprint: Fingerprint): Fingerprint = {
    all += fingerprint
    fingerprint
  }

  // Terran
  lazy val fiveRax          = addFingerprint(new Fingerprint5Rax)
  lazy val bbs              = addFingerprint(new FingerprintBBS)
  lazy val twoRax1113       = addFingerprint(new Fingerprint2Rax1113)
  lazy val oneFac           = addFingerprint(new Fingerprint1Fac)
  lazy val twoFac           = addFingerprint(new Fingerprint2Fac)
  lazy val twoFacVultures   = addFingerprint(new Fingerprint2FacVultures)
  lazy val threeFac         = addFingerprint(new Fingerprint3Fac)
  lazy val threeFacVultures = addFingerprint(new Fingerprint3FacVultures)
  lazy val siegeExpand      = addFingerprint(new FingerprintSiegeExpand)
  lazy val oneRaxFE         = addFingerprint(new Fingerprint1RaxFE)
  lazy val fourteenCC       = addFingerprint(new Fingerprint14CC)

  // Protoss
  lazy val gatewayFirst = addFingerprint(new FingerprintGatewayFirst)
  lazy val earlyForge   = addFingerprint(new FingerprintEarlyForge)
  lazy val proxyGateway = addFingerprint(new FingerprintProxyGateway)
  lazy val cannonRush   = addFingerprint(new FingerprintCannonRush)
  lazy val twoGate      = addFingerprint(new Fingerprint2Gate)
  lazy val oneGateCore  = addFingerprint(new Fingerprint1GateCore)
  lazy val robo         = addFingerprint(new FingerprintRobo)
  lazy val fourGateGoon = addFingerprint(new Fingerprint4GateGoon)
  lazy val nexusFirst   = addFingerprint(new FingerprintNexusFirst)
  lazy val forgeFe      = addFingerprint(new FingerprintForgeFE)
  lazy val gatewayFe    = addFingerprint(new FingerprintGatewayFE)
  lazy val dtRush       = addFingerprint(new FingerprintDTRush)
  
  // Zerg
  lazy val fourPool     = addFingerprint(new Fingerprint4Pool)
  lazy val ninePool     = addFingerprint(new Fingerprint9Pool)
  lazy val overpool     = addFingerprint(new FingerprintOverpool)
  lazy val tenHatch     = addFingerprint(new Fingerprint10Hatch9Pool)
  lazy val twelvePool   = addFingerprint(new Fingerprint12Pool)
  lazy val twelveHatch  = addFingerprint(new Fingerprint12Hatch)
}
