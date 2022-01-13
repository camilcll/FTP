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
public class Vehicule {
    private static int count = 1;
    private int id;
    private Coord position;
    private String type;
    private int idcaserne;
    private boolean disponible;
    
    public Vehicule(){
        
    }

    public Vehicule(String type, Coord position, int idcaserne, boolean disponible) {
        this.id = count++;
        this.position = position;
        this.type = type;
        this.idcaserne = idcaserne;
        this.disponible = disponible;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdcaserne() {
        return idcaserne;
    }

    public void setIdcaserne(int idcaserne) {
        this.idcaserne = idcaserne;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Vehicule{" + "id=" + id + ", position=" + position + ", type=" + type + ", idcaserne=" + idcaserne + ", disponible=" + disponible + '}';
    }

    

}
