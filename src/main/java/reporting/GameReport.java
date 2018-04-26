package reporting;

import game.Game;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * @author Andrew Walker
 * This class is a wrapper for generating a game report
 */
@XmlRootElement(name = "report")
public class GameReport {

    private ArrayList<ReportObject> records;

    /**
     * @author Andrew Walker
     * The default constructor for a GameReport
     */
    public GameReport(){
        records = new ArrayList<>();
    }

    /**
     * @author Andrew Walker
     * This function returns the collection of the games
     * @return the collection of the games
     */
    @XmlElement(name = "game")
    public ArrayList<ReportObject> getRecords() {
        return records;
    }

    /**
     * @author Andrew Walker
     * This function sets the collection of the games
     * @param records the collection of the games
     */
    public void setRecords(ArrayList<ReportObject> records) {
        this.records = records;
    }

    /**
     * @author Andrew Walker
     * This function adds a new Game to the record
     * @param g the Game to add to the record
     */
    public void ammend(Game g){
        records.add(new ReportObject(g.getPlayerOneUsername(), g.getPlayerTwoUsername(), g.getPlayerOneScore(), g.getPlayerTwoScore()));
    }
}
