package ee.rsx.kata.codurance.battleships

data class FiringResult(
  val coordinatesFired: Coordinates,
  val type: ResultType
) {
  fun missedShots(): Set<Coordinates> {
    return emptySet()
  }
}

enum class ResultType {
  MISSED,
  HIT
}
