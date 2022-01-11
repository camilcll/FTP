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
import emergencyjava.model.Caserne;
import emergencyjava.model.Coord;
import emergencyjava.model.Feu;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    
    public static void askData(ArrayList<Caserne> listcaserne){
        
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
                    String data = "";
                    
                    while ((output = br.readLine()) != null) {
                        System.out.println(output);
                        data += output;
                    }
                    
                    System.out.println(data);
                    
                    checkCapteur(data, listcaserne);
                    
                    System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                    conn.disconnect();

                } catch (IOException ex) { 
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Ask to receive data");
            }
            
        }, 5000, 10000);
    }
    
    public static void checkCapteur(String data, ArrayList<Caserne> listcaserne){
        
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
        
        //creerFeu(tabCapteurActif, listecaserne);
    }
    
    public static void creerFeu(ArrayList<Capteur> listcapteur, ArrayList<Caserne> listcaserne){
        
        ArrayList listcapteurvoisin;
        listcapteurvoisin = new ArrayList<Capteur>();
        
        ArrayList listcapteurvoisinintensite = null;
        listcapteurvoisin = new ArrayList();
        
        Feu feu = null;
        
        for(Capteur capteurActif : listcapteur){
            if (Capteur.estVoisinDe(listcapteur, capteurActif).isEmpty()){
                System.out.println("Pas de voisin actif");
                System.out.println("le feu est dans la zone du capteur" + capteurActif.getId());
                if(capteurActif.getIntensite() == 8){
                    feu = new Feu(capteurActif.getPosition(), 3, 6);
                }else{
                    feu  = new Feu(capteurActif.getPosition(), 5, 2);
                }
            }else if(Capteur.estVoisinDe(listcapteur, capteurActif).size() == 1){
                listcapteurvoisin = Capteur.estVoisinDe(listcapteur, capteurActif);
                Capteur capteurvoisin = (Capteur) listcapteurvoisin.get(0);
                int xFeuCalculee = 0;
                int yFeuCalculee = 0;
                System.out.println("1 voisin actif");
                System.out.println("le feu est dans la zone entre le capteur" + capteurActif.getId() + "et le capteur" + capteurvoisin.getId());
                if (capteurActif.getIntensite() == 8 && capteurvoisin.getIntensite() == 8){
                    xFeuCalculee = (capteurActif.getPosition().getX() + capteurvoisin.getPosition().getX())/2;
                    yFeuCalculee = (capteurActif.getPosition().getY() + capteurvoisin.getPosition().getY())/2;
                    feu = new Feu(new Coord(xFeuCalculee, yFeuCalculee), 4, 8);
                }else if(capteurActif.getIntensite() < 8 && capteurvoisin.getIntensite() < 8){
                    xFeuCalculee = (capteurActif.getPosition().getX() + capteurvoisin.getPosition().getX())/2;
                    yFeuCalculee = (capteurActif.getPosition().getY() + capteurvoisin.getPosition().getY())/2;
                    feu = new Feu(new Coord(xFeuCalculee, yFeuCalculee), 5, 8);
                }else if(capteurActif.getIntensite() == 8 && capteurvoisin.getIntensite() < 8){
                    if(capteurActif.getPosition().getX() == capteurvoisin.getPosition().getX()){
                        xFeuCalculee = capteurActif.getPosition().getX();
                        yFeuCalculee = ((7/10)*capteurActif.getPosition().getY()) + ((3/10)*capteurvoisin.getPosition().getY());
                    }else{
                        xFeuCalculee = ((7/10)*capteurActif.getPosition().getY()) + ((3/10)*capteurvoisin.getPosition().getY());
                        yFeuCalculee = capteurActif.getPosition().getX();
                    }
                    feu = new Feu(new Coord(xFeuCalculee, yFeuCalculee), 5, 8);
                }else if(capteurActif.getIntensite() < 8 && capteurvoisin.getIntensite() == 8){
                    if(capteurActif.getPosition().getX() == capteurvoisin.getPosition().getX()){
                        xFeuCalculee = capteurActif.getPosition().getX();
                        yFeuCalculee = ((3/10)*capteurActif.getPosition().getY()) + ((7/10)*capteurvoisin.getPosition().getY());
                    }else{
                        xFeuCalculee = ((3/10)*capteurActif.getPosition().getY()) + ((7/10)*capteurvoisin.getPosition().getY());
                        yFeuCalculee = capteurActif.getPosition().getX();
                    }
                    feu = new Feu(new Coord(xFeuCalculee, yFeuCalculee), 5, 8);
                    listcapteur.remove(capteurvoisin);
                }
            }else if(Capteur.estVoisinDe(listcapteur, capteurActif).size() == 2){
                listcapteurvoisin = Capteur.estVoisinDe(listcapteur, capteurActif);
                Capteur capteurvoisin1 = (Capteur) listcapteurvoisin.get(0);
                Capteur capteurvoisin2 = (Capteur) listcapteurvoisin.get(1);
                
                listcapteur.remove(capteurvoisin1);
                listcapteur.remove(capteurvoisin2);
                
            }else if(Capteur.estVoisinDe(listcapteur, capteurActif).size() == 3){
                listcapteurvoisin = Capteur.estVoisinDe(listcapteur, capteurActif);
                int xFeuCalculee = 0;
                int yFeuCalculee = 0;
                Capteur capteurvoisin1 = (Capteur) listcapteurvoisin.get(0);
                Capteur capteurvoisin2 = (Capteur) listcapteurvoisin.get(1);
                Capteur capteurvoisin3 = (Capteur) listcapteurvoisin.get(2);
                listcapteurvoisinintensite.add(capteurvoisin1.getIntensite());
                listcapteurvoisinintensite.add(capteurvoisin2.getIntensite());
                listcapteurvoisinintensite.add(capteurvoisin3.getIntensite());
                
                if((capteurActif.getIntensite() == capteurvoisin1.getIntensite()) && 
                        (capteurActif.getIntensite() == capteurvoisin2.getIntensite()) && 
                        (capteurActif.getIntensite() == capteurvoisin3.getIntensite())){
                    xFeuCalculee = (capteurActif.getPosition().getX() + capteurvoisin1.getPosition().getX() + capteurvoisin2.getPosition().getX() + capteurvoisin3.getPosition().getX())/4;
                    yFeuCalculee = (capteurActif.getPosition().getY() + capteurvoisin1.getPosition().getY() + capteurvoisin2.getPosition().getY() + capteurvoisin3.getPosition().getY())/4;
                    if(capteurActif.getIntensite() == 2){
                        feu = new Feu(new Coord(xFeuCalculee, yFeuCalculee), 1, 2);
                    }else if(capteurActif.getIntensite() == 3){
                        feu = new Feu(new Coord(xFeuCalculee, yFeuCalculee), 2, 4);
                    }else{
                        feu = new Feu(new Coord(xFeuCalculee, yFeuCalculee), (capteurActif.getIntensite()-1)/2, capteurActif.getIntensite()-1);
                    }
                }
                
                
                listcapteur.remove(capteurvoisin1);
                listcapteur.remove(capteurvoisin2);
                listcapteur.remove(capteurvoisin3);
                
            }else if(Capteur.estVoisinDe(listcapteur, capteurActif).size() == 4){
                listcapteurvoisin = Capteur.estVoisinDe(listcapteur, capteurActif);
                Capteur capteurcentre = null;
                Capteur capteurvoisin1 = (Capteur) listcapteurvoisin.get(0);
                Capteur capteurvoisin2 = (Capteur) listcapteurvoisin.get(1);
                Capteur capteurvoisin3 = (Capteur) listcapteurvoisin.get(2);
                Capteur capteurvoisin4 = (Capteur) listcapteurvoisin.get(3);
                listcapteurvoisinintensite.add(capteurvoisin1.getIntensite());
                listcapteurvoisinintensite.add(capteurvoisin2.getIntensite());
                listcapteurvoisinintensite.add(capteurvoisin3.getIntensite());
                listcapteurvoisinintensite.add(capteurvoisin4.getIntensite());
                
                for(int i = 0; i < listcapteurvoisinintensite.size(); i++){
                    if(listcapteurvoisinintensite.get(i).equals(8)){
                        capteurcentre = (Capteur) listcapteurvoisin.get(i);
                    }
                }
                
                if(!listcapteurvoisinintensite.contains(2)){
                    feu = new Feu(new Coord(capteurcentre.getPosition().getX(), capteurcentre.getPosition().getY()), 3, 6);
                }else{
                    feu = new Feu(new Coord(capteurcentre.getPosition().getX(), capteurcentre.getPosition().getY()), 5, 8);
                }
                
                listcapteur.remove(capteurvoisin1);
                listcapteur.remove(capteurvoisin2);
                listcapteur.remove(capteurvoisin3);
                listcapteur.remove(capteurvoisin4);
                
            }
            
            if(checkFeu(feu)){
                creerIntervention(feu, listcaserne);
            }
        }
    }
    
    public static float checkCercle(int xFeu, int yFeu, float rangeFeu, int x, int y, int range){
        float d2 = (xFeu-x)*(xFeu-x) + (yFeu-y)*(yFeu-y);
        float d1 = (rangeFeu + range)*(rangeFeu + range);
        if (d2 > d1){
            return d2-d1;
        }else{
           return d2-d1;
        }
    }
    
    public static boolean checkFeu(Feu feu) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("debut requete");
            URL url = new URL("http://localhost:5000/api/emergency/feu");
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
            String data = "";

            while ((output = br.readLine()) != null) {
                System.out.println(output);
                data += output;
            }

            System.out.println(data);

            List<Feu> listFeu = mapper.readValue(data, new TypeReference<List<Feu>>(){});

            for(Feu feuExistant : listFeu){
                float temp = checkCercle(feu.getPositionCalculee().getX(), feu.getPositionCalculee().getY(), 
                        feu.getZone(), feuExistant.getPositionCalculee().getX(), feuExistant.getPositionCalculee().getY(), feuExistant.getZone());
                if(temp<=0){
                    return false;
                }
            }

                System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                conn.disconnect();

            } catch (IOException ex) { 
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Ask to receive data");
        return true;
    }
    
    public static void creerIntervention(Feu feu, ArrayList<Caserne> listcaserne){
        
    }
    
    
    
    
}
