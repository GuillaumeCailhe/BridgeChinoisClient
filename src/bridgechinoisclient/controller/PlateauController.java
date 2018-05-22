/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import LibrairieCarte.Carte;
import LibrairieCarte.SymboleCarte;
import LibrairieCarte.ValeurCarte;
import bridgechinoisclient.view.ObjetGraphique.MainAdversaireFX;
import bridgechinoisclient.view.ObjetGraphique.MainJoueurFX;
import bridgechinoisclient.view.ObjetGraphique.PaquetFX;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class PlateauController extends Controller {

    @FXML
    private AnchorPane plateauPane;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // On récupère les mains des joueurs.
        ArrayList<Carte> mainJoueur = new ArrayList();

        mainJoueur.add(new Carte(ValeurCarte.VALET, SymboleCarte.PIQUE));
        mainJoueur.add(new Carte(ValeurCarte.AS, SymboleCarte.PIQUE));
        mainJoueur.add(new Carte(ValeurCarte.DIX, SymboleCarte.COEUR));
        mainJoueur.add(new Carte(ValeurCarte.TROIS, SymboleCarte.TREFLE));
        mainJoueur.add(new Carte(ValeurCarte.NEUF, SymboleCarte.TREFLE));
        mainJoueur.add(new Carte(ValeurCarte.DEUX, SymboleCarte.COEUR));
        mainJoueur.add(new Carte(ValeurCarte.DEUX, SymboleCarte.CARREAU));
        mainJoueur.add(new Carte(ValeurCarte.NEUF, SymboleCarte.PIQUE));
        mainJoueur.add(new Carte(ValeurCarte.DAME, SymboleCarte.COEUR));
        mainJoueur.add(new Carte(ValeurCarte.HUIT, SymboleCarte.PIQUE));
        mainJoueur.add(new Carte(ValeurCarte.CINQ, SymboleCarte.CARREAU));
        
        // On stocke la hauteur et la largeur du plateau.
        int largeurPlateauPane = (int) (plateauPane.getPrefWidth());
        int hauteurPlateauPane = (int) (plateauPane.getPrefHeight());

        // On initialise un objet graphique MainJoueur.
        MainAdversaireFX mainAdversaireFX = new MainAdversaireFX(60,0);
        MainJoueurFX mainJoueurFX = new MainJoueurFX(60, hauteurPlateauPane - 85);
        
        
        // On initialise le paquet de carte.
        PaquetFX paquetFX = new PaquetFX(52, largeurPlateauPane/2 - 60, hauteurPlateauPane/2 - 85, mainJoueurFX, mainAdversaireFX);
        
        // On ajoute les mains au plateau.
        this.plateauPane.getChildren().add(mainAdversaireFX);
        this.plateauPane.getChildren().add(mainJoueurFX);
        this.plateauPane.getChildren().add(paquetFX);
        paquetFX.animationDistributionInitiale(mainJoueur);
    }

}
