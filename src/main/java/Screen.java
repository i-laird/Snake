/**
 * This class should be a singleton b/c only one screen should exist
 */
public class Screen {
    private static Screen thisInstance = new Screen();

    /**
     * @author: Ian Laird
     * Because singleton the constructor is private
     */
    private Screen()
    {
    }
    public static Screen getInstance()
    {
        return thisInstance;
    }

    /**
     * @author: Ian Laird
     * This function will simply update the game screen. It itself does not change.
     */
    public void updateScreen()
    {

    }

    /**
     * this function should color the rectangle at a certain location in the game the desired color.
     * This indicates that the player currently has visited there
     */
    public void colorLocation(){

    }

    /**
     * This function should restore a block to default color indicating that the player no longer covers
     * that space.
     */
    public void unColorLocation(){

    }
}
