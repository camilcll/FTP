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
    
    public static void sauvegarderCapteur(Capteur capteur) {
        class OneShotTask implements Runnable {
            Capteur str;
            OneShotTask(Capteur capteur) { str = capteur; }
            public void run() {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String data = mapper.writeValueAsString(str).toString();
                    System.out.println(data);
                    
                    URL url = new URL("http://localhost:5000/API/capteur");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Type", "application/json");

                    byte[] out = data.getBytes(StandardCharsets.UTF_8);

                    OutputStream stream = conn.getOutputStream();
                    stream.write(out);

                    System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                    conn.disconnect();
                    
                } catch (ProtocolException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("send data");
            }
        }
        Thread t = new Thread(new OneShotTask(capteur));
        t.start();
    }
    
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
                sauvegarderCapteur(capteur);
                temp = capteur;
                tabCapteur[z] = capteur;
                z++;
            } 
        }
        
        GenereFeu(tabCapteur);
        
        for(Capteur capteur : tabCapteur){
            System.out.println(capteur.toString());
        }
        
        
    }
    
}
