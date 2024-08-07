package ee.rsx.kata.codurance.battleships

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
}
