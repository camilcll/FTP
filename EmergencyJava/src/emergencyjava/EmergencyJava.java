/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emergencyjava;

import emergencyjava.controller.Controller;
import emergencyjava.model.Capteur;
import emergencyjava.model.Caserne;
import emergencyjava.model.Coord;
import emergencyjava.model.Vehicule;
import java.util.ArrayList;

/**
 *
 * @author Sami
 */
public class EmergencyJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList listcaserne;
        listcaserne = new ArrayList<Caserne>();
        
        ArrayList listvehicule1;
        listvehicule1 = new ArrayList<Vehicule>();
        ArrayList listvehicule2;
        listvehicule2 = new ArrayList<Vehicule>();
        ArrayList listvehicule3;
        listvehicule3 = new ArrayList<Vehicule>();
        ArrayList listvehicule4;
        listvehicule4 = new ArrayList<Vehicule>();
        Vehicule vehicule = null;
       
        for(int i = 0; i<5; i++){
            vehicule = new Vehicule("Camion", new Coord(15,25), 1, true);
            listvehicule1.add(vehicule);
            vehicule = new Vehicule("Camion", new Coord(45,25), 2, true);
            listvehicule2.add(vehicule);
            vehicule = new Vehicule("Camion", new Coord(45,75), 3, true);
            listvehicule3.add(vehicule);
            vehicule = new Vehicule("Camion", new Coord(15,75), 4, true);
            listvehicule4.add(vehicule);
        }
        for(int i = 0; i<3; i++){
            vehicule = new Vehicule("Voiture", new Coord(15,25), 1, true);
            listvehicule1.add(vehicule);
            vehicule = new Vehicule("Voiture", new Coord(45,25), 2, true);
            listvehicule2.add(vehicule);
            vehicule = new Vehicule("Voiture", new Coord(45,75), 3, true);
            listvehicule3.add(vehicule);
            vehicule = new Vehicule("Voiture", new Coord(15,75), 4, true);
            listvehicule4.add(vehicule);
            
        }
        
        Caserne caserne1 = new Caserne(new Coord(15,25), listvehicule1);
        Caserne caserne2 = new Caserne(new Coord(45,25), listvehicule2);
        Caserne caserne3 = new Caserne(new Coord(45,75), listvehicule3);
        Caserne caserne4 = new Caserne(new Coord(15,75), listvehicule4);
        
        listcaserne.add(caserne1);
        listcaserne.add(caserne2);
        listcaserne.add(caserne3);
        listcaserne.add(caserne4);
        
        Controller.askData(listcaserne);
    }
    
}
