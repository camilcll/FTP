/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emergencyjava.model;

import java.util.ArrayList;

/**
 *
 * @author Sami
 */
public class Intervention {
    
    private static int count = 1;
    private int id;
    private FeuCalculee feu;
    private ArrayList<Vehicule> listeVehicule;
    private int etat;

    public Intervention(FeuCalculee feu, ArrayList<Vehicule> listevehicule, int etat) {
        this.id = count++;
        this.feu = feu;
        this.listeVehicule = listevehicule;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FeuCalculee getFeu() {
        return feu;
    }

    public void setFeu(FeuCalculee feu) {
        this.feu = feu;
    }

    public ArrayList<Vehicule> getListeVehicule() {
        return listeVehicule;
    }

    public void setListeVehicule(ArrayList<Vehicule> listeVehicule) {
        this.listeVehicule = listeVehicule;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Intervention{" + "id=" + id + ", feu=" + feu + ", listeVehicule=" + listeVehicule + ", etat=" + etat + '}';
    }
    
}
