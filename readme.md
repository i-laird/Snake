# Snake Game

This Snake game was created as the group project for Software Engineering 1/ Intro to Java Programming at Baylor University.

In this game two players control snakes and attempt to reach the longest length through eating fruits. if you hit the other snake you lose.

Check [here](classOverview.md) to see an overview of classes

# Built Using
+ Java 8
   + TCP is used for the networking
   + Java FX is used for the GUI
   + Swing is used to display the game
+ Maven
+ Spot Bugs for Code Analysis
+ Junit for unit testing

# Running the Game

Run this command in your terminal when you are in the project folder
```
mvn clean javafx:run
```

You will see a GUI appear asking if you are a host or a client. Since currenly only two players are supported one player must choose to be a host and one must choose to be a client.

If you choose to be a server your ip address will be given to you and then you will be told to hit ok. Once you do so you are ready to accept incoming connections from a client. Note: if the server GUI is still open when the client attempts to connect the connection will fail and the client will have to try again.

# Authors
+ Ian laird
+ Andrew Walker

