package ee.rsx.kata.codurance.battleships.board

import ee.rsx.kata.codurance.battleships.Board
import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.ShipType
import ee.rsx.kata.codurance.battleships.ship.GameShip

class GameBoard : Board {

  private val ships: MutableList<GameShip> = mutableListOf()

  override val rows: Set<Row>
    get() = Row.entries.toSet()
  override val columns: Set<Column>
    get() = Column.entries.toSet()

  override fun print() {
    TODO("Not yet implemented")
  }

  override fun shipAt(row: Row, column: Column): GameShip? {
    return ships.find { it.isAt(row, column) }
  }

  override fun place(shipType: ShipType, start: Coordinates, end: Coordinates) {
    val newShip = GameShip(shipType, start, end)
    ensureDoesNotOverlapWithOtherShips(newShip)
    ensureIsNotAdjacentToOtherShips(newShip)
    ensureShipsLimitNotFullFor(newShip)
    ships.add(newShip)
  }

  private fun ensureShipsLimitNotFullFor(newShip: GameShip) {
    val existingShips = ships.count { it.type == newShip.type }
    if (newShip.type == ShipType.MOTHERSHIP) {
      check(existingShips < 1) {
        "Only 1 ${newShip.type} can be placed"
      }
    } else if (newShip.type == ShipType.DESTROYER) {
      check(existingShips < 2) {
        "Only 2 ${newShip.type}-s can be placed"
      }
    } else if (newShip.type == ShipType.WARSHIP) {
      check(existingShips < 3) {
        "Only 3 ${newShip.type}-s can be placed"
      }
    } else if (newShip.type == ShipType.GUNSHIP) {
      check(existingShips < 4) {
        "Only 4 ${newShip.type}-s can be placed"
      }
    }
  }

  private fun ensureDoesNotOverlapWithOtherShips(newShip: GameShip) {
    ships.forEach {
      val overlap = it.overlapWith(newShip)
      check(overlap.isEmpty()) {
        "Ship cannot be placed, since it would overlap with ${it.type} at: $overlap"
      }
    }
  }

  private fun ensureIsNotAdjacentToOtherShips(newShip: GameShip) {
    ships.forEach {
      val isAdjacent = it.isAdjacentTo(newShip)
      check(!isAdjacent) {
        "Ship cannot be placed, since it would be adjacent to another ${it.type} at: ${it.coveredCoordinates()}"
      }
    }
  }
}
