/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

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
    private static final double deplacementAnimationSurvolMain = 40;
    private static final double agrandissementAnimationSurvolMain = 0.2;
    private Rectangle hitboxSurvolMain;
    private static final double agrandissementAnimationSurvolPile = 0.3;
    private Rectangle hitboxSurvolPile;
    private boolean estSurvoleeMain;

    /**
     * Crée un objet CarteFX à partir du nom de la carte
     *
     * @param nom le nom de la carte (exemple 10 de coeur)
     */
    public CarteFX(double posX, double posY, Carte carte) {
        this.carte = carte;
        this.positionX = posX;
        this.positionY = posY;
        this.estSurvoleeMain = false;
        if (carte == null) {
            creerCarte(posX, posY, "../ressources/cartes/back-navy.png");
        } else {
            creerCarte(posX, posY, getCheminVersImage(carte));
        }
    }
    
    private String getCheminVersImage(Carte carte){
        String nom = carte.getValeur() + "_" + carte.getSymbole();
        return "../ressources/cartes/" + nom + ".png";
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
     *
     * @return
     */
    public Carte getCarte() {
        return carte;
    }

    /**
     * Déplace la carte de en nouvellePositionX, nouvellePositionY
     *
     * @param duree la durée de l'animation
     * @param nouvellePositionX
     * @param nouvellePositionY
     * @return la transition d'animation de déplacement
     */
    public TranslateTransition animationDeplacementCarte(double duree, double nouvellePositionX, double nouvellePositionY) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(duree), this);
        tt.setToX(nouvellePositionX);
        tt.setToY(nouvellePositionY);

        return tt;
    }

    /**
     * Découvre une carte face cachée et lui attribue la valeur de carte
     *
     * @param carte l'objet carte associé à la nouvelle carte.
     */
    public ScaleTransition animationDecouverteCarte(Carte carte) {
        // On retourne la carte face cachée.
        CarteFX carteFaceCachee = this;
        ScaleTransition stAncienneCarte = new ScaleTransition(Duration.millis(500), carteFaceCachee);
        stAncienneCarte.setByX(-1);
        
        stAncienneCarte.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Suite de l'animation : on retourne la carte.
                carteFaceCachee.getChildren().remove(carteFaceCachee);
                carteFaceCachee.creerCarte(positionX, positionY, getCheminVersImage(carte));
                ScaleTransition stNouvelleCarte = new ScaleTransition(Duration.millis(500), carteFaceCachee);
                stNouvelleCarte.setFromX(0);
                stNouvelleCarte.setByX(1);
                stNouvelleCarte.play();
            }
        });
        return stAncienneCarte;
    }

    /**
     * Bouge la carte vers le haut lorsqu'on met la souris dessus. Concerne les
     * cartes dans la main.
     */
    public void animationSurvolMain() {
        hitboxSurvolMain = new Rectangle(positionX, positionY, largeur + agrandissementAnimationSurvolPile, hauteur + deplacementAnimationSurvolMain + agrandissementAnimationSurvolPile);
        hitboxSurvolMain.setFill(Color.TRANSPARENT);
        this.getChildren().add(hitboxSurvolMain);
        this.setTranslateY(this.getTranslateY() - deplacementAnimationSurvolMain);
        this.setScaleX(this.getScaleX() + agrandissementAnimationSurvolMain);
        this.setScaleY(this.getScaleY() + agrandissementAnimationSurvolMain);
        this.toFront();
        estSurvoleeMain = true;
    }

    /**
     * Agrandit la carte lorsqu'on met la souris dessus. Concerne les cartes sur
     * la pile.
     */
    public void animationSurvolPile() {
        Rectangle hitbox = new Rectangle(positionX, positionY, largeur + agrandissementAnimationSurvolPile, hauteur + agrandissementAnimationSurvolPile);
        hitbox.setFill(Color.TRANSPARENT);
        this.getChildren().add(hitbox);
        this.setScaleX(this.getScaleX() + agrandissementAnimationSurvolPile);
        this.setScaleY(this.getScaleY() + agrandissementAnimationSurvolPile);
    }

    /**
     * Remet la carte à sa position initiale losqu'on relâche la souris.
     * Concerne les cartes sur la pile.
     */
    public void animationRelachementMain() {
        if (estSurvoleeMain) {
            this.setTranslateY(this.getTranslateY() + deplacementAnimationSurvolMain);
            this.getChildren().remove(hitboxSurvolMain);
            this.setScaleX(this.getScaleX() - agrandissementAnimationSurvolMain);
            this.setScaleY(this.getScaleY() - agrandissementAnimationSurvolMain);
            estSurvoleeMain = false;
        }
    }

    /**
     * Redonne à la carte sa taille initiale lorsqu'on relâche la souris.
     *
     */
    public void animationRelachementPile() {
        this.setScaleX(this.getScaleX() - agrandissementAnimationSurvolPile);
        this.setScaleY(this.getScaleY() - agrandissementAnimationSurvolPile);
        this.getChildren().remove(hitboxSurvolPile);
    }

    @Override
    public String toString() {
        if (carte != null) {
            return carte.toString();
        } else {
            return "Carte face cachée.";
        }
    }
}
