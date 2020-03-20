import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Player {

    Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    ArrayList<Card> hand;


    Player(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream,ArrayList<Card> hand){
        this.socket = socket;
        this.outputStream = objectOutputStream;
        this.inputStream = objectInputStream;
        this.hand = hand;

    }

}
