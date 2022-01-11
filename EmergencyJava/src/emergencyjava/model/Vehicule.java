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
public class Vehicule {
    
    private static int count = 1;
    private int id;
    private String type;
    private int idcaserne;
    private boolean disponible;

    public Vehicule(String type, int idcaserne, boolean disponible) {
        this.id = count++;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Vehicule{" + "id=" + id + ", type=" + type + ", disponible=" + disponible + '}';
    }

   
    
    
    
    
}
