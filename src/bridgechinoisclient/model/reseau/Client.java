/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.model.reseau;

import LibrairieReseau.CodeMessage;
import LibrairieReseau.Communication;
import LibrairieReseau.Message;
import LibrairieReseau.MessageString;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Pepefab
 */
public class Client {
    
    public Client() {
        try {
           Socket client = new Socket("localhost", 31000);

           System.out.println("Just connected to " + client.getRemoteSocketAddress());
           Communication c = new Communication(client);
           c.envoyerPseudo("Pepefab");
           c.recevoirDonnees();
           Message m = c.getMessage();
           if(m.getCode() == CodeMessage.PSEUDO){
               System.out.println("Reçu: " + m.getDonnees());
           }
           client.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
    
}
