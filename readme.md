Overview of classes
===========
game:
- Abstract
- Contains two player Snakes
- Contains screen which is where game will be displayed
- Communication done through reader and Writer
- These will be generated with TCP

Snake:
- represented by Deque
- Two exist to represent the two players

Cell:
- represents a Cell in the Game

ServerGame:
- Inherits from Game
- Hosts the game
- Communicates with ClientGame on other machine
- TCP

ClientGame
- Inherits from Game
- Does not host game
- Commicates with Server
- TCP

GameMaker:
- Actually created either ServerGame or ClientGame
- ONLY ONE GAME OF ANY KIND CAN EXIST

Screen:
- Is the game screen
- How everything is displayed
- Probably need to put Swing stuff here

