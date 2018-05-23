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
public class MainFX extends Parent {

    private static final int offsetCarteX = 50;
    private ArrayList<CarteFX> mainFX;

    public MainFX(int positionX, int positionY) {
        this.mainFX = new ArrayList<>();

        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
    }

    public static int getOffsetCarteX() {
        return offsetCarteX;
    }

    public void ajouterCarte(Carte carte, int positionDansLaMain) {
        // Création de la carte
        CarteFX carteFX = new CarteFX(positionDansLaMain * this.getOffsetCarteX(), 0, carte);

        // Ajout des événements sur la carte
        if (carte != null) {
            carteFX.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    carteFX.animationSurvolMain();
                }
            });

            carteFX.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    carteFX.animationRelachementMain();
                    recalculerZOrder();
                }
            });

            MainFX mainFX = this;
            carteFX.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mainFX.jouerCarte(carteFX);
                }
            });
        }

        // Affichage de la carte.
        this.mainFX.add(carteFX);
        this.getChildren().add(carteFX);

    }
    
    /**
     * Joue une carte de la main au milieu.
     * @param carteFXJouee 
     */
    public void jouerCarte(CarteFX carteFXJouee) {
        System.out.println("Je joue la carte " + carteFXJouee.toString());
        carteFXJouee.setTranslateX(230);
        carteFXJouee.setTranslateY(-200);
    }
    
    /**
     * Remet les cartes les unes au dessus des autres.
     */
    public void recalculerZOrder(){
        Iterator<CarteFX> it = this.mainFX.iterator();
        while(it.hasNext()){
            CarteFX carteFX = it.next();
            carteFX.toFront();
        }
    }
}
