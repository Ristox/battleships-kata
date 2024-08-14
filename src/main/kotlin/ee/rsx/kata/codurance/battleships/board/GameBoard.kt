package ee.rsx.kata.codurance.battleships.board

import ee.rsx.kata.codurance.battleships.Board
import ee.rsx.kata.codurance.battleships.Column
import ee.rsx.kata.codurance.battleships.Coordinates
import ee.rsx.kata.codurance.battleships.Row
import ee.rsx.kata.codurance.battleships.Ship
import ee.rsx.kata.codurance.battleships.ShipType
import ee.rsx.kata.codurance.battleships.ShipType.DESTROYER
import ee.rsx.kata.codurance.battleships.ShipType.GUNSHIP
import ee.rsx.kata.codurance.battleships.ShipType.MOTHERSHIP
import ee.rsx.kata.codurance.battleships.ShipType.WARSHIP
import ee.rsx.kata.codurance.battleships.ship.GameShip

class GameBoard : Board {

  private val ships: MutableList<GameShip> = mutableListOf()

  private val shipsLimits:  Map<ShipType, Int> = mapOf(
    MOTHERSHIP to 1,
    DESTROYER to 2,
    WARSHIP to 3,
    GUNSHIP to 4
  )

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

  override fun shipsPlaced(): List<Ship> {
    return ships.toList()
  }

  private fun ensureShipsLimitNotFullFor(newShip: GameShip) {
    val existingShips = ships.count { it.type == newShip.type }
    val shipLimit = shipsLimits.getValue(newShip.type)
    val plural = if (shipLimit > 1) "-s" else ""

    check(existingShips < shipLimit) {
      "Only $shipLimit ${newShip.type}$plural can be placed"
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
