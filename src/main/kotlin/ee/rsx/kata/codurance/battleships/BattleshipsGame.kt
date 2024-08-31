package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.ResultType.HIT
import ee.rsx.kata.codurance.battleships.ResultType.MISSED
import ee.rsx.kata.codurance.battleships.ResultType.SUNK
import ee.rsx.kata.codurance.battleships.ResultType.WIN
import ee.rsx.kata.codurance.battleships.player.GamePlayer
import java.util.Objects.nonNull

class BattleshipsGame : Battleships {
  companion object {

    private const val PLAYERS_COUNT = 2
  }

  private val players = mutableListOf<GamePlayer>()
  private var currentPlayer: Player? = null

  private var winner: Player? = null

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
    check(!hasEnded()) { "Game has ended" }

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
    check(!hasEnded()) { "Game has ended" }

    val player = currentPlayer
      ?: throw IllegalStateException("cannot fire, game has not been started yet")

    val shipAtTarget = opponent().board.shipAt(target.row, target.column)

    val result = shipAtTarget
      ?.let {
        val shipDestroyed =
          (player.shotsHit + target).containsAll(it.coveredCoordinates())

        if (!shipDestroyed)
          HIT
        else {
          player.destroyedOpponentShips.add(it)
          if (player.hasDestroyedAllOpponentShips()) {
            winner = player
            WIN
          } else {
            SUNK
          }
        }
      }

      ?: MISSED

    when (result) {
      MISSED -> {
        player.missed(target)
        switchPlayer()
      }

      HIT, SUNK, WIN -> {
        player.hit(target)
      }
    }
    return FiringResult(target, result, player)
  }

  private fun switchPlayer() {
    currentPlayer = opponent()
  }

  private fun Player.hasDestroyedAllOpponentShips() =
    destroyedOpponentShips.size == opponent().board.shipsPlaced().size

  override fun winner() = winner

  override fun hasEnded() = nonNull(winner)
}
