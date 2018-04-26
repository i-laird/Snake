package game;

import javax.xml.bind.annotation.*;

//@XmlRootElement(name = "game")
//@XmlType(propOrder = {"user1", "user2", "user1score", "user2score"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportObject {
    @XmlElement(name = "Player 1")
    private String user1;
    @XmlElement(name = "Player 2")
    private String user2;
    @XmlElement(name = "Player 1 Score")
    private int user1score;
    @XmlElement(name = "Player 2 Score")
    private int user2score;

    public ReportObject(){
        user1 = "";
        user2 = "";
        user1score = 0;
        user2score = 0;
    }

    public ReportObject(String user1, String user2, int user1score, int user2score) {
        this.user1 = user1;
        this.user2 = user2;
        this.user1score = user1score;
        this.user2score = user2score;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public int getUser1score() {
        return user1score;
    }

    public void setUser1score(int user1score) {
        this.user1score = user1score;
    }

    public int getUser2score() {
        return user2score;
    }

    public void setUser2score(int user2score) {
        this.user2score = user2score;
    }
}
