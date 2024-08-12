package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.player.GamePlayer

class BattleshipsGame : Battleships {

  override fun addPlayer(name: String): Player {
    return GamePlayer(name)
  }

  override fun start() {
    TODO("Not yet implemented")
  }

  override fun print() {
    TODO("Not yet implemented")
  }
}
