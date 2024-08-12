package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.Column.*
import ee.rsx.kata.codurance.battleships.Row.*
import ee.rsx.kata.codurance.battleships.Ship.DESTROYER
import ee.rsx.kata.codurance.battleships.Ship.MOTHERSHIP
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

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

      assertThat(shipAt(E, ONE)).isNull()
      assertThat(shipAt(E, TWO)).isEqualTo(MOTHERSHIP)
      assertThat(shipAt(E, THREE)).isEqualTo(MOTHERSHIP)
      assertThat(shipAt(E, FOUR)).isEqualTo(MOTHERSHIP)
      assertThat(shipAt(E, FIVE)).isEqualTo(MOTHERSHIP)
      assertThat(shipAt(E, SIX)).isNull()
    }
  }

  @Test
  fun `placing a ship places a DESTROYER on the board horizontally`() {
    with(game.addPlayer("John")) {
      place(DESTROYER, start = Coordinates(G, SIX), end = Coordinates(G, EIGHT))

      assertThat(shipAt(G, FIVE)).isNull()
      assertThat(shipAt(G, SIX)).isEqualTo(DESTROYER)
      assertThat(shipAt(G, SEVEN)).isEqualTo(DESTROYER)
      assertThat(shipAt(G, EIGHT)).isEqualTo(DESTROYER)
      assertThat(shipAt(G, NINE)).isNull()
    }
  }
}
