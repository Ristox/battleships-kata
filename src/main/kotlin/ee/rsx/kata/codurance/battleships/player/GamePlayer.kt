package ee.rsx.kata.codurance.battleships.player

import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.Player
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.ShipType
import ee.rsx.kata.codurance.battleships.board.GameBoard

class GamePlayer(override val name: String) : Player {

  override val board = GameBoard()

  override val misses = mutableSetOf<Coordinates>()
  override val hits = mutableSetOf<Coordinates>()

  override fun place(shipType: ShipType, start: Coordinates, end: Coordinates) =
    board.apply { place(shipType, start, end) }

  override fun hasPlacedAllShips() =
    board.allShipsHaveBeenPlaced()

  override fun shipTypeAt(row: Row, column: Column): ShipType? =
    board.shipAt(row, column)?.type

  override fun shipTypeAt(row: Row, column: Int): ShipType? =
    shipTypeAt(row, Column.entries.first { it.index == column })
}
