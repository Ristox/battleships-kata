package ee.rsx.kata.codurance.battleships

interface Player {
  val board: Board
  val name: String
  val misses: MutableSet<Coordinates>
  val hits: MutableSet<Coordinates>

  fun place(shipType: ShipType, start: Coordinates, end: Coordinates): Board
  fun hasPlacedAllShips(): Boolean
  fun shipTypeAt(row: Row, column: Column): ShipType?
  fun shipTypeAt(row: Row, column: Int): ShipType?
}
