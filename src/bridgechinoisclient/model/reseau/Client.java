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
import LibrairieReseau.MessageEntier;
import bridgechinoisclient.ApplicationGraphique;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Pepefab
 */
public class Client implements Runnable {

    private ApplicationGraphique app;
    private ModeDeJeu mode;
    private Communication c;
    private String pseudo;
    private String pseudoAdversaire;
    private int nbManches;

    private boolean peutJouer;
    private boolean peutPiocher;

    private boolean joueurAJoue = false;
    private boolean joueurAPioche = false;
    private boolean adversaireAJoue = false;
    private boolean adversaireAPioche = false;
    private boolean joueurARemportePli;

    Carte derniereCarteJoueeAdversaire;
    int indicePileDerniereCartePiocheeAdversaire;
    int indiceDerniereCarteDecouverte;

    private ArrayList<Carte> main;
    private ArrayList<Carte> piles;
    private SymboleCarte atout;

    public Client(String pseudo, ModeDeJeu mode, int nbManches, ApplicationGraphique app) throws IOException, InterruptedException {
        this.pseudo = pseudo;
        this.mode = mode;
        this.nbManches = nbManches;
        this.app = app;
        this.main = new ArrayList<Carte>();
        this.piles = new ArrayList<Carte>();
        this.peutJouer = false;
        this.peutPiocher = false;
        //client.close();
    }

    public ArrayList<Carte> getMain() {
        return main;
    }

    public ArrayList<Carte> getPiles() {
        return piles;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getPseudoAdversaire() {
        return pseudoAdversaire;
    }

    public boolean peutJouer() {
        return peutJouer;
    }

    public boolean peutPiocher() {
        return peutPiocher;
    }

    public SymboleCarte getAtout() {
        return atout;
    }

    public boolean joueurAJoue() {
        return joueurAJoue;
    }

    public boolean joueurAPioche() {
        return joueurAPioche;
    }

    public boolean adversaireAJoue() {
        return adversaireAJoue;
    }

    public boolean adversaireAPioche() {
        return adversaireAPioche;
    }

    public Carte getDerniereCarteJoueeAdversaire() {
        return derniereCarteJoueeAdversaire;
    }

    public int getIndicePileDerniereCarteDecouverte() {
        return indiceDerniereCarteDecouverte;
    }

    public int getIndicePileDerniereCartePiocheeAdversaire() {
        return indicePileDerniereCartePiocheeAdversaire;
    }
    
    public boolean joueurARemportePli() {
        return joueurARemportePli;
    }

    private void jeu() {
        Scanner sc = new Scanner(System.in);
        Message msg;

        for (int mancheActuelle = 1; mancheActuelle <= this.nbManches; mancheActuelle++) {

            initialisationManche();

            do {
                receptionAtout();

                tour();

                // Récupération du vainqueur
                recupererResultat();

                piocher();

            } while (!main.isEmpty());

            // Réception victoire/défaite
        }

    }

    /**
     * Réalise la connexion au serveur et la demande de partie
     *
     * @throws IOException
     */
    private void connexion() throws IOException {
        // Connexion au serveur
        Socket client = new Socket("localhost", 31000);
        this.c = new Communication(client);
        this.c.addNotifie(this);
        Thread t = new Thread(c);
        t.start();

        // Envoi d'une requête de nouvelle partie
        switch (mode) {
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

        Platform.runLater(() -> app.afficherPlateau());
        Message msg = c.getMessageParCode(CodeMessage.PARTIE_DEMARRER);

    }

    /**
     * Négocie le nombre de manches et échange les pseudos
     *
     * @throws InterruptedException
     */
    private void initialisation() throws InterruptedException {
        Message msg;

        // Négociation du nombre de manches
        c.envoyerEntier(CodeMessage.PARTIE_NBMANCHES, (byte) this.nbManches);
        if (c.getNbMessages() == 0) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException ex) {
                throw new Error("Erreur wait() dans client");
            }
        }
        this.nbManches = (int) c.getMessageParCode(CodeMessage.PARTIE_NBMANCHES).getDonnees();

        // Envoi du pseudo
        c.envoyerString(CodeMessage.PSEUDO, pseudo);

        // Réception du pseudo de l'adversaire
        this.attendreMessage();
        msg = c.getMessageParCode(CodeMessage.PSEUDO);
        this.pseudoAdversaire = (String) msg.getDonnees();
    }

    private void initialisationManche() {
        receptionMain();
        receptionPiles();
    }

    private void receptionMain() {
        // Réception de la main   
        attendreMessage();
        Message msg = this.c.getMessageParCode(CodeMessage.MAIN);
        for (Carte carte : (ArrayList<Carte>) msg.getDonnees()) {
            main.add(carte);
        }
    }

    private void receptionPiles() {
        // Réception de la pile
        attendreMessage();
        Message msg = this.c.getMessageParCode(CodeMessage.PILES);
        for (Carte carte : (ArrayList<Carte>) msg.getDonnees()) {
            piles.add(carte);
        }
    }

    private void receptionAtout() {
        attendreMessage();
        MessageEntier msg = (MessageEntier) this.c.getMessageParCode(CodeMessage.ATOUT);
        int i = msg.getDonnees();
        if (i < 4) {
            this.atout = SymboleCarte.values()[i];
        } else {
            this.atout = null;
        }
    }

    /**
     * Met la variable peutJouer à jour et prévient l'application graphique.
     */
    private void prevenirTourJoueur() {
        peutJouer = true;
        //Platform.runLater(() -> app.getPlateauController().prevenirTourJoueur());
    }

    /**
     * Met la variable peutJouer à jour et prévient l'application graphique.
     */
    private void prevenirTourAdversaire() {
        peutJouer = false;
        //Platform.runLater(() -> app.getPlateauController().prevenirTourAdversaire());
    }

    private void tour() {
        adversaireAJoue = false;
        adversaireAPioche = false;
        Message msg;

        attendreMessage();
        msg = this.c.getPremierMessage();
        switch (msg.getCode()) {
            case TOUR_OK:
                prevenirTourJoueur();
                attendreJoueur();
                prevenirTourAdversaire();
                recupererCoupAdversaire();
                break;
            case TOUR_KO:
                prevenirTourAdversaire();
                recupererCoupAdversaire();
                prevenirTourJoueur();
                attendreJoueur();
                break;
        }
    }

    /**
     * Met la variable peutPiocher à jour et prévient l'application graphique.
     */
    private void prevenirPiocheJoueur() {
        peutPiocher = true;
        //Platform.runLater(() -> app.getPlateauController().prevenirPiocheJoueur());
    }

    /**
     *
     */
    private void prevenirPiocheAdversaire() {
        peutPiocher = false;
        //Platform.runLater(() -> app.getPlateauController().prevenirPiocheAdversaire());
    }

    private void piocher() {
        Message msg;

        attendreMessage();
        msg = this.c.getPremierMessage();
        if (msg.getCode() == CodeMessage.PIOCHER_OK) {
            attendreMessage();
            msg = this.c.getPremierMessage();
            switch (msg.getCode()) {
                case TOUR_OK:
                    prevenirPiocheJoueur();
                    attendreJoueur();
                    prevenirPiocheAdversaire();
                    Collections.sort(main);
                    // Permet de révéler la carte retournée
                    indiceDerniereCarteDecouverte = recupererPiocheAdversaire();
                    joueurAPioche = true;
                    // Permet de savoir la carte piochée par l'adversaire et la carte retournée
                    indicePileDerniereCartePiocheeAdversaire = recupererPiocheAdversaire();
                    adversaireAPioche = true;
                    //Platform.runLater(() -> app.getPlateauController().piocherCarteAdversaire(mode, indicePioche));
                    break;
                case TOUR_KO:
                    indicePileDerniereCartePiocheeAdversaire = recupererPiocheAdversaire();
                    adversaireAPioche = true;
                    //Platform.runLater(() -> app.getPlateauController().piocherCarteAdversaire(mode, indicePioche2));
                    attendreMessage();
                    c.getMessageParCode(CodeMessage.TOUR_OK);
                    prevenirPiocheJoueur();
                    attendreJoueur();
                    prevenirPiocheAdversaire();
                    Collections.sort(main);
                    indiceDerniereCarteDecouverte = recupererPiocheAdversaire();
                    joueurAPioche = true;
                    break;
            }
        }
    }

    private void recupererCoupAdversaire() {
        Message msg;

        attendreMessage();
        msg = c.getMessageParCode(CodeMessage.JOUER_ADVERSAIRE);
        Carte carte = ((ArrayList<Carte>) msg.getDonnees()).get(0);
        derniereCarteJoueeAdversaire = carte;
        adversaireAJoue = true;
        //Platform.runLater(() -> app.getPlateauController().jouerCarteAdversaire(mode, carte));
    }

    private int recupererPiocheAdversaire() {
        Message msg;

        attendreMessage();
        msg = c.getMessageParCode(CodeMessage.PIOCHER_ADVERSAIRE);
        //msg = c.getPremierMessage();
        //System.out.println("a - " +msg.getCode());
        ArrayList<Carte> pioche = (ArrayList<Carte>) msg.getDonnees();
        int i;
        for (i = 0; i < 6; i++) {
            if (this.piles.get(i).compareTo(pioche.get(0)) == 0) {
                break;
            }
        }
        Carte carteDecouverte = pioche.get(1);
        this.piles.set(i, carteDecouverte);
        //Platform.runLater(() -> app.getPlateauController().decouvrirCartePile(i, carteDecouverte));
        
        return i;
    }

    private void recupererResultat() {
        Message msg;
        attendreMessage();
        msg = c.getPremierMessage();
        switch (msg.getCode()) {
            case VICTOIRE_PLI:
                joueurARemportePli = true;
                //Platform.runLater(() -> app.getPlateauController().comparerCartesPli(true));
                break;
            case DEFAITE_PLI:
                joueurARemportePli = false;
                //Platform.runLater(() -> app.getPlateauController().comparerCartesPli(false));
                break;
        }
    }

    private synchronized void attendreMessage() {
        while (c.getNbMessages() == 0) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientTextuel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private synchronized void attendreJoueur() {
        try {
            while (peutJouer || peutPiocher) {
                wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientTextuel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized boolean jouer(int i) {
        if (peutJouer) {
            this.c.envoyerEntier(CodeMessage.JOUER, (byte) i);
            attendreMessage();
            Message msg = this.c.getPremierMessage();
            if (msg.getCode() == CodeMessage.JOUER_OK) {
                main.remove(i);
                peutJouer = false;
                notify();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public synchronized boolean piocher(int i) {
        if (peutPiocher) {
            this.c.envoyerEntier(CodeMessage.PIOCHER, (byte) i);
            this.main.add(this.piles.get(i));
            attendreMessage();
            //Message msg = this.c.getPremierMessage();
            peutPiocher = false;
            notify();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        try {
            connexion();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            initialisation();
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        jeu();
    }

}
