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
public class FeuCalculee {
    
    private static int count = 1;
    private int id;
    private Coord positionCalculee;
    private int zone;
    private int intensiteCalculee;// entre 1 et 10

    public FeuCalculee(Coord position, int zone, int intensite) {
        this.id = count++;
        this.positionCalculee = position;
        this.zone = zone;
        this.intensiteCalculee = intensite;
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

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public int getIntensiteCalculee() {
        return intensiteCalculee;
    }

    public void setIntensiteCalculee(int intensite) {
        this.intensiteCalculee = intensite;
    }
    
    public String toString() {
        return "Feucalculee "+ getId() + " placé en " + getPositionCalculee() + " -> intensité : " + getIntensiteCalculee();
    }
    
}
