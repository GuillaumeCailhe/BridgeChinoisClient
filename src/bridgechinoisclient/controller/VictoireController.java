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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class VictoireController extends Controller {

    @FXML
    private Button button;
    @FXML
    private Label labelVictoire;
    
    public void setMessageVictoire(String messageVictoire) {
        labelVictoire.setText(messageVictoire);
    }
    
    private void fermer() {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    
    
    @FXML
    private void traiterClicRevanche(){
        System.out.println("Fonctionnalité non implémentée");
    }
    
    @FXML
    private void traiterClicQuitter(){
        this.getApplicationGraphique().afficherMenu();
        fermer();
    }
}
