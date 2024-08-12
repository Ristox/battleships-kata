package ee.rsx.kata.codurance.battleships

interface Board {
  val rows: Set<Row>
  val columns: Set<Column>

  fun print()
}
