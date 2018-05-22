/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.model.reseau;

import LibrairieCarte.Carte;
import LibrairieCarte.SymboleCarte;
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
import java.util.Scanner;
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
    
    private boolean peutJouer;
    private boolean peutPiocher;
    
    private ArrayList<Carte> main;
    private ArrayList<Carte> piles;
    private SymboleCarte atout;
    
    public Client(String pseudo, ModeDeJeu mode, int nbManches) throws IOException, InterruptedException {     
        System.out.println("Vous êtes " + pseudo);
        this.pseudo = pseudo;
        this.mode = mode;
        this.nbManches = nbManches;
        this.main = new ArrayList<Carte>();
        this.piles = new ArrayList<Carte>();
        this.peutJouer = false;
        this.peutPiocher = false;
        
        connexion();

        initialisation();
        
        jeu();

        //client.close();

    } 
        
    private void jeu(){
        Scanner sc = new Scanner(System.in);
        Message msg;
        
        for(int mancheActuelle = 1; mancheActuelle <= this.nbManches; mancheActuelle++){
            System.out.println("Début de la manche " + mancheActuelle);
            
            initialisationManche();
            TEXTUELafficherMain();
            TEXTUELafficherPiles();
            
            do{
                receptionAtout();
                System.out.println("L'atout est " + atout);
                
                tour();
                
                // Récupération du vainqueur
                recupererResultat();
                
                piocher();
                
            } while(!main.isEmpty());
            
            // Réception victoire/défaite
            
        }
        
    }
    
    /**
     * Réalise la connexion au serveur et la demande de partie
     * @throws IOException 
     */
    private void connexion() throws IOException{
        // Connexion au serveur
        Socket client = new Socket("localhost", 31000);
        System.out.println("Connecté");
        this.c = new Communication(client);
        this.c.addNotifie(this);
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
    
    private void initialisationManche(){
        receptionMain();
        receptionPiles();
    }
    
    private void receptionMain(){
        // Réception de la main   
        attendreMessage();
        Message msg = this.c.getMessageParCode(CodeMessage.MAIN);
        for(Carte carte : (ArrayList<Carte>) msg.getDonnees()){
            main.add(carte);
        }
    }
    
    private void receptionPiles(){
        // Réception de la pile
        attendreMessage();
        Message msg = this.c.getMessageParCode(CodeMessage.PILES);
        for(Carte carte : (ArrayList<Carte>) msg.getDonnees()){
            piles.add(carte);
        }        
    }
    
    private void receptionAtout(){
        attendreMessage();
        MessageEntier msg = (MessageEntier) this.c.getMessageParCode(CodeMessage.ATOUT);
        int i = msg.getDonnees();
        if(i < 4) {
            this.atout = SymboleCarte.values()[i];
        } else {
            this.atout = null;
        }
    }
    
    private void tour(){
        Message msg;
        
        attendreMessage();
        msg = this.c.getPremierMessage();
        switch(msg.getCode()){
            case TOUR_OK:
                System.out.println("A votre tour");
                peutJouer = true;
                TEXTUELjouer();
                System.out.println("L'adversaire joue...");
                recupererCoupAdversaire();
                break;
            case TOUR_KO:
                System.out.println("L'adversaire joue...");
                recupererCoupAdversaire();
                peutJouer = true;
                TEXTUELjouer();
                break;
        }
    }
        
    private void TEXTUELjouer(){
        Scanner sc = new Scanner(System.in);
        int iCarte;
        Message msg;
        
        do{
            System.out.println("Index de la carte à jouer:");
            iCarte = sc.nextInt();
            System.out.println("Vous avez joué la carte : " + main.get(iCarte));
            c.envoyerEntier(CodeMessage.JOUER, (byte) iCarte);

            attendreMessage();
            msg = c.getPremierMessage();
        }while(msg.getCode() != CodeMessage.JOUER_OK);
        
        main.remove(iCarte);
    }
    
    private void piocher(){
        Message msg;
        
        attendreMessage();
        msg = this.c.getPremierMessage();
        if(msg.getCode() == CodeMessage.PIOCHER_OK){
            attendreMessage();
            msg = this.c.getPremierMessage();
            switch(msg.getCode()){
                case TOUR_OK:
                    peutPiocher = true;
                    TEXTUELpiocher();
                    TEXTUELafficherMain();
                    peutPiocher = false;
                    // Permet de révéler la carte retournée
                    recupererPiocheAdversaire();
                    System.out.println("L'adversaire pioche...");
                    // Permet de savoir la carte piochée par l'adversaire et la carte retournée
                    recupererPiocheAdversaire();
                    break;
                case TOUR_KO:
                    System.out.println("L'adversaire pioche...");
                    recupererPiocheAdversaire();
                    attendreMessage();
                    msg = c.getMessageParCode(CodeMessage.TOUR_OK);
                    peutPiocher = true;
                    TEXTUELpiocher();
                    TEXTUELafficherMain();
                    peutPiocher = false;
                    recupererPiocheAdversaire();
                    break;
            }   
        }
    }
       
    private void TEXTUELpiocher(){
        Scanner sc = new Scanner(System.in);
        int iCarte;
        
        System.out.println("Index de la carte à piocher: ");
        iCarte = sc.nextInt();
        System.out.println("Vous avez pioché la carte : " + piles.get(iCarte));
        c.envoyerEntier(CodeMessage.PIOCHER,(byte) iCarte);
        main.add(piles.get(iCarte));
    }
    
    private void recupererCoupAdversaire(){
        Message msg;
        
        attendreMessage();
        msg = c.getMessageParCode(CodeMessage.JOUER_ADVERSAIRE);
        Carte carte = ((ArrayList<Carte>) msg.getDonnees()).get(0);
        System.out.println("L'adversaire a joué " + carte);
    }
    
    private void recupererPiocheAdversaire(){
        Message msg;
        
        attendreMessage();
        msg = c.getMessageParCode(CodeMessage.PIOCHER_ADVERSAIRE);
        ArrayList<Carte> pioche = (ArrayList<Carte>) msg.getDonnees();
        int i;
        for(i = 0; i < 6; i++){
            if(this.piles.get(i).compareTo(pioche.get(0)) == 0){
                break;
            }
        }
        this.piles.set(i, pioche.get(1));
        System.out.println("L'adversaire a pioché " + pioche.get(0) + " et a révélée la carte " + pioche.get(1));
        TEXTUELafficherPiles();
    }
    
    private void recupererResultat(){
        Message msg;
        attendreMessage();
        msg = c.getPremierMessage();
        switch(msg.getCode()){
            case VICTOIRE_PLI:
                System.out.println("Vous remportez le pli");
                break;
            case DEFAITE_PLI:
                System.out.println("Vous perdez le pli");
                break;
        }
    }

    
    private void TEXTUELafficherPiles(){
        System.out.println("Piles: ");
        for(Carte carte : this.piles){
            System.out.print(carte + "; ");
        }
        System.out.println();        
    }
    
    private void TEXTUELafficherMain(){
        System.out.println("Main: ");
        for(Carte carte : this.main){
            System.out.print(carte + "; ");
        }
        System.out.println();
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
    
    public boolean jouer(int i){
        if(peutJouer){
            this.c.envoyerEntier(CodeMessage.JOUER, (byte) i);
            attendreMessage();
            Message msg = this.c.getPremierMessage();
            if(msg.getCode() == CodeMessage.JOUER_OK){
                peutJouer = false;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
   
}
