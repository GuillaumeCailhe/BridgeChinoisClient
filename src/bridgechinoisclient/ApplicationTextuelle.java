/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient;

import LibrairieMoteur.ModeDeJeu;
import bridgechinoisclient.model.reseau.Client;
import java.io.IOException;

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
        Client c = new Client("Gerard de la riviere",ModeDeJeu.JOUEUR_CONTRE_JOUEUR,2);
    }
    
}
