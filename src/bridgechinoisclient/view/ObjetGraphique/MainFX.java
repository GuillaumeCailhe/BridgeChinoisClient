/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import bridgechinoisclient.ApplicationGraphique;
import bridgechinoisclient.controller.PlateauController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author helgr
 */
public class MainFX extends Parent {

    private static final int offsetCarteX = 50;
    private ArrayList<CarteFX> mainFX;
    private PlateauController plateauController;

    public MainFX(PlateauController plateauController, int positionX, int positionY) {
        this.mainFX = new ArrayList<>();
        this.plateauController = plateauController;

        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
    }

    public static int getOffsetCarteX() {
        return offsetCarteX;
    }

    public void ajouterCarte(Carte carte, int positionDansLaMain) {
        // Création de la carte
        CarteFX carteFX = new CarteFX(positionDansLaMain * this.getOffsetCarteX(), 0, carte);

        // Affichage de la carte.
        this.mainFX.add(carteFX);
        this.getChildren().add(carteFX);
    }

    /**
     * Joue une carte de la main au milieu.
     *
     * @param carteFXJouee
     */
    public void jouerCarteJoueur(CarteFX carteFXJouee) {
        int positionDansLaMain = this.mainFX.indexOf(carteFXJouee);
        boolean aJoue = this.plateauController.getApplicationGraphique().getClient().jouer(positionDansLaMain);

        if (aJoue) {
            carteFXJouee.animationRelachementMain();
            // Animation de déplacement
            double offsetX = carteFXJouee.getLargeur() - 10;
            double coordonneesX = 230 - (offsetX * positionDansLaMain);
            double coordonneesY = -150;

            TranslateTransition tt = carteFXJouee.animationDeplacementCarte(200, coordonneesX, coordonneesY);
            tt.play();
        }
    }

    /**
     * Joue la carte de l'adversaire
     *
     * @param carte la carte à jouer.
     */
    public void jouerCarteAdversaire(Carte carte) {
        // On choisit une carte au hasard de la main de l'adversaire.
        Random rand = new Random();
        int indiceCarteJouee = rand.nextInt(mainFX.size());
        CarteFX carteFXJouee = this.mainFX.get(indiceCarteJouee);
        
        //Séquence d'animation
        SequentialTransition seqT = new SequentialTransition();

        // Retournement de la carte choisie.
        ScaleTransition st = carteFXJouee.animationDecouverteCarte(carte);
        seqT.getChildren().add(st);
        
        // Déplacement de la carte choisie vers la destination.
        double offsetX = carteFXJouee.getLargeur() - 10;
        double coordonneesX = 230 - (offsetX * indiceCarteJouee);
        double coordonneesY = 120;
        TranslateTransition tt = carteFXJouee.animationDeplacementCarte(250, coordonneesX, coordonneesY);
        seqT.getChildren().add(tt);
        
        // Lancement de la séquence
        seqT.play(); 
    }

    /**
     * Remet les cartes les unes au dessus des autres.
     */
    public void recalculerZOrder() {
        Iterator<CarteFX> it = this.mainFX.iterator();
        while (it.hasNext()) {
            CarteFX carteFX = it.next();
            carteFX.toFront();
        }
    }

    /**
     * Ajoute les événements de souris sur la main du joueur.
     */
    public void ajouterEvenementCartes() {
        Iterator<CarteFX> it = this.mainFX.iterator();
        while (it.hasNext()) {
            CarteFX carteFX = it.next();

            carteFX.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    carteFX.animationSurvolMain();
                }
            });

            carteFX.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    carteFX.animationRelachementMain();
                    recalculerZOrder();
                }
            });

            MainFX mainFX = this;
            carteFX.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mainFX.jouerCarteJoueur(carteFX);
                }
            });
        }
    }

    /**
     * Retire les événements de souris sur la main du joueur.
     */
    public void retirerEvenementCartes() {
        Iterator<CarteFX> it = this.mainFX.iterator();
        while (it.hasNext()) {
            CarteFX carteFX = it.next();
            carteFX.setOnMouseEntered(null);
            carteFX.setOnMouseExited(null);
            carteFX.setOnMouseClicked(null);
        }
    }
}
