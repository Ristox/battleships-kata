package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.ResultType.HIT
import ee.rsx.kata.codurance.battleships.ResultType.MISSED
import ee.rsx.kata.codurance.battleships.ResultType.SUNK
import ee.rsx.kata.codurance.battleships.Row.A
import ee.rsx.kata.codurance.battleships.Row.B
import ee.rsx.kata.codurance.battleships.Row.C
import ee.rsx.kata.codurance.battleships.Row.D
import ee.rsx.kata.codurance.battleships.Row.E
import ee.rsx.kata.codurance.battleships.Row.F
import ee.rsx.kata.codurance.battleships.Row.G
import ee.rsx.kata.codurance.battleships.Row.J
import ee.rsx.kata.codurance.battleships.ShipType.DESTROYER
import ee.rsx.kata.codurance.battleships.ShipType.GUNSHIP
import ee.rsx.kata.codurance.battleships.ShipType.MOTHERSHIP
import ee.rsx.kata.codurance.battleships.ShipType.WARSHIP
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
  fun `when game has not been started, there is no current player`() {
    val john = game.addPlayer("John")
    john.placeDefaultShips()
    val james = game.addPlayer("James")
    james.placeDefaultShips()

    val currentPlayer = game.currentPlayer()

    assertThat(currentPlayer).isNull()
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
    //John's turn
    sinkShip(from(E, 5), to(E, 6))  // WARSHIP
    sinkShip(from(A, 2), to(A, 4))  // DESTROYER
    sinkShip(from(B, 10), to(C, 10))  // WARSHIP

    game.fire(at(J, 2)) // GUNSHIP
    game.fire(at(J, 4)) // John misses

    // James's turn
    game.fire(at(A, 1)) // James misses

    // John's turn
    game.fire(at(J, 6)) // John misses

    // James's turn
    game.fire(at(A, 7)) // James misses

    // John's turn
    game.fire(at(J, 8)) // John misses

    // James's turn
    game.fire(at(A, 6)) // James sinks GUNSHIP
    sinkShip(at(C, 5), at(C, 7)) // James sinks WARSHIP

    // overview of opponent's (John's) board for James
    with(game.currentPlayer()!!) {
      assertThat(shotsMissed)
        .containsExactlyInAnyOrder(at(A, 1), at(A, 7))
      assertThat(shotsHit)
        .containsExactlyInAnyOrder(at(A, 6), at(C, 5), at(C, 6), at(C, 7))
      assertThat(destroyedOpponentShips)
        .containsExactlyInAnyOrder(
          gunship(at(A, 6)),
          DESTROYER.placed(from(C, 5), to(C, 7))
        )
    }

    game.fire(at(G, 9)) // James misses

    // overview of opponent's (James's) board for John
    with(game.currentPlayer()!!) {
      assertThat(shotsMissed)
        .containsExactlyInAnyOrder(at(J, 4), at(J, 6), at(J, 8))
      assertThat(shotsHit)
        .containsExactlyInAnyOrder(
          at(E, 5), at(E, 6),
          at(A, 2), at(A, 3), at(A, 4),
          at(B, 10), at(C, 10),
          at(J, 2)
        )
      assertThat(destroyedOpponentShips)
        .containsExactly(
          WARSHIP.placed(from(E, 5), to(E, 6)),
          DESTROYER.placed(from(A, 2), to(A, 4)),
          WARSHIP.placed(from(B, 10), to(C, 10)),
          gunship(at(J, 2))
        )
    }
  }

}
