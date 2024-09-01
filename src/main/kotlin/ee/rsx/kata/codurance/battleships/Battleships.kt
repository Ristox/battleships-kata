package ee.rsx.kata.codurance.battleships

interface Battleships {

  fun addPlayer(name: String): Player
  fun start()
  fun render(): GameRender
  fun currentPlayer(): Player?
  fun fire(at: Coordinates): FiringResult
  fun hasEnded(): Boolean
  fun winner(): Player?
}
