import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Card implements Serializable {

    private int color;
    int value;
    String eightColor;

    Card(int color, int value) {
        this.color = color;
        this.value = value;
        this.eightColor = "";
    }


    void drawCard(Graphics g, int x, int y) {
        g.setColor(Color.lightGray);
        g.fillRect(x, y, 80, 140);
        g.setColor(Color.BLACK);
        g.drawString(Game.getColor(color), x + 20, y + 20);
        g.drawString("" + value, x + 25, y + 40);
        g.drawString(eightColor, x + 20, y + 70);
    }


    boolean fits(Card card, boolean strict, int eightColor) {
        if (card.value == 8) {
            if (this.color == eightColor && !strict) return true;
            return this.value == 8 && !strict;
        } else if (!strict) {
            if (this.color == card.color) return true;
            if (this.value == card.value) return true;
            return (this.value == 8);
        } else {
            return this.value == card.value;
        }
    }
    void move(ArrayList<Card> from, ArrayList<Card> to){
        to.add(this);
        from.remove(this);
    }

    @Override
    public String toString() {
        return "Card[value=" + value + ",color=" + color + "]";
    }

}
