package ee.rsx.kata.codurance.battleships.player

import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.Player
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.Ship
import ee.rsx.kata.codurance.battleships.ShipType
import ee.rsx.kata.codurance.battleships.board.GameBoard

data class GamePlayer(override val name: String) : Player {

  override val board = GameBoard()

  override val destroyedOpponentShips = mutableSetOf<Ship>()
  override val shotsMissed = mutableSetOf<Coordinates>()
  override val shotsHit = mutableSetOf<Coordinates>()

  override fun place(shipType: ShipType, start: Coordinates, end: Coordinates) =
    board.apply { place(shipType, start, end) }

  override fun hasPlacedAllShips() =
    board.allShipsHaveBeenPlaced()

  override fun shipTypeAt(row: Row, column: Column): ShipType? =
    board.shipAt(row, column)?.type

  override fun shipTypeAt(row: Row, column: Int): ShipType? =
    shipTypeAt(row, Column.entries.first { it.index == column })

  override fun missed(targetAt: Coordinates) {
    shotsMissed.add(targetAt)
  }

  override fun hit(targetAt: Coordinates) {
    shotsHit.add(targetAt)
  }
}
