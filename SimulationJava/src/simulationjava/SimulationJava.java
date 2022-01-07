/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationjava;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import simulationjava.model.Capteur;
import simulationjava.model.Coord;
import simulationjava.controller.Controller;
import static simulationjava.controller.Controller.GenereFeu;
import simulationjava.model.Feu;

/**
 *
 * @author Sami
 */
public class SimulationJava {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        Capteur[] tabCapteur;
        tabCapteur = new Capteur[60];
        Capteur temp = null;
        int z = 0;
        int x = -5;
        int y = -5;
        for(int i = 1; i<=6; i++){
            y = -5;
            x = x + 10;
            for(int k = 1; k<=10; k++){
                y = y + 10;
                Capteur capteur = new Capteur(new Coord(y,x), 0);
                temp = capteur;
                tabCapteur[z] = capteur;
                z++;
            } 
        }

        GenereFeu(tabCapteur);
        
        
    }
    
}
