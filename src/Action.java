import java.io.Serializable;

public class Action implements Serializable {

    int action;
    boolean done;
    Action(int action, boolean done){
        this.action = action;
        this.done = done;
    }

}
