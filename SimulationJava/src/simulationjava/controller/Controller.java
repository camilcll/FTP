/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationjava.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import simulationjava.model.Capteur;
import simulationjava.model.Coord;
import simulationjava.model.Feu;

/**
 *
 * @author Sami
 */
public class Controller {
    
    public static void GenereFeu(Capteur[] tabCapteur){
        int x = new Random().nextInt(101);
        int y = new Random().nextInt(61);
        Coord position = new Coord(x, y);
        
        int intensite = new Random().nextInt(9);
        if (intensite == 0){
            intensite++;
        }
        
        Feu feu = new Feu(position, intensite);
        System.out.println(feu.toString());
        CapteurDetecteFeu(feu, tabCapteur);
        
    }
    
    public static void CapteurDetecteFeu(Feu feu, Capteur[] tabCapteur){
        Coord positionFeu = feu.getPosition();
        int xFeu = positionFeu.getX();
        int yFeu = positionFeu.getY();  
        int intensiteFeu = feu.getIntensite();
        int range = (int) Math.ceil((double)intensiteFeu / 2);
        
        JSONObject jsonCapteur = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        int i = 0;
        String capteurJson = "";
        String[] listeCapteurJson;
        listeCapteurJson = new String[60];
        
        for(Capteur capteur : tabCapteur){
            try {
                listeCapteurJson[i] = mapper.writeValueAsString(capteur).toString();
                System.out.println(listeCapteurJson[i]);
                i++;
            } catch (JsonProcessingException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            int temp = checkCercle(xFeu, yFeu, range, capteur.getPosition().getX(), capteur.getPosition().getY(), capteur.getRange());
            if (temp <= 0 ){
                System.out.println("Capteur " + capteur.getId()+ " detecte le feu");
                if ((Math.pow((capteur.getPosition().getX() - xFeu), 2) + Math.pow((capteur.getPosition().getY() - yFeu), 2)) < (Math.pow(range, 2))){
                    capteur.setIntensite(8);
                }else if((temp <= 0 ) && (temp > -6)){
                    capteur.setIntensite(1);
                }else if((temp < -6) && (temp > -12)){
                    capteur.setIntensite(2);
                }else if((temp < -12) && (temp > -18)){
                    capteur.setIntensite(3);
                }else if((temp < -18) && (temp > -24)){
                    capteur.setIntensite(4);
                }else if((temp < -24) && (temp > -30)){
                    capteur.setIntensite(5);
                }else if((temp < -30) && (temp > -36)){
                    capteur.setIntensite(6);
                }else if((temp < -36) && (temp >  -42)){
                    capteur.setIntensite(7);
                }else if(temp < -42){
                    capteur.setIntensite(8);
                }
            }
            
        }
        
        System.out.println(Arrays.toString(listeCapteurJson));
       
    } 
    
    public static int checkCercle(int xFeu, int yFeu, int rangeFeu, int x, int y, int range){
        int d2 = (xFeu-x)*(xFeu-x) + (yFeu-y)*(yFeu-y);
        int d1 = (rangeFeu + range)*(rangeFeu + range);
        if (d2 > d1){
            return d2-d1;
        }else{
           return d2-d1;
        }
    }
    
    /*public static void sendData(String data){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                try {
                    URL url = new URL("https://reqbin.com/echo/post/json");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
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
                System.out.println("send data");
            }
            
        }, 5000, 10000);
    }*/
}
