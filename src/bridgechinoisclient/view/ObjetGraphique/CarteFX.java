/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;


import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author helgr
 */
public class CarteFX extends Parent {

    public CarteFX() {
        Image imageCarte = new Image(getClass().getResourceAsStream("../ressources/cartes/10_club.png"));
        ImageView carte = new ImageView();
        carte.setImage(imageCarte);
        carte.setFitWidth(60);
        carte.setFitHeight(85);
        
        this.getChildren().add(carte);
        
    }

}
