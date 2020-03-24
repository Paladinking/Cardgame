import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends JFrame implements MouseListener {


    private Main(String s) {
        this.setFocusable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(new Menu(s));
        this.pack();
        this.setLocationRelativeTo(null);
        this.addMouseListener(this);
        this.setTitle("Kortspel");
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            String s;
            if (args.length > 0) {
                s = args[0];
            } else {
                s = "";
            }
            new Main(s).setVisible(true);
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (new Rectangle(this.getWidth() / 2 - 50 + this.getComponent(0).getX(), 200 + this.getComponent(0).getY(), 100, 40).contains(e.getX(), e.getY())) {
            int port = 0;
            String ip;
            JTextField portField = new JTextField(5);
            JTextField ipField = new JTextField(5);
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Port:"));
            myPanel.add(portField);
            myPanel.add(Box.createHorizontalStrut(15));
            myPanel.add(new JLabel("Ip:"));
            myPanel.add(ipField);
            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Join", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    port = Integer.valueOf(portField.getText());
                } catch (NumberFormatException ignored) {

                }
                ip = ipField.getText();
                if(port!=0&&ip!=null) {
                    Game.start(port, ip);
                    this.setVisible(false);
                    this.dispose();
                }
            }

        } else if (new Rectangle(this.getWidth() / 2 - 50 + this.getComponent(0).getX(), 300 + this.getComponent(0).getY(), 100, 40).contains(e.getX(), e.getY())) {
            int port = 0;
            int players = 0;
            JTextField portField = new JTextField(5);
            JTextField playersField = new JTextField(5);
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Port:"));
            myPanel.add(portField);
            myPanel.add(Box.createHorizontalStrut(15));
            myPanel.add(new JLabel("Players:"));
            myPanel.add(playersField);
            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Host", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    port = Integer.valueOf(portField.getText());
                    players = Integer.valueOf(playersField.getText());
                } catch (NumberFormatException ignored) {

                }
                if(port!=0&&players!=0) {
                    int finalPort = port;
                    int finalPlayers = players;
                    new Thread(() -> new Server(finalPort,finalPlayers)).start();
                    Game.start(port, "localhost");
                    this.setVisible(false);
                    this.dispose();
                }
            }
        }
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

class Menu extends JPanel {
    private final String s;

    Menu(String s) {
        this.setPreferredSize(new Dimension(800, 600));
        repaint();
        this.s = s;
    }


    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.orange);
        g.fillRect(350, 200, 100, 40);
        g.fillRect(this.getWidth() / 2 - 50, 300, 100, 40);
        g.setColor(Color.BLACK);
        g.drawString("Join", this.getWidth() / 2 - 20, 220);
        g.drawString("Host", this.getWidth() / 2 - 20, 320);
        g.setFont(new Font("jeff", Font.PLAIN, 20));
        g.drawString(s, 100, 100);
    }


}
