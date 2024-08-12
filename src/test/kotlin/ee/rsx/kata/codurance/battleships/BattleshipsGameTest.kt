package ee.rsx.kata.codurance.battleships

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

    assertThrows<IllegalStateException> {
      game.addPlayer("Bob")
    }
  }
}
