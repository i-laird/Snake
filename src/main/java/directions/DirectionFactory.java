package directions;

import enums.Direct;

/**
 * @author Ian Laird
 * {@link DirectionFactory} is an abstract factory for creating
 * a {@link Direction} based on the given instance of a {@link Direct}.
 */
public class DirectionFactory {
    /**
     * @param d-the indicated {@link Direct}
     * @return the created Direction
     * @author Ian Laird
     * creates the desired {@link Direction}
     */
    public static Direction make(Direct d) {
        switch (d) {
            case DOWN:
                return Down.create();
            case UP:
                return Up.create();
            case LEFT:
                return Left.create();
            case RIGHT:
                return Right.create();
            default:
                return null;
        }
    }
}
