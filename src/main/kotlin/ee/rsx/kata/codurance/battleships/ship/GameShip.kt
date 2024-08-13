package ee.rsx.kata.codurance.battleships.ship

import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.Ship
import ee.rsx.kata.codurance.battleships.ShipType

class GameShip(
  override val type: ShipType,
  override val start: Coordinates,
  override val end: Coordinates
) : Ship {

  init {
    require(start.row == end.row || start.column == end.column) {
      "Ship must be placed horizontally or vertically"
    }

    val lengthSpan = if (start.row == end.row) {
      end.column.ordinal - start.column.ordinal + 1
    } else {
      end.row.ordinal - start.row.ordinal + 1
    }

    require(lengthSpan >= type.size) {
      "Given coordinates length ($lengthSpan) is less than ship size (${type.size})"
    }
  }

  override fun isAt(row: Row, column: Column): Boolean {
    val isHorizontal = start.row == row && end.row == row
    val isVertical = start.column == column && end.column == column

    return isHorizontal && start.column <= column && column <= end.column
      || isVertical && start.row <= row && row <= end.row
  }
}
