package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.ResultType.*
import ee.rsx.kata.codurance.battleships.Row.*
import ee.rsx.kata.codurance.battleships.ShipType.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

@DisplayName("When game has been started")
class BattleShipsGameStartedTest {

  private lateinit var john: Player
  private lateinit var james: Player
  private lateinit var game: Battleships

  @BeforeEach
  fun setup() {
    game = BattleshipsGame()
    john = game.addPlayer("John")
    james = game.addPlayer("James")
    john.placeDefaultShips()
    james.placeDefaultShips()
    game.start()
  }

  @Test
  fun `first player is the current player`() {
    val currentPlayer = game.currentPlayer()

    assertThat(currentPlayer).isEqualTo(john)
  }

  @Test
  fun `player can fire`() {
    assertDoesNotThrow { game.fire(at(A, 1)) }
  }

  @Test
  fun `firing result is a miss, when opponent has no ship at coordinate fired`() {
    val result = game.fire(at(A, 1))

    assertThat(result.type).isEqualTo(MISSED)
  }

  @Test
  fun `firing result is a hit, when opponent has a ship at coordinate fired`() {
    val result = game.fire(at(A, 2))

    assertThat(result.type).isEqualTo(HIT)
  }

  @Test
  fun `firing result shows missed shots, containing the target that was missed`() {
    val missedTarget = at(A, 1)
    val result = game.fire(at = missedTarget)

    val missedShots = result.currentPlayer.shotsMissed

    assertThat(missedShots)
      .containsOnly(missedTarget)
  }

  @Test
  fun `firing result shows hit shots, containing the target that was hit`() {
    val hitTarget = at(A, 2)
    val result = game.fire(at = hitTarget)

    val hits = result.currentPlayer.shotsHit

    assertThat(hits).containsOnly(hitTarget)
  }

  @Test
  fun `when firing result is a miss, turn goes from john to james`() {
    assertThat(game.currentPlayer()).isEqualTo(john)
    val missedTarget = at(A, 1)

    game.fire(at = missedTarget)

    assertThat(game.currentPlayer())
      .isEqualTo(james)
  }

  @Test
  fun `when both john and james fire a miss, turn goes back to john`() {
    assertThat(game.currentPlayer()).isEqualTo(john)
    val missedTarget = at(A, 1)

    game.fire(at = missedTarget)
    game.fire(at = missedTarget)

    assertThat(game.currentPlayer()).isEqualTo(john)
  }

  @Test
  fun `when firing result is a hit, turn does not change (same player can fire again)`() {
    val initialPlayer = game.currentPlayer()
    val hitTarget = at(A, 2)

    game.fire(at = hitTarget)

    assertThat(game.currentPlayer()).isEqualTo(initialPlayer)
  }

  @Test
  fun `when firing result is a ship sunk, result shows the sunken ship`() {
    game.fire(at(D, 8))
    game.fire(at(E, 8))
    game.fire(at(F, 8))

    val result = game.fire(at(G, 8))

    with(result) {
      assertThat(type).isEqualTo(SUNK)
      assertThat(currentPlayer.destroyedOpponentShips)
        .containsExactly(MOTHERSHIP.placed(from(D, 8), to(G, 8)))
    }
  }

  @Test
  fun `when sinking 4 ships, all ships are shown as destroyed`() {
    sinkShip(from(E, 5), to(E, 6))  // WARSHIP
    sinkShip(from(A, 2), to(A, 4))  // DESTROYER
    sinkShip(from(B, 10), to(C, 10))  // WARSHIP

    val lastFiringResult = game.fire(at(J, 2)) // GUNSHIP

    with(lastFiringResult) {
      assertThat(currentPlayer.destroyedOpponentShips)
        .containsExactlyInAnyOrder(
          WARSHIP.placed(from(E, 5), to(E, 6)),
          WARSHIP.placed(from(B, 10), to(C, 10)),
          DESTROYER.placed(from(A, 2), to(A, 4)),
          gunship(at(J, 2))
        )
    }
  }

  @Test
  fun `when sinking 4 ships, player still remains as current player`() {
    val initialPlayer = game.currentPlayer()
    sinkShip(from(E, 5), to(E, 6))  // WARSHIP
    sinkShip(from(A, 2), to(A, 4))  // DESTROYER
    sinkShip(from(B, 10), to(C, 10))  // WARSHIP

    game.fire(at(J, 2)) // GUNSHIP

    assertThat(game.currentPlayer()).isEqualTo(initialPlayer)
  }

  private fun sinkShip(start: Coordinates, end: Coordinates) {
    if (start.row == end.row) {
      Column.entries
        .filter { it.index in start.column.index..end.column.index }
        .forEach {
          game.fire(at = Coordinates(start.row, it))
        }
    } else if (start.column == end.column) {
      Row.entries
        .filter { it.index in start.row.index..end.row.index }
        .forEach {
          game.fire(at = Coordinates(it, start.column))
        }
    }
  }

  @Test
  fun `game shows running status of opponent board (hits, misses, sunken ships)`() {
    john.run {
      sinkShip(from(E, 5), to(E, 6))  // WARSHIP
      sinkShip(from(A, 2), to(A, 4))  // DESTROYER
      sinkShip(from(B, 10), to(C, 10))  // WARSHIP
      shootHit(at(J, 2)) // GUNSHIP

      shootMiss(at(J, 4))
    }

    james.run {
      shootMiss(at(A, 1))
    }

    john.run {
      shootMiss(at(J, 6))
    }

    james.run {
      shootMiss(at(A, 7))
    }

    john.run {
      shootMiss(at(J, 8))
    }

    james.run {
      shootHit(at(A, 6)) // GUNSHIP
      sinkShip(at(C, 5), at(C, 7)) // WARSHIP

      assertShotsMissed(at(A, 1), at(A, 7))
      assertShotsHit(at(A, 6), at(C, 5), at(C, 6), at(C, 7))
      assertHasDestroyedOpponentShips(
        gunship(at(A, 6)),
        DESTROYER.placed(from(C, 5), to(C, 7))
      )
      shootMiss(at(G, 9))
    }

    john.run {
      assertShotsMissed(at(J, 4), at(J, 6), at(J, 8))
      assertShotsHit(
        at(E, 5), at(E, 6),
        at(A, 2), at(A, 3), at(A, 4),
        at(B, 10), at(C, 10),
        at(J, 2)
      )
      assertHasDestroyedOpponentShips(
        WARSHIP.placed(from(E, 5), to(E, 6)),
        DESTROYER.placed(from(A, 2), to(A, 4)),
        WARSHIP.placed(from(B, 10), to(C, 10)),
        gunship(at(J, 2))
      )
    }
  }

  @Test
  fun `when all ships are sunk, game ends in a win for the current player`() {
    val endResult = playGameUntilEndWithJamesWinning()

    assertThat(endResult.type).isEqualTo(WIN)
    assertThat(game.winner()).isEqualTo(james)
    assertThat(game.hasEnded()).isTrue()
  }

  @Test
  fun `when all ships are sunk, game winner is the player who sunk all opponents ships (John)`() {
    playGameUntilEndWithJamesWinning()

    assertThat(game.winner()).isEqualTo(james)
  }

  @Test
  fun `when all ships are sunk, game has ended`() {
    playGameUntilEndWithJamesWinning()

    assertThat(game.hasEnded()).isTrue()
  }

  @Test
  fun `when all ships are sunk and game ended, player cannot fire anymore`() {
    playGameUntilEndWithJamesWinning()

    val test: () -> Unit = { game.fire(at(J, 1)) }

    assertFailsWith<IllegalStateException>(
      "Game has ended",
      test
    )
  }

  private fun shootHit(at: Coordinates) = game.fire(at)

  private fun shootMiss(at: Coordinates) = game.fire(at)

  private fun ensurePlayerIs(player: Player): Player =
    with(game.currentPlayer()) {
      assertThat(this).isEqualTo(player)
      player
    }

  private fun Player.assertShotsMissed(vararg coordinates: Coordinates) {
    assertThat(shotsMissed).containsExactlyInAnyOrder(*coordinates)
  }

  private fun Player.assertShotsHit(vararg coordinates: Coordinates) {
    assertThat(shotsHit).containsExactlyInAnyOrder(*coordinates)
  }

  private fun Player.assertHasDestroyedOpponentShips(vararg ships: Ship) {
    assertThat(destroyedOpponentShips).containsExactlyInAnyOrder(*ships)
  }

  private fun playGameUntilEndWithJamesWinning(): FiringResult {
    ensurePlayerIs(john).run {
      shootMiss(at(G, 1))
    }

    ensurePlayerIs(james).run {
      shootMiss(at(I, 10))
    }

    ensurePlayerIs(john).run {
      shootMiss(at(F, 9))
    }

    ensurePlayerIs(james).run {
      sinkShip(from(E, 2), to(F, 2)) // WARSHIP
      ensurePlayerIs(james)
      sinkShip(from(E, 5), to(E, 6)) // WARSHIP
      ensurePlayerIs(james)
      shootMiss(at(A, 1))
    }

    ensurePlayerIs(john).run {
      shootHit(at(H, 10)) // GUNSHIP
      ensurePlayerIs(john)
      sinkShip(from(A, 2), to(A, 4)) // DESTROYER
      ensurePlayerIs(john)
      shootMiss(at(B, 9))
    }

    return ensurePlayerIs(james).run {
      sinkShip(from(B, 10), to(C, 10)) // WARSHIP
      ensurePlayerIs(james)
      sinkShip(from(A, 2), to(A, 4)) // DESTROYER
      ensurePlayerIs(james)
      sinkShip(from(C, 5), to(C, 7)) // DESTROYER
      ensurePlayerIs(james)
      sinkShip(from(D, 8), to(G, 8)) // MOTHERSHIP
      ensurePlayerIs(james)
      shootHit(at(J, 2)) // GUNSHIP
      ensurePlayerIs(james)
      shootHit(at(H, 10)) // GUNSHIP
      ensurePlayerIs(james)
      shootHit(at(A, 6)) // GUNSHIP
      ensurePlayerIs(james)

      shootHit(at(F, 4)) // GUNSHIP
    }
  }
}
