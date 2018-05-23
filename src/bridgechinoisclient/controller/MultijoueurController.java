/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class MultijoueurController extends Controller {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     * Annule la recherche de partenaire et retourne au menu.
     */
    public void traiterClicAnnuler(){
        this.getApplicationGraphique().afficherMenu();
    }
    
}
