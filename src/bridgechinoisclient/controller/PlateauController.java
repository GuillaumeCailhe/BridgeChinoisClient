/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import LibrairieCarte.Carte;
import LibrairieCarte.SymboleCarte;
import LibrairieCarte.ValeurCarte;
import bridgechinoisclient.view.ObjetGraphique.MainFX;
import bridgechinoisclient.view.ObjetGraphique.PaquetFX;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class PlateauController extends Controller {

    private MainFX mainAdversaireFX;
    private MainFX mainJoueurFX;

    @FXML
    private AnchorPane plateauPane;
    @FXML
    private Label nomJoueurLabel;
    @FXML
    private Label nomAdversaireLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void distributionInitiale(ArrayList<Carte> mainJoueur, ArrayList<Carte> piles, String nomJoueur, String nomAdversaire) {
        // On affiche les noms des joueurs.
        nomJoueurLabel.setText(nomJoueur);
        nomAdversaireLabel.setText(nomAdversaire);

        // On stocke la hauteur et la largeur du plateau.
        int largeurPlateauPane = (int) (plateauPane.getPrefWidth());
        int hauteurPlateauPane = (int) (plateauPane.getPrefHeight());

        // On initialise un objet graphique MainJoueur.
        this.mainAdversaireFX = new MainFX(this, 60, 0);
        this.mainJoueurFX = new MainFX(this, 60, hauteurPlateauPane - 85);

        // On initialise le paquet de carte.
        PaquetFX paquetFX = new PaquetFX(52, largeurPlateauPane / 2 - 60, hauteurPlateauPane / 2 - 65, mainJoueurFX, mainAdversaireFX);

        // On ajoute les mains au plateau.
        this.plateauPane.getChildren().add(mainAdversaireFX);
        this.plateauPane.getChildren().add(mainJoueurFX);
        this.plateauPane.getChildren().add(paquetFX);
        paquetFX.animationDistributionInitiale(mainJoueur, this.getApplicationGraphique().getClient().peutJouer(), piles, plateauPane);
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
     * Prévient le joueur que c'est son tour.
     */
    public void prevenirTourJoueur() {
        System.out.println("C'est ton tour.");

        // On change la couleur du nom
        changerClasseLabel(this.nomJoueurLabel, "labelNomTourJoueur");
        changerClasseLabel(this.nomAdversaireLabel, "labelNom");
        // On met la main en surbrillance.
        
        // On rend l'usage de la main.
        this.mainJoueurFX.ajouterEvenementCartes();
    }

    /**
     * Prévient le joueur que c'est le tour de l'adversaire.
     */
    public void prevenirTourAdversaire() {
        System.out.println("C'est le tour de l'adversaire.");
        // On change la couleur du nom
        changerClasseLabel(this.nomAdversaireLabel, "labelNomTourJoueur");
        changerClasseLabel(this.nomJoueurLabel, "labelNom");
        // On enlève la surbrillance de la main.

        // On bloque l'usage de la main.
        this.mainJoueurFX.retirerEvenementCartes();
    }

}
