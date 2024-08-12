package ee.rsx.kata.codurance.battleships

interface Player {
  val board: Board
  val name: String

  fun place(shipType: ShipType, start: Coordinates, end: Coordinates): Board
  fun shipTypeAt(row: Row, column: Column): ShipType?
  fun fireAtOpponent(coordinates: Coordinates): FiringResult
}
