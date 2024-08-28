package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.player.GamePlayer

class BattleshipsGame : Battleships {
  companion object {

    private const val PLAYERS_COUNT = 2
  }

  private val players = mutableListOf<GamePlayer>()

  override fun addPlayer(name: String): Player {
    check(players.size < PLAYERS_COUNT) { "Maximum of $PLAYERS_COUNT players can be added" }

    players.forEach { player ->
      require(player.name != name) {
        "Player with the same name ($name) already exists"
      }
    }

    return GamePlayer(name).apply { players.add(this) }
  }

  override fun start() {
    check(players.size == PLAYERS_COUNT) { "$PLAYERS_COUNT players must be added before starting the game" }

    players.forEach { player ->
      check(player.hasPlacedAllShips()) {
        "Each player must place their ships before starting the game"
      }
    }
  }

  override fun print() {
    TODO("Not yet implemented")
  }

  override fun currentPlayer(): Player? {
    TODO("Not yet implemented")
  }
}
