/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.model.reseau;

import bridgechinoisclient.model.carte.Carte;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Pepefab
 */
public class CommunicationServeur {

    Socket client;
    DataInputStream fluxEntrant;
    DataOutputStream fluxSortant;

    Queue<MessageServeur> buffer;

    public CommunicationServeur(Socket client) {
        this.client = client;
        try {
            this.fluxEntrant = new DataInputStream(client.getInputStream());
            this.fluxSortant = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            throw new java.lang.Error("Erreur I/O socket");
        }

        this.buffer = new LinkedList<MessageServeur>();
    }

    /**
     * Sert à envoyer le pseudo de l'adversaire
     *
     * @param pseudo
     */
    public void envoyerPseudo(String pseudo) {
        StringBuilder sb = new StringBuilder();
        sb.append(CodeMessage.PSEUDO);
        sb.append((byte) pseudo.length());
        sb.append(pseudo);
        envoyerDonnees(sb.toString());
    }

    /**
     * Joue la carte indicée par indiceCarte dans la main sur le plateau.
     *
     * @param indiceCarte l'indice de la carte dans la main du joueur.
     */
    public void jouerCoup(int indiceCarte) {
        StringBuilder sb = new StringBuilder();
        sb.append(CodeMessage.JOUER);
        sb.append(indiceCarte);
        envoyerDonnees(sb.toString());
    }

    /**
     * Pioche la carte de la pile d'indice indicePile.
     *
     * @param indicePile l'indice de la pile que le joueur a choisi.
     */
    public void piocher(int indicePile) {
        StringBuilder sb = new StringBuilder();
        sb.append(CodeMessage.JOUER);
        sb.append(indicePile);
        envoyerDonnees(sb.toString());
    }

    /**
     * Indique que le joueur souhaite abandonner.
     */
    public void capituler() {
        StringBuilder sb = new StringBuilder();
        sb.append(CodeMessage.CAPITULER);
        envoyerDonnees(sb.toString());
    }

    /**
     * Indique le joueur souhaite annuler son coup.
     */
    public void annuler() {
        StringBuilder sb = new StringBuilder();
        sb.append(CodeMessage.ANNULER);
        envoyerDonnees(sb.toString());
    }

    /**
     * Indique que le joueur souhaite sauvegarder la partie courante.
     */
    public void sauvegarder() {
        StringBuilder sb = new StringBuilder();
        sb.append(CodeMessage.SAUVEGARDER);
        envoyerDonnees(sb.toString());
    }

    /**
     * Indique que le joueur souhaite charger la partie.
     */
    public void charger() {
        StringBuilder sb = new StringBuilder();
        sb.append(CodeMessage.CHARGER);
        envoyerDonnees(sb.toString());
    }

    /**
     * Envoie un message dans le chat.
     *
     * @param message le message envoyé par le joueur.
     */
    public void envoyerMessageChat(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(CodeMessage.MESSAGE_CHAT);
        sb.append((byte) message.length());
        sb.append(message);
        envoyerDonnees(sb.toString());
    }

    private void envoyerDonnees(String donnees) {
        try {
            fluxSortant.writeChars(donnees);
        } catch (Exception e) {
            throw new java.lang.Error("Erreur d'envoi de données");
        }
    }

    public void recevoirDonnees() {
        MessageServeur msg = new MessageServeur(fluxEntrant);
        this.buffer.add(msg);
    }

}
