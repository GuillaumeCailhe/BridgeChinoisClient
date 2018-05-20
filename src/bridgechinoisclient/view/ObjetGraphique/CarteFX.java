/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author helgr
 */
public class CarteFX extends Parent {

    private int largeur;
    private int hauteur;

    /**
     * Crée un objet CarteFX à partir du nom de la carte
     *
     * @param nom le nom de la carte (exemple 10 de coeur)
     */
    public CarteFX(double posX, double posY, String nom) {
        creerCarte(posX, posY, "../ressources/cartes/" + nom + ".png");
    }

    /**
     * Crée une carteFX retournée
     */
    public CarteFX(double posX, double posY) {
        creerCarte(posX, posY, "../ressources/cartes/back-navy.png");
    }

    /**
     * Crée la carte
     *
     * @param cheminVersImage le chemin vers l'image de la carte.
     */
    private void creerCarte(double posX, double posY, String cheminVersImage) {
        largeur = 60;
        hauteur = 85;

        Image imageCarte = new Image(getClass().getResourceAsStream(cheminVersImage));
        ImageView carte = new ImageView();
        carte.setImage(imageCarte);
        carte.setFitWidth(largeur);
        carte.setFitHeight(hauteur);
        carte.setLayoutX(posX);
        carte.setLayoutY(posY);

        this.getChildren().add(carte);
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

}
