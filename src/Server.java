import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class Server extends Thread {

    private static final int PLAYERS =1;

    private ArrayList<Card> deck;
    //private ArrayList<Card> playerOneHand;
    //private ArrayList<Card> playerTwoHand;
    private ArrayList<Card> putDown;
    private ServerSocket socket;
    private ArrayList<Player> players;
    private int turn = 0;
    static int eightColor = 0;
    private final static String[] COLORS = {"HEARTS", "DIAMONDS", "CLUBS", "SPADES"};
    private boolean strict;
    private int drawn;


    private Server() {
        deck = new ArrayList<>();
        putDown = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) {
                deck.add(new Card(i, j));
            }
        }
        Random rand = new Random();
        for (int i = 0; i < deck.size(); i++) {
            int randomIndexToSwap = rand.nextInt(deck.size());
            Card temp = deck.get(randomIndexToSwap);
            deck.set(randomIndexToSwap, deck.get(i));
            deck.set(i, temp);
        }
        putDown.add(deck.get(0));
        deck.remove(0);

        try {
            socket = new ServerSocket(6066);
            System.out.println(1);
            players = new ArrayList<>();
            for (int i = 0; i < PLAYERS; i++) {
                Socket s = socket.accept();
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                oos.flush();
                ArrayList<Card> hand = new ArrayList<>();
                for(int j=0;j<7;j++){
                    hand.add(deck.get(0));
                    deck.remove(0);
                }
                players.add(new Player(s, ois, oos, hand));
            }
            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        System.out.println(2);
        System.out.println(players.size());
        for (Player p:players){
            System.out.println(p.socket.getInetAddress());
            try {
                p.outputStream.writeObject(p.hand);
                p.outputStream.flush();
                p.outputStream.writeObject(putDown.get(putDown.size()-1));
                p.outputStream.flush();
                ArrayList<Integer> other = new ArrayList<>();
                for (Player player : players) {
                    if (player != p) {
                        other.add(player.hand.size());
                    }
                }
                p.outputStream.writeObject(other);
                p.outputStream.flush();
                p.outputStream.writeBoolean(players.indexOf(p)==turn);
                p.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println(4);
        while (true) {
            try {
                Point p = (Point) players.get(turn).inputStream.readObject();
                System.out.println(p.toString());
                for (int i = 0; i < players.get(turn).hand.size(); i++) {
                    if (new Rectangle(10 + 82 * i, 380, 80, 140).contains(p.getX(), p.getY())) {
                        System.out.println(players.get(turn).hand.get(i).toString());
                        if (players.get(turn).hand.get(i).fits(putDown.get(putDown.size() - 1), strict)) {
                            /*if (players.get(turn).hand.get(i).value == 8) {
                                eightColor = JOptionPane.showOptionDialog(null, "Select a Color",
                                        "Click a button",
                                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, COLORS, COLORS[0]);
                            }*/
                            putDown.add(players.get(turn).hand.get(i));
                            players.get(turn).hand.remove(players.get(turn).hand.get(i));
                            send(players.get(turn));
                            strict = true;
                            boolean can = false;
                            for (Card c : players.get(turn).hand) {
                                if (c.fits(putDown.get(putDown.size() - 1), strict)) can = true;
                            }
                            if (!can) {
                                passTurn();
                            }
                        }

                    }
                }
                if (new Rectangle(100, 200, 80, 140).contains(p.getX(), p.getY())) {
                    if (strict) {
                        passTurn();
                    }
                    boolean can = false;
                    for (Card c : players.get(turn).hand) {
                        if (c.fits(putDown.get(putDown.size() - 1), strict)) can = true;
                    }
                    if (deck.size() == 0) {
                        System.out.println("Blandar om");
                        Card temp = putDown.get(putDown.size() - 1);
                        putDown.remove(putDown.get(putDown.size() - 1));

                        Random rand = new Random();
                        for (int i = 0; i < putDown.size(); i++) {
                            int randomIndexToSwap = rand.nextInt(putDown.size());
                            Card temp2 = putDown.get(randomIndexToSwap);
                            putDown.set(randomIndexToSwap, putDown.get(i));
                            putDown.set(i, temp2);
                        }
                        deck.addAll(putDown);
                        putDown.add(temp);

                    }
                    if (!can) {
                        if (drawn == 3) {
                            passTurn();
                        } else {
                            players.get(turn).hand.add(deck.get(0));
                            deck.remove(0);
                            drawn++;
                            send(players.get(turn));
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {


            }


        }
    }

    private void send(Player player) throws IOException {
        player.outputStream.reset();
        player.outputStream.writeObject(player.hand);
        player.outputStream.reset();
        player.outputStream.writeObject(putDown.get(putDown.size()-1));
        player.outputStream.reset();
        ArrayList<Integer> other = new ArrayList<>();
        for (Player p : players) {
            if (p !=  player) {
                other.add(player.hand.size());
            }
        }
        player.outputStream.writeObject(other);
        player.outputStream.reset();
        player.outputStream.writeBoolean(players.indexOf(player)==turn);
        player.outputStream.reset();
    }

    private void passTurn() throws IOException {
        for (Player p:players){
            for (Card c:p.hand){
                System.out.println(c.toString());
            }
            p.outputStream.reset();
            p.outputStream.writeObject(p.hand);
            p.outputStream.reset();
            p.outputStream.writeObject(putDown.get(putDown.size()-1));
            p.outputStream.reset();
            ArrayList<Integer> other = new ArrayList<>();
            for (Player player : players) {
                if (player != p) {
                    other.add(player.hand.size());
                }
            }
            p.outputStream.writeObject(other);
            p.outputStream.reset();
            p.outputStream.writeBoolean(players.indexOf(p)==turn);
            p.outputStream.reset();
        }
        drawn = 0;
        turn++;
        if (turn >= PLAYERS) turn = 0;
        strict = false;
    }


    public static void main(String[] args) {
        new Server();

    }


}
