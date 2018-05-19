/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.controller;

import bridgechinoisclient.view.ObjetGraphique.CarteFX;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

/**
 * FXML Controller class
 *
 * @author helgr
 */
public class PlateauController extends Controller {
    @FXML
    private FlowPane plateau;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CarteFX carte = new CarteFX();
        plateau.getChildren().add(carte);
    }    
    
    
    
}
