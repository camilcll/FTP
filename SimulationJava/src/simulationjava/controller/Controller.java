/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationjava.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import simulationjava.model.Capteur;
import simulationjava.model.Coord;
import simulationjava.model.Feu;
import simulationjava.model.FeuCalculee;
import simulationjava.model.Intervention;
import simulationjava.model.Vehicule;

/**
 *
 * @author Sami
 */
public class Controller {
    
    public static synchronized void start(Capteur[] tabCapteur){
        recevoirFeu(tabCapteur);
        envoyerCapteur(tabCapteur);
        //recevoirIntervention(tabCapteur);
    }
    
    public static void GenereFeu(Capteur[] tabCapteur){
        int x = new Random().nextInt(101);
        int y = new Random().nextInt(61);
        Coord position = new Coord(18, 12);
        
        int intensite = new Random().nextInt(9);
        if (intensite == 0){
            intensite++;
        }
        
        Feu feu = new Feu(position, 5, true);
        
        System.out.println(feu.toString());
        System.out.println("genere feu");
        
        sauvegarderFeu(feu);
       
        CapteurDetecteFeu(feu, tabCapteur);
        
    }
    
    public static void recevoirFeu(Capteur[] tabCapteur){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Feu> listFeu = null;
                    System.out.println("debut requete");
                    URL url = new URL("http://164.4.1.4:5000/api/simulation/feu");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Type", "application/json");
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP Error code : "
                                + conn.getResponseCode());
                    }
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(in);
                    String output;
                    String data = "";
                    
                    while ((output = br.readLine()) != null) {
                        System.out.println(output);
                        data += output;
                    }
                    
                    System.out.println(data);
                    
                    if(data.equals("[]")){
                        /*int rand = new Random().nextInt(10);
                        if(rand == 5){
                            GenereFeu(tabCapteur);
                        }*/
                        GenereFeu(tabCapteur);
                    }else{
                        try {
                            listFeu = mapper.readValue(data, new TypeReference<List<Feu>>(){});
                            
                            for(Feu feu : listFeu){
                                feu.setDetecte(true);
                                CapteurDetecteFeu(feu, tabCapteur);
                            }

                        } catch (JsonProcessingException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                    conn.disconnect();
                    
                } catch (ProtocolException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("receive feu end");
            }
            
        }, 0, 50000);
    }
    
    public static ArrayList<Capteur> CapteurDetecteFeu(Feu feu, Capteur[] tabCapteur){
        ArrayList<Capteur> listcapteuractive = null;
        
        System.out.println("capteur detecte feu start");
        Coord positionFeu = feu.getPosition();
        int xFeu = positionFeu.getX();
        int yFeu = positionFeu.getY();  
        int intensiteFeu = feu.getIntensite();
        float range = (float)intensiteFeu / 2;
        
        for(Capteur capteur : tabCapteur){
            
            float temp = checkCercle(xFeu, yFeu, range, capteur.getPosition().getX(), capteur.getPosition().getY(), capteur.getRange());
            if (temp <= 0 ){
                listcapteuractive.add(capteur);
                System.out.println("Capteur " + capteur.getId()+ " detecte le feu");
                if ((Math.pow((capteur.getPosition().getX() - xFeu), 2) + Math.pow((capteur.getPosition().getY() - yFeu), 2)) < (Math.pow(range, 2))){
                    capteur.setIntensite(8);
                }else if(temp == 0){
                    capteur.setIntensite(1);
                }else if((temp < 0) && (temp > -20)){
                    capteur.setIntensite(2);
                }else if((temp <= -20) && (temp > -37)){
                    capteur.setIntensite(3);
                }else if((temp <= -37) && (temp > -44)){
                    capteur.setIntensite(4);
                }else if((temp <= -44) && (temp > -52)){
                    capteur.setIntensite(5);
                }else if((temp <= -52) && (temp > -65)){
                    capteur.setIntensite(6);
                }else if((temp <= -65) && (temp >  -76)){
                    capteur.setIntensite(7);
                }else if(temp <= -76){
                    capteur.setIntensite(8);
                }
                
                System.out.println(capteur.toString());
            }
        }
        System.out.println("capteur detecte feu end");
        
        return listcapteuractive;
       
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
    
    public static void sauvegarderFeu(Feu feu) {
        class OneShotTask implements Runnable {
            Feu str;
            OneShotTask(Feu feu) { str = feu; }
            public void run() {
                try {
                    System.out.println("save Feu start");
                    ObjectMapper mapper = new ObjectMapper();
                    String data = mapper.writeValueAsString(str).toString();
                    System.out.println(data);
                    
                    URL url = new URL("http://164.4.1.4:5000/api/simulation/feu");
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
                System.out.println("save Feu end");
            }
        }
        Thread t = new Thread(new OneShotTask(feu));
        t.start();
    }
    
    
    public static void envoyerCapteur(Capteur[] tabCapteur){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                try {
                    System.out.println("send capteurs start");
                    ObjectMapper mapper = new ObjectMapper();
                    int i = 0;
                    String data = "";
                    String[] listeCapteurJson;
                    listeCapteurJson = new String[60];
                    for(Capteur capteur : tabCapteur){
                        try {
                            listeCapteurJson[i] = mapper.writeValueAsString(capteur).toString();
                            i++;
                        } catch (JsonProcessingException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    data = Arrays.toString(listeCapteurJson);
                    System.out.println(data);
                    URL url = new URL("http://164.4.1.4:5000/api/simulation/capteur");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Type", "application/json");

                    String dataToSend = data;

                    byte[] out = dataToSend.getBytes(StandardCharsets.UTF_8);

                    OutputStream stream = conn.getOutputStream();
                    stream.write(out);

                    System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                    conn.disconnect();
                    
                } catch (ProtocolException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("send capteur end");
            }
            
        }, 10000, 20000);
    }
    
    public static void recevoirIntervention(Capteur[] tabCapteur){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                try {
                    System.out.println("debut requete");
                    URL url = new URL("http://164.4.1.4:5000/api/simulation/intervention");
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
                    TraiterIntervention(data, tabCapteur);
                    
                    System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                    conn.disconnect();
                    
                } catch (ProtocolException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("ask data");
            }
            
        }, 5000, 20000);
    }
    
    public static void TraiterIntervention(String data, Capteur[] tabCapteur){
        ObjectMapper mapper = new ObjectMapper();
        List<Intervention> listIntervention = null;
        
        ArrayList listfeucal = null;
        listfeucal = new ArrayList<FeuCalculee>();
        
        ArrayList<Feu> listfeu = null;
        listfeu = new ArrayList<Feu>();
        
        List<Vehicule> listvehicule = null;
        
        
        FeuCalculee feucal = null;
        Feu feuidentifie = null;
        
        try {
            listIntervention = mapper.readValue(data, new TypeReference<List<Intervention>>(){});
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        listfeu = recevoirFeu();
        
        for(Intervention intervention : listIntervention){
            feucal = intervention.getFeu();
            listfeucal.add(feucal);
            listvehicule = intervention.getListeVehicule();
            for (Vehicule vehicule : listvehicule){
                vehicule.setPosition(feucal.getPositionCalculee());
            }
            
            listfeu = recevoirFeu();
            
            for(Feu feu : listfeu){
                if(checkCercle(feu.getPosition().getX(), feu.getPosition().getY(), feu.getIntensite()/2, feucal.getPositionCalculee().getX(), feucal.getPositionCalculee().getY(), feucal.getZone())<=0){
                    for (Vehicule vehicule : listvehicule){
                        vehicule.setPosition(feuidentifie.getPosition());
                    }

                    ArrayList<Capteur> listcapteur = CapteurDetecteFeu(feu, tabCapteur);
                    
                    for(Capteur capteur : listcapteur){
                        tabCapteur[capteur.getId()-1].setIntensite(0);
                    }
                    
                    feu.setIntensite(0);
                    
                    
                    
                }
                    
            }
            
            
                
        }
        
                
    }
    
    public static ArrayList<Feu> recevoirFeu() {
        ObjectMapper mapper = new ObjectMapper();
        List<Feu> listfeu = null;
        try {
            System.out.println("debut requete");
            URL url = new URL("http://164.4.1.4:5000/api/simulation/feu");
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
            
            listfeu = mapper.readValue(data, new TypeReference<List<Feu>>(){});

                System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
                conn.disconnect();

            } catch (IOException ex) { 
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Ask to receive feu detecte");
        return (ArrayList<Feu>) listfeu;
    }
    
}
