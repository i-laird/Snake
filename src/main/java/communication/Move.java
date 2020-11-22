package communication;

import communication.message.Message;
import enums.Direct;

import java.io.Serializable;

public class Move extends Message implements Serializable {
    private Direct direction;

    public Direct getDirection() {
        return direction;
    }

    public void setDirection(Direct direction) {
        this.direction = direction;
    }

    public Move(Direct direction) {
        this.direction = direction;
    }
    public Move(int direction){
        this.direction = Direct.fromNum(direction);
    }
}
