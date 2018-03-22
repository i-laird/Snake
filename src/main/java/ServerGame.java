public class ServerGame extends Game {

    private static ServerGame singleGame = null;

    private ServerGame(){}

    public static Game getServerGame(){
        if(singleGame == null)
            singleGame = new ServerGame();
        return  singleGame;
    }
}
