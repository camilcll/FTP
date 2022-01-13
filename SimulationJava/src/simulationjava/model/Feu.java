/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationjava.model;

/**
 *
 * @author Sami
 */
public class Feu {
     
    private static int count = 1;
    private int id;
    private Coord position;
    private int intensite;// entre 1 et 8
    private boolean detecte;
    
    public Feu(){
        super();
    }

    public Feu(Coord position, int intensite, boolean detecte) {
        this.id = count++;
        this.position = position;
        this.intensite = intensite;
        this.detecte = detecte;
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

    public int getIntensite() {
        return intensite;
    }

    public void setIntensite(int intensite) {
        this.intensite = intensite;
    }

    public boolean isDetecte() {
        return detecte;
    }

    public void setDetecte(boolean detecte) {
        this.detecte = detecte;
    }
    
    public String toString() {
        return "Feu "+ getId() + " placé en " + getPosition() + " -> intensité : " + getIntensite();
    }
}
