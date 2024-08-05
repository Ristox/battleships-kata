package ee.rsx.kata.codurance.battleships

interface Battleships {

  fun addPlayer(name: String)
  fun start()
  fun fire(x: Int, y: Int)
  fun endTurn()
  fun print()
}
