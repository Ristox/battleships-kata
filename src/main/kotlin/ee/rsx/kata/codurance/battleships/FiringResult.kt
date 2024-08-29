package ee.rsx.kata.codurance.battleships

data class FiringResult(
  val coordinatesFired: Coordinates,
  val type: ResultType,
  val currentPlayer: Player,

) {
}

enum class ResultType {
  MISSED,
  HIT,
  SUNK
}
