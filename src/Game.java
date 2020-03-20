import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Game extends JPanel implements MouseListener {
   /* private ArrayList<Card> deck;
    private ArrayList<Card> playerOneHand;
    private ArrayList<Card> playerTwoHand;
    private ArrayList<Card> putDown;
    private final static String[] COLORS = {"HEARTS","DIAMONDS","CLUBS","SPADES"};

    private String turn = "PLAYER ONE";
    private int drawn = 0;
    static int eightColor = 0;

    */
    private boolean strict;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private ArrayList<Card> hand;
    private Card putDown;
    private ArrayList<Integer> theirHands;
    private boolean myTurn = false;



    private Game() {
        System.out.println(3);
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        this.addMouseListener(this);
        try {
            socket = new Socket("localhost",6066);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();

            hand = (ArrayList<Card>) inputStream.readObject();
            putDown = (Card) inputStream.readObject();
            theirHands = (ArrayList<Integer>) inputStream.readObject();
            myTurn = inputStream.readBoolean();
            new Thread(() -> {
                while (true){
                    try {
                        System.out.println("hej");
                        hand = (ArrayList<Card>) inputStream.readObject();
                        for (Card c: hand) {
                            System.out.println(c.toString());
                        }
                        putDown = (Card) inputStream.readObject();
                        System.out.println(putDown.toString());
                        theirHands = (ArrayList<Integer>) inputStream.readObject();
                        myTurn = inputStream.readBoolean();
                    } catch (IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                    repaint();
                }
            }).start();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            System.out.println(1);
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.add(new Game());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setTitle("Vändåtta");
            f.setVisible(true);
            System.out.println(2);

        });

    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(100, 200, 80, 140);
        putDown.drawCard(g, 200, 200);
        /*if(putDown.value==8){
            g.drawString(getColor(eightColor),225,260);
        }*/
        //g.drawString(turn, 10, 10);
        for (int i = 0; i < hand.size(); i++) {
            hand.get(i).drawCard(g, 10 + 82 * i, 380);
        }

    }

    static String getColor(int c) {
        switch (c) {
            case 0:
                return "Hearts";
            case 1:
                return "Diamonds";
            case 2:
                return "Clubs";
            case 3:
                return "Spades";
        }
        return "";
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(myTurn);
        if(myTurn){
            try {
                outputStream.writeObject(new Point(e.getX(),e.getY()));
                outputStream.flush();
                outputStream.reset();

            } catch (IOException e1) {
                e1.printStackTrace();
            }


        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
