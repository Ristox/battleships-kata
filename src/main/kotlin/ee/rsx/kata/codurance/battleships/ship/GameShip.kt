package ee.rsx.kata.codurance.battleships.ship

import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.Ship
import ee.rsx.kata.codurance.battleships.ShipType
import kotlin.math.max
import kotlin.math.min

class GameShip(
  override val type: ShipType,
  override val start: Coordinates,
  override val end: Coordinates
) : Ship {

  init {
    require(start.row == end.row || start.column == end.column) {
      "Ship must be placed horizontally or vertically"
    }

    val startColumn = min(start.column.ordinal, end.column.ordinal)
    val endColumn = max(start.column.ordinal, end.column.ordinal)

    val startRow = min(start.row.ordinal, end.row.ordinal)
    val endRow = max(start.row.ordinal, end.row.ordinal)

    val lengthSpan = if (start.row == end.row) {
      endColumn - startColumn + 1
    } else {
      endRow - startRow + 1
    }

    require(lengthSpan >= type.size) {
      "Given coordinates length ($lengthSpan) is less than ship size (${type.size})"
    }
    require(lengthSpan == type.size) {
      "Given coordinates length ($lengthSpan) is larger than ship size (${type.size})"
    }
  }

  override fun isAt(row: Row, column: Column): Boolean {
    val isHorizontal = start.row == row && end.row == row
    val isVertical = start.column == column && end.column == column

    return isHorizontal && start.column <= column && column <= end.column
      || isVertical && start.row <= row && row <= end.row
  }
}
