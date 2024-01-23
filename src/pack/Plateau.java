package pack;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
//créer une liste de case
public class Plateau {
    private Piece[][] matrice;
    private int taille=70;


    public Plateau(){
        matrice = new Piece[8][8];
    };

    public int getTaille() {
        return taille;
    }

    public void dessiner(Graphics g){
        int taille= 70;
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
