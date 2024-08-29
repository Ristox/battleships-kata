package ee.rsx.kata.codurance.battleships

interface Player {
  val board: Board
  val name: String
  val shotsMissed: MutableSet<Coordinates>
  val shotsHit: MutableSet<Coordinates>

  fun place(shipType: ShipType, start: Coordinates, end: Coordinates): Board
  fun hasPlacedAllShips(): Boolean
  fun shipTypeAt(row: Row, column: Column): ShipType?
  fun shipTypeAt(row: Row, column: Int): ShipType?

  fun missed(targetAt: Coordinates)
  fun hit(targetAt: Coordinates)
}
