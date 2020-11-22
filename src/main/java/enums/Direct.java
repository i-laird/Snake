package enums;

/**
 * @author Ian Laird
 * {@link Direct}is used to indicate a move direction
 */
public enum Direct {
    DOWN, LEFT, UP, RIGHT, NO_MOVE;

    public static Direct fromNum(int num){
        switch(num){
            case 0:
                return DOWN;
            case 1:
                return LEFT;
            case 2:
                return UP;
            case 3:
                return RIGHT;
            default:
                return NO_MOVE;
        }
    }
}
