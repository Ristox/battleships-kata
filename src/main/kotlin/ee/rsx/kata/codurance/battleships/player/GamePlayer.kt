package ee.rsx.kata.codurance.battleships.player

import ee.rsx.kata.codurance.battleships.Board
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.FiringResult
import ee.rsx.kata.codurance.battleships.Player
import ee.rsx.kata.codurance.battleships.Ship
import ee.rsx.kata.codurance.battleships.board.GameBoard

class GamePlayer(override val name: String) : Player {

  override val board = GameBoard()

  override fun place(ship: Ship, start: Coordinates, end: Coordinates): Board {
    TODO("Not yet implemented")
  }

  override fun fireAtOpponent(coordinates: Coordinates): FiringResult {
    TODO("Not yet implemented")
  }
}
