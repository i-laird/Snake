/**
 * This class should be a singleton b/c only one screen should exist
 */
//TODO Most of this class is unfinished...We need to implement Swing stuff
    //-Ian
public class Screen {
    private int width;
    private int length;
    private static Screen thisInstance = null;

    /**
     * @author: Ian Laird
     * Because singleton the constructor is private
     */
    private Screen(int width, int length)
    {
    }
    public static Screen getInstance(int width, int length)
    {
        return (thisInstance == null ? thisInstance = new Screen(width, length) : thisInstance);
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
    public void colorLocation(int row, int col, Color color){

    }

    /**
     * This function should restore a block to default color indicating that the player no longer covers
     * that space.
     */
    public void unColorLocation(int row, int col){
        //This might not be a necessary function
        //COuld just plot background and redraw snake instead
        //Probably Unncessary
    }

    public void plotWinScreen(){

    }

    public void plotDefeatScreen(){

    }

    public void plotBackground(){

    }

    public void plotPowerUp(int row, int col){

    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.length;
    }

    /**
     *
     * @return direction the player wants to move
     */
    public Direct readMoveFromKeyboard(){
        /*TODO implement so that keystrokes from user are recorded
        This means that the input has to be unbuffered so as to not wait for ENTER
        Unsure how to do this we will have to research -Ian
        */
        return null;
    }
}
