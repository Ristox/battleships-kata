package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.Column.*
import ee.rsx.kata.codurance.battleships.Row.*
import ee.rsx.kata.codurance.battleships.ShipType.DESTROYER
import ee.rsx.kata.codurance.battleships.ShipType.GUNSHIP
import ee.rsx.kata.codurance.battleships.ShipType.MOTHERSHIP
import ee.rsx.kata.codurance.battleships.ShipType.WARSHIP
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
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
      place(MOTHERSHIP, start = Coordinates(E, 2), end = Coordinates(E, 5))

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
      place(DESTROYER, start = Coordinates(G, 6), end = Coordinates(G, 8))

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
      place(DESTROYER, start = Coordinates(C, 4), end = Coordinates(E, 4))

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
      place(MOTHERSHIP, start = Coordinates(F, 2), end = Coordinates(I, 2))

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

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(E, 5), end = Coordinates(G, 6)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Ship must be placed horizontally or vertically")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally to column span shorter than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, 5), end = Coordinates(E, 6)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (2) is less than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER vertically to row span shorter than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, 10), end = Coordinates(F, 10)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (2) is less than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally to column span longer than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, 5), end = Coordinates(E, 8)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (4) is larger than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically to row span longer than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(B, 1), end = Coordinates(B, 5)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (5) is larger than ship size (4)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally succeeds also, when start column is larger than end column`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, 10), end = Coordinates(E, 8)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically succeeds also, when start row is larger than end row`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(G, 3), end = Coordinates(D, 3)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically and DESTROYER horizontally fails, when they cross`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, start = Coordinates(D, 4), end = Coordinates(G, 4))

      val test: () -> Unit = {
        place(DESTROYER, start = Coordinates(F, 3), end = Coordinates(F, 5))
      }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Ship cannot be placed, since it would overlap with MOTHERSHIP at: [(F,4)]")
      }
    }
  }

  @Test
  fun `placing two DESTROYERS horizontally fails, when they overlap each other`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(H, 2), end = Coordinates(H, 4))

      val test: () -> Unit = {
        place(DESTROYER, start = Coordinates(H, 3), end = Coordinates(H, 5))
      }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Ship cannot be placed, since it would overlap with DESTROYER at: [(H,3), (H,4)]")
      }
    }
  }

  @Test
  fun `placing two ships (eg DESTROYERS) fails, when they touch each other (coordinates are adjacent)`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(H, 2), end = Coordinates(H, 4))

      val test: () -> Unit = {
        place(DESTROYER, start = Coordinates(I, 4), end = Coordinates(I, 6))
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
      place(MOTHERSHIP, start = Coordinates(A, 2), end = Coordinates(D, 2))

      val test: () -> Unit = {
        place(DESTROYER, start = Coordinates(E, 2), end = Coordinates(E, 4))
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
      place(DESTROYER, start = Coordinates(C, 2), end = Coordinates(C, 4))

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, 2), end = Coordinates(E, 4)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing two ships, DESTROYER vertically and MOTHERSHIP horizontally, with space between them, succeeds`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(D, 8), end = Coordinates(F, 8))

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(H, 7), end = Coordinates(H, 10)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing two DESTROYERS, so that they touch in corners of their surrounding area, succeeds`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(E, 5), end = Coordinates(E, 7))

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(D, 2), end = Coordinates(D, 4)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing three GUNSHIPS so that they touch in corners of their surrounding area, succeeds`() {
    with(game.addPlayer("John")) {
      val gunshipOneLocation = Coordinates(B, 1)
      place(GUNSHIP, start = gunshipOneLocation, end = gunshipOneLocation)

      val gunshipTwoLocation = Coordinates(A, 2)
      val test: () -> Unit = { place(GUNSHIP, start = gunshipTwoLocation, end = gunshipTwoLocation) }

      val gunshipThreeLocation = Coordinates(C, 2)
      val test2: () -> Unit = { place(GUNSHIP, start = gunshipThreeLocation, end = gunshipThreeLocation) }

      assertDoesNotThrow(test)
      assertDoesNotThrow(test2)
    }
  }

  @Test
  fun `placing one MOTHERSHIP is successful`() {
    with(game.addPlayer("John")) {
      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(D, EIGHT), end = Coordinates(G, EIGHT)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing more than one MOTHERSHIP fails (only 1 is allowed)`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, start = Coordinates(D, 8), end = Coordinates(G, 8))
      place(DESTROYER, start = Coordinates(A,3), end = Coordinates(A,5))

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(C, 5), end = Coordinates(F, 5)) }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Only 1 MOTHERSHIP can be placed")
      }
    }
  }

  @Test
  fun `placing more than two DESTROYERS fails (only 2 is allowed)`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(D, 8), end = Coordinates(F, 8))
      place(DESTROYER, start = Coordinates(A,3), end = Coordinates(A,5))

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(C, 5), end = Coordinates(E, 5)) }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Only 2 DESTROYER ships can be placed")
      }
    }
  }

  @Test
  fun `placing more than three WARSHIPS fails (only 2 is allowed)`() {
    with(game.addPlayer("John")) {
      place(WARSHIP, start = Coordinates(D, 8), end = Coordinates(E, 8))
      place(WARSHIP, start = Coordinates(A,3), end = Coordinates(A,4))
      place(WARSHIP, start = Coordinates(C, 5), end = Coordinates(D, 5))

      val test: () -> Unit = { place(WARSHIP, start = Coordinates(J, 7), end = Coordinates(J, 8)) }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Only 3 WARSHIP-s can be placed")
      }
    }
  }
}
