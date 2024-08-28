package ee.rsx.kata.codurance.battleships

interface Battleships {

  fun addPlayer(name: String): Player
  fun start()
  fun print()
  fun currentPlayer(): Player?
}
