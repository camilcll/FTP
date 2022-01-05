/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationjava;

import simulationjava.model.Capteur;
import simulationjava.model.Coord;
import simulationjava.controller.Controller;
import static simulationjava.controller.Controller.GenereFeu;

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
        
        for(Capteur capteur : tabCapteur){
            System.out.println(capteur.toString());
        }
        
        
    }
    
}
