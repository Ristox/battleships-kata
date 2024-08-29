package ee.rsx.kata.codurance.battleships

data class FiringResult(
  val coordinatesFired: Coordinates,
  val type: ResultType,
  val missedShots: Set<Coordinates>,
  val hits: Set<Coordinates>
)

enum class ResultType {
  MISSED,
  HIT
}
