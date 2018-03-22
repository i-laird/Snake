public class ServerGame extends Game {

    private ServerGame singleGame = null;

    private ServerGame(){}

    public Game getGame(){
        if(singleGame == null)
            singleGame = new ServerGame();
        return  singleGame;
    }
}
