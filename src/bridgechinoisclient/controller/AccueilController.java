/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import bridgechinoisclient.ApplicationGraphique;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class AccueilController extends Controller {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Affiche la fenêtre multijoueur lors du clic sur le bouton.
     */
    @FXML
    private void traiterClicMultijoueur() {
        this.getApplicationGraphique().afficherMultijoueur();
    }

    /**
     * Affiche la fenêtre Mode IA lors du clic sur le bouton.
     */
    @FXML
    private void traiterClicIA() {
        this.getApplicationGraphique().afficherModeIA();
    }

    /**
     * Affiche la fenêtre tutoriel lors du clic sur le bouton.
     */
    @FXML
    private void traiterClicTutoriel() {
        this.getApplicationGraphique().afficherTutoriel();
    }

    /**
     * Affiche la fenêtre règles lors du clic sur le bouton.
     */
    @FXML
    private void traiterClicRegles() {
        this.getApplicationGraphique().afficherRegles();
    }

    /**
     * Affiche la fenêtre scores lors du clic sur le bouton.
     */
    @FXML
    private void traiterClicsOptions() {
        this.getApplicationGraphique().afficherOptions();
    }

    /**
     * Quitte l'application. Appelé lors du clic sur le bouton "Quitter".
     */
    @FXML
    private void traiterClicQuitter() {
        this.getApplicationGraphique().quitterApplication();
    }

}
