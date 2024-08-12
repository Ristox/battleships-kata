package ee.rsx.kata.codurance.battleships.board

import ee.rsx.kata.codurance.battleships.Board
import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Row

class GameBoard : Board {
  override val rows: Set<Row>
    get() = Row.entries.toSet()
  override val columns: Set<Column>
    get() = Column.entries.toSet()

  override fun print() {
    TODO("Not yet implemented")
  }
}
