package Strategery.Strategies.Protoss

import Strategery.Strategies.AllRaces.WorkerRush
import Strategery.Strategies.Protoss.FFA._
import Strategery.Strategies.Protoss.PvE._
import Strategery.Strategies.Protoss.PvR._
import Strategery.Strategies._

object ProtossChoices {
  
  val pvr = Vector(
    PvROpen2Gate910,
    PvROpen2Gate1012,
    PvROpenZZCore,
    PvROpenZCoreZ,
    PvROpenProxy2Gate,
    PvROpenTinfoil,
    ProtossBigFFACarriers,
    ProtossHuntersFFAFFEGatewayCarriers,
    ProtossHuntersFFAFFEGateway,
    ProtossHuntersFFAAggroGateway,
    ProtossHuntersFFAFFEScoutReaver,
    ProtossHuntersFFAFFECarriers,
    PvEIslandPlasmaCarriers3Base,
    PvEIslandPlasmaCarriers1Base
  )
  
  /////////
  // PvT //
  /////////
  
  val pvtOpenersWithoutTransitions = Vector(
    PvTProxy2Gate,
    PvT13NexusNZ,
  )
  
  val pvtOpenersTransitioningFrom1Gate = Vector(
    PvT21Nexus,
    PvT23Nexus,
    PvT28Nexus,
    PvT25BaseCarrier,
    PvTDTExpand,
    PvT1GateRobo,
    PvT2GateObserver,
    PvT1015Expand,
    PvT1015DT,
    PvTStove
  )
  
  val pvtOpenersTransitioningFrom2Gate = Vector(
    PvT21Nexus,
    PvT2GateObserver,
    PvT1015Expand,
    PvT1015DT
  )
  
  val pvtOpenersWithTransitions: Vector[Strategy] = (pvtOpenersTransitioningFrom1Gate ++ pvtOpenersTransitioningFrom2Gate).distinct
  
  val pvtOpenersAll: Vector[Strategy] = (pvtOpenersWithoutTransitions ++ pvtOpenersTransitioningFrom1Gate ++ pvtOpenersTransitioningFrom2Gate).distinct
  
  /////////
  // PvP //
  /////////
  
  val pvpOpenersWithoutTransitions = Vector(
    PvP2GateDTExpand,
    PvPProxy2Gate
  )
  
  val pvpOpenersTransitioningFrom2Gate = Vector(
    PvP2Gate1012,
    PvP2Gate1012Goon
  )
  
  val pvpOpenersTransitioningFrom1GateCore = Vector(
    PvP1GateReaverExpand,
    PvP2GateRobo,
    PvP3GateRobo,
    PvP3GateGoon,
    PvP4GateGoon
  )
  
  val pvpOpenersAll: Vector[Strategy] = (pvpOpenersWithoutTransitions ++ pvpOpenersTransitioningFrom2Gate ++ pvpOpenersTransitioningFrom1GateCore).distinct
  
  /////////
  // PvZ //
  /////////
  
  val pvzOpenersWithoutTransitions = Vector(
    PvZFFEConservative,
    PvZFFEEconomic,
    PvZGatewayFE,
    PvZProxy2Gate
  )
  
  val pvzOpenersTransitioningFrom1Gate = Vector(
    PvZ4Gate99,
    PvZ4Gate1012
  )

  val pvzOpenersTransitioningFrom2Gate = Vector(
    PvZ4Gate99,
    PvZ4Gate1012
  )
  
  val pvzMidgameTransitioningFromOneBase = Vector(
    PvZMidgame4Gate2Archon,
    PvZMidgame5GateGoon
  )
  
  val pvzMidgameTransitioningFromTwoBases = Vector(
    PvZMidgame4Gate2Archon,
    PvZMidgameBisu,
    PvZMidgame5GateGoon,
    PvZMidgameNeoBisu
  )
  
  val pvzOpenersAll: Vector[Strategy] = (pvzOpenersWithoutTransitions ++ pvzOpenersTransitioningFrom1Gate ++ pvzOpenersTransitioningFrom2Gate).distinct
  
  /////////
  // All //
  /////////
  
  val gimmickOpeners = Vector(
    WorkerRush,
    MassPhotonCannon,
    ProxyDarkTemplar,
    PvEIslandPlasmaCarriers3Base,
    CarriersWithNoDefense,
    DarkArchonsWithNoDefense,
    ProtossBigFFACarriers,
    PvTReaverCarrierCheese)
  
  val standardOpeners: Vector[Strategy] = (pvr ++ pvtOpenersAll ++ pvpOpenersAll ++ pvzOpenersAll).distinct
  
  val all: Vector[Strategy] = (gimmickOpeners ++ standardOpeners).distinct
}