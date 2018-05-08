/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechinoisclient.model.reseau;

import java.io.DataInputStream;

/**
 *
 * @author Pepefab
 */
public class MessageServeur {
    
    CodeMessage code;
    String donnees;
    
    public MessageServeur(DataInputStream fluxEntrant){
        try{
            this.code = CodeMessage.values()[fluxEntrant.readByte()];
            
            switch(code){
                case PSEUDO:
                    this.donnees = fluxEntrant.readUTF();
                    
                    
            }
        } catch (Exception e){
            throw new Error("Erreur lecture message client");
        }

        
    }
    
}
