/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import javafx.scene.Parent;

/**
 *
 * @author helgr
 */
public class MainAdversaireFX extends Parent {

    /**
     * Crée l'objet MainAdversaire FX et l'initialise avec des cartes non
     * découvertes.
     */
    public MainAdversaireFX() {
        int nombreCartes = 11;
        double posCarteX = 0;
        double posCarteY = 0;

        while (nombreCartes > 0) {
            CarteFX carteFX = new CarteFX(posCarteX, posCarteY);
            posCarteX += carteFX.getLargeur() - 10;
            nombreCartes--;

            this.getChildren().add(carteFX);
        }
    }
}
