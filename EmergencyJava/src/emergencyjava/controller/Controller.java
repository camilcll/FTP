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
import emergencyjava.model.FeuCalculee;
import emergencyjava.model.Intervention;
import emergencyjava.model.Vehicule;
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
    
    public static synchronized void start(ArrayList<Caserne> listcaserne){
        askData(listcaserne);
    }
    
    public static void askData(ArrayList<Caserne> listcaserne){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                try {
                    System.out.println("debut requete");
                    URL url = new URL("http://164.4.1.5:5000/api/emergency/capteur");
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
                System.out.println("Ask to receive data debut");
            }
            
        }, 5000, 80000);
    }
    
    public static void checkCapteur(String data, ArrayList<Caserne> listcaserne){
        
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("check capteurs");
        
        ArrayList tabCapteurActif;
        tabCapteurActif = new ArrayList<Capteur>();
        int i = 0;
        
        //data = "[{\"id\":1,\"position\":{\"x\":5,\"y\":5},\"intensite\":8},{\"id\":1,\"position\":{\"x\":5,\"y\":15},\"intensite\":0},{\"id\":1,\"position\":{\"x\":15,\"y\":5},\"intensite\":0},{\"id\":1,\"position\":{\"x\":15,\"y\":15},\"intensite\":0}]";
        
        try {
            List<Capteur>
                    listCapteur = mapper.readValue(data, new TypeReference<List<Capteur>>(){});
                    System.out.println(listCapteur.toString());
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
        
        creerFeu(tabCapteurActif, listcaserne);
    }
    
    public static void creerFeu(ArrayList<Capteur> listcapteur, ArrayList<Caserne> listcaserne){
        
        ArrayList<Capteur> listcapteurvoisin;
        listcapteurvoisin = new ArrayList<Capteur>();
        
        ArrayList listcapteurvoisinintensite = null;
        listcapteurvoisin = new ArrayList();
        
        FeuCalculee feu = null;
        
        for(Capteur capteurActif : listcapteur){
            if (Capteur.estVoisinDe(listcapteur, capteurActif).isEmpty()){
                System.out.println("Pas de voisin actif");
                System.out.println("le feu est dans la zone du capteur" + capteurActif.getId());
                if(capteurActif.getIntensite() == 8){
                    feu = new FeuCalculee(capteurActif.getPosition(), 3, 6);
                }else{
                    feu  = new FeuCalculee(capteurActif.getPosition(), 5, 2);
                }
            }else if(Capteur.estVoisinDe(listcapteur, capteurActif).size() == 1){
                listcapteurvoisin = Capteur.estVoisinDe(listcapteur, capteurActif);
                Capteur capteurvoisin = (Capteur) listcapteurvoisin.get(0);
                int xFeuCalculee = 0;
                int yFeuCalculee = 0;
                System.out.println("1 voisin actif");
                System.out.println("le feu est dans la zone entre le capteur" + capteurActif.getId() + "et le capteur" + capteurvoisin.getId());
                if(capteurActif.getIntensite() == capteurvoisin.getIntensite()){
                    xFeuCalculee = (capteurActif.getPosition().getX() + capteurvoisin.getPosition().getX())/2;
                    yFeuCalculee = (capteurActif.getPosition().getY() + capteurvoisin.getPosition().getY())/2;
                    if(capteurActif.getIntensite() < 6){
                        feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), (capteurActif.getIntensite() - 2)/2, capteurActif.getIntensite() - 2);
                    }else if(capteurActif.getIntensite() == 6){
                        feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), 7/2, 7);
                    }else if (capteurActif.getIntensite() == 7 || capteurActif.getIntensite() == 8){
                        xFeuCalculee = (capteurActif.getPosition().getX() + capteurvoisin.getPosition().getX())/2;
                        yFeuCalculee = (capteurActif.getPosition().getY() + capteurvoisin.getPosition().getY())/2;
                        feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), 4, 8);
                    }
                } else if(capteurActif.getIntensite() == 8 && capteurvoisin.getIntensite() < 7){
                    if(capteurActif.getPosition().getX() == capteurvoisin.getPosition().getX()){
                        xFeuCalculee = capteurActif.getPosition().getX();
                        yFeuCalculee = ((7/10)*capteurActif.getPosition().getY()) + ((3/10)*capteurvoisin.getPosition().getY());
                    }else{
                        xFeuCalculee = ((7/10)*capteurActif.getPosition().getY()) + ((3/10)*capteurvoisin.getPosition().getY());
                        yFeuCalculee = capteurActif.getPosition().getX();
                    }
                    feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), 5, 8);
                }else if(capteurActif.getIntensite() < 7 && capteurvoisin.getIntensite() == 8){
                    if(capteurActif.getPosition().getX() == capteurvoisin.getPosition().getX()){
                        xFeuCalculee = capteurActif.getPosition().getX();
                        yFeuCalculee = ((3/10)*capteurActif.getPosition().getY()) + ((7/10)*capteurvoisin.getPosition().getY());
                    }else{
                        xFeuCalculee = ((3/10)*capteurActif.getPosition().getY()) + ((7/10)*capteurvoisin.getPosition().getY());
                        yFeuCalculee = capteurActif.getPosition().getX();
                    }
                    feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), 5, 8);
                    listcapteur.remove(capteurvoisin);
                }else{
                    xFeuCalculee = (capteurActif.getPosition().getX() + capteurvoisin.getPosition().getX())/2;
                    yFeuCalculee = (capteurActif.getPosition().getY() + capteurvoisin.getPosition().getY())/2;
                    feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), 5, 8);
                }
            }else if(Capteur.estVoisinDe(listcapteur, capteurActif).size() == 2){
                listcapteurvoisin = Capteur.estVoisinDe(listcapteur, capteurActif);
                int xFeuCalculee = 0;
                int yFeuCalculee = 0;
                Capteur capteurvoisin1 = (Capteur) listcapteurvoisin.get(0);
                Capteur capteurvoisin2 = (Capteur) listcapteurvoisin.get(1);
                
                int x1 = ((capteurActif.getIntensite()/(capteurActif.getIntensite()+capteurvoisin1.getIntensite()))*capteurActif.getPosition().getX()) 
                            + ((capteurvoisin1.getIntensite()/(capteurActif.getIntensite()+capteurvoisin1.getIntensite()))*capteurvoisin1.getPosition().getX());
                int y1 = ((capteurActif.getIntensite()/(capteurActif.getIntensite()+capteurvoisin1.getIntensite()))*capteurActif.getPosition().getY()) 
                        + ((capteurvoisin1.getIntensite()/(capteurActif.getIntensite()+capteurvoisin1.getIntensite()))*capteurvoisin1.getPosition().getY());

                int x2 = ((capteurActif.getIntensite()/(capteurActif.getIntensite()+capteurvoisin2.getIntensite()))*capteurActif.getPosition().getX()) 
                            + ((capteurvoisin2.getIntensite()/(capteurActif.getIntensite()+capteurvoisin2.getIntensite()))*capteurvoisin2.getPosition().getX());
                int y2 = ((capteurActif.getIntensite()/(capteurActif.getIntensite()+capteurvoisin2.getIntensite()))*capteurActif.getPosition().getY()) 
                        + ((capteurvoisin2.getIntensite()/(capteurActif.getIntensite()+capteurvoisin2.getIntensite()))*capteurvoisin2.getPosition().getY());
                
                int a1 = (capteurvoisin2.getPosition().getY()-y1)/(capteurvoisin2.getPosition().getX()-x1);
                int b1 = y1 - a1*x1 ;
                int a2 = (capteurvoisin1.getPosition().getY()-y2)/(capteurvoisin1.getPosition().getX()-x2);
                int b2 = y2 - a2*x2;
                
                xFeuCalculee = -(b1-b2)/(a1-a2);
                yFeuCalculee = a1 * xFeuCalculee + b1;
                
                //feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), , );
                
                listcapteur.remove(capteurvoisin1);
                listcapteur.remove(capteurvoisin2);
                
            }else if(Capteur.estVoisinDe(listcapteur, capteurActif).size() == 3){
                listcapteurvoisin = Capteur.estVoisinDe(listcapteur, capteurActif);
                int xFeuCalculee = 0;
                int yFeuCalculee = 0;
                Capteur capteurvoisin1 = (Capteur) listcapteurvoisin.get(0);
                Capteur capteurvoisin2 = (Capteur) listcapteurvoisin.get(1);
                Capteur capteurvoisin3 = (Capteur) listcapteurvoisin.get(2);
                
                ArrayList<Capteur> capteurangle = null;
                Capteur capteuroppose = null;
                
                
                if((capteurActif.getIntensite() == capteurvoisin1.getIntensite()) && 
                        (capteurActif.getIntensite() == capteurvoisin2.getIntensite()) && 
                        (capteurActif.getIntensite() == capteurvoisin3.getIntensite())){
                    xFeuCalculee = (capteurActif.getPosition().getX() + capteurvoisin1.getPosition().getX() + capteurvoisin2.getPosition().getX() + capteurvoisin3.getPosition().getX())/4;
                    yFeuCalculee = (capteurActif.getPosition().getY() + capteurvoisin1.getPosition().getY() + capteurvoisin2.getPosition().getY() + capteurvoisin3.getPosition().getY())/4;
                    if(capteurActif.getIntensite() == 2){
                        feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), 1, 2);
                    }else if(capteurActif.getIntensite() == 3){
                        feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), 2, 4);
                    }else{
                        feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), (capteurActif.getIntensite()-1)/2, capteurActif.getIntensite()-1);
                    }
                }else{
                    for(Capteur capteur : listcapteurvoisin){
                        int d = (capteurActif.getPosition().getX()-capteur.getPosition().getX())*(capteurActif.getPosition().getX()-capteur.getPosition().getX()) + 
                                (capteurActif.getPosition().getY()-capteur.getPosition().getY())*(capteurActif.getPosition().getY()-capteur.getPosition().getY());
                        if(d == 10){
                             capteurangle.add(capteur);
                        }else{
                            capteuroppose = capteur;
                        }
                    }
                    
                    int x1 = ((capteurActif.getIntensite()/(capteurActif.getIntensite()+capteurangle.get(0).getIntensite()))*capteurActif.getPosition().getX()) 
                            + ((capteurangle.get(0).getIntensite()/(capteurActif.getIntensite()+capteurangle.get(0).getIntensite()))*capteurangle.get(0).getPosition().getX());
                    int y1 = ((capteurActif.getIntensite()/(capteurActif.getIntensite()+capteurangle.get(0).getIntensite()))*capteurActif.getPosition().getY()) 
                            + ((capteurangle.get(0).getIntensite()/(capteurActif.getIntensite()+capteurangle.get(0).getIntensite()))*capteurangle.get(0).getPosition().getY());
                    
                    int x2 = ((capteuroppose.getIntensite()/(capteuroppose.getIntensite()+capteurangle.get(1).getIntensite()))*capteuroppose.getPosition().getX()) 
                            + ((capteurangle.get(1).getIntensite()/(capteuroppose.getIntensite()+capteurangle.get(1).getIntensite()))*capteurangle.get(1).getPosition().getX());
                    int y2 = ((capteuroppose.getIntensite()/(capteuroppose.getIntensite()+capteurangle.get(1).getIntensite()))*capteuroppose.getPosition().getY()) 
                            + ((capteurangle.get(1).getIntensite()/(capteuroppose.getIntensite()+capteurangle.get(1).getIntensite()))*capteurangle.get(1).getPosition().getY());
                    
                    
                    int x3 = ((capteurActif.getIntensite()/(capteurActif.getIntensite()+capteurangle.get(1).getIntensite()))*capteurActif.getPosition().getX()) 
                            + ((capteurangle.get(1).getIntensite()/(capteurActif.getIntensite()+capteurangle.get(1).getIntensite()))*capteurangle.get(1).getPosition().getX());
                    int y3 = ((capteurActif.getIntensite()/(capteurActif.getIntensite()+capteurangle.get(1).getIntensite()))*capteurActif.getPosition().getY()) 
                            + ((capteurangle.get(1).getIntensite()/(capteurActif.getIntensite()+capteurangle.get(1).getIntensite()))*capteurangle.get(1).getPosition().getY());
                    
                    int x4 = ((capteuroppose.getIntensite()/(capteuroppose.getIntensite()+capteurangle.get(0).getIntensite()))*capteuroppose.getPosition().getX()) 
                            + ((capteurangle.get(0).getIntensite()/(capteuroppose.getIntensite()+capteurangle.get(0).getIntensite()))*capteurangle.get(0).getPosition().getX());
                    int y4 = ((capteuroppose.getIntensite()/(capteuroppose.getIntensite()+capteurangle.get(0).getIntensite()))*capteuroppose.getPosition().getY()) 
                            + ((capteurangle.get(0).getIntensite()/(capteuroppose.getIntensite()+capteurangle.get(0).getIntensite()))*capteurangle.get(0).getPosition().getY());
                    
                    int a1 = (y2-y1)/(x2-x1);
                    int b1 = y1 - a1*x1 ;
                    int a2 = (y4-y3)/(x4-x3);
                    int b2 = y3 - a2*x3;
                    
                    xFeuCalculee = -(b1-b2)/(a1-a2);
                    yFeuCalculee = a1 * xFeuCalculee + b1;

                   //feu = new FeuCalculee(new Coord(xFeuCalculee, yFeuCalculee), , );
                    
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
                    feu = new FeuCalculee(new Coord(capteurcentre.getPosition().getX(), capteurcentre.getPosition().getY()), 3, 6);
                }else{
                    feu = new FeuCalculee(new Coord(capteurcentre.getPosition().getX(), capteurcentre.getPosition().getY()), 5, 8);
                }
                
                listcapteur.remove(capteurvoisin1);
                listcapteur.remove(capteurvoisin2);
                listcapteur.remove(capteurvoisin3);
                listcapteur.remove(capteurvoisin4);
                
            }
            
            System.out.println(feu.toString());
            
            if(checkFeu(feu)){
                sauvegarderFeu(feu);
                creerIntervention(feu, listcaserne);
            }
        }
    }
    
    public static void sauvegarderFeu(FeuCalculee feu) {
        class OneShotTask implements Runnable {
            FeuCalculee str;
            OneShotTask(FeuCalculee feu) { str = feu; }
            public void run() {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String data = mapper.writeValueAsString(str).toString();
                    System.out.println(data);
                    
                    URL url = new URL("http://164.4.1.5:5000/api/emergency/feu");
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
        Thread t = new Thread(new OneShotTask(feu));
        t.start();
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
    
    public static boolean checkFeu(FeuCalculee feu) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("debut requete");
            URL url = new URL("http://164.4.1.5:5000/api/emergency/feu");
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

            List<FeuCalculee> listFeu = mapper.readValue(data, new TypeReference<List<FeuCalculee>>(){});

            for(FeuCalculee feuExistant : listFeu){
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
            System.out.println("Ask to receive data check feu ");
        return true;
    }
    
    public static void creerIntervention(FeuCalculee feu, ArrayList<Caserne> listcaserne){
        int xFeu = feu.getPositionCalculee().getX();
        int yFeu = feu.getPositionCalculee().getY();
        int nbcamion = 0;
        int nbvoiture = 0;
        int intensite = feu.getIntensiteCalculee();
        Caserne caserne = null;
        
        ArrayList listcasernevoisin;
        listcasernevoisin = new ArrayList();
        
        ArrayList listvehicule;
        listvehicule = new ArrayList<Vehicule>();
        
        for (int i = 0; i<4; i++){
            int d = (xFeu-listcaserne.get(i).getPosition().getX())*(xFeu-listcaserne.get(i).getPosition().getX()) 
                + (yFeu-listcaserne.get(i).getPosition().getY())*(yFeu-listcaserne.get(i).getPosition().getY());
            listcasernevoisin.add(d);
        }
        
        int index = 0;
        int numcaserne = 0;
        int etat = 0; // etat = 0 -> en cours , 1 -> en attente, 2 -> terminé
        
        boolean temp = false;
        while (!temp){
            index = listcasernevoisin.indexOf(Collections.min(listcasernevoisin));
            if(intensite <= 3){
                System.out.println("Petit Feu -> 1 Camion ou 2 voitures");
                if(listcaserne.get(index).checkVehiculeDispo(1, 0)){
                    numcaserne = index;
                    nbcamion = 1;
                    temp = true;
                }else if(listcaserne.get(index).checkVehiculeDispo(0, 2)){
                    numcaserne = index;
                    nbvoiture = 2;
                    temp = true;
                }else{
                    listcasernevoisin.remove(index);
                }
            }else if(intensite > 3 && intensite <= 6){
                System.out.println("Moyen Feu -> 1 camion et une voiture");
                if(listcaserne.get(index).checkVehiculeDispo(1, 1)){
                    numcaserne = index;
                    nbcamion = 1;
                    nbvoiture = 1;
                    temp = true;
                }else{
                    listcasernevoisin.remove(index);
                }
            }else{
                System.out.println("Gros Feu -> 2 camions et 1 voiture");
                if(listcaserne.get(index).checkVehiculeDispo(2, 1)){
                    numcaserne = index;
                    nbcamion = 2;
                    nbvoiture = 1;
                    temp = true;
                }else{
                    listcasernevoisin.remove(index);
                }
            }
            if(listcasernevoisin.isEmpty()){
                System.out.println("Aucune caserne dispo");
                etat = 1;
                temp = true;
            }
        }
        
        for(Vehicule vehicule : listcaserne.get(numcaserne).getListeVehicule()){
            if(vehicule.isDisponible() && vehicule.getType() == "Camion" && nbcamion > 0){  
                vehicule.setDisponible(false);
                listvehicule.add(vehicule);
                nbcamion--;
            }
            
            if(vehicule.isDisponible() && vehicule.getType() == "Voiture" && nbvoiture > 0){
                vehicule.setDisponible(false);
                listvehicule.add(vehicule);
                nbvoiture--;
            }
        }
        
        Intervention inter = new Intervention(feu, listvehicule, etat);
        EnvoyerIntervention(inter);
    }
    
    public static void EnvoyerIntervention(Intervention intervention) {
        class OneShotTask implements Runnable {
            Intervention str;
            OneShotTask(Intervention intervention) { str = intervention; }
            public void run() {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String data = mapper.writeValueAsString(str).toString();
                    System.out.println(data);
                    
                    URL url = new URL("http://164.4.1.5:5000/api/emergency/intervention");
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
                System.out.println("send data intervention");
            }
        }
        Thread t = new Thread(new OneShotTask(intervention));
        t.start();
    }
    
    
    
    
}
