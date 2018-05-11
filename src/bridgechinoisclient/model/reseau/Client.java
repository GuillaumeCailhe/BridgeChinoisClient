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

/**
 *
 * @author Pepefab
 */
public class Client {
    
    public Client() throws IOException, InterruptedException {
        // MANQUANT : négociation du nombre de manches (minimum des 2 demandés ?)
        
        // 1 : Connexion au serveur
        Socket client = new Socket("localhost", 31000);
        System.out.println("Just connected to " + client.getRemoteSocketAddress());
        Communication c = new Communication(client);
        Thread t = new Thread(c);
        t.start();
        
        // 2 : Envoi d'une requête de nouvelle partie
        c.envoyer(CodeMessage.PARTIE_JCJ);
        
        // 3 : Attente d'une réponse
        while(c.getNbMessages() == 0);
        Message msg = c.getMessage();
        if(msg.getCode() != CodeMessage.PARTIE_DEMARRER){
            throw new Error("Erreur de réponse du serveur");
        } else {
            System.out.println("Début de la partie !");
        }
        
        // 4 : Envoi du pseudo et réception de 4 messages (PSEUDO, MAIN, PILES, TOUR_OK ou TOUR_KO)
        c.envoyerString(CodeMessage.PSEUDO, "PEPEFAB");
        for(int i = 0; i < 4; i++){
            while(c.getNbMessages() == 0) { t.sleep(10); }
            msg = c.getMessage();
            switch(msg.getCode()){
                case PSEUDO:
                    System.out.println("Pseudo adversaire: " + (String) msg.getDonnees());
                case MAIN:
                    System.out.println("Main: ");
                    for(Carte carte : (ArrayList<Carte>) msg.getDonnees()){
                        System.out.print(carte + "; ");
                    }
                case PILES:
                    System.out.println("Piles: ");
                    for(Carte carte : (ArrayList<Carte>) msg.getDonnees()){
                        System.out.print(carte + "; ");
                    }
                case TOUR_OK:
                    System.out.println("Vous jouez en premier.");
                case TOUR_KO:
                    System.out.println("Vous jouez en second.");
            }
        }
        
        // 5 : Début de la première manche ! :D

        client.close();

    }
    
}
