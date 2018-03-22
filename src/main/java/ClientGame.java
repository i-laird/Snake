import javafx.util.Pair;

public class ClientGame extends Game {
    private static ClientGame singleGame = null;

    private ClientGame(){}

    public static Game getClientGame(){
        if(singleGame == null)
            singleGame = new ClientGame();
        return  singleGame;
    }

    public void setUpGame(String networkStuff){

    }

    /**
     * Player 1 is the home player so in this case the client
     * @return
     */
    protected Pair<Integer, Integer> getPlayerOneMove(){

    }

    /**
     * Player 2 is the server
     * @return
     */
    protected  Pair<Integer, Integer> getPlayerTwoMove(){

    }

}
