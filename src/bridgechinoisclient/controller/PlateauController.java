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
        MainFX mainAdversaireFX = new MainFX(60, 0);
        MainFX mainJoueurFX = new MainFX(60, hauteurPlateauPane - 85);

        // On initialise le paquet de carte.
        PaquetFX paquetFX = new PaquetFX(52, largeurPlateauPane / 2 - 60, hauteurPlateauPane / 2 - 65, mainJoueurFX, mainAdversaireFX);

        // On ajoute les mains au plateau.
        this.plateauPane.getChildren().add(mainAdversaireFX);
        this.plateauPane.getChildren().add(mainJoueurFX);
        this.plateauPane.getChildren().add(paquetFX);
        paquetFX.animationDistributionInitiale(mainJoueur, piles, plateauPane);
    }

}
