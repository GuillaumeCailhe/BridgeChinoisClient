/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import bridgechinoisclient.ApplicationGraphique;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author helgr
 */
public class Controller implements Initializable {

    private ApplicationGraphique applicationGraphique;

    /**
     * Est appelée par le contrôleur principal pour envoyer une référence vers
     * lui-même.
     *
     * @param mainApp
     */
    public void setMainApp(ApplicationGraphique mainApp) {
        this.applicationGraphique = mainApp;
    }

    public ApplicationGraphique getApplicationGraphique() {
        return applicationGraphique;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
