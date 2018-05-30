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
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class Popup_AbandonController extends Controller {

    @FXML
    Button button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private void fermer() {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void traiterClicOui() {
        this.getApplicationGraphique().afficherMenu();
        fermer();
    }

    @FXML
    private void traiterClicNon() {
        fermer();
    }
}
