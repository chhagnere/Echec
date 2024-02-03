package pack;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Plateau {
    private int taille=70;
    public int getTaille() {
        return taille;
    }
    public void dessiner(Graphics g){
        for (int i = 0;i<8;i++){
            for (int j=0;j<8;j++){
                if ((i+j)%2 ==0) {
                    g.setColor(Color.WHITE);
                }
                else {
                    Color clr = new Color(203, 169, 126);
                    g.setColor(clr);
                }
                g.fillRect(j*taille,i*taille,taille,taille);
            }
        }

    }


}
