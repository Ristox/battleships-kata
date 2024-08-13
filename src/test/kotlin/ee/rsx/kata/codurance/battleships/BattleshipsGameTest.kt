package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.Column.*
import ee.rsx.kata.codurance.battleships.Row.*
import ee.rsx.kata.codurance.battleships.ShipType.DESTROYER
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
        assertThat(it.message).isEqualTo("Given coordinates length is less than ship size")
      }
    }
  }
}
