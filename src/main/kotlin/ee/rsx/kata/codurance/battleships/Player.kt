package ee.rsx.kata.codurance.battleships

interface Player {
  val board: Board
  val name: String

  fun place(ship: Ship, start: Coordinates, end: Coordinates): Board
  fun shipAt(row: Row, column: Column): Ship
  fun fireAtOpponent(coordinates: Coordinates): FiringResult
}
