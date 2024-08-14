package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.Column.*
import ee.rsx.kata.codurance.battleships.Row.*
import ee.rsx.kata.codurance.battleships.ShipType.DESTROYER
import ee.rsx.kata.codurance.battleships.ShipType.GUNSHIP
import ee.rsx.kata.codurance.battleships.ShipType.MOTHERSHIP
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
      place(MOTHERSHIP, start = Coordinates(E, TWO), end = Coordinates(E, FIVE))

      assertThat(shipTypeAt(E, ONE)).isNull()
      assertThat(shipTypeAt(E, TWO)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(E, THREE)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(E, FOUR)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(E, FIVE)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(E, SIX)).isNull()
    }
  }

  @Test
  fun `placing a ship places a DESTROYER on the board horizontally`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(G, SIX), end = Coordinates(G, EIGHT))

      assertThat(shipTypeAt(G, FIVE)).isNull()
      assertThat(shipTypeAt(G, SIX)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(G, SEVEN)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(G, EIGHT)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(G, NINE)).isNull()
    }
  }

  @Test
  fun `placing a ship places a DESTROYER on the board vertically`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(C, FOUR), end = Coordinates(E, FOUR))

      assertThat(shipTypeAt(B, FOUR)).isNull()
      assertThat(shipTypeAt(C, FOUR)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(D, FOUR)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(E, FOUR)).isEqualTo(DESTROYER)
      assertThat(shipTypeAt(F, FOUR)).isNull()
    }
  }

  @Test
  fun `placing a ship places a MOTHERSHIP on the board vertically`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, start = Coordinates(F, TWO), end = Coordinates(I, TWO))

      assertThat(shipTypeAt(E, TWO)).isNull()
      assertThat(shipTypeAt(F, TWO)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(G, TWO)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(H, TWO)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(I, TWO)).isEqualTo(MOTHERSHIP)
      assertThat(shipTypeAt(J, TWO)).isNull()
    }
  }

  @Test
  fun `placing a MOTHERSHIP across different rows and columns throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(E, FIVE), end = Coordinates(G, SIX)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Ship must be placed horizontally or vertically")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally to column span shorter than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, FIVE), end = Coordinates(E, SIX)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (2) is less than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER vertically to row span shorter than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, TEN), end = Coordinates(F, TEN)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (2) is less than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally to column span longer than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, FIVE), end = Coordinates(E, EIGHT)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (4) is larger than ship size (3)")
      }
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically to row span longer than its size throws exception`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(B, ONE), end = Coordinates(B, FIVE)) }

      assertThrows<IllegalArgumentException>(test).let {
        assertThat(it.message).isEqualTo("Given coordinates length (5) is larger than ship size (4)")
      }
    }
  }

  @Test
  fun `placing a DESTROYER horizontally succeeds also, when start column is larger than end column`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, TEN), end = Coordinates(E, EIGHT)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically succeeds also, when start row is larger than end row`() {
    with(game.addPlayer("John")) {

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(G, THREE), end = Coordinates(D, THREE)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing a MOTHERSHIP vertically and DESTROYER horizontally fails, when they cross`() {
    with(game.addPlayer("John")) {
      place(MOTHERSHIP, start = Coordinates(D, FOUR), end = Coordinates(G, FOUR))

      val test: () -> Unit = {
        place(DESTROYER, start = Coordinates(F, THREE), end = Coordinates(F, FIVE))
      }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Ship cannot be placed, since it would overlap with MOTHERSHIP at: [(F,4)]")
      }
    }
  }

  @Test
  fun `placing two DESTROYERS horizontally fails, when they overlap each other`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(H, TWO), end = Coordinates(H, FOUR))

      val test: () -> Unit = {
        place(DESTROYER, start = Coordinates(H, THREE), end = Coordinates(H, FIVE))
      }

      assertThrows<IllegalStateException>(test).let {
        assertThat(it.message).isEqualTo("Ship cannot be placed, since it would overlap with DESTROYER at: [(H,3), (H,4)]")
      }
    }
  }

  @Test
  fun `placing two ships (eg DESTROYERS) fails, when they touch each other (coordinates are adjacent)`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(H, TWO), end = Coordinates(H, FOUR))

      val test: () -> Unit = {
        place(DESTROYER, start = Coordinates(I, FOUR), end = Coordinates(I, SIX))
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
      place(MOTHERSHIP, start = Coordinates(A, TWO), end = Coordinates(D, TWO))

      val test: () -> Unit = {
        place(DESTROYER, start = Coordinates(E, TWO), end = Coordinates(E, FOUR))
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
      place(DESTROYER, start = Coordinates(C, TWO), end = Coordinates(C, FOUR))

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(E, TWO), end = Coordinates(E, FOUR)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing two ships, DESTROYER vertically and MOTHERSHIP horizontally, with space between them, succeeds`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(D, EIGHT), end = Coordinates(F, EIGHT))

      val test: () -> Unit = { place(MOTHERSHIP, start = Coordinates(H, SEVEN), end = Coordinates(H, TEN)) }

      assertDoesNotThrow(test)
    }
  }

  @Test
  fun `placing two DESTROYERS, so that they touch in corners of their surrounding area, succeeds`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(E, FIVE), end = Coordinates(E, SEVEN))

      val test: () -> Unit = { place(DESTROYER, start = Coordinates(D, TWO), end = Coordinates(D, FOUR)) }

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
}
