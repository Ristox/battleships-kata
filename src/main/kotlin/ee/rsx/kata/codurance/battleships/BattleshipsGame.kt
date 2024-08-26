package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.player.GamePlayer

class BattleshipsGame : Battleships {

  private val players = mutableListOf<GamePlayer>()

  override fun addPlayer(name: String): Player {
    check(players.size < 2) { "Maximum of 2 players can be added" }
    
    return GamePlayer(name).apply { players.add(this) }
  }

  override fun start() {
    check(players.size == 2) { "2 players must be added before starting the game" }

    players.forEach {
      check(it.board.shipsPlaced().isNotEmpty()) {
        "Each player must place their ships before starting the game"
      }
    }
  }

  override fun print() {
    TODO("Not yet implemented")
  }
}
