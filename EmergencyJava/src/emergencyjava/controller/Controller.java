/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emergencyjava.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import emergencyjava.model.Capteur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sami
 */
public class Controller {
    
    public static void askData(){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                try {
                    System.out.println("debut requete");
                    URL url = new URL("http://localhost:5000/API/feu");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Type", "application/json");
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP Error code : "
                                + conn.getResponseCode());
                    }
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());
                    System.out.println(in);
                    BufferedReader br = new BufferedReader(in);
                    System.out.println(br);
                    String output;
                    
                    while ((output = br.readLine()) != null) {
                        System.out.println(output);
                        checkCapteur(output);
                    }
                    
                    System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                    conn.disconnect();

                } catch (IOException ex) { 
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Ask to receive data");
            }
            
        }, 5000, 10000);
    }
    
    public static void checkCapteur(String data){
        
        ObjectMapper mapper = new ObjectMapper();
        int i = 0;  
        Capteur[] tabCapteurActif;
        tabCapteurActif = new Capteur[60];
        
        try {
            List<Capteur> listCar = mapper.readValue(data, new TypeReference<List<Capteur>>(){});
            System.out.println(listCar.toString());
            /*for(Capteur capteur : tabCapteur){
                if (capteur.getIntensite() != 0){
                    tabCapteurActif[i] = capteur;
                }
            }*/
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        for(Capteur capteur : tabCapteurActif){
            System.out.println(capteur.toString());
        }
        
    }
    
    /*public static void creerFeu(Capteur[] capteur){
        
    }*/
    
}
