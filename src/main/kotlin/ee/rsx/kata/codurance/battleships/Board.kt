package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.ship.GameShip

interface Board {
  val rows: Set<Row>
  val columns: Set<Column>

  fun print()
  fun shipAt(row: Row, column: Column): GameShip?
  fun place(shipType: ShipType, start: Coordinates, end: Coordinates)
}
