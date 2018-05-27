/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.view.ObjetGraphique;

import bridgechinoisclient.controller.PlateauController;
import bridgechinoisclient.model.reseau.Client;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * Cette classe gère des événements et lances les animations selon eux.
 *
 * @author helgr
 */
public class Animateur {

    private Client client;
    private PlateauController plateau;

    // permet de savoir si les joueurs ont joué leur coup pour les synchroniser.
    // donc que 1) le client a joué le coup.
    // 2) l'animation est terminée sur le plateau.
    boolean joueurAJouePlateau = false;
    boolean joueurAPiochePlateau = false;
    boolean adversaireAJouePlateau = false;
    boolean adversaireAPiochePlateau = false;

    public Animateur(Client client, PlateauController plateau) {
        this.client = client;
        this.plateau = plateau;

        onConnexion();
    }

    public Client getClient() {
        return client;
    }

    public PlateauController getPlateau() {
        return plateau;
    }

    /**
     * Appelé lorsque le joueur se connecte.
     */
    public void onConnexion() {
        plateau.initialiser(this, this.client.getMain(), this.client.getPiles(), this.client.getPseudo(), this.client.getPseudoAdversaire());
    }

    /**
     * Appelé à chaque début de tour.
     */
    public void onDebutDeTour() {
        // On met les booléens à faux.
        this.joueurAJouePlateau = false;
        this.joueurAPiochePlateau = false;
        this.adversaireAJouePlateau = false;
        this.adversaireAPiochePlateau = false;

        // Changement de l'atout.
        plateau.changerAtout(this.client.getAtout());

        // On joue le coup du joueur à qui c'est le tour de jouer.
        boolean peutJouer = this.client.peutJouer();
        if (peutJouer) { // tour du joueur
            plateau.prevenirJouerJoueur();
        } else { // tour de l'adversaire
            plateau.prevenirJouerAdversaire();
            // On est pas certains que l'adversaire a joué.
            if (client.adversaireAJoue() && !adversaireAJouePlateau) {
                plateau.jouerCarteAdversaire(client.getDerniereCarteJoueeAdversaire());
            }
        }
    }

    /**
     * Est appelé lorque le joueur joue une carte sur le plateau.
     */
    public void onJouerCarteJoueur(CarteFX carteFXJouee) {
        this.joueurAJouePlateau = true;
        plateau.setCartePliJoueur(carteFXJouee);
        if (!adversaireAJouePlateau) {
            plateau.prevenirJouerAdversaire();
            plateau.jouerCarteAdversaire(client.getDerniereCarteJoueeAdversaire());
        } else {
            plateau.comparerCartesPli(client.joueurARemportePli());
        }
    }

    /**
     * Est appelé lorsque l'adversaire joue une carte sur le plateau.
     */
    public void onJouerCarteAdversaire(CarteFX carteFXJouee) {
        this.joueurAJouePlateau = true;
        plateau.setCartePliAdversaire(carteFXJouee);
        if (joueurAJouePlateau) {
            plateau.comparerCartesPli(client.joueurARemportePli());
        } else {
            plateau.prevenirJouerJoueur();
        }
    }

    /**
     * Est appelé lorsque les deux cartes jouées ont été comparées pour savoir
     * qui a remporté le pli.
     */
    public void onComparaisonDePlis() {
        // On commene le tour de pioche.
        if (client.adversaireAPioche()) { // tour de l'adversaire.
            if (!adversaireAPiochePlateau) { // On vérifie que l'adversaire n'a pas déjà joué son coup sur le plateau.
                plateau.prevenirPiocheAdversaire();
                plateau.piocherCarteAdversaire(client.getIndicePileDerniereCartePiocheeAdversaire());
            }
        } else { // tour du joueur
            if (!joueurAPiochePlateau) { // On vérifie que le joueur n'a pas déjà joué.
                plateau.prevenirPiocheJoueur();
            }
        }
    }

    /**
     * Est appelé lorque le joueur joue une carte.
     */
    public void onPiocherCarteJoueur() {
        joueurAPiochePlateau = true;
        int indice = client.getIndicePileDerniereCarteDecouverte();
        plateau.decouvrirCartePile(indice, client.getPiles().get(indice));

        if (!adversaireAPiochePlateau) {
            plateau.prevenirPiocheAdversaire();
            plateau.piocherCarteAdversaire(client.getIndicePileDerniereCartePiocheeAdversaire());
        }else{
            this.onFinDeTourDePioche();
        }
    }

    /**
     * Est appelé lorsque l'adversaire pioche une carte.
     */
    public void onPiocherCarteAdversaire() {
        adversaireAPiochePlateau = true;
        int indice = client.getIndicePileDerniereCartePiocheeAdversaire();
        plateau.decouvrirCartePile(indice, client.getPiles().get(indice));
        
        if(!joueurAPiochePlateau){
            plateau.prevenirPiocheJoueur();
        }else{
            this.onFinDeTourDePioche();
        }
    }

    /**
     * Est appelé quand les deux joueurs ont pioché.
     */
    public void onFinDeTourDePioche() {
        int indice = client.getIndicePileDerniereCarteDecouverte();
        
        PauseTransition pt = new PauseTransition(Duration.millis(1000));
        pt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plateau.decouvrirCartePile(indice, client.getPiles().get(indice));
                onDebutDeTour();
            }
        });  
        
        pt.play();
    }
}
