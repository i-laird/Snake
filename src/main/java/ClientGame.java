public class ClientGame extends Game {
    private ClientGame singleGame = null;

    private ClientGame(){}

    public Game getGame(){
        if(singleGame == null)
            singleGame = new ClientGame();
        return  singleGame;
    }
}
