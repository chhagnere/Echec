package pack;

import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {
    private Point coinSuperieurGauche;
    private String nom;
    private String couleur;
    private String type;

    public Piece() {
        coinSuperieurGauche = new Point(0,0);
    }

    public Piece(Point p, String n, String c, String t){
        coinSuperieurGauche=p;
        nom=n;
        couleur=c;
        type=t;
    };

    public Point getCoinSuperieurGauche() {return coinSuperieurGauche;}
    public void setCoinSuperieurGauche(Point p){coinSuperieurGauche=p;}
    public String getCouleur(){return couleur;}
    public String getType(){return type;}

    public abstract void dessiner(Graphics g);


}
