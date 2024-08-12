package ee.rsx.kata.codurance.battleships

interface Ship {
  val type: ShipType
  val start: Coordinates
  val end: Coordinates

  fun isAt(row: Row, column: Column): Boolean
}
