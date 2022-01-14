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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import simulationjava.model.Capteur;
import simulationjava.model.Caserne;
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
        GenereFeu(tabCapteur);
        recevoirFeu(tabCapteur);
        envoyerCapteur(tabCapteur);
        recevoirIntervention(tabCapteur);
    }
    
    public static void GenereFeu(Capteur[] tabCapteur){
        int x = new Random().nextInt(101);
        int y = new Random().nextInt(61);
        Coord position = new Coord(15, 15);
        
        int intensite = new Random().nextInt(9);
        if (intensite == 0){
            intensite++;
        }
        
        Feu feu = new Feu(position, 4, true);
        
        System.out.println(feu.toString());
        System.out.println("Un feu est apparu");
        
        sauvegarderFeu(feu);
       
        CapteurDetecteFeu(feu, tabCapteur);
        
    }
    
    public static void recevoirFeu(Capteur[] tabCapteur){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                ObjectMapper mapper = new ObjectMapper();
                List<Feu> listFeu = null;
                String data = null;
                try {
                    data = apiGet(new URL("http://164.4.1.4:5000/api/simulation/feunonDetecte"));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                    if(data.equals("[]")){
                        int rand = new Random().nextInt(10);
                        if(rand == 5){
                            GenereFeu(tabCapteur);
                        }
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
            }
            
        }, 0, 200000);
    }
    
    public static ArrayList<Capteur> CapteurDetecteFeu(Feu feu, Capteur[] tabCapteur){
        ArrayList<Capteur> listcapteuractive;
        listcapteuractive = new ArrayList<Capteur>();
        
        System.out.println("capteur detecte feu start ---------");
        Coord positionFeu = feu.getPosition();
        int xFeu = positionFeu.getX();
        int yFeu = positionFeu.getY();  
        int intensiteFeu = feu.getIntensite();
        float range = (float)intensiteFeu / 2;
        
        for(Capteur capteur : tabCapteur){
            
            float temp = checkCercle(xFeu, yFeu, range, capteur.getPosition().getX(), capteur.getPosition().getY(), capteur.getRange());
            if (temp <= 0 ){
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
                
                listcapteuractive.add(capteur);
                
                //System.out.println(capteur.toString());
            }
        }
        System.out.println("capteur detecte feu end ---------");
        
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
                    System.out.println("Sauvegarde du FEU en base start -------------");
                    ObjectMapper mapper = new ObjectMapper();
                    String data = mapper.writeValueAsString(str).toString();
                    //System.out.println(data);
                    
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
                System.out.println("Sauvegarde du FEU en base end -------------");
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
                    System.out.println("envoi capteurs start -----------------");
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
                    //System.out.println(data);
                    System.out.println("Capteurs envoyés");
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
                System.out.println("envoi capteur end -------------");
            }
            
        }, 20000, 200000);
    }
    
    public static void recevoirIntervention(Capteur[] tabCapteur){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                System.out.println("recevoir intervention ----------------");
                String data = null;
                try {
                    data = apiGet(new URL("http://164.4.1.4:5000/api/simulation/intervention"));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(data);
                TraiterIntervention(data, tabCapteur);

            }
            
        }, 80000, 200000);
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
        
        int numcaserne = 0;
        
        try {
            listIntervention = mapper.readValue(data, new TypeReference<List<Intervention>>(){});
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        listfeu = recevoirFeureel();
        
        for(Intervention intervention : listIntervention){
            
            if(intervention.getEtat() == 0){
                
                feucal = intervention.getFeu();
                listfeucal.add(feucal);
                listvehicule = intervention.getListeVehicule();
                for (Vehicule vehicule : listvehicule){
                    vehicule.setPosition(feucal.getPositionCalculee());
                    numcaserne = vehicule.getIdcaserne();
                }
                
                System.out.println("avant le mouvement du camion ");

                MoveVehicule((ArrayList<Vehicule>) listvehicule);
                
                System.out.println("apres le mouvement du camion ");

                listfeu = recevoirFeureel();

                for(Feu feu : listfeu){
                    if (feu.getIntensite() > 0){
                        if(checkCercle(feu.getPosition().getX(), feu.getPosition().getY(), feu.getIntensite()/2, feucal.getPositionCalculee().getX(), feucal.getPositionCalculee().getY(), feucal.getZone())<=0){
                        System.out.println("le feu calcule" + feucal.toString() + " correspondau feu réel" + feu.toString());
                        for (Vehicule vehicule : listvehicule){
                            vehicule.setPosition(feu.getPosition());
                        }
                        
                        try {
                            System.out.println("Le camion est sur le feu");
                            TimeUnit.SECONDS.sleep(30);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        MoveVehicule((ArrayList<Vehicule>) listvehicule);

                        ArrayList<Capteur> listcapteur = CapteurDetecteFeu(feu, tabCapteur);

                        for(Capteur capteur : listcapteur){
                            tabCapteur[capteur.getId()-1].setIntensite(0);
                        }

                        feu.setIntensite(0);

                        intervention.setEtat(2);

                        ArrayList<Caserne> listcaserne = recevoirCaserne();

                        for(Caserne caserne : listcaserne){
                            if(caserne.getId() == numcaserne){
                                for (Vehicule vehicule : listvehicule){
                                    vehicule.setPosition(caserne.getPosition());
                                    vehicule.setDisponible(true);
                                }
                                MoveVehicule((ArrayList<Vehicule>) listvehicule);
                            }
                        }

                        updateFeu(listfeu);

                        updateIntervention((ArrayList<Intervention>) listIntervention);
                        
                        System.out.println("FEUUUUUUUUU ETTTTTEINNNNNT");

                    }
                    }


                }

                }
            
                
        }
        
                
    }
    
    public static void MoveVehicule(ArrayList<Vehicule> listvehicule) {
        class OneShotTask implements Runnable {
            ArrayList<Vehicule> str;
            OneShotTask(ArrayList<Vehicule> listvehicule) { str = listvehicule; }
            public void run() {
                try {
                    System.out.println("update vehicule start -------------");
                    ObjectMapper mapper = new ObjectMapper();
                    String data = mapper.writeValueAsString(str).toString();
                    //System.out.println(data);
                    
                    URL url = new URL("http://164.4.1.4:5000/api/simulation/vehicule");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("PUT");
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
                System.out.println("update vehicule end -------------");
            }
        }
        Thread t = new Thread(new OneShotTask(listvehicule));
        t.start();
    }
    
    public static void updateFeu(ArrayList<Feu> listfeu) {
        class OneShotTask implements Runnable {
            ArrayList<Feu> str;
            OneShotTask(ArrayList<Feu> listfeu) { str = listfeu; }
            public void run() {
                try {
                    System.out.println("update feu start ---------------");
                    ObjectMapper mapper = new ObjectMapper();
                    String data = mapper.writeValueAsString(str).toString();
                    //System.out.println(data);
                    
                    URL url = new URL("http://164.4.1.4:5000/api/simulation/feu");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("PUT");
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
                System.out.println("unpdate Feu end --------------");
            }
        }
        Thread t = new Thread(new OneShotTask(listfeu));
        t.start();
    }
    
    public static void updateIntervention(ArrayList<Intervention> listinter) {
        class OneShotTask implements Runnable {
            ArrayList<Intervention> str;
            OneShotTask(ArrayList<Intervention> listinter) { str = listinter; }
            public void run() {
                try {
                    System.out.println("update intervention start ---------------");
                    ObjectMapper mapper = new ObjectMapper();
                    String data = mapper.writeValueAsString(str).toString();
                    //System.out.println(data);
                    
                    URL url = new URL("http://164.4.1.4:5000/api/simulation/intervention");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("PUT");
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
                System.out.println("update intervention end -----------------");
            }
        }
        Thread t = new Thread(new OneShotTask(listinter));
        t.start();
    }
    
    public static ArrayList<Caserne> recevoirCaserne() {
        ObjectMapper mapper = new ObjectMapper();
        List<Caserne> listcaserne = null;
        String data;
        try {
            System.out.println("recevoir caserne start ------------");
            data = apiGet(new URL("http://164.4.1.4:5000/api/simulation/caserne"));
            listcaserne = mapper.readValue(data, new TypeReference<List<Caserne>>(){});
        } catch (MalformedURLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("recevoir caserne end ------------");
        
        return (ArrayList<Caserne>) listcaserne;
    }
    
    public static ArrayList<Feu> recevoirFeureel() {
        ObjectMapper mapper = new ObjectMapper();
        List<Feu> listfeu = null;
        String data;
        try {
            System.out.println("recevoir feu reel start ------------");
            data = apiGet(new URL("http://164.4.1.4:5000/api/simulation/feuDetecte"));
            listfeu = mapper.readValue(data, new TypeReference<List<Feu>>(){});
        } catch (MalformedURLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("recevoir feu reel end ------------");
        return (ArrayList<Feu>) listfeu;
    }
    
    public static String apiGet(URL url){
        String data = "";
        try {
            URL urlApi = url;
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

            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                data += output;
            }
        }   catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        return data;
    }

}
