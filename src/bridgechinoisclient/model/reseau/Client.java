/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.model.reseau;

import LibrairieCarte.Carte;
import LibrairieMoteur.ModeDeJeu;
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
    
    private ModeDeJeu mode;
    private Communication c;
    private String pseudo;
    private int nbManches;
    
    ArrayList<Carte> main;
    ArrayList<Carte> piles;
    
    public Client(String pseudo, ModeDeJeu mode, int nbManches) throws IOException, InterruptedException {        
        this.pseudo = pseudo;
        this.mode = mode;
        this.nbManches = nbManches;
        this.main = new ArrayList<Carte>();
        this.piles = new ArrayList<Carte>();
        
        connection();

        initialisation();
        
        jeu();

        //client.close();

    }
    
    /**
     * Réalise la connexion au serveur et la demande de partie
     * @throws IOException 
     */
    private void connection() throws IOException{
        // Connexion au serveur
        Socket client = new Socket("localhost", 31000);
        System.out.println("Connecté");
        this.c = new Communication(client, this);
        Thread t = new Thread(c);
        t.start();
        
        // Envoi d'une requête de nouvelle partie
        switch(mode){
            case JOUEUR_CONTRE_JOUEUR:
                c.envoyer(CodeMessage.PARTIE_JCJ);
                break;
            case JOUEUR_CONTRE_IA_FACILE:
                c.envoyer(CodeMessage.PARTIE_JCFACILE);
                break;
            case JOUEUR_CONTRE_IA_INTERMEDIAIRE:
                c.envoyer(CodeMessage.PARTIE_JCINTERMEDIAIRE);
                break;
            case JOUEUR_CONTRE_IA_DIFFICILE:
                c.envoyer(CodeMessage.PARTIE_JCDIFFICILE);
                break;           
        }
        
        // Attente d'une réponse
        this.attendreMessage();
        Message msg = c.getMessageParCode(CodeMessage.PARTIE_DEMARRER);
        if(msg.getCode() != CodeMessage.PARTIE_DEMARRER){
            throw new Error("Erreur de réponse du serveur");
        } else {
            System.out.println("Adversaire trouvé : Début de la partie !");
        }
        
    }
    
    /**
     * Négocie le nombre de manches et échange les pseudos
     * @throws InterruptedException 
     */
    private void initialisation() throws InterruptedException{
        Message msg;
         
        // Négociation du nombre de manches
        System.out.println("Négociation du nombre de manches");
        c.envoyerEntier(CodeMessage.PARTIE_NBMANCHES,(byte) this.nbManches);
        if(c.getNbMessages() == 0){
            try {
                synchronized(this){
                    wait();
                }
            } catch (InterruptedException ex) {
                throw new Error("Erreur wait() dans client");
            }
        }
        this.nbManches = (int) c.getMessageParCode(CodeMessage.PARTIE_NBMANCHES).getDonnees();
        System.out.println("Il y aura " + nbManches + " manches");
        
        // Envoi du pseudo
        c.envoyerString(CodeMessage.PSEUDO, pseudo);

        // Réception du pseudo de l'adversaire
        this.attendreMessage();
        msg = c.getMessageParCode(CodeMessage.PSEUDO);
        System.out.println("Pseudo adversaire: " + (String) msg.getDonnees());


    }
    
    private void jeu(){
        Message msg;
        
        for(int mancheActuelle = 1; mancheActuelle <= this.nbManches; mancheActuelle++){
            receptionMain();
            receptionPiles();

            System.out.println();
            System.out.println("Début de la manche " + mancheActuelle);
        }
        
    }
    
    private void receptionMain(){
        // Réception de la main   
        attendreMessage();
        Message msg = this.c.getMessageParCode(CodeMessage.MAIN);
        System.out.println("Main: ");
        for(Carte carte : (ArrayList<Carte>) msg.getDonnees()){
            System.out.print(carte + "; ");
        }
    }
    
    private void receptionPiles(){
        // Réception de la pile
        attendreMessage();
        Message msg = this.c.getMessageParCode(CodeMessage.PILES);
        System.out.println();
        System.out.println("Piles: ");
        for(Carte carte : (ArrayList<Carte>) msg.getDonnees()){
            System.out.print(carte + "; ");
        }        
    }

    private void attendreMessage(){
        if(c.getNbMessages() == 0) { 
            synchronized(this){
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
   
}
