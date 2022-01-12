/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationjava.model;

import java.util.ArrayList;

/**
 *
 * @author Sami
 */
public class Caserne {
    
    private static int count = 1;
    private int id;
    private Coord position;
    private ArrayList<Vehicule> listeVehicule;

    public Caserne(Coord position, ArrayList<Vehicule> listeVehicule) {
        this.id = count++;
        this.position = position;
        this.listeVehicule = listeVehicule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coord getPosition() {
        return position;
    }

    public void setPosition(Coord position) {
        this.position = position;
    }

    public ArrayList<Vehicule> getListeVehicule() {
        return listeVehicule;
    }

    public void setListeVehicule(ArrayList<Vehicule> listeVehicule) {
        this.listeVehicule = listeVehicule;
    }

    @Override
    public String toString() {
        return "Caserne{" + "id=" + id + ", position=" + position + ", listeVehicule=" + listeVehicule + '}';
    }
    
    public boolean checkVehiculeDispo(int nbcamion, int nbvoiture) {
        int nbcamiondispo = 0;
        int nbvoituredispo = 0;
        
        for(Vehicule vehicule : this.listeVehicule){
            if(vehicule.isDisponible() && vehicule.getType() == "Camion"){
                nbcamiondispo++;
            }
            if(vehicule.isDisponible() && vehicule.getType() == "Voiture"){
                nbvoituredispo++;
            }
        }
        
        if(nbcamiondispo > nbcamion && nbvoituredispo > nbvoiture){
            return true;
        }
        
        return false;
    }
    
}
