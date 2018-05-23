/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author helgr
 */
public class CarteFX extends Parent {

    private static final int largeur = 60;
    private static final int hauteur = 85;
    private double positionX;
    private double positionY;
    private Carte carte;
    private static final int deplacementAnimation = 40;

    /**
     * Crée un objet CarteFX à partir du nom de la carte
     *
     * @param nom le nom de la carte (exemple 10 de coeur)
     */
    public CarteFX(double posX, double posY, Carte carte) {
        this.carte = carte;
        this.positionX = posX;
        this.positionY = posY;
        if(carte == null){
            creerCarte(posX, posY, "../ressources/cartes/back-navy.png");
        }else{
            String nom = carte.getValeur() + "_" + carte.getSymbole();
            creerCarte(posX, posY, "../ressources/cartes/" + nom + ".png");
        }
    }

    /**
     * Crée la carte
     *
     * @param cheminVersImage le chemin vers l'image de la carte.
     */
    private void creerCarte(double posX, double posY, String cheminVersImage) {
        
        Image imageCarte = new Image(getClass().getResourceAsStream(cheminVersImage));
        ImageView carte = new ImageView();
        carte.setImage(imageCarte);
        carte.setFitWidth(largeur);
        carte.setFitHeight(hauteur);
        carte.setTranslateX(posX);
        carte.setTranslateY(posY);
              
        this.getChildren().add(carte);
    }
    
    /**
     * @return la largeur de la carte.
     */
    public int getLargeur() {
        return largeur;
    }
    
    /**
     * @return la hauteur de la carte.
     */
    public int getHauteur() {
        return hauteur;
    }
    
    /**
     * L'objet carte associé. null si la carte est inconnue.
     * @return 
     */
    public Carte getCarte() {
        return carte;
    }
    
    /**
     * Bouge la carte vers le haut lorsqu'on met la souris dessus.
     * Est appelée par MainJoueurFX.
     */
    public void animationSurvol(){
        Rectangle hitbox = new Rectangle(positionX, positionY, largeur, hauteur+deplacementAnimation);
        hitbox.setFill(Color.TRANSPARENT);
        this.getChildren().add(hitbox);
        this.setTranslateY(this.getTranslateY() - deplacementAnimation);
    }
    
    /**
     * Remet la carte à sa position initiale losqu'on relâche la souris.
     * Est appelée par MainJoueurFX.
     */
    public void animationRelachement(){
        this.setTranslateY(this.getTranslateY() + deplacementAnimation);
    }

    @Override
    public String toString() {
        if(carte != null){
            return carte.toString();
        }else{
            return "Carte face cachée.";
        }
    }
}
