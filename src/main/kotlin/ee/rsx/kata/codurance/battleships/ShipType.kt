package ee.rsx.kata.codurance.battleships

enum class ShipType(size: Int) {
  MOTHERSHIP(size = 4),
  DESTROYER(size = 3),
  WARSHIP(size = 2),
  GUNSHIP(size = 1)
}
