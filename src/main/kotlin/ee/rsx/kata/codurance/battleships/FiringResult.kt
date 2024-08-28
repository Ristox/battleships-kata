package ee.rsx.kata.codurance.battleships

data class FiringResult(
  val coordinatesFired: Coordinates,
  val type: ResultType
)

enum class ResultType {
  MISSED
}
