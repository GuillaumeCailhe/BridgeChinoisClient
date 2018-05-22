/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author helgr
 */
public abstract class MainFX extends Parent {

    private static final int offsetCarteX = 50;
    private ArrayList<CarteFX> mainFX;

    public MainFX() {
        this.mainFX = new ArrayList<>();
    }

    public void ajouterCarte(Carte carte, int positionDansLaMain) {
        // Création de la carte
        CarteFX carteFX = new CarteFX(positionDansLaMain * this.getOffsetCarteX(), 0, carte);

        // Ajout des événements sur la carte
        if (carte != null) {
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
        }

        // Affichage de la carte.
        this.mainFX.add(carteFX);
        this.getChildren().add(carteFX);

    }

    public abstract void jouerCarte();

    public static int getOffsetCarteX() {
        return offsetCarteX;
    }

}
