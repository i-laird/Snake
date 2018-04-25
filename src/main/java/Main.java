import display.Initializer;
import game_stuff.Game;
import game_stuff.GameRecord;
import game_stuff.gameMaker;
import exceptions.NetworkException;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    //RANDOM PORT NUM PROBABLY OK
    public final static int PORT_NUM = 2000;
    private static Logger LOGGER = Logger.getLogger("Main Class");

    public static void main(String [] args) {
        boolean isServer;
        String host;

        Initializer i = new Initializer();
        i.startModal();
        isServer = Initializer.getIsServer();
        host = Initializer.getHost();
        LOGGER.info("Generated Game Data");

        //This creates the Screen in Game makeScreen
        LOGGER.info("Generating Game");
        Game ourGame =  gameMaker.generateGame(isServer);

        try {
            LOGGER.info("Initializing Game");
            if(ourGame.initConnection(host, PORT_NUM))
                i.close();
        } catch(NetworkException e){
            LOGGER.severe("Network failed to initialize!");
            LOGGER.info("Game is shutting down now");
            System.exit(-1);
        }

        try{
            ourGame.initGame();
        }
        catch(IOException f){
            LOGGER.severe("Network write/ read error");
            LOGGER.info("Game is shutting down now");
            System.exit(-1);
        }

        //Save the game state
        GameRecord initialGame = ourGame.createRecord();
        try {
            do {
                while(!ourGame.hasBegun()){
                    Thread.sleep(250);
                }
                while (!ourGame.isGameOver()) {
                    ourGame.MovePlayers();
                    Thread.sleep(500);
                }
            } while (ourGame.playAgain(initialGame));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe("Exception occurred while playing the game: " + e.getMessage());
        }
    }

    /**
     * This method creates a Game and initializes with network settings.
     * @author Ian Laird
     * @return initialized Game
     */
    /*public static Game initgame(){
        Scanner cin = new Scanner(System.in);
        boolean isServer = false;
        String option;
        //See if the player will be a Server or a Client
        do {
            System.out.println("Will you be a server? (y n)");
            option = cin.next();
            isServer = option.matches("[yY]");
        }while(!isServer && !option.matches("[nN]"));
        if(isServer) {
            System.out.println("Now waiting for second player to connect!");
        }
        Game thisGame =  gameMaker.generateGame(isServer);
        //If client we need to find out host name
        if(!isServer){
            System.out.println("Please enter the host ip.");
            option = cin.next();
        }

        //Network error could happen
        try {
            thisGame.initGame(option, PORT_NUM);
        }catch(NetworkException e){
            LOGGER.severe("Network failed to initialize!");
            LOGGER.info("Game is shutting down now");
            System.exit(-1);
        }catch(IOException f){
            LOGGER.severe("Network write/ read error");
            LOGGER.info("Game is shutting down now");
            System.exit(-1);
        }

        return thisGame;
    }

    public static boolean playAgain(Game toReset, GameRecord record){
        System.out.println("Would you like to play again? (y | n)");
        Scanner cin = new Scanner(System.in);
        boolean ret;
        try {
            ret = toReset.playAgain(cin.next().matches("[yY]"), record);
        } catch (IOException e){
            ret = false;
        }
        return ret;
    }*/
}
