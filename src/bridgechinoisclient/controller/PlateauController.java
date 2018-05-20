/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import LibrairieCarte.Carte;
import LibrairieCarte.SymboleCarte;
import LibrairieCarte.ValeurCarte;
import bridgechinoisclient.view.ObjetGraphique.CarteFX;
import bridgechinoisclient.view.ObjetGraphique.MainAdversaireFX;
import bridgechinoisclient.view.ObjetGraphique.MainJoueurFX;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class PlateauController extends Controller {

    @FXML
    private HBox mainAdversaireHBox;
    @FXML
    private HBox mainJoueurHBox;

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

        // On initialise un objet graphique MainJoueur.
        MainAdversaireFX mainAdversaireFX = new MainAdversaireFX();
        MainJoueurFX mainJoueurFX = new MainJoueurFX(mainJoueur);

        // On ajoute les mains au plateau.
        this.mainAdversaireHBox.getChildren().add(mainAdversaireFX);
        this.mainJoueurHBox.getChildren().add(mainJoueurFX);
    }

}
