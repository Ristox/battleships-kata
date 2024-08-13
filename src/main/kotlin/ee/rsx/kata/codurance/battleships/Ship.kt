package ee.rsx.kata.codurance.battleships

interface Ship {
  val type: ShipType
  val start: Coordinates
  val end: Coordinates

  fun isAt(row: Row, column: Column): Boolean
  fun coveredCoordinates(): Set<Coordinates>
  fun overlapWith(another: Ship): Set<Coordinates>
}
