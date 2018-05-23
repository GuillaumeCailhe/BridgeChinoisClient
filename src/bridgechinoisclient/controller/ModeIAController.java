/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import LibrairieMoteur.ModeDeJeu;
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
        this.getApplicationGraphique().connexionServeur(ModeDeJeu.JOUEUR_CONTRE_IA_FACILE);
    }

    /**
     * Affiche le plateau et charge l'IA confirmé lorsqu'on clique sur
     * le bouton Confirmé.
     */
    @FXML
    private void traiterClicConfirme() {
        this.getApplicationGraphique().connexionServeur(ModeDeJeu.JOUEUR_CONTRE_IA_INTERMEDIAIRE);
    }

    /**
     * Affiche le plateau et charge l'IA master lorsqu'on clique sur
     * le bouton Master.
     */
    @FXML
    private void traiterClicMaster() {
        this.getApplicationGraphique().connexionServeur(ModeDeJeu.JOUEUR_CONTRE_IA_DIFFICILE);
    }
    
    /**
     * Retourne au menu principal lors du clic sur le bouton Retour
     */
    @FXML
    private void traiterClicRetour(){
        this.getApplicationGraphique().afficherMenu();
    }

}
