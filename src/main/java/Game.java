public abstract class Game {
    protected Snake playerOne;
    protected Snake playerTwo;
    protected Screen gameScreen;
    private boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

    public void playAgain() {
        this.gameOver = false;
    }

    public abstract Game getGame();




}
