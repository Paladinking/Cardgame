import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Card implements Serializable {

    int color;
    int value;
    int eightColor;
    private static  Image[][] images;

    Card(int color, int value) {
        this.color = color;
        this.value = value;
        this.eightColor = this.color;
    }

    static void loadImages(Image[][] imgs){
        images = imgs;
    }
    void drawCard(Graphics g, int x, int y) {
        g.drawImage(images[eightColor][value-1],x,y,null);

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
