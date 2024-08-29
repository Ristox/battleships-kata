package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.ResultType.HIT
import ee.rsx.kata.codurance.battleships.ResultType.MISSED
import ee.rsx.kata.codurance.battleships.player.GamePlayer

class BattleshipsGame : Battleships {
  companion object {

    private const val PLAYERS_COUNT = 2
  }

  private val players = mutableListOf<GamePlayer>()
  private var currentPlayer: Player? = null

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

    currentPlayer = players.first()
  }

  override fun print() {
    TODO("Not yet implemented")
  }

  override fun currentPlayer() = currentPlayer

  private fun opponent() = players.filterNot { it == currentPlayer }.first()

  override fun fire(target: Coordinates): FiringResult {
    val player = currentPlayer
      ?: throw IllegalStateException("cannot fire, game has not been started yet")

    val shipAtTargetCoordinates = opponent().shipTypeAt(target.row, target.column)

    val result = shipAtTargetCoordinates?.let { HIT } ?: MISSED

    when (result) {
      MISSED -> {
        player.missed(target)
        currentPlayer = opponent()
      }
      HIT -> {
        player.hit(target)
      }
    }
    return FiringResult(target, result, player)
  }

}
