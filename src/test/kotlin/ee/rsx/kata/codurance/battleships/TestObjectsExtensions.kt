package ee.rsx.kata.codurance.battleships

import ee.rsx.kata.codurance.battleships.Row.*
import ee.rsx.kata.codurance.battleships.ShipType.*
import ee.rsx.kata.codurance.battleships.ship.GameShip

internal fun Player.placeDefaultShips() {
  place(MOTHERSHIP, from(D, 8), to(G, 8))
  placeDefaultDestroyers()
  placeDefaultWarships()
  placeDefaultGunships()
}

internal fun Player.placeDefaultDestroyers() {
  place(DESTROYER, from(C, 5), to(C, 7))
  place(DESTROYER, from(A, 2), to(A, 4))
}

internal fun Player.placeDefaultWarships() {
  place(WARSHIP, from(E, 2), to(F, 2))
  place(WARSHIP, from(E, 5), to(E, 6))
  place(WARSHIP, from(B, 10), to(C, 10))
}

internal fun Player.placeDefaultGunships() {
  placeGunshipAt(F, 4)
  placeGunshipAt(H, 10)
  placeGunshipAt(A, 6)
  placeGunshipAt(J, 2)
}

internal fun ShipType.placed(start: Coordinates, end: Coordinates) =
  GameShip(this, start, end)

internal fun gunship(location: Coordinates) = GameShip(GUNSHIP, location, location)

internal fun from(row: Row, column: Int) = Coordinates(row, column)

internal fun to(row: Row, column: Int) = Coordinates(row, column)

internal fun at(row: Row, column: Int) = Coordinates(row, column)

internal fun Player.placeGunshipAt(row: Row, column: Int) =
  place(GUNSHIP, from(row, column), to(row, column))
