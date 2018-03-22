import javafx.util.Pair;

import java.io.IOException;
import java.net.Socket;

public class ClientGame extends Game {
    private static ClientGame singleGame = null;

    private ClientGame(){}

    public static Game getClientGame(){
        if(singleGame == null)
            singleGame = new ClientGame();
        return  singleGame;
    }

    public void initializeSocket(String hostName, int portNumber) throws IOException{
        networkSocket = new Socket(hostName, portNumber);
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
