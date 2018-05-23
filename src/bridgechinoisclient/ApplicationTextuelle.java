/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient;

import LibrairieMoteur.ModeDeJeu;
import bridgechinoisclient.model.reseau.Client;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author helgr
 */
public class ApplicationTextuelle {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO code application logic here
        String[] pseudos = new String[] {"Pepefab","Helgrind","Djoko","Amadou","Kyle","Perceval","Karadoc","Kadoc","Gabe"};
        Random r = new Random();
        Client c = new Client(pseudos[r.nextInt(pseudos.length)],ModeDeJeu.JOUEUR_CONTRE_IA_FACILE,1+r.nextInt(5));
    }
    
}
