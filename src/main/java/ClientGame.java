public class ClientGame extends Game {
    private static ClientGame singleGame = null;

    private ClientGame(){}

    public static Game getClientGame(){
        if(singleGame == null)
            singleGame = new ClientGame();
        return  singleGame;
    }
}
