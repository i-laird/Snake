package game;

/**
 * @author Ian Laird
 * {@link GameMaker}is the way that Games are actually generated and it allows there to be one instance of a Game in existance.
 * Server or Client can be returned.
 */
public class GameMaker {
    private static Game thisGame = null;

    /**
     * @param isServer indicates if the generated game should be a server
     * @return the generated Game
     * @author Ian Laird
     * Allows there to only be one game type in existence
     */
    public static Game generateGame(boolean isServer) {
        if (thisGame == null) {
            if (isServer) {
                thisGame = ServerGame.getServerGame();
            } else {
                thisGame = ClientGame.getClientGame();
            }
        }
        return thisGame;
    }
}
