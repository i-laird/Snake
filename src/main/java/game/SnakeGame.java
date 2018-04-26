package game;

import display.Initializer;
import exceptions.NetworkError;
import reporting.GameReport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Andrew Walker
 * This class is the implementation of a SnakeGame
 */
public class SnakeGame {

    public final static int PORT_NUM = 2000;
    private static final Logger LOGGER = Logger.getLogger("command.SnakeRunner Class");

    /**
     * @author Andrew Walker
     * This function starts the game
     */
    public void startGame() {
        boolean isServer;
        String host;
        String username;

        //Initializer i = new Initializer();
        Initializer.startModal();
        isServer = Initializer.getIsServer();
        host = Initializer.getHost();
        username = Initializer.getUsername();
        LOGGER.info("Generated Game Data");

        //This creates the Screen in Game makeScreen
        LOGGER.info("Generating Game");
        Game ourGame =  GameMaker.generateGame(isServer);
        ourGame.setPlayerOneUsername(username);

        GameReport gameReport = null;
        JAXBContext context = null;
        FileReader readFrom = null;
        try {
            context = JAXBContext.newInstance(GameReport.class);
            Unmarshaller um = context.createUnmarshaller();
            File toRead = new File("./gameReport.xml");
            if(toRead.exists()) {
                readFrom = new FileReader(toRead);
                gameReport = (GameReport) um.unmarshal(readFrom);
            }
            else{
                gameReport = new GameReport();
            }
        } catch (FileNotFoundException e){
            LOGGER.severe("FIle not found");
        }catch(JAXBException f){
            LOGGER.severe("JAXB error caught");
        }finally{
            try {
                if (readFrom != null) {
                    readFrom.close();
                }
            }catch(IOException e){}
        }

        try {
            LOGGER.info("Initializing Game");
            if(ourGame.initConnection(host, PORT_NUM))
                Initializer.close();
        } catch(NetworkError e){
            LOGGER.severe("Network failed to initialize!");
            LOGGER.info("Game is shutting down now");
            throw new RuntimeException();
        }

        try{
            ourGame.initGame();
        }
        catch(IOException f){
            LOGGER.severe("Network write/ read error");
            LOGGER.info("Game is shutting down now");
            throw new RuntimeException();
        }

        //Save the game state
        GameRecord initialGame = ourGame.createRecord();
        try {
            do {
                while(!ourGame.hasBegun()){
                    Thread.sleep(250);
                }
                while (!ourGame.isGameOver()) {
                    ourGame.movePlayers();
                    Thread.sleep(500);
                }
                gameReport.ammend(ourGame);
            } while (ourGame.playAgain(initialGame));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe("Exception occurred while playing the game: " + e.getMessage());
        }

        try{
            context = JAXBContext.newInstance(GameReport.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(gameReport, new File("./gameReport.xml"));
        } catch(JAXBException e){
            e.printStackTrace();
        }
    }
}
