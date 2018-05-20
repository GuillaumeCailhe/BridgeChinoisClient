/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import LibrairieCarte.Carte;
import java.util.Stack;
import javafx.scene.Parent;

/**
 * 
 * @author helgr
 */
public class PaquetFX extends Parent {

    private int nombreCartes;
    private Stack<CarteFX> cartes;
    private int positionXTete = 0;
    private int positionYTete = 0;
    private int offsetX = 2;
    private int offsetY = 2;

    public PaquetFX(int nombreCartes) {
        this.nombreCartes = nombreCartes;
        this.cartes = new Stack<>();

        int nombreCartesAAfficher = 0;
        // Affichage des cartes et initialisation du tableau d'objets.
        while (nombreCartesAAfficher < this.nombreCartes) {
            CarteFX carteFX = new CarteFX(positionXTete, positionYTete);
            if (nombreCartesAAfficher < 11) { // pour ne pas surcharger l'affichage
                positionXTete += offsetX;
                positionYTete += offsetY;
            }

            nombreCartesAAfficher++;
            cartes.add(carteFX);
            this.getChildren().add(carteFX);
        }
    }
    
    /**
     * Révèle une carte du paquet
     * @param carte la carte à découvrir.
     */
    public void decouvrirCarte(Carte carte) {
        CarteFX carteFX = new CarteFX(positionXTete, positionYTete, carte);
        
        // Mise à jour de la pile
        CarteFX carteTete = cartes.pop();
        cartes.add(carteFX);
        
        // Mise à jour de l'affichage.
        this.getChildren().remove(carteTete);
        this.getChildren().add(carteFX);
    }
}
