import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class Player {

    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    ArrayList<Card> hand;


    Player(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream,ArrayList<Card> hand){
        this.outputStream = objectOutputStream;
        this.inputStream = objectInputStream;
        this.hand = hand;

    }

}
