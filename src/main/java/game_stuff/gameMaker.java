package game_stuff;

/**
 * @author: Ian Laird
 * This is the way that Games are actually generated and it allows there to be one instance of a Game in existance.
 */
public class gameMaker {
    private static Game thisGame = null;
    public static Game generateGame(boolean isServer){
        if(thisGame == null) {
            if (isServer) {
                thisGame = ServerGame.getServerGame();
            }
            else {
                thisGame = ClientGame.getClientGame();
            }
        }
        return thisGame;
    }
}
