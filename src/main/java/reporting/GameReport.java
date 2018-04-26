package game;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

//@XmlRootElement(namespace = "edu.baylor.ecs.si.a1")
//@XmlRootElement(name = "report")
//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "report")
public class GameReport {

    private ArrayList<ReportObject> records;

    public GameReport(){
        records = new ArrayList<>();
    }

    //@XmlElementWrapper(name = "records")
    @XmlElement(name = "game")
    public ArrayList<ReportObject> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<ReportObject> records) {
        this.records = records;
    }

    public void ammend(Game g){
        records.add(new ReportObject(g.getUsername(), g.getPlayer2Username(), g.getPlayerOneScore(), g.getPlayerTwoScore()));
    }
}
