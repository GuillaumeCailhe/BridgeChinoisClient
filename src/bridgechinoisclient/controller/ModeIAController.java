/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class ModeIAController extends Controller {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Affiche le plateau et charge l'IA débutant lorsqu'on clique sur
     * le bouton Débutant.
     */
    @FXML
    private void traiterClicDebutant() {
        this.getApplicationGraphique().afficherPlateau();
    }

    /**
     * Affiche le plateau et charge l'IA confirmé lorsqu'on clique sur
     * le bouton Confirmé.
     */
    @FXML
    private void traiterClicConfirme() {
        this.getApplicationGraphique().afficherPlateau();
    }

    /**
     * Affiche le plateau et charge l'IA master lorsqu'on clique sur
     * le bouton Master.
     */
    @FXML
    private void traiterClicMaster() {
        this.getApplicationGraphique().afficherPlateau();
    }

}
