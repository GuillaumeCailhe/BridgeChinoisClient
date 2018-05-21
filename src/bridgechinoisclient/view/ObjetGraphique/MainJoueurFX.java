/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

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
            // Création de la carte
            Carte carte = it.next();
            CarteFX carteFX = new CarteFX(posCarteX, posCarteY, carte);
            posCarteX += carteFX.getLargeur() - 10;

            // Ajout des événements sur la carte
            carteFX.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    carteFX.animationSurvol();
                }
            });

            carteFX.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    carteFX.animationRelachement();
                }
            });

            carteFX.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println(carteFX.getCarte().toString());
                }
            });

            // Affichage de la carte.
            this.getChildren().add(carteFX);
        }
    }
}
