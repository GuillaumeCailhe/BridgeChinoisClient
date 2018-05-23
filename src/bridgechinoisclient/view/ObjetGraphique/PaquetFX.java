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
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 *
 * @author helgr
 */
public class PaquetFX extends Parent {

    /* Gestion du paquet*/
    private int nombreCartes;
    private Stack<CarteFX> cartesFX;

    /* Position de l'objet graphique paquet */
    private int positionPaquetX; // position du paquet du le plateau
    private int positionPaquetY;
    private int positionXTete = 0; // position de la carte en tête de paquet
    private int positionYTete = 0;
    private int offsetX = 2; // décalage d'une carte par rapport à une autre
    private int offsetY = 2;

    /* Mains pour les mettre à jour après les animations */
    private MainFX mainJoueurFX;
    private MainFX mainAdversaireFX;

    /**
     * @param nombreCartes le nombre de cartes initiale du paquet.
     * @param positionPaquetX la position du paquet en X.
     * @param mainJoueurFX l'objet graphique qui contiendra la main du joueur.
     * @param mainAdversaireFX l'objet graphique qui contiendra la main de
     * l'adversaire.
     */
    public PaquetFX(int nombreCartes, int positionPaquetX, int positionPaquetY, MainFX mainJoueurFX, MainFX mainAdversaireFX) {
        this.nombreCartes = nombreCartes;
        this.cartesFX = new Stack<>();
        this.positionXTete = positionPaquetX;
        this.positionYTete = positionPaquetY;
        this.positionPaquetX = positionPaquetX;
        this.positionPaquetY = positionPaquetY;
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
        // Création de la carte
        CarteFX carteFX = new CarteFX(positionXTete - offsetX, positionYTete - offsetY, carte);

        // Rajout des événements associés de survol/relâchement.
        carteFX.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                carteFX.animationSurvolPile();
            }
        });

        carteFX.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                carteFX.animationRelachementPile();
            }
        });

        carteFX.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Je pioche la carte " + carte.toString());
            }
        });

        // On enlève la carte inconnue et on la remplace par la carte connue dans la pile.
        CarteFX carteTete = cartesFX.pop();
        cartesFX.add(carteFX);

        // Animation : on retourne la carte face cachée.
        ScaleTransition stAncienneCarte = new ScaleTransition(Duration.millis(500), carteTete);
        stAncienneCarte.setByX(-1);

        PaquetFX paquet = this;
        stAncienneCarte.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                paquet.getChildren().remove(carteTete);
                paquet.getChildren().add(carteFX);
                // Suite de l'animation : on retourne la carte.
                ScaleTransition stNouvelleCarte = new ScaleTransition(Duration.millis(500), carteFX);
                stNouvelleCarte.setFromX(0);
                stNouvelleCarte.setByX(1);
                stNouvelleCarte.play();
            }
        });
        stAncienneCarte.play();

        // Mise à jour de l'affichage.
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
        return animationDistributionCarte(this.mainJoueurFX, carte, 0f, 150f, positionCarteDansLaMain);
    }

    /**
     * Déplace une carte du paquet vers l'adversaire.
     *
     * @param positionCarteDansLaMain la position de la carte dans la main
     * @return l'animation
     */
    private TranslateTransition animationDistributionCarteAdversaire(int positionCarteDansLaMain) {
        return animationDistributionCarte(this.mainAdversaireFX, null, 0f, -200f, positionCarteDansLaMain);
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
    private TranslateTransition animationDistributionCarte(MainFX mainFX, Carte carteADeplacer, double nouvellePositionX, double nouvellePositionY, int positionCarteDansLaMain) {
        CarteFX carteFX = retirerCarteSansDecouvrir();
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), carteFX);
        tt.setFromX(0);
        tt.setFromY(0);
        tt.setToX(nouvellePositionX);
        tt.setToY(nouvellePositionY);

        PaquetFX paquet = this;
        tt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                mainFX.ajouterCarte(carteADeplacer, positionCarteDansLaMain);
                paquet.getChildren().remove(carteFX);
            }
        });

        return tt;
    }

    /**
     * Distribue les cartes au début de la partie.
     *
     * @param mainJoueur la main du joueur courant.
     * @param plateau le plateau où dessiner les cartes.
     */
    public void animationDistributionInitiale(ArrayList<Carte> mainJoueur, ArrayList<Carte> piles, AnchorPane plateau) {
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
        seqT.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                animationScinderEnPiles(piles, plateau);
            }
        });
    }

    /**
     * Scinde le paquet en 6 paquets, les piles.
     *
     * @param plateau le plateau où dessiner les piles.
     */
    private void animationScinderEnPiles(ArrayList<Carte> piles, AnchorPane plateau) {
        // Création d'une liste d'animations jouées en parallèle.
        ParallelTransition parT = new ParallelTransition();
        // Création des piles.
        for (int i = 0; i < 6; i++) {
            int offsetY = (int) (plateau.getPrefWidth() / 6);
            PaquetFX pile = new PaquetFX(5, 0, positionPaquetY, mainJoueurFX, mainAdversaireFX);

            // Animation de déplacement de la pile.
            TranslateTransition tt = new TranslateTransition(Duration.millis(1000), pile);
            tt.setFromX(positionPaquetX);
            tt.setToX((i * offsetY) + 20);

            int j = i;
            tt.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    pile.decouvrirCarte(piles.get(j));
                }
            });

            // On ajoute l'animation à la liste d'animations.
            parT.getChildren().add(tt);

            // On ajoute la pile au plateau.
            plateau.getChildren().add(pile);
        }
        // Suppression du paquet initial.
        plateau.getChildren().remove(this);

        // Lancement de l'animation.
        parT.play();
    }

}
