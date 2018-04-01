import game_stuff.Game;
import game_stuff.gameMaker;
import exceptions.NetworkException;
import resources.Screen;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    //RANDOM PORT NUM PROBABLY OK
    public final static int PORT_NUM = 45;
    private static Logger LOGGER = Logger.getLogger("Main Class");

    public static void main(String [] args){
        Screen screen = Screen.getInstance(400, 400);
    }

    /**
     * This method creates a Game and initializes with network settings.
     * @author Ian Laird
     * @return initialized Game
     */
    public Game initgame(){
        Scanner cin = new Scanner(System.in);
        boolean isServer = false;
        String option;
        //See if the player will be a Server or a Client
        do {
            System.out.println("Will you be a server? (y n)");
            option = cin.next();
            isServer = option.matches("[yY]");
        }while(!isServer && !option.matches("nN"));
        Game thisGame =  gameMaker.generateGame(isServer);
        //If client we need to find out host name
        if(!isServer){
            System.out.println("Please enter the host name.");
            option = cin.next();
        }
        //Network error could happen
        try {
            thisGame.initGame(option, PORT_NUM);
        }catch(NetworkException e){
            LOGGER.severe("Network failed to initialize!");
            LOGGER.info("Game is shutting down now");
            System.exit(-1);
        }
        return thisGame;
    }
}
