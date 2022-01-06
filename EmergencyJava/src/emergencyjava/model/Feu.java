/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emergencyjava.model;

/**
 *
 * @author Sami
 */
public class Feu {
    
    private static int count = 1;
    private int id;
    private Coord positionCalculee;
    private int intensite;// entre 1 et 10

    public Feu(Coord position, int intensite) {
        this.id = count++;
        this.positionCalculee = position;
        this.intensite = intensite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coord getPositionCalculee() {
        return positionCalculee;
    }

    public void setPositionCalculee(Coord position) {
        this.positionCalculee = position;
    }

    public int getIntensite() {
        return intensite;
    }

    public void setIntensite(int intensite) {
        this.intensite = intensite;
    }
    
    public String toString() {
        return "Feu "+ getId() + " placé en " + getPositionCalculee() + " -> intensité : " + getIntensite();
    }
    
}
