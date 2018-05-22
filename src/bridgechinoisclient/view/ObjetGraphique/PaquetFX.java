/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import LibrairieCarte.SymboleCarte;
import LibrairieCarte.ValeurCarte;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 *
 * @author helgr
 */
public class PaquetFX extends Parent {

    private int nombreCartes;
    private Stack<CarteFX> cartesFX;
    private int positionXTete = 0;
    private int positionYTete = 0;
    private int offsetX = 2;
    private int offsetY = 2;
    private MainJoueurFX mainJoueurFX;
    private MainAdversaireFX mainAdversaireFX;

    public PaquetFX(int nombreCartes, MainJoueurFX mainJoueurFX, MainAdversaireFX mainAdversaireFX) {
        this.nombreCartes = nombreCartes;
        this.cartesFX = new Stack<>();
        this.mainJoueurFX = mainJoueurFX;
        this.mainAdversaireFX = mainAdversaireFX;
        int nombreCartesAAfficher = 0;
        // Affichage des cartes et initialisation du tableau d'objets.
        while (nombreCartesAAfficher < this.nombreCartes) {
            CarteFX carteFX = new CarteFX(positionXTete, positionYTete, null);
            if (nombreCartesAAfficher < 11) { // pour ne pas surcharger l'affichage
                positionXTete += offsetX;
                positionYTete += offsetY;
            }

            nombreCartesAAfficher++;
            cartesFX.add(carteFX);
            this.getChildren().add(carteFX);
        }
    }

    /**
     * Révèle une carte du paquet
     *
     * @param carte la carte à découvrir.
     */
    public CarteFX decouvrirCarte(Carte carte) {
        CarteFX carteFX = new CarteFX(positionXTete, positionYTete, carte);

        // On enlève la carte inconnue et on la remplace par la carte connue
        CarteFX carteTete = cartesFX.pop();
        cartesFX.add(carteFX);

        // Mise à jour de l'affichage.
        this.getChildren().remove(carteTete);
        this.getChildren().add(carteFX);

        return carteFX;
    }

    /**
     * Retire une carte de la pile mais ne la révèle pas.
     *
     * @return la carte retirée.
     */
    public CarteFX retirerCarteSansDecouvrir() {
        return cartesFX.pop();
    }

    /**
     * Déplace une carte du paquet vers le joueur.
     *
     * @param carte la carte à dessiner.
     * @param positionCarteDansLaMain la position de la carte dans la main 
     * @return l'animation
     */
    private TranslateTransition animationDistributionCarteJoueur(Carte carte, int positionCarteDansLaMain) {
        CarteFX carteFX = decouvrirCarte(carte);
        retirerCarteSansDecouvrir();
        
        return animationDistributionCarte(this.mainJoueurFX, carteFX, 100f, 220f, positionCarteDansLaMain);
    }

    /**
     * Déplace une carte du paquet vers l'adversaire.
     *
     * @param positionCarteDansLaMain la position de la carte dans la main 
     * @return l'animation
     */
    private TranslateTransition animationDistributionCarteAdversaire(int positionCarteDansLaMain) {
        CarteFX carteFX = retirerCarteSansDecouvrir();
        return animationDistributionCarte(this.mainAdversaireFX, carteFX, -150f, 220f, positionCarteDansLaMain);
    }

    /**
     * Déplace une carte vers une nouvelle position.
     *
     * @param mainFX la main dans laquelle ajouter la carte.
     * @param carteADeplacer la carte à déplacer.
     * @param nouvellePositionX la position finale en X.
     * @param nouvellePositionY la position finale en Y.
     * @return l'animation
     */
    private TranslateTransition animationDistributionCarte(MainFX mainFX, CarteFX carteADeplacer, double nouvellePositionX, double nouvellePositionY, int positionCarteDansLaMain) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(700), carteADeplacer);
        tt.setByY(nouvellePositionX);
        tt.setByX(nouvellePositionY);

        PaquetFX paquet = this;
        tt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {   
                mainFX.ajouterCarte(carteADeplacer.getCarte(), positionCarteDansLaMain);
                paquet.getChildren().remove(carteADeplacer);
            }
        });

        return tt;
    }

    /**
     * Distribue les cartes au début de la partie.
     *
     * @param mainJoueur la main du joueur courant.
     */
    public void animationDistributionInitiale(ArrayList<Carte> mainJoueur) {
        SequentialTransition seqT = new SequentialTransition();
        Iterator<Carte> it = mainJoueur.iterator();
        int positionDansLaMain = 0;
        while (it.hasNext()) {
            Carte carteJoueur = it.next();
            TranslateTransition ttJoueur = animationDistributionCarteJoueur(carteJoueur, positionDansLaMain);
            TranslateTransition ttAdversaire = animationDistributionCarteAdversaire(positionDansLaMain);

            seqT.getChildren().add(ttJoueur);
            seqT.getChildren().add(ttAdversaire);
            positionDansLaMain++;
        }

        seqT.play();
    }
}
