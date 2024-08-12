package ee.rsx.kata.codurance.battleships.player

import ee.rsx.kata.codurance.battleships.Board
import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Column.ONE
import ee.rsx.kata.codurance.battleships.Column.SIX
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.FiringResult
import ee.rsx.kata.codurance.battleships.Player
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.Ship
import ee.rsx.kata.codurance.battleships.Ship.MOTHERSHIP
import ee.rsx.kata.codurance.battleships.board.GameBoard

class GamePlayer(override val name: String) : Player {

  override val board = GameBoard()

  override fun place(ship: Ship, start: Coordinates, end: Coordinates): Board {
    // TODO implement
    return board
  }

  override fun shipAt(row: Row, column: Column): Ship? {
    if (column == ONE || column == SIX)
      return null
    return MOTHERSHIP
  }

  override fun fireAtOpponent(coordinates: Coordinates): FiringResult {
    TODO("Not yet implemented")
  }
}
