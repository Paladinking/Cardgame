import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Game extends JPanel implements MouseListener {

    private final static String[] COLORS = {"HEARTS", "DIAMONDS", "CLUBS", "SPADES"};

    private int eightColor = 0;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private ArrayList<Card> hand;
    private Card putDown;
    private ArrayList<Integer> theirHands;
    private boolean myTurn = false;
    private int myPos;
    static final BufferedImage[][] images;

    /**
     * @noinspection InfiniteLoopStatement
     */
    @SuppressWarnings("unchecked")
     Game() {
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocus();
        this.addMouseListener(this);
        try {
            Socket socket = new Socket("localhost", 6066);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();

            hand = (ArrayList<Card>) inputStream.readObject();
            putDown = (Card) inputStream.readObject();
            theirHands = (ArrayList<Integer>) inputStream.readObject();
            myTurn = inputStream.readBoolean();
            myPos = inputStream.read();
            new Thread(() -> {
                while (true) {
                    try {
                        hand = (ArrayList<Card>) inputStream.readObject();
                        putDown = (Card) inputStream.readObject();
                        theirHands = (ArrayList<Integer>) inputStream.readObject();
                        myTurn = inputStream.readBoolean();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    repaint();
                }
            }).start();
        } catch (IOException | ClassNotFoundException e) {
            this.setVisible(false);
            Main.main(new String[]{"Could not Connect"});
        }
    }

    static {
        images = new BufferedImage[4][13];
        try {
            BufferedImage h, d, c, s;
            h = ImageIO.read(Game.class.getResource("assets/hearts.png"));
            d = ImageIO.read(Game.class.getResource("assets/diamonds.png"));
            c = ImageIO.read(Game.class.getResource("assets/clubs.png"));
            s = ImageIO.read(Game.class.getResource("assets/spades.png"));
            for (int i = 0; i < 13; i++) {

                BufferedImage h2 = h.getSubimage(80 * (i % 7), 120 * (i / 7), 80, 120);
                BufferedImage d2 = d.getSubimage(80 * (i % 7), 120 * (i / 7), 80, 120);
                BufferedImage c2 = c.getSubimage(80 * (i % 7), 120 * (i / 7), 80, 120);
                BufferedImage s2 = s.getSubimage(80 * (i % 7), 120 * (i / 7), 80, 120);
                images[0][i] = h2;
                images[1][i] = d2;
                images[2][i] = c2;
                images[3][i] = s2;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static void start() {
        EventQueue.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.add(new Game());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setTitle("Vändåtta");
            f.setVisible(true);
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(0, 237, 255));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(100, 200, 80, 120);
        putDown.drawCard(g, 200, 200);
        for (int i = 0; i < hand.size(); i++) {
            hand.get(i).drawCard(g, 10 + 82 * i, 380);
        }
        g.setColor(Color.red);
        if (myTurn) g.setColor(Color.green);
        g.fillRect(500, 20, 40, 40);
        g.setColor(Color.black);
        for (int i = 0; i < theirHands.size(); i++) {
            g.drawString("PLAYER " + (i + 1) + ": " + theirHands.get(i) + " card(s) left", 40, 80 + 20 * i);
        }
        g.setFont(new Font("jeff", Font.PLAIN, 20));
        g.drawString("PLAYER " + (myPos + 1), 40, 40);
        if (hand.size() == 0) {
            g.drawString("You Won", 120, 120);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (hand.size() > 1) {
            for (int i = 0; i < hand.size(); i++) {
                if (new Rectangle(10 + 82 * i, 380, 80, 140).contains(e.getX(), e.getY()) && hand.get(i).value == 8) {
                    boolean can = false;
                    for (Card c : hand) {
                        if (c != hand.get(i)) {
                            if (c.fits(putDown, false, 0)) can = true;
                        }
                    }
                    if (!can) {
                        int temp = JOptionPane.showOptionDialog(null, "Select a Color",
                                "Click a button",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, COLORS, COLORS[0]);
                        if (temp != -1) {
                            eightColor = temp;
                        }
                    }
                }
            }
        }
        if (myTurn) {
            try {
                outputStream.writeObject(new Point(e.getX(), e.getY()));
                outputStream.flush();
                outputStream.reset();
                outputStream.write(eightColor);
                outputStream.flush();
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
