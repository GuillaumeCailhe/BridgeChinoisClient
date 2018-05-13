/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.model.reseau;

import LibrairieCarte.Carte;
import LibrairieReseau.CodeMessage;
import LibrairieReseau.Communication;
import LibrairieReseau.Message;
import LibrairieReseau.MessageCartes;
import LibrairieReseau.MessageEntier;
import LibrairieReseau.MessageString;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pepefab
 */
public class Client {
    
    public Client() throws IOException, InterruptedException {
        // MANQUANT : négociation du nombre de manches (minimum des 2 demandés ?)
        
        // Connexion au serveur
        Socket client = new Socket("localhost", 31000);
        System.out.println("Connecté");
        Communication c = new Communication(client, this);
        Thread t = new Thread(c);
        t.start();
        
        // Envoi d'une requête de nouvelle partie
        c.envoyer(CodeMessage.PARTIE_JCJ);
        
        // Attente d'une réponse
        if(c.getNbMessages() == 0){
            try {
                synchronized(this){
                    wait();
                }
            } catch (InterruptedException ex) {
                throw new Error("Erreur wait() dans client");
            }
        }
        Message msg = c.getMessageParCode(CodeMessage.PARTIE_DEMARRER);
        if(msg.getCode() != CodeMessage.PARTIE_DEMARRER){
            throw new Error("Erreur de réponse du serveur");
        } else {
            System.out.println("Début de la partie !");
        }
        
        // Envoi du pseudo et réception de 4 messages (PSEUDO, MAIN, PILES, TOUR_OK ou TOUR_KO)
        c.envoyerString(CodeMessage.PSEUDO, "PEPEFAB");
        for(int i = 0; i < 4; i++){
            if(c.getNbMessages() == 0) { 
                synchronized(this){
                    wait();
                }
            }
            msg = c.getPremierMessage();
            switch(msg.getCode()){
                case PSEUDO:
                    System.out.println("Pseudo adversaire: " + (String) msg.getDonnees());
                    break;
                case MAIN:
                    System.out.println("Main: ");
                    for(Carte carte : (ArrayList<Carte>) msg.getDonnees()){
                        System.out.print(carte + "; ");
                    }
                    System.out.println();
                    break;
                case PILES:
                    System.out.println("Piles: ");
                    for(Carte carte : (ArrayList<Carte>) msg.getDonnees()){
                        System.out.print(carte + "; ");
                    }
                    System.out.println();
                    break;
                case TOUR_OK:
                    System.out.println("Vous jouez en premier.");
                    break;
                case TOUR_KO:
                    System.out.println("Vous jouez en second.");
                    break;
            }
        }
        
        // Début de la première manche ! :D

        //client.close();

    }
    
}
