package pack;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Pion extends Piece{
    private ImageIcon image;
    public Pion(){};

    public Pion(Point p, String nom, String couleur, String type){
        super(p,null, couleur, type);
        image = new ImageIcon(nom);
    };

    @Override
    public void dessiner(Graphics g){
        g.drawImage(image.getImage(),this.getCoinSuperieurGauche().x,
                this.getCoinSuperieurGauche().y, 70,70, null);
    }

}
