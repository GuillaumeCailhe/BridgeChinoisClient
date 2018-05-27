/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import LibrairieMoteur.ModeDeJeu;
import bridgechinoisclient.ApplicationGraphique;
import bridgechinoisclient.controller.PlateauController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.animation.ParallelTransition;
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
    private Animateur animateur;
    private static final int offsetCarteX = 50;
    private ArrayList<CarteFX> cartesFX;

    public MainFX(Animateur animateur, int positionX, int positionY) {
        this.cartesFX = new ArrayList<>();
        this.animateur = animateur;

        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
    }

    public ArrayList<CarteFX> getCartesFX() {
        return cartesFX;
    }

    public static int getOffsetCarteX() {
        return offsetCarteX;
    }

    public void ajouterCarte(Carte carte, int positionDansLaMain) {
        // Création de la carte
        CarteFX carteFX = new CarteFX(positionDansLaMain * this.getOffsetCarteX(), 0, carte);

        // Affichage de la carte.
        this.cartesFX.add(carteFX);
        this.getChildren().add(carteFX);
    }

    /**
     * Joue une carte de la main au milieu.
     *
     * @param carteFXJouee
     */
    public void jouerCarteJoueur(CarteFX carteFXJouee) {
        int positionDansLaMain = this.cartesFX.indexOf(carteFXJouee);
        boolean aJoue = this.animateur.getClient().jouer(positionDansLaMain);

        if (aJoue) {
            this.retirerEvenementCartes();
            carteFXJouee.animationRelachementMain();
            // Animation de déplacement
            double offsetX = carteFXJouee.getLargeur() - 10;
            double coordonneesX = 230 - (offsetX * positionDansLaMain);
            double coordonneesY = -150;

            TranslateTransition tt = carteFXJouee.animationDeplacementCarte(200, coordonneesX, coordonneesY);
            tt.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    cartesFX.remove(carteFXJouee);
                    animateur.onJouerCarteJoueur(carteFXJouee);
                }
            });
            tt.play();
        }
    }

    /**
     * Joue la carte de l'adversaire
     *
     * @param carte la carte à jouer.
     */
    public SequentialTransition jouerCarteAdversaire(Carte carte) {
        // On choisit une carte au hasard de la main de l'adversaire.
        Random rand = new Random();
        int indiceCarteJouee = rand.nextInt(cartesFX.size());
        CarteFX carteFXJouee = this.cartesFX.get(indiceCarteJouee);

        //Séquence d'animation
        SequentialTransition seqT = new SequentialTransition();

        // Retournement de la carte choisie.
        SequentialTransition st = carteFXJouee.animationDecouverteCarte(carte);
        seqT.getChildren().add(st);

        // Déplacement de la carte choisie vers la destination.
        double offsetX = carteFXJouee.getLargeur() - 10;
        double coordonneesX = 230 - (offsetX * indiceCarteJouee);
        double coordonneesY = 120;
        TranslateTransition tt = carteFXJouee.animationDeplacementCarte(250, coordonneesX, coordonneesY);
        seqT.getChildren().add(tt);

        seqT.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                cartesFX.remove(carteFXJouee);
                animateur.onJouerCarteAdversaire(carteFXJouee);
            }
        });

        // Lancement de la séquence
        return seqT;
    }

    /**
     * Remet les cartes les unes au dessus des autres.
     */
    public void recalculerZOrder() {
        Iterator<CarteFX> it = this.cartesFX.iterator();
        while (it.hasNext()) {
            CarteFX carteFX = it.next();
            carteFX.toFront();
        }
    }

    /**
     * Ajoute les événements de souris sur la main du joueur.
     */
    public void ajouterEvenementCartes() {
        Iterator<CarteFX> it = this.cartesFX.iterator();
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
        Iterator<CarteFX> it = this.cartesFX.iterator();
        while (it.hasNext()) {
            CarteFX carteFX = it.next();
            carteFX.setOnMouseEntered(null);
            carteFX.setOnMouseExited(null);
            carteFX.setOnMouseClicked(null);
        }
    }

    /**
     * Trie la main (enlève les cartes et remplace par une main triée)
     *
     * @param nouvelleMain la main déjà triée.
     * @return l'animation de tri.
     */
    public ParallelTransition animationTriCarte(ArrayList<Carte> nouvelleMain) {
        ParallelTransition pt = new ParallelTransition();
        Iterator<CarteFX> iteratorMainFX = this.cartesFX.iterator();
        int positionDansLaMainFX = 0;

        // On déplace chaque carte hors du plateau.
        while (iteratorMainFX.hasNext()) {
            CarteFX carteFX = iteratorMainFX.next();
            TranslateTransition tt = carteFX.animationDeplacementCarte(500, carteFX.getTranslateX() - (carteFX.getLargeur() + 10) * positionDansLaMainFX, carteFX.getTranslateY());

            // Suppression des cartes.
            MainFX cetteMain = this;
            tt.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    cetteMain.getChildren().remove(carteFX);
                }
            });
            positionDansLaMainFX++;
            pt.getChildren().add(tt);
        }
        // On met à jour la main.

        MainFX cetteMain = this;
        pt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                cetteMain.getCartesFX().clear();
                int positionDansLaMain = 0;

                if (nouvelleMain.isEmpty()) { // main adverse
                    for (int i = 0; i < 11; i++) {
                        cetteMain.ajouterCarte(null, i);
                    }
                } else { // mainJoueur
                    Iterator<Carte> iteratorNouvelleMainFX = nouvelleMain.iterator();
                    while (iteratorNouvelleMainFX.hasNext()) {
                        Carte carte = iteratorNouvelleMainFX.next();
                        cetteMain.ajouterCarte(carte, positionDansLaMain);
                        positionDansLaMain++;
                    }
                }
            }
        });

        return pt;
    }
}
