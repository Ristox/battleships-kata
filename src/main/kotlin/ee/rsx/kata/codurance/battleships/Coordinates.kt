package ee.rsx.kata.codurance.battleships

data class Coordinates(val row: Row, val column: Column)

fun Coordinates(row: Row, col: Int) = Coordinates(row, Column.entries.first { it.index == col })

enum class Row(val index: Int) {
  A(1),
  B(2),
  C(3),
  D(4),
  E(5),
  F(6),
  G(7),
  H(8),
  I(9),
  J(10)
}

enum class Column(val index: Int) {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  TEN(10)
}
