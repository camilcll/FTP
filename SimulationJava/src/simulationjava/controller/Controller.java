/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationjava.controller;

import java.util.Random;
import simulationjava.model.Capteur;
import simulationjava.model.Coord;
import simulationjava.model.Feu;

/**
 *
 * @author Sami
 */
public class Controller {

    public Controller() {
    }
    
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
        int range = (int) Math.ceil(intensiteFeu / 2);
        
        for(Capteur capteur : tabCapteur){
            int temp = checkCercle(xFeu, yFeu, range, capteur.getPosition().getX(), capteur.getPosition().getY(), capteur.getRange());
            if (temp < 0 ){
                System.out.println("Capteur " + capteur.getId()+ " detecte le feu");
                if(temp > -6){
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
    
}
