package Directions;

import Enums.Direct;

public class DirectionFactory {
    public static Direction make(Direct d){
        switch(d){
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
