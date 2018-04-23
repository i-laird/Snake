Overview of Progress 4/22/2018
====
+ Game is running
+ Need 1 more design pattern(we have 6)
+ polishing up Swing stuff( Andrew)
+ Diagramming
    + Design Model
    + Sequence Diagrams
+ High Scores

Overview of Progress 3/22/2018 Ian Laird
=====
+ I have made good progress
+ Game class is almost entirely "finished"
    + One thing left undone is reading unbuffered input from keyboard for moves
+ Screen class is nowhere bear being done
    + Methods I need for Game are created but most are unfinished
+ Look into KeyListener Interface
   - https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyListener.html

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

