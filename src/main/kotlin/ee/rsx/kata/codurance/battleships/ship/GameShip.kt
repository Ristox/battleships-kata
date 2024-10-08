package ee.rsx.kata.codurance.battleships.ship

import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.Ship
import ee.rsx.kata.codurance.battleships.ShipType
import ee.rsx.kata.codurance.battleships.ShipType.GUNSHIP
import kotlin.math.max
import kotlin.math.min

data class GameShip(
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

  override fun toString(): String {
    return if (type == GUNSHIP)
      "$type at $start"
    else
      "$type from $start to $end"
  }

  override fun isAt(row: Row, column: Column): Boolean {
    val isHorizontal = start.row == row && end.row == row
    val isVertical = start.column == column && end.column == column

    return isHorizontal && start.column <= column && column <= end.column
      || isVertical && start.row <= row && row <= end.row
  }

  override fun overlapWith(another: Ship): Set<Coordinates> =
      coveredCoordinates().intersect(another.coveredCoordinates())

  override fun isAdjacentTo(newShip: Ship): Boolean {
    val areaStartRow = if (startRow.isFirst()) first() else startRow.previous()
    val areaEndRow = if (endRow.isLast()) last() else endRow.next()

    val areaStartColumn = if (startColumn.isFirst()) first() else startColumn.previous()
    val areaEndColumn = if (endColumn.isLast()) last() else endColumn.next()

    val rows: IntRange = areaStartRow..areaEndRow
    val columns: IntRange = areaStartColumn..areaEndColumn

    val forbiddenSurroundingArea = rows.flatMap {
      row -> columns.map { column -> Coordinates(row, column) }
    }
      .toSet()
      .minus(coveredCoordinates())
      .minus(cornersOfSurroundingArea())

    return newShip.coveredCoordinates()
      .intersect(forbiddenSurroundingArea)
      .isNotEmpty()
  }

  override fun coveredCoordinates(): Set<Coordinates> {
    val rows: IntRange = startRow..endRow
    val columns: IntRange = startColumn..endColumn

    return rows.flatMap { row -> columns.map { column -> Coordinates(row, column) } }.toSet()
  }

  private fun cornersOfSurroundingArea(): Set<Coordinates> {
    val upperLeftCorner =
      if (startRow.isFirst() || startColumn.isFirst()) null
        else Coordinates(startRow.previous(), startColumn.previous())

    val upperRightCorner =
      if (startRow.isFirst() || endColumn.isLast()) null
        else Coordinates(startRow.previous(), endColumn.next())

    val lowerLeftCorner =
      if (endRow.isFirst() || startColumn.isFirst()) null
        else Coordinates(endRow.next(), startColumn.previous())

    val lowerRightCorner =
      if (endRow.isLast() || endColumn.isLast()) null
        else Coordinates(endRow.next(), endColumn.next())

    return setOfNotNull(
      upperLeftCorner,
      upperRightCorner,
      lowerLeftCorner,
      lowerRightCorner
    )
  }

  private fun Int.isFirst() = this == 1

  private fun Int.isLast() = this == 10

  private fun Int.previous() = this - 1

  private fun Int.next() = this + 1

  private fun first() = 1

  private fun last() = 10
}
