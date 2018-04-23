package game_stuff;

/**
 * @author Ian Laird
 * {@link gameMaker}is the way that Games are actually generated and it allows there to be one instance of a Game in existance.
 * Server or Client can be returned.
 */
public class gameMaker {
    private static Game thisGame = null;

    /**
     * @author Ian Laird
     * @param isServer indicates if the generated game should be a server
     * @return the generated Game
     * Allows there to only be one game type in existence
     */
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
