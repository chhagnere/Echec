package pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Graphics;
import java.sql.*;


//régler pour tour, fou, et reine qu'elle peut pas manger si un pion est sur sa trajectoire !
// pawn peut pas manger tout droit !
// affichage du score
// cas d'échec


public class FenetrePrincipale extends JFrame{
    private JPanel myContentPane;
    private Plateau ChessBoard ;
    private ArrayList<Piece> listePion = new ArrayList<Piece>();
    private Piece selectedPiece;
    private Point selectedPoint;
    private JButton bouton_score;
    private String joueur="white";
    private int Score = 0;
    private JLabel texte_score;



    public FenetrePrincipale(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setVisible(true);
        setBounds(100, 100, 800, 600);
        setTitle("Jeu d'échec");
        setLocationRelativeTo(null);

        ChessBoard = new Plateau();

        myContentPane = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                ChessBoard.dessiner(g);
                dessiner(g);
            }
        };
        myContentPane.setLayout(null);
        texte_score = new JLabel("Score : " );
        texte_score.setBounds(600,150,100,20);
        myContentPane.add(texte_score);

        this.setContentPane(myContentPane);

        int taille=ChessBoard.getTaille();
        listePion.add(new Pion(new Point(0, 0), "black-rook.png","black","rook"));
        listePion.add(new Pion(new Point(taille*6, 0), "black-knight.png","black","knight"));
        listePion.add(new Pion(new Point(taille*7, 0), "black-rook.png","black","rook"));
        listePion.add(new Pion(new Point(taille, 0), "black-knight.png","black","knight"));
        listePion.add(new Pion(new Point(taille*2, 0), "black-bishop.png","black","bishop"));
        listePion.add(new Pion(new Point(taille*5, 0), "black-bishop.png","black","bishop"));
        listePion.add(new Pion(new Point(taille*3, 0), "black-queen.png","black","queen"));
        listePion.add(new Pion(new Point(taille*4, 0), "black-king.png","black","king"));
        listePion.add(new Pion(new Point(0, 7*taille), "white-rook.png","white","rook"));
        listePion.add(new Pion(new Point(taille*7, 7*taille), "white-rook.png","white","rook"));
        listePion.add(new Pion(new Point(taille, 7*taille), "white-knight.png","white","knight"));
        listePion.add(new Pion(new Point(taille*6, 7*taille), "white-knight.png","white","knight"));
        listePion.add(new Pion( new Point(taille*2, 7*taille), "white-bishop.png","white","bishop"));
        listePion.add(new Pion(new Point(taille*5, 7*taille), "white-bishop.png","white","bishop"));
        listePion.add(new Pion(new Point(taille*3, 7*taille), "white-queen.png","white","queen"));
        listePion.add(new Pion(new Point(taille*4, 7*taille), "white-king.png","white","king"));

        for (int i=0;i<8;i++){
            listePion.add(new Pion(new Point(i*taille, taille), "black-pawn.png","black","pawn"));
            listePion.add(new Pion(new Point(i*taille, 6*taille), "white-pawn.png","white","pawn"));
        }

        myContentPane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                formMouseClicked(e);
                myContentPane.repaint();}

            @Override
            public void mousePressed(MouseEvent e) {    }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) {    }

            @Override
            public void mouseExited(MouseEvent e) {   }
        });

        bouton_score=new JButton("Enregistrer");
        bouton_score.setBounds(600,100,100,40);
        myContentPane.add(bouton_score);
        bouton_score.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrer(e);
            }
        });

    }

    public void dessiner(Graphics g){
        ArrayList<Point> bouger = new ArrayList<Point>();
        if (listePion != null)
            for (Piece p : listePion)
                p.dessiner(g);

        if (selectedPiece!=null){
            g.setColor(Color.black);
            g.drawRect(selectedPiece.getCoinSuperieurGauche().x,selectedPiece.getCoinSuperieurGauche().y, ChessBoard.getTaille(), ChessBoard.getTaille());

            switch (selectedPiece.getType()){
                case "pawn": //a faire
                    bouger=pawn_deplacement(selectedPiece,selectedPoint);
                    break;
                case "rook" :
                    bouger=rook_deplacement(selectedPiece,selectedPoint);
                    break;
                case "bishop":
                    bouger = bishop_deplacement(selectedPiece,selectedPoint);
                    break;
                case "queen":
                    bouger=queen_deplacement(selectedPiece,selectedPoint);
                    break;
                case "king":
                    bouger= king_deplacement(selectedPiece,selectedPoint);
                    break;
                case "knight":
                    bouger=knight_deplacement(selectedPiece,selectedPoint);
                    break;
            }
            for (Point point_liste: bouger){
                g.setColor(Color.green);
                g.drawOval(point_liste.x+15,point_liste.y+15, 40, 40);
            }
        }
    }

    private void formMouseClicked(MouseEvent evt) {
        // On récupere les coordonnées du pointeur de la souris dans la fenêtre
        int taille = 70;
        int sourisX = evt.getPoint().x / taille;
        int sourisY = evt.getPoint().y / taille;
        //int i=0;

        if (sourisX > 7 || sourisY > 7) {
            return;
        }

        if (!listePion.isEmpty()){
            for (Piece p :listePion){
                if (p.getCouleur()==joueur){
                    if (sourisX == (p.getCoinSuperieurGauche().x/taille) && sourisY == (p.getCoinSuperieurGauche().y/taille)) {
                        selectedPiece = p;
                        break;
                    }
                }
                else{
                    if (selectedPiece != null){
                        selectedPoint=new Point(sourisX * taille, sourisY * taille);
                        deplacerPiece(selectedPiece, selectedPoint);
                        selectedPiece = null;
                        break;
                    }
                }
            }
        }
    }

    private void deplacerPiece(Piece p, Point t){
        int i=0;
        Piece pion_present=null;
        int j=-1; //-1 : pas de déplacement, i : déplacement + pion à supprimer
        int ok=0;
        int taille= ChessBoard.getTaille();
        ArrayList<Point> bouger = new ArrayList<Point>();

        switch (p.getType()){
            case "pawn": //a faire
                bouger=pawn_deplacement(p,t);
                break;
            case "rook" :
                bouger=rook_deplacement(p,t);
                break;
            case "bishop":
                bouger = bishop_deplacement(p,t);
                break;
            case "queen":
                bouger=queen_deplacement(p,t);
                break;
            case "king":
                bouger= king_deplacement(p,t);
                break;
            case "knight":
                bouger=knight_deplacement(p,t); // OK tout bon !
                break;
        }

        for (Point point_new: bouger){
            if ((t.x== point_new.x) && (t.y== point_new.y)){
                ok=1; // point dans la liste de déplacement possible
            }
        }
        if (ok==1){
            for (Piece piece :listePion) {
                if (piece != p) {
                    if ((piece.getCoinSuperieurGauche().x == t.x) && (piece.getCoinSuperieurGauche().y == t.y)) {
                        j = i;
                        if (piece.getCouleur() != p.getCouleur()) { //pion de la couleur opposée
                            pion_present = piece; //il y a déjà un pion sur la case
                        }
                    }
                }
                i = i + 1;
            }
        }

        if ((pion_present!=null) && (j!=-1)){ // cas où pion déjà dans la case
            listePion.remove(j);
            p.setCoinSuperieurGauche(t);
            if (joueur=="white"){
                joueur="black";
            }
            else {joueur="white";}
            Score=calcul_score(Score);
        }
        else if ((pion_present==null) && (j==-1) && (ok==1)){ // cas où pas de pion dans la case
            p.setCoinSuperieurGauche(t);
            if (joueur=="white"){
                joueur="black";
            }
            else {joueur="white";}
            Score=calcul_score(Score);
        }

    }


    //renvoie la liste des cases où le pion peut aller
    public ArrayList<Point> rook_deplacement(Piece p, Point t) {
        int taille = ChessBoard.getTaille();
        ArrayList<Point> case_possible = new ArrayList<Point>();
        int x_min=7;
        int x=7;
        int y_min=7;
        int y=7;

        // cas horizontal
        for (int i=p.getCoinSuperieurGauche().x/taille;i>=0;i--){
            for (Piece piece : listePion) {
                if (piece.getCoinSuperieurGauche().x != p.getCoinSuperieurGauche().x) {
                    if ((piece.getCoinSuperieurGauche().x == i * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y)) {
                        case_possible.add(new Point(p.getCoinSuperieurGauche().x - i * taille, p.getCoinSuperieurGauche().y));
                    }
                }
            }
        }
        for (int i=p.getCoinSuperieurGauche().x/taille;i<=7;i++){
            for (Piece piece : listePion) {
                if (piece.getCoinSuperieurGauche().x != p.getCoinSuperieurGauche().x) {
                    if ((piece.getCoinSuperieurGauche().x == i * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y)) {
                        case_possible.add(new Point(p.getCoinSuperieurGauche().x + i * taille, p.getCoinSuperieurGauche().y));
                    }
                }
            }
        }

        // cas vertical
        for (int i=p.getCoinSuperieurGauche().y/taille;i>=0;i--){
            for (Piece piece : listePion) {
                if (piece.getCoinSuperieurGauche().y != p.getCoinSuperieurGauche().y) {
                    if ((piece.getCoinSuperieurGauche().y == i * taille) && (piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x)) {
                        case_possible.add(new Point(p.getCoinSuperieurGauche().x, p.getCoinSuperieurGauche().y- i * taille));
                    }
                }
            }
        }
        for (int i=p.getCoinSuperieurGauche().y/taille;i<=7;i++){
            for (Piece piece : listePion) {
                if (piece.getCoinSuperieurGauche().y != p.getCoinSuperieurGauche().y) {
                    if ((piece.getCoinSuperieurGauche().y == i * taille) && (piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x)) {
                        case_possible.add(new Point(p.getCoinSuperieurGauche().x , p.getCoinSuperieurGauche().y+ i * taille));
                    }
                }
            }
        }

        return case_possible;
    }

    public ArrayList<Point> bishop_deplacement(Piece p, Point t){
        int taille= ChessBoard.getTaille();
        ArrayList<Point> case_possible = new ArrayList<Point>();

        int maxi=Math.max(p.getCoinSuperieurGauche().x,p.getCoinSuperieurGauche().y);
        int mini=Math.min(p.getCoinSuperieurGauche().x,p.getCoinSuperieurGauche().y);
        for (int i=1; i<=mini/taille;i++) { // diagonale en haut à gauche => ok
            case_possible.add(new Point(p.getCoinSuperieurGauche().x - i * taille, p.getCoinSuperieurGauche().y - i * taille));
        }
        for (int i=1; i<=7-maxi/taille;i++) { //diagonale en bas à droite => ok
            case_possible.add(new Point(p.getCoinSuperieurGauche().x + i * taille, p.getCoinSuperieurGauche().y + i * taille));
        }
        for (int j=1; j<=7-p.getCoinSuperieurGauche().y/taille;j++) { //diagonale en bas à gauche => ok
            case_possible.add(new Point(p.getCoinSuperieurGauche().x - j * taille, p.getCoinSuperieurGauche().y + j * taille));
        }
        for (int j=1; j<=7-p.getCoinSuperieurGauche().x/taille;j++) { //diagonale en haut à droite => ok
            case_possible.add(new Point(p.getCoinSuperieurGauche().x + j * taille, p.getCoinSuperieurGauche().y - j * taille));
        }
        return case_possible;
    }

    public ArrayList<Point> queen_deplacement(Piece p, Point t){
        int taille= ChessBoard.getTaille();
        ArrayList<Point> case_possible = new ArrayList<Point>();

        int maxi=Math.max(p.getCoinSuperieurGauche().x,p.getCoinSuperieurGauche().y);
        int mini=Math.min(p.getCoinSuperieurGauche().x,p.getCoinSuperieurGauche().y);
        for (int i=1; i<=mini/taille;i++) { // diagonale en haut à gauche => ok
            case_possible.add(new Point(p.getCoinSuperieurGauche().x - i * taille, p.getCoinSuperieurGauche().y - i * taille));
        }
        for (int i=1; i<=7-maxi/taille;i++) { //diagonale en bas à droite => ok
            case_possible.add(new Point(p.getCoinSuperieurGauche().x + i * taille, p.getCoinSuperieurGauche().y + i * taille));
        }

        for (int j=1; j<=7-p.getCoinSuperieurGauche().y/taille;j++) { //diagonale en bas à gauche => ok
            case_possible.add(new Point(p.getCoinSuperieurGauche().x - j * taille, p.getCoinSuperieurGauche().y + j * taille));
        }
        for (int j=1; j<=7-p.getCoinSuperieurGauche().x/taille;j++) { //diagonale en haut à droite => ok
            case_possible.add(new Point(p.getCoinSuperieurGauche().x + j * taille, p.getCoinSuperieurGauche().y - j * taille));
        }

        // verticale + horizontale
        for (int i=0; i<=7;i++) {
            if (i != p.getCoinSuperieurGauche().x) {
                case_possible.add(new Point(i * taille, p.getCoinSuperieurGauche().y));
            }
            if (i != p.getCoinSuperieurGauche().y) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x, i * taille));
            }
        }
        return case_possible;
    }

    public ArrayList<Point> king_deplacement(Piece p, Point t){
        int taille= ChessBoard.getTaille();
        ArrayList<Point> case_possible = new ArrayList<Point>();

        case_possible.add(new Point(p.getCoinSuperieurGauche().x, p.getCoinSuperieurGauche().y - taille));
        case_possible.add(new Point(p.getCoinSuperieurGauche().x - taille, p.getCoinSuperieurGauche().y));
        case_possible.add(new Point(p.getCoinSuperieurGauche().x - taille, p.getCoinSuperieurGauche().y - taille));
        if (p.getCoinSuperieurGauche().x>6*taille) {
            if (p.getCoinSuperieurGauche().y<=6*taille){
                case_possible.add(new Point(p.getCoinSuperieurGauche().x, p.getCoinSuperieurGauche().y + taille));
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - taille, p.getCoinSuperieurGauche().y + taille));
            }
        }
        else {
            case_possible.add(new Point(p.getCoinSuperieurGauche().x + taille, p.getCoinSuperieurGauche().y));
            case_possible.add(new Point(p.getCoinSuperieurGauche().x + taille, p.getCoinSuperieurGauche().y - taille));
            if (p.getCoinSuperieurGauche().y <= 6 * taille) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + taille, p.getCoinSuperieurGauche().y + taille));
                case_possible.add(new Point(p.getCoinSuperieurGauche().x, p.getCoinSuperieurGauche().y + taille));
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - taille, p.getCoinSuperieurGauche().y + taille));
            }
        }
        return case_possible;
    }

    public ArrayList<Point> knight_deplacement(Piece p, Point t) {
        int taille = ChessBoard.getTaille();
        ArrayList<Point> case_possible = new ArrayList<Point>();
        int a1=0,a2=0,a3=0,a4=0,a5=0,a6=0,a7=0,a8=0;

        for (Piece piece:listePion){
            if (piece.getCouleur()==joueur) { // signe != normalement mais jsp pourquoi ça met vrai alors qu'on a white et black
                if ((piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x - 2 * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y - 1 * taille)) {
                    a1 = 1;
                } else if ((piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x - 1 * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y - 2 * taille)) {
                    a2 = 1;
                } else if ((piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x + 2 * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y - 1 * taille)) {
                    a3 = 1;
                } else if ((piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x + 1 * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y - 2 * taille)) {
                    a4 = 1;
                } else if ((piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x - 2 * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y + 1 * taille)) {
                    a5 = 1;
                } else if ((piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x - 1 * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y + 2 * taille)) {
                    a6 = 1;
                } else if ((piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x + 2 * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y + 1 * taille)) {
                    a7 = 1;
                } else if ((piece.getCoinSuperieurGauche().x == p.getCoinSuperieurGauche().x + 1 * taille) && (piece.getCoinSuperieurGauche().y == p.getCoinSuperieurGauche().y + 2 * taille)) {
                    a8 = 1;
                }
            }
        }

        if (a1==0){
            case_possible.add(new Point(p.getCoinSuperieurGauche().x - 2 * taille, p.getCoinSuperieurGauche().y - 1 * taille));
        }
        if (a2==0){
            case_possible.add(new Point(p.getCoinSuperieurGauche().x - 1 * taille, p.getCoinSuperieurGauche().y - 2 * taille));
        }

        if ((p.getCoinSuperieurGauche().x < 6 * taille) && (p.getCoinSuperieurGauche().y < 6 * taille)) {
            if (a3 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 2 * taille, p.getCoinSuperieurGauche().y - 1 * taille));
            }
            if (a4 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 1 * taille, p.getCoinSuperieurGauche().y - 2 * taille));
            }
            if (a5 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 2 * taille, p.getCoinSuperieurGauche().y + 1 * taille));
            }
            if (a6 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 1 * taille, p.getCoinSuperieurGauche().y + 2 * taille));
            }
            if (a7 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 2 * taille, p.getCoinSuperieurGauche().y + 1 * taille));
            }
            if (a8 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 1 * taille, p.getCoinSuperieurGauche().y + 2 * taille));
            }
        }

        else if ((p.getCoinSuperieurGauche().x == 6 * taille) && (p.getCoinSuperieurGauche().y < 6 * taille)) {
            if (a4 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 1 * taille, p.getCoinSuperieurGauche().y - 2 * taille));
            }
            if (a5 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 2 * taille, p.getCoinSuperieurGauche().y + 1 * taille));
            }
            if (a6 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 1 * taille, p.getCoinSuperieurGauche().y + 2 * taille));
            }
            if (a8 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 1 * taille, p.getCoinSuperieurGauche().y + 2 * taille));
            }
        }

        else if ((p.getCoinSuperieurGauche().x > 6 * taille) && (p.getCoinSuperieurGauche().y < 6 * taille)) {
            if (a6 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 1 * taille, p.getCoinSuperieurGauche().y + 2 * taille));
            }
            if (a5 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 2 * taille, p.getCoinSuperieurGauche().y + 1 * taille));
            }
        }

        else if ((p.getCoinSuperieurGauche().x < 6 * taille) && (p.getCoinSuperieurGauche().y == 6 * taille)) {
            if (a3 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 2 * taille, p.getCoinSuperieurGauche().y - 1 * taille));
            }
            if (a4 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 1 * taille, p.getCoinSuperieurGauche().y - 2 * taille));
            }
            if (a5 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 2 * taille, p.getCoinSuperieurGauche().y + 1 * taille));
            }
            if (a7 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 2 * taille, p.getCoinSuperieurGauche().y + 1 * taille));
            }
        }

        else if ((p.getCoinSuperieurGauche().x < 6 * taille) && (p.getCoinSuperieurGauche().y > 6 * taille)) {
            if (a3 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 2 * taille, p.getCoinSuperieurGauche().y - 1 * taille));
            }
            if (a4 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 1 * taille, p.getCoinSuperieurGauche().y - 2 * taille));
            }
        }

        else if ((p.getCoinSuperieurGauche().x == 6 * taille) && (p.getCoinSuperieurGauche().y == 6 * taille)) {
            if (a4 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 1 * taille, p.getCoinSuperieurGauche().y - 2 * taille));
            }
            if (a5 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 2 * taille, p.getCoinSuperieurGauche().y + 1 * taille));
            }
        }

        else if ((p.getCoinSuperieurGauche().x == 6 * taille) && (p.getCoinSuperieurGauche().y > 6 * taille)) {
            if (a4 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x + 1 * taille, p.getCoinSuperieurGauche().y - 2 * taille));
            }
        }

        else if ((p.getCoinSuperieurGauche().x > 6 * taille) && (p.getCoinSuperieurGauche().y == 6 * taille)) {
            if (a5 == 0) {
                case_possible.add(new Point(p.getCoinSuperieurGauche().x - 2 * taille, p.getCoinSuperieurGauche().y + 1 * taille));
            }
        }

        return case_possible;
    }

    // peut manger en avant
    //ne peut pas avancer de 2 cases au début
    //cas de si un pion devant lui
    private ArrayList<Point>  pawn_deplacement(Piece pion, Point point){
        int taille= ChessBoard.getTaille();
        ArrayList<Point> bouger = new ArrayList<Point>();
        int pas_possible=0;

        if (pion.getCouleur()=="black"){
            for (Piece piece:listePion){
                //pouvoir manger en diagonale
                if (piece.getCouleur()=="white") {
                    if ((piece.getCoinSuperieurGauche().x ==pion.getCoinSuperieurGauche().x-1* taille) && (piece.getCoinSuperieurGauche().y==pion.getCoinSuperieurGauche().y+1*taille)){
                        bouger.add(new Point(piece.getCoinSuperieurGauche().x, piece.getCoinSuperieurGauche().y));
                    }
                    else if ((piece.getCoinSuperieurGauche().x==pion.getCoinSuperieurGauche().x+1*taille) && (piece.getCoinSuperieurGauche().y==pion.getCoinSuperieurGauche().y+1*taille)){
                        bouger.add(new Point(piece.getCoinSuperieurGauche().x, piece.getCoinSuperieurGauche().y));
                    }
                }

                    // cas vertical
                    //pion juste devant
                    if ((piece.getCoinSuperieurGauche().x==pion.getCoinSuperieurGauche().x)&& (piece.getCoinSuperieurGauche().y==pion.getCoinSuperieurGauche().y+1*taille)) {
                        pas_possible=1;
                    }

                    // pion à une case
                    else if ((piece.getCoinSuperieurGauche().x!=pion.getCoinSuperieurGauche().x)&& (piece.getCoinSuperieurGauche().y!=pion.getCoinSuperieurGauche().y+2*taille)) {
                        pas_possible=2;
                    }
                }
                if (pas_possible==2){
                    bouger.add(new Point(pion.getCoinSuperieurGauche().x, pion.getCoinSuperieurGauche().y + 1 * taille));
                    if (pion.getCoinSuperieurGauche().y == taille) {
                        if (pas_possible==0){
                            bouger.add(new Point(pion.getCoinSuperieurGauche().x, pion.getCoinSuperieurGauche().y + 2 * taille));
                        }
                    }
                }
            }
            else {
                for (Piece piece:listePion){
                    if (piece.getCouleur()=="black") {
                        if ((piece.getCoinSuperieurGauche().x ==pion.getCoinSuperieurGauche().x+1* taille) && (piece.getCoinSuperieurGauche().y==pion.getCoinSuperieurGauche().y-1*taille)){
                            bouger.add(new Point(piece.getCoinSuperieurGauche().x, piece.getCoinSuperieurGauche().y));
                        }
                        else if ((piece.getCoinSuperieurGauche().x==pion.getCoinSuperieurGauche().x-1*taille) && (piece.getCoinSuperieurGauche().y==pion.getCoinSuperieurGauche().y-1*taille)){
                            bouger.add(new Point(piece.getCoinSuperieurGauche().x, piece.getCoinSuperieurGauche().y));
                        }
                    }

                    // cas vertical
                    //pion juste devant
                    if ((piece.getCoinSuperieurGauche().x==pion.getCoinSuperieurGauche().x)&& (piece.getCoinSuperieurGauche().y==pion.getCoinSuperieurGauche().y-1*taille)) {
                        pas_possible=1;
                    }

                    // pion à une case
                    else if ((piece.getCoinSuperieurGauche().x!=pion.getCoinSuperieurGauche().x)&& (piece.getCoinSuperieurGauche().y!=pion.getCoinSuperieurGauche().y-2*taille)) {
                        pas_possible=2;
                    }
                }
                if (pas_possible==2){
                    bouger.add(new Point(pion.getCoinSuperieurGauche().x, pion.getCoinSuperieurGauche().y - 1 * taille));
                    if (pion.getCoinSuperieurGauche().y == taille) {
                        if (pas_possible==0){
                            bouger.add(new Point(pion.getCoinSuperieurGauche().x, pion.getCoinSuperieurGauche().y - 2 * taille));
                        }
                    }
                }
            }
            return bouger;
        }


    private void enregistrer(ActionEvent e) {
        try {
            String url = "jdbc:mysql://172.16.23.115:3306/tpjavaimds5A";
            String user = "IMDS5A";
            String password = "Polytech";

            Connection connection = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO scores (pseudo, score) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "joueur 1");
                preparedStatement.setInt(2, 5);
                //myContentPane.getScore()
                preparedStatement.executeUpdate();
            }
            String selectSql = "SELECT * FROM scores ORDER BY score DESC";;
            try (Statement selectStatement = connection.createStatement();
                 ResultSet resultSet = selectStatement.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    String pseudo = resultSet.getString("pseudo");
                    int score = resultSet.getInt("score");
                    System.out.println("Pseudo: " + pseudo + ", Score: " + score);
                }
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    // ajouter le jar : Echec (click droit), Open Module Settings, Libraries, +

    private int calcul_score(int s){
        s=s+20;
        return s;
    }
// dépot code première date puis code+rapport
    // rapport : diagramme de classe uml indisponible
    // focus point important
    //dire quelle version du jeu j'ai fait

}




//rapport : une partie sur git, une partie sur la base de données, sur jeu d'echec quel type de jeu j'ai programmé, qu'est-ce que j'ai fait,
// pas de code !!! juste zoomé sur les points importants (phrase en français, petit schéma)
// qq captures d'écran
//montrer ce qui marche  et ce qui marche pas
// 1 dépot : le code dimanche 4 février