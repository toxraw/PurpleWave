package Startup

import bwapi.{BWEventListener, Player}

object BotListener extends BWEventListener{
  val mirror:bwapi.Mirror = new bwapi.Mirror()
  var bot:Option[Bot] = None

  def initialize(): Unit = {
    System.out.println("Starting Scala bot.")
    mirror.getModule.setEventListener(this)
    mirror.startGame
    System.out.println("Finished configuring BWMirror. Now ready and waiting for a game to begin.")
  }

  override def onStart(): Unit = {
    System.out.println("BWMirror dispatched an onStart event.")
    bot = Some(new Bot(mirror.getGame))
    var foo = 3
    var bar = 5
    var qux = foo * bar
    bot.get.onStart
  }

  override def onEnd(b: Boolean):                         Unit = { bot.get.onEnd(b) }
  override def onFrame():                                 Unit = { bot.get.onFrame }
  override def onSendText(s: String):                     Unit = { bot.get.onSendText(s) }
  override def onReceiveText(player: Player, s: String):  Unit = { bot.get.onReceiveText(player, s) }
  override def onPlayerLeft(player: Player):              Unit = { bot.get.onPlayerLeft(player) }
  override def onPlayerDropped(player: Player):           Unit = { bot.get.onPlayerDropped(player) }
  override def onNukeDetect(position: bwapi.Position):    Unit = { bot.get.onNukeDetect(position) }
  override def onUnitComplete(unit: bwapi.Unit):          Unit = { bot.get.onUnitComplete(unit) }
  override def onUnitCreate(unit: bwapi.Unit):            Unit = { bot.get.onUnitCreate(unit) }
  override def onUnitDestroy(unit: bwapi.Unit):           Unit = { bot.get.onUnitDestroy(unit) }
  override def onUnitDiscover(unit: bwapi.Unit):          Unit = { bot.get.onUnitDiscover(unit) }
  override def onUnitEvade(unit: bwapi.Unit):             Unit = { bot.get.onUnitEvade(unit) }
  override def onUnitHide(unit: bwapi.Unit):              Unit = { bot.get.onUnitHide(unit) }
  override def onUnitMorph(unit: bwapi.Unit):             Unit = { bot.get.onUnitMorph(unit) }
  override def onUnitRenegade(unit: bwapi.Unit):          Unit = { bot.get.onUnitRenegade(unit) }
  override def onUnitShow(unit: bwapi.Unit):              Unit = { bot.get.onUnitShow(unit) }
  override def onSaveGame(s: String):                     Unit = { bot.get.onSaveGame(s) }
}
