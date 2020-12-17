package old.reporting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Andrew Walker
 * This class holds the data for a specific old.game
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportObject {
    @XmlElement(name = "Player1")
    private String user1;
    @XmlElement(name = "Player2")
    private String user2;
    @XmlElement(name = "Player1Score")
    private int user1score;
    @XmlElement(name = "Player2Score")
    private int user2score;

    /**
     * @author Andrew Walker
     * This constructs a default ReportObject
     */
    public ReportObject() {
        user1 = "";
        user2 = "";
        user1score = 0;
        user2score = 0;
    }

    /**
     * @param user1      the username of player one
     * @param user2      the username of player two
     * @param user1score the score of player one
     * @param user2score the score of player two
     * @author Andrew Walker
     * This is a constructor for generating a real old.game
     */
    public ReportObject(String user1, String user2, int user1score, int user2score) {
        this.user1 = user1;
        this.user2 = user2;
        this.user1score = user1score;
        this.user2score = user2score;
    }

    /**
     * @return the username of player one
     * @author Andrew Walker
     * This function returns the username of player one
     */
    public String getUser1() {
        return user1;
    }

    /**
     * @param user1 the username of player one
     * @author Andrew Walker
     * This function sets the username of player one
     */
    public void setUser1(String user1) {
        this.user1 = user1;
    }

    /**
     * @return the username of player two
     * @author Andrew Walker
     * This function returns the username of player two
     */
    public String getUser2() {
        return user2;
    }

    /**
     * @param user2 the username of player two
     * @author Andrew Walker
     * This function sets the username of player two
     */
    public void setUser2(String user2) {
        this.user2 = user2;
    }

    /**
     * @return the score of player one
     * @author Andrew Walker
     * This function returns the score of player one
     */
    public int getUser1score() {
        return user1score;
    }

    /**
     * @param user1score the score of player one
     * @author Andrew Walker
     * This function sets the score of player one
     */
    public void setUser1score(int user1score) {
        this.user1score = user1score;
    }

    /**
     * @return the score of player two
     * @author Andrew Walker
     * This function returns the score of player two
     */
    public int getUser2score() {
        return user2score;
    }

    /**
     * @param user2score the score of player two
     * @author Andrew Walker
     * This function sets the score of player two
     */
    public void setUser2score(int user2score) {
        this.user2score = user2score;
    }
}
