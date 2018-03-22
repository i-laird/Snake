import javafx.util.Pair;

public class ServerGame extends Game {

    private static ServerGame singleGame = null;

    private ServerGame(){}

    public static Game getServerGame(){
        if(singleGame == null)
            singleGame = new ServerGame();
        return  singleGame;
    }

    public void setUpGame(String networkStuff){

    }

    /**
     * Player 1 is the home player so in this case the Server
     * @return
     */
    protected Pair<Integer, Integer> getPlayerOneMove(){

    }

    /**
     * Player 2 is the client
     * @return
     */
    protected  Pair<Integer, Integer> getPlayerTwoMove(){

    }

}
