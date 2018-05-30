/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import LibrairieCarte.Carte;
import LibrairieCarte.SymboleCarte;
import LibrairieMoteur.ModeDeJeu;
import bridgechinoisclient.model.reseau.Client;
import bridgechinoisclient.view.ObjetGraphique.CarteFX;
import bridgechinoisclient.view.ObjetGraphique.MainFX;
import bridgechinoisclient.view.ObjetGraphique.PaquetFX;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class PlateauController extends Controller {

    private MainFX mainJoueurFX;
    private MainFX mainAdversaireFX;
    private CarteFX cartePliJoueur;
    private CarteFX cartePliAdversaire;
    private PaquetFX paquetPliJoueur;
    private PaquetFX paquetPliAdversaire;
    private ArrayList<PaquetFX> piles;
    private Client client;

    @FXML
    private AnchorPane plateauPane;
    @FXML
    private Label nomJoueurLabel;
    @FXML
    private Label nomAdversaireLabel;
    @FXML
    private ImageView atoutImageView;

    public MainFX getMainJoueurFX() {
        return mainJoueurFX;
    }

    public MainFX getMainAdversaireFX() {
        return mainAdversaireFX;
    }

    public AnchorPane getPlateauPane() {
        return plateauPane;
    }

    public synchronized void prevenirAnimationsTerminees() {
        this.client.prevenirFinAnimation();
    }

    public void setCartePliJoueur(CarteFX cartePliJoueur) {
        this.cartePliJoueur = cartePliJoueur;
    }

    public void setCartePliAdversaire(CarteFX cartePliAdversaire) {
        this.cartePliAdversaire = cartePliAdversaire;
    }

    public void setPiles(ArrayList<PaquetFX> piles) {
        this.piles = piles;
    }
    
    @FXML
    private void traiterClicRegles(){
        this.getApplicationGraphique().afficherRegles();
    }
    
    public void initialiser(Client client, ArrayList<Carte> mainJoueur, ArrayList<Carte> piles, String nomJoueur, String nomAdversaire) {
        this.client = client;

        // On affiche les noms des joueurs.
        nomJoueurLabel.setText(nomJoueur);
        nomAdversaireLabel.setText(nomAdversaire);

        // On stocke la hauteur et la largeur du plateau.
        int largeurPlateauPane = (int) (plateauPane.getPrefWidth());
        int hauteurPlateauPane = (int) (plateauPane.getPrefHeight());

        // On initialise un objet graphique MainJoueur.
        this.mainAdversaireFX = new MainFX(this, 30, 0);
        this.mainJoueurFX = new MainFX(this, 30, hauteurPlateauPane - 85);

        // On initialise les piles de pli.
        this.paquetPliJoueur = new PaquetFX(0, this, 600, hauteurPlateauPane - 90, mainJoueurFX, mainAdversaireFX);
        this.paquetPliAdversaire = new PaquetFX(0, this, 600, 0, mainJoueurFX, mainAdversaireFX);

        // On initialise le paquet de carte.
        PaquetFX paquetFX = new PaquetFX(52, this, largeurPlateauPane / 2, hauteurPlateauPane / 2 - 65, mainJoueurFX, mainAdversaireFX);

        // On ajoute les mains au plateau.
        this.plateauPane.getChildren().add(mainAdversaireFX);
        this.plateauPane.getChildren().add(mainJoueurFX);
        this.plateauPane.getChildren().add(paquetFX);
        this.plateauPane.getChildren().add(paquetPliJoueur);
        this.plateauPane.getChildren().add(paquetPliAdversaire);
        paquetFX.animationDistributionInitiale(mainJoueur, piles);
    }

    /**
     * Change la classe css d'un label.
     *
     * @param label le label à modifier.
     * @param classe la nouvelle classe du label.
     */
    private void changerClasseLabel(Label label, String classe) {
        label.getStyleClass().clear();
        label.getStyleClass().add(classe);
    }

    /**
     * Met l'image de l'atout à jour.
     */
    public void changerAtout() {
        SymboleCarte symbole = client.getAtout();
        if (symbole != null) {
            Image imageAtout = new Image(getClass().getResourceAsStream("../view/ressources/cartes/symbole_" + symbole.toString().toLowerCase() + ".png"));
            this.atoutImageView.setImage(imageAtout);
        }
    }

    private void setLabelJoueurActif() {
        changerClasseLabel(this.nomJoueurLabel, "labelNomTourJoueur");
        changerClasseLabel(this.nomAdversaireLabel, "labelNom");
    }

    private void setLabelJoueurInactif() {
        changerClasseLabel(this.nomAdversaireLabel, "labelNomTourJoueur");
        changerClasseLabel(this.nomJoueurLabel, "labelNom");
    }

    /**
     * Prévient le joueur que c'est son tour de jeu.
     */
    public void prevenirJouerJoueur() {
        // On change la couleur du nom
        setLabelJoueurActif();
        // On met la main en surbrillance.

        // On rend l'usage de la main.
        this.mainJoueurFX.ajouterEvenementCartes();
    }

    /**
     * Prévient le joueur que c'est le tour de jeu de l'adversaire.
     */
    public void prevenirJouerAdversaire() {
        // On change la couleur du nom
        setLabelJoueurInactif();
        // On enlève la surbrillance de la main.

        // On bloque l'usage de la main.
        this.mainJoueurFX.retirerEvenementCartes();
    }

    /**
     * Définit le gagnant d'un pli.
     *
     * @param estVictorieux vrai si le joueur a remporté le pli.
     */
    public void comparerCartesPli(boolean estVictorieux) {
        // L'un des deux joueurs vient de jouer, on attend un peu pour ne pas surprendre l'utilisateur.
        PauseTransition pt = new PauseTransition(Duration.millis(1000));
        pt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Création de la séquence d'animations.
                SequentialTransition seqT = new SequentialTransition();

                // On cherche qui a gagné le pli.
                CarteFX carteGagnante = cartePliAdversaire;
                CarteFX cartePerdante = cartePliJoueur;
                int deplacementY = -250;
                PaquetFX paquetDesPlisGagnant = paquetPliAdversaire;
                int posXPaquetDesPlis = 350;
                int posYPaquetDesPlis = -120;

                if (estVictorieux) {
                    carteGagnante = cartePliJoueur;
                    cartePerdante = cartePliAdversaire;
                    deplacementY = 250;
                    paquetDesPlisGagnant = paquetPliJoueur;
                    posYPaquetDesPlis = 150;
                }
                // On met les cartes en avant (plus esthétique)
                cartePerdante.toFront();
                carteGagnante.toFront();
                // On agrandit la carte gagnante.
                ScaleTransition st = new ScaleTransition(Duration.millis(250), carteGagnante);
                st.setToX(1.5);
                st.setToY(1.5);
                ScaleTransition st2 = new ScaleTransition(Duration.millis(250), carteGagnante);
                st2.setToX(1);
                st2.setToY(1);
                seqT.getChildren().add(st);
                seqT.getChildren().add(st2);

                // On déplace la carte perdante vers la carte gagnante.
                TranslateTransition tt = cartePerdante.animationDeplacementCarte(300, cartePerdante.getTranslateX(), cartePerdante.getTranslateY() + deplacementY);
                CarteFX carteASupprimer = cartePerdante;
                tt.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        carteASupprimer.setVisible(false);
                    }
                });

                seqT.getChildren().add(tt);

                // On met les cartes face cachée.
                seqT.getChildren().add(carteGagnante.animationMettreFaceCachee());

                // On déplace les cartes vers leur destination
                seqT.getChildren().add(carteGagnante.animationDeplacementCarte(200, carteGagnante.getTranslateX() + posXPaquetDesPlis, carteGagnante.getTranslateY() + posYPaquetDesPlis));

                // On joue les animations.
                seqT.play();
                PaquetFX paquet = paquetDesPlisGagnant;
                CarteFX carteASupprimer2 = carteGagnante;
                seqT.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        // Mise à jour de l'affichage
                        carteASupprimer2.setVisible(false);
                        paquet.ajouterCarte(null);
                        paquet.ajouterCarte(null);
                        prevenirAnimationsTerminees();
                    }
                });
            }
        });

        pt.play();
    }

    /**
     * Prévient le joueur que c'est son tour.
     */
    public void prevenirPiocheJoueur() {
        setLabelJoueurActif();

        Iterator<PaquetFX> it = this.piles.iterator();
        while (it.hasNext()) {
            it.next().ajouterEvenementsPioche();
        }
    }

    /**
     * Prévient le joueur que c'est le tour de l'adversaire.
     */
    public void prevenirPiocheAdversaire() {
        setLabelJoueurInactif();
        retirerEvenementsPaquets();
    }

    public void retirerEvenementsPaquets() {
        Iterator<PaquetFX> it = this.piles.iterator();
        while (it.hasNext()) {
            it.next().retirerEvenementsPioche();
        }
    }

    /**
     * Découvre une carte d'une pile.
     *
     * @param indicePile l'indice de la pile à découvrir.
     * @param carteDecouverte la carte qui a été découverte.
     */
    public void decouvrirCartePile(int indicePile, Carte carteDecouverte) {
        this.piles.get(indicePile).decouvrirCarte(carteDecouverte);
    }

    public void jouerCarteAdversaire(Carte carteJoueeParAdversaire) {
        mainAdversaireFX.jouerCarteAdversaire(carteJoueeParAdversaire).play();
    }

    /**
     * Permet à l'adversaire de jouer
     *
     * @param modeDeJeu le mode de jeu courant
     * @param carte la carte à jouer.
     */
    /*public void jouerCarteAdversaire(ModeDeJeu modeDeJeu, Carte carte) {
        mainAdversaireFX.jouerCarteAdversaire(carte).play();
    }*/
    /**
     * Permet à l'adversaire de piocher.
     *
     * @param indicePilePiochee l'indice de la pile que l'adversaire a choisi.
     */
    public void piocherCarteAdversaire(int indicePilePiochee) {
        PauseTransition pt = new PauseTransition(Duration.millis(1000));
        SequentialTransition animDistribution = piles.get(indicePilePiochee).distribuerCarteEtRetrierMainAdversaire();

        SequentialTransition seqT = new SequentialTransition(pt, animDistribution);
        seqT.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                prevenirAnimationsTerminees();
            }
        });

        seqT.play();
    }

    /**
     * Vérifie si les piles sont vides ou non.
     *
     * @return vrai si les piles sont vides.
     */
    public boolean pilesSontVides() {
        Iterator<PaquetFX> it = this.piles.iterator();
        int i = 0;
        while (it.hasNext()) {
            int nombreCartes = it.next().getNombreCartes();
            System.out.println("Pile numéro " + i + " contient " + nombreCartes);
            if (nombreCartes != 0) {
                return false;
            }
            i++;
        }
        return true;
    }
}
