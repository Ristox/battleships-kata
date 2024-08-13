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

  private val startColumn = min(start.column.index, end.column.index)
  private val endColumn = max(start.column.index, end.column.index)

  private val startRow = min(start.row.index, end.row.index)
  private val endRow = max(start.row.index, end.row.index)

  init {
    require(start.row == end.row || start.column == end.column) {
      "Ship must be placed horizontally or vertically"
    }

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

  override fun overlapWith(another: Ship): Set<Coordinates> =
      coveredCoordinates().intersect(another.coveredCoordinates())

  override fun coveredCoordinates(): Set<Coordinates> {
    val rows: IntRange = startRow..endRow
    val columns: IntRange = startColumn..endColumn

    return rows.flatMap { row -> columns.map { column -> Coordinates(row, column) } }.toSet()
  }
}
