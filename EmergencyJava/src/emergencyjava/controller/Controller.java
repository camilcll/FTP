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
import emergencyjava.model.Coord;
import emergencyjava.model.Feu;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.list;
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
                    URL url = new URL("http://localhost:5000/api/emergency/capteur");
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
                    }
                    
                    checkCapteur(output);
                    
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
        
        ArrayList tabCapteurActif;
        tabCapteurActif = new ArrayList<Capteur>();
        int i = 0;
        
        //data = "[{\"id\":1,\"position\":{\"x\":5,\"y\":5},\"intensite\":8},{\"id\":1,\"position\":{\"x\":5,\"y\":15},\"intensite\":0},{\"id\":1,\"position\":{\"x\":15,\"y\":5},\"intensite\":0},{\"id\":1,\"position\":{\"x\":15,\"y\":15},\"intensite\":0}]";
        
        try {
            List<Capteur> listCapteur = mapper.readValue(data, new TypeReference<List<Capteur>>(){});
            //System.out.println(listCapteur.toString());
            for(Capteur capteur : listCapteur){
                if (capteur.getIntensite() != 0){
                    tabCapteurActif.add(capteur);
                    i++;
                }
            }
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Capteurs Actifs");
        System.out.println(tabCapteurActif.toString());
        
        //creerFeu(tabCapteurActif);
    }
    
    public static void creerFeu(ArrayList<Capteur> listcapteur){
        
        ArrayList listcapteurvoisin;
        listcapteurvoisin = new ArrayList<Capteur>();
        
        for(Capteur capteurActif : listcapteur){
            if (Capteur.estVoisinDe(listcapteur, capteurActif).isEmpty()){
                System.out.println("Pas de voisin actif");
                System.out.println("le feu est dans la zone du capteur" + capteurActif.getId());
                Feu feu = new Feu(capteurActif.getPosition(), 5);
            }else if(Capteur.estVoisinDe(listcapteur, capteurActif).size() == 1){
                listcapteurvoisin = Capteur.estVoisinDe(listcapteur, capteurActif);
                Capteur capteurvoisin = (Capteur) listcapteurvoisin.get(0);
                System.out.println("1 voisin actif");
                System.out.println("le feu est dans la zone entre le capteur" + capteurActif.getId() + "et le capteur" + capteurvoisin.getId());
                if (capteurActif.getIntensite() < 8 && capteurvoisin.getIntensite() < 8){
                    int xFeuCalculee = Math.abs(capteurActif.getPosition().getX() - capteurvoisin.getPosition().getX());
                    int yFeuCalculee = Math.abs(capteurActif.getPosition().getY() - capteurvoisin.getPosition().getY());
                    Feu feu = new Feu(new Coord(xFeuCalculee, yFeuCalculee), 5);
                }else if(capteurActif.getIntensite() == 8 && capteurvoisin.getIntensite() < 8){
                    
                }
                
            }
        }
    }
    
    
    
    
}
