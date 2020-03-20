import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Card implements Serializable {
    public static final int HEARTS= 0, DIAMONDS = 1, CLUBS = 2, SPADES = 3;

    int color, value;

    Card(int color, int value){
        this.color = color;
        this.value = value;
    }



    public void drawCard(Graphics g, int x, int y){
        g.setColor(Color.lightGray);
        g.fillRect(x,y,80,140);
        g.setColor(Color.BLACK);
        g.drawString(Game.getColor(color),x+20,y+20);
        g.drawString(""+value,x+25,y+40);
    }


    public boolean fits(Card card, boolean strict) {
        if(card.value==8){
           // return  this.color == Game.eightColor&&!strict;
            if (this.color == card.color) return true;
            return this.value == card.value;
        } else if(!strict) {
            if (this.color == card.color) return true;
            if (this.value == card.value) return true;
            return (this.value == 8);
        } else {
            return this.value == card.value;
        }
    }
    @Override
    public String toString(){
        return "Card[value="+value+",color="+color+"]";
    }

}
