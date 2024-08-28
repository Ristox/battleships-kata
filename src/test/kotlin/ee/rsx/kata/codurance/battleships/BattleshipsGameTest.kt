package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.ResultType.MISSED
import ee.rsx.kata.codurance.battleships.Row.*
import ee.rsx.kata.codurance.battleships.ShipType.DESTROYER
import ee.rsx.kata.codurance.battleships.ShipType.GUNSHIP
import ee.rsx.kata.codurance.battleships.ShipType.MOTHERSHIP
import ee.rsx.kata.codurance.battleships.ShipType.WARSHIP
import ee.rsx.kata.codurance.battleships.ship.GameShip
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BattleshipsGameTest {

  private lateinit var game: Battleships

  @BeforeEach
  fun setup() {
    game = BattleshipsGame()
  }

  @Test
  fun `add player`() {
    val player = game.addPlayer("John")

    assertEquals("John", player.name)
  }

  @Test
  fun `game board has 10 rows and 10 columns`() {
    val player = game.addPlayer("John")

    val board = player.board

    assertThat(board.rows).size().isEqualTo(10)
    assertThat(board.columns).size().isEqualTo(10)
  }

  @Test
  fun `maximum of 2 players can be added`() {
    game.addPlayer("John")
    game.addPlayer("Jane")

    val test: () -> Unit = { game.addPlayer("Bob") }

    assertThrows<IllegalStateException>(test).let {
      assertThat(it.message).isEqualTo("Maximum of 2 players can be added")
    }
  }

  @Test
  fun `game cannot be started when only single player added`() {
    game.addPlayer("John")

    val test: () -> Unit = { game.start() }

    assertThrows<IllegalStateException>(test).let {
      assertThat(it.message).isEqualTo("2 players must be added before starting the game")
    }
  }

  @Test
  fun `game cannot be started when players don't have ships placed`() {
    game.addPlayer("John")
    game.addPlayer("Jane")

    val test: () -> Unit = { game.start() }

    assertThrows<IllegalStateException>(test).let {
      assertThat(it.message).isEqualTo("Each player must place their ships before starting the game")
    }
  }

  @Test
  fun `placing a ship places the MOTHERSHIP on the board horizontally`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, from(E, 2), to(E, 5))

      assertThat(shipTypeAt(E, 1)).isNull()
      assertThat(shipTypeAt(E, 2)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(E, 3)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(E, 4)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(E, 5)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(E, 6)).isNull()
    }
  }

  @Test
  fun `placing a ship places a DESTROYER on the board horizontally`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, from(G, 6), to(G, 8))

      assertThat(shipTypeAt(G, 5)).isNull()
      assertThat(shipTypeAt(G, 6)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(G, 7)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(G, 8)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(G, 9)).isNull()
    }
  }

  @Test
  fun `placing a ship places a DESTROYER on the board vertically`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, from(C, 4), to(E, 4))

      assertThat(shipTypeAt(B, 4)).isNull()
      assertThat(shipTypeAt(C, 4)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(D, 4)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(E, 4)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(F, 4)).isNull()
    }
  }

  @Test
  fun `placing a ship places a MOTHERSHIP on the board vertically`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, from(F, 2), to(I, 2))

      assertThat(shipTypeAt(E, 2)).isNull()
      assertThat(shipTypeAt(F, 2)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(G, 2)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(H, 2)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(I, 2)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(J, 2)).isNull()
    }
  }

  @Test
  fun `placing a MOTHERSHIP across different rows and columns throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(MOTHERSHIP, from(E, 5), to(G, 6)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Ship must be placed horizontally or vertically")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally to column span shorter than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, from(E, 5), to(E, 6)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (2) is less than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER vertically to row span shorter than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, from(E, 10), to(F, 10)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (2) is less than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally to column span longer than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, from(E, 5), to(E, 8)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (4) is larger than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically to row span longer than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(MOTHERSHIP, from(B, 1), to(B, 5)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (5) is larger than ship size (4)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally succeeds also, when start column is larger than end column`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, from(E, 10), to(E, 8)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically succeeds also, when start row is larger than end row`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(MOTHERSHIP, from(G, 3), to(D, 3)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically and DESTROYER horizontally fails, when they cross`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, from(D, 4), to(G, 4))

      val test: () -> Unit = {
        place(DESTROYER, from(F, 3), to(F, 5))
      }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Ship cannot be placed, since it would overlap with MOTHERSHIP at: [(F,4)]")
      }
    }
  }

  @Test
  fun `placing two DESTROYERS horizontally fails, when they overlap each other`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, from(H, 2), to(H, 4))

      val test: () -> Unit = {
        place(DESTROYER, from(H, 3), to(H, 5))
      }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Ship cannot be placed, since it would overlap with DESTROYER at: [(H,3), (H,4)]")
      }
    }
  }

  @Test
  fun `placing two ships (eg DESTROYERS) fails, when they touch each other (coordinates are adjacent)`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, from(H, 2), to(H, 4))

      val test: () -> Unit = {
        place(DESTROYER, from(I, 4), to(I, 6))
      }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message)
          .isEqualTo(
            "Ship cannot be placed, since it would be adjacent to another DESTROYER at: [(H,2), (H,3), (H,4)]"
          )
      }
    }
  }

  @Test
  fun `placing two ships (a MOTHERSHIP and DESTROYER) vertically and horizontally fails, when part of them is adjacent`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, from(A, 2), to(D, 2))

      val test: () -> Unit = {
        place(DESTROYER, from(E, 2), to(E, 4))
      }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message)
          .isEqualTo(
            "Ship cannot be placed, since it would be adjacent to another MOTHERSHIP at: [(A,2), (B,2), (C,2), (D,2)]"
          )
      }
    }
  }

  @Test
  fun `placing two ships horizontally, with one adjacent row in between, succeeds`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, from(C, 2), to(C, 4))

      val test: () -> Unit = { place(DESTROYER, from(E, 2), to(E, 4)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing two ships, DESTROYER vertically and MOTHERSHIP horizontally, with space between them, succeeds`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, from(D, 8), to(F, 8))

      val test: () -> Unit = { place(MOTHERSHIP, from(H, 7), to(H, 10)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing two DESTROYERS, so that they touch in corners of their surrounding area, succeeds`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, from(E, 5), to(E, 7))

      val test: () -> Unit = { place(DESTROYER, from(D, 2), to(D, 4)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing three GUNSHIPS so that they touch in corners of their surrounding area, succeeds`() {
    with(game.addPlayer("John")) {
      placeGunshipAt(B, 1)

      val test: () -> Unit = { placeGunshipAt(A, 2) }
      val test2: () -> Unit = { placeGunshipAt(C, 2) }

      assertDoesNotThrow(test)
      assertDoesNotThrow(test2)
    }
  }

  @Test
  fun `placing one MOTHERSHIP is successful`() {
    with(game.addPlayer("John")) {
      val test: () -> Unit = { place(MOTHERSHIP, from(D, 8), to(G, 8)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing more than one MOTHERSHIP fails (only 1 is allowed)`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, from(D, 8), to(G, 8))
      place(DESTROYER, from(A, 3), to(A, 5))

      val test: () -> Unit = { place(MOTHERSHIP, from(C, 5), to(F, 5)) }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Only 1 MOTHERSHIP can be placed")
      }
    }
  }

  @Test
  fun `placing more than two DESTROYERS fails (only 2 is allowed)`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, from(D, 8), to(F, 8))
      place(DESTROYER, from(A, 3), to(A, 5))

      val test: () -> Unit = { place(DESTROYER, from(C, 5), to(E, 5)) }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Only 2 DESTROYER-s can be placed")
      }
    }
  }

  @Test
  fun `placing more than three WARSHIPS fails (only 3 is allowed)`() {
    with(game.addPlayer("John")) {
      place(WARSHIP, from(D, 8), to(E, 8))
      place(WARSHIP, from(A, 3), to(A, 4))
      place(WARSHIP, from(C, 5), to(D, 5))

      val test: () -> Unit = { place(WARSHIP, from(J, 7), to(J, 8)) }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Only 3 WARSHIP-s can be placed")
      }
    }
  }

  @Test
  fun `placing more than four GUNSHIPS fails (only 4 is allowed)`() {
    with(game.addPlayer("John")) {
      placeGunshipAt(A, 3)
      placeGunshipAt(A, 7)
      placeGunshipAt(I, 5)
      placeGunshipAt(C, 9)

      val test: () -> Unit = { placeGunshipAt(J, 9) }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Only 4 GUNSHIP-s can be placed")
      }
    }
  }

  @Test
  fun `current board contains all the placed ships in expected locations`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, from(D, 8), to(G, 8))
      place(DESTROYER, from(C, 5), to(C, 7))
      place(DESTROYER, from(A, 2), to(A, 4))
      place(WARSHIP, from(E, 2), to(F, 2))
      place(WARSHIP, from(E, 5), to(E, 6))
      place(WARSHIP, from(B, 10), to(C, 10))
      placeGunshipAt(F, 4)
      placeGunshipAt(H, 10)
      placeGunshipAt(A, 6)
      val board = placeGunshipAt(J, 2)

      val shipsPlaced: List<Ship> = board.shipsPlaced()

      assertThat(shipsPlaced).containsExactlyInAnyOrder(
        MOTHERSHIP.placed(from(D, 8), to(G, 8)),

        DESTROYER.placed(from(C, 5), to(C, 7)),
        DESTROYER.placed(from(A, 2), to(A, 4)),

        WARSHIP.placed(from(E, 2), to(F, 2)),
        WARSHIP.placed(from(E, 5), to(E, 6)),
        WARSHIP.placed(from(B, 10), to(C, 10)),

        gunship(at(F, 4)),
        gunship(at(H, 10)),
        gunship(at(A, 6)),
        gunship(at(J, 2))
      )
    }
  }

  @Test
  fun `game can be started when both players have placed their ships`() {
    game.addPlayer("John").placeDefaultShips()
    game.addPlayer("James").placeDefaultShips()

    assertDoesNotThrow { game.start() }
  }

  @Test
  fun `game cannot be started when one of players has a ship missing (a DESTROYER)`() {
    game.addPlayer("John").placeDefaultShips()

    with(game.addPlayer("James")) {
      place(MOTHERSHIP, from(D, 8), to(G, 8))

      place(DESTROYER, from(A, 2), to(A, 4))

      placeDefaultWarships()
      placeDefaultGunships()
    }

    val test = { game.start() }

    assertThrows<IllegalStateException>(test).let {
      assertThat(it.message).isEqualTo("Each player must place their ships before starting the game")
    }
  }

  @Test
  fun `adding a player fails, if another player with same name already exists`() {
    game.addPlayer("James")

    val test: () -> Unit = { game.addPlayer("James") }

    assertThrows<IllegalArgumentException>(test).let {
      assertThat(it.message).isEqualTo("Player with the same name (James) already exists")
    }
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
  fun `when game has not yet been started, player cannot fire`() {
    val test: () -> Unit = { game.fire(at(A, 1)) }

    assertThrows<IllegalStateException>(test).let {
      assertThat(it.message).isEqualTo("cannot fire, game has not been started yet")
    }
  }

  @Nested
  @DisplayName("When game has been started")
  inner class WhenGameHasBeenStarted {
    private lateinit var john: Player

    private lateinit var james: Player

    @BeforeEach
    fun setup() {
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
  }

  private fun Player.placeDefaultShips() {
    place(MOTHERSHIP, from(D, 8), to(G, 8))
    placeDefaultDestroyers()
    placeDefaultWarships()
    placeDefaultGunships()
  }

  private fun Player.placeDefaultDestroyers() {
    place(DESTROYER, from(C, 5), to(C, 7))
    place(DESTROYER, from(A, 2), to(A, 4))
  }

  private fun Player.placeDefaultWarships() {
    place(WARSHIP, from(E, 2), to(F, 2))
    place(WARSHIP, from(E, 5), to(E, 6))
    place(WARSHIP, from(B, 10), to(C, 10))
  }

  private fun Player.placeDefaultGunships() {
    placeGunshipAt(F, 4)
    placeGunshipAt(H, 10)
    placeGunshipAt(A, 6)
    placeGunshipAt(J, 2)
  }

  private fun ShipType.placed(start: Coordinates, end: Coordinates) =
    GameShip(this, start, end)

  private fun gunship(location: Coordinates) = GameShip(GUNSHIP, location, location)

  private fun from(row: Row, column: Int) = Coordinates(row, column)

  private fun to(row: Row, column: Int) = Coordinates(row, column)

  private fun at(row: Row, column: Int) = Coordinates(row, column)

  private fun Player.placeGunshipAt(row: Row, column: Int) =
    place(GUNSHIP, from(row, column), to(row, column))
}
