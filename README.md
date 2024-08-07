## Battleships

### Intro

This is a modified version of the Codurance Battleships kata at https://www.codurance.com/katas/battleships

### Overview

The Battleships Game has four types of ships, represented on a board with the following symbols
* 1 Mothership: size 4 cells - symbol 'M'
* 2 Destroyers: size 3 cells - symbol 'D'
* 3 Warships: size 2 cells - symbol 'W'
* 4 Gun Ships: size 1 cell - symbol 'G'

Description:
* Game has a board of 10 x 10 (100) cells - rows A...J and columns 1...10
* Game has exactly 2 registered players
* Each player places their ships across the game board
* Ships can be placed on the board vertically or horizontally (not diagonally)
* Each ship must occupy consecutive number of cells in a row, according to the size of that type of ship (depending on symbol)
* Game board is separate for each player (has player's ships and opposite player's hits and misses)
* Before the game can be started, both players must register themselves
* When registering, a player must provide their name
* After registering, the player must place all their ships to the board
* Another player can also be registered, and add their ships at the same time
* Player can add a ship by defining its type (symbol) and start-end coordinates (x1,y1) - (x2,y2). 
* Start-end coordinates must represent the size of ship (consecutive cells) either vertially or horizontally
* Ships cannot be placed right next to each other (there must be a free cell between ships next to each other)
* Ships can be placed in such a way that vertical and horizontal ships close to each other can touch by their corners
* Ships cannot cross each other
* Subsequent ship of same type can be placed by placing the same type of ship again to another location
* Changing an already placed ship's location is not supported at the moment
* Each type of ship must be placed as many times, as described in the overview (1-4 times, based on type of ship)
* Player can get an overview of their current board at any time (get a visual output of the board and its ships currently placed)
* During placing ships, player can retrieve overview of the ships which are still to be placed onto the board
* If a ship of given type has already been placed as many times as needed, then trying to place another of the same type results in error
* Game can be started as soon as both player's ships have all been placed
* When game starts, first player starts firing
* To fire, player must specify (x, y) coordinate to fire upon
* Player can get a view of opponent's board before firing.
   * That view shows the hits and misses of player, but all other opponent's ships are hidden. 
   * The view also shows statistics of the battle: how many shots, hits and misses, what ships are sunk and what still remain
* After firing, when the fire is a miss, it is marked on the board with an 'o'
* After firing, when the fire is a hit, it is marked on the board with an 'X'
* When fire is a hit (including sink of ship), same player shall fire again
* When fire is a miss, the firing turn goes to another player
* When fire is a hit and hits the last cell of a given ship, the game tells the player that the ship has sunk and type of the ship
* Game ends when one of players hits the last cell of the last ship of the opponent. Whoever sinks all opponents ships first, wins.


