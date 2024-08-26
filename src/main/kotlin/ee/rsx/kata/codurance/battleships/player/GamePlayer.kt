package ee.rsx.kata.codurance.battleships.player

import ee.rsx.kata.codurance.battleships.Board
import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.FiringResult
import ee.rsx.kata.codurance.battleships.Player
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.ShipType
import ee.rsx.kata.codurance.battleships.board.GameBoard

class GamePlayer(override val name: String) : Player {

  override val board = GameBoard()

  override fun place(shipType: ShipType, start: Coordinates, end: Coordinates): Board {
    board.place(shipType, start, end)
    return board
  }

  override fun hasPlacedAllShips() = board.allShipsHaveBeenPlaced()

  override fun shipTypeAt(row: Row, column: Column): ShipType? {
    return board.shipAt(row, column)?.type
  }

  override fun shipTypeAt(row: Row, column: Int): ShipType? {
    return shipTypeAt(row, Column.entries.first { it.index == column })
  }

  override fun fireAtOpponent(coordinates: Coordinates): FiringResult {
    TODO("Not yet implemented")
  }
}
