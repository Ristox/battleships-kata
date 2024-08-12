package ee.rsx.kata.codurance.battleships.board

import ee.rsx.kata.codurance.battleships.Board
import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.ShipType
import ee.rsx.kata.codurance.battleships.ship.GameShip

class GameBoard : Board {

  private val ships: MutableList<GameShip> = mutableListOf()

  override val rows: Set<Row>
    get() = Row.entries.toSet()
  override val columns: Set<Column>
    get() = Column.entries.toSet()

  override fun print() {
    TODO("Not yet implemented")
  }

  override fun shipAt(row: Row, column: Column): GameShip? {
    return ships.find { it.isAt(row, column) }
  }

  override fun place(shipType: ShipType, start: Coordinates, end: Coordinates) {
    ships.add(GameShip(shipType, start, end))
  }
}
