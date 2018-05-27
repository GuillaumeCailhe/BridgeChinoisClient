/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import bridgechinoisclient.controller.PlateauController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 *
 * @author helgr
 */
public class PaquetFX extends Parent {

    /* Pour gérer les événements liées à l'animation. */
    private Animateur animateur;

    /* Gestion du paquet*/
    private int idPile;
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
    public PaquetFX(int nombreCartes, Animateur animateur, int positionPaquetX, int positionPaquetY, MainFX mainJoueurFX, MainFX mainAdversaireFX) {
        this.animateur = animateur;
        this.cartesFX = new Stack<>();
        this.positionXTete = positionPaquetX;
        this.positionYTete = positionPaquetY;
        this.positionPaquetX = positionPaquetX;
        this.positionPaquetY = positionPaquetY;
        this.mainJoueurFX = mainJoueurFX;
        this.mainAdversaireFX = mainAdversaireFX;
        // Affichage des cartes et initialisation du tableau d'objets.
        for (int i = 0; i < nombreCartes; i++) {
            ajouterCarte(null);
        }
    }

    public PaquetFX(int idPile, int nombreCartes, Animateur animateur, int positionPaquetX, int positionPaquetY, MainFX mainJoueurFX, MainFX mainAdversaireFX) {
        this(nombreCartes, animateur, positionPaquetX, positionPaquetY, mainJoueurFX, mainAdversaireFX);
        this.idPile = idPile;
    }

    public int getNombreCartes() {
        return cartesFX.size();
    }    
    /**
     * Ajoute une carte dans le paquet.
     *
     * @param carte la carte à ajouter
     */
    public void ajouterCarte(Carte carte) {
        CarteFX carteFX = new CarteFX(positionXTete, positionYTete, null);
        if (this.cartesFX.size() < 11) { // pour ne pas surcharger l'affichage
            positionXTete += offsetX;
            positionYTete += offsetY;
        }

        cartesFX.add(carteFX);
        this.getChildren().add(carteFX);
    }

    /**
     * Révèle une carte du paquet
     *
     * @param carte la carte à découvrir.
     */
    public CarteFX decouvrirCarte(Carte carte) {
        // On récupère la carte en tête de pile.
        CarteFX carteFX;
        if (cartesFX.empty()) {
            return null;
        } else {
            carteFX = cartesFX.peek();
        }

        // Animation : on retourne la carte face cachée.
        SequentialTransition st = carteFX.animationDecouverteCarte(carte);
        st.play();

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
    private TranslateTransition animationDistributionCarteAdversaire(Carte carte, int positionCarteDansLaMain) {
        return animationDistributionCarte(this.mainAdversaireFX, carte, 0f, -200f, positionCarteDansLaMain);
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
        TranslateTransition tt = carteFX.animationDeplacementCarte(250, nouvellePositionX, nouvellePositionY);

        PaquetFX paquet = this;
        tt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if (positionCarteDansLaMain != -1) {
                    mainFX.ajouterCarte(carteADeplacer, positionCarteDansLaMain);
                }
                paquet.getChildren().remove(carteFX);
            }
        });

        return tt;
    }

    /**
     * Distribue les cartes au début de la partie.
     *
     * @param mainJoueur la main du joueur courant.
     * @param estTourJoueur vrai si c'est le tour du joueur.
     * @param piles les cartes au dessus des piles.
     * @param plateau le plateau où dessiner les cartes.
     */
    public void animationDistributionInitiale(ArrayList<Carte> mainJoueur, ArrayList<Carte> piles) {
        SequentialTransition seqT = new SequentialTransition();
        Iterator<Carte> it = mainJoueur.iterator();
        int positionDansLaMain = 0;
        while (it.hasNext()) {
            Carte carteJoueur = it.next();
            TranslateTransition ttJoueur = animationDistributionCarteJoueur(carteJoueur, positionDansLaMain);
            TranslateTransition ttAdversaire = animationDistributionCarteAdversaire(null, positionDansLaMain);

            seqT.getChildren().add(ttJoueur);
            seqT.getChildren().add(ttAdversaire);
            positionDansLaMain++;
        }

        seqT.play();
        seqT.setRate(2);
        seqT.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                animationScinderEnPiles(piles);
            }
        });
    }

    /**
     * Scinde le paquet en 6 paquets, les piles.
     *
     * @param plateau le plateau où dessiner les piles.
     */
    private void animationScinderEnPiles(ArrayList<Carte> piles) {
        // Création d'une liste d'animations jouées en parallèle.
        ParallelTransition parT = new ParallelTransition();
        ArrayList<PaquetFX> pilesFX = new ArrayList<>();
        // Création des piles.
        for (int i = 0; i < 6; i++) {
            int offsetY = (int) (animateur.getPlateau().getPlateauPane().getPrefWidth() / 6);
            PaquetFX pile = new PaquetFX(i, 5, animateur, 0, positionPaquetY, mainJoueurFX, mainAdversaireFX);
            pilesFX.add(pile);

            // Animation de déplacement de la pile.
            TranslateTransition tt = new TranslateTransition(Duration.millis(1000), pile);
            tt.setFromX(positionPaquetX);
            tt.setToX((i * offsetY) + 130);

            int j = i;
            tt.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    // découverte des cartes
                    pile.decouvrirCarte(piles.get(j));
                }
            });

            // On ajoute l'animation à la liste d'animations.
            parT.getChildren().add(tt);

            // On ajoute la pile au plateau.
            animateur.getPlateau().getPlateauPane().getChildren().add(pile);
        }
        // Suppression du paquet initial.
        animateur.getPlateau().getPlateauPane().getChildren().remove(this);
        parT.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Découverte des cartes
                animateur.onDebutDeTour();
            }
        });

        // Lancement de l'animation.
        parT.play();
        // Enregistrement des piles dans le plateau
        animateur.getPlateau().setPiles(pilesFX);
    }

    /**
     * Ajoute les événements de souris sur le paquet.
     */
    public void ajouterEvenementsPioche() {
        if (!cartesFX.isEmpty()) {
            CarteFX tete = this.cartesFX.peek();
            if (tete != null) {
                // Rajout des événements associés de survol/relâchement.
                tete.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        tete.animationSurvolPile();
                    }
                });

                tete.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        tete.animationRelachementPile();
                    }
                });

                PaquetFX pile = this;
                tete.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        boolean aPioche = animateur.getPlateau().getApplicationGraphique().getClient().piocher(idPile);
                        if (aPioche) {
                            TranslateTransition tt = animationDistributionCarteJoueur(tete.getCarte(), -1);
                            ParallelTransition parT = mainJoueurFX.animationTriCarte(animateur.getClient().getMain());
                            SequentialTransition seqT = new SequentialTransition(tt, parT);
                            seqT.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent arg0) {
                                    // Découverte des cartes
                                    animateur.onPiocherCarteJoueur();
                                }
                            });
                            seqT.play();
                        }
                    }
                });
            }
        }
    }

    public SequentialTransition distribuerCarteEtRetrierMainAdversaire() {
        TranslateTransition tt = animationDistributionCarteAdversaire(this.cartesFX.peek().getCarte(), -1);
        ParallelTransition parT = mainAdversaireFX.animationTriCarte(new ArrayList());
        SequentialTransition seqT = new SequentialTransition(tt, parT);
        return seqT;
    }

    /**
     * Retire les événements de souris sur le paquet.
     */
    public void retirerEvenementsPioche() {
        if (!cartesFX.isEmpty()) {
            CarteFX tete = this.cartesFX.peek();
            if (tete != null) {
                tete.setOnMouseEntered(null);
                tete.setOnMouseExited(null);
                tete.setOnMouseClicked(null);
            }
        }
    }
}
