/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.Parent;

/**
 *
 * @author helgr
 */
public class MainJoueurFX extends Parent {

    /**
     * Crée l'objet MainJoueurFX à partir de la main du joueur.
     *
     * @param main la main du joueur
     */
    public MainJoueurFX(ArrayList<Carte> main) {
        Iterator<Carte> it = main.iterator();
        double posCarteX = 0;
        double posCarteY = 0;

        while (it.hasNext()) {
            Carte carte = it.next();
            CarteFX carteFX = new CarteFX(posCarteX, posCarteY, carte);
            posCarteX += carteFX.getLargeur() - 10;
            this.getChildren().add(carteFX);
        }
    }
}
