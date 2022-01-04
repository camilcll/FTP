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
public class Capteur {
    
    private static int count = 1;
    private int id;
    private Coord position;
    private static int range = 5;// max = 5
    private int intensite;// entre 1 et 10
    
    public Capteur(Coord position, int intensite) {
        this.id = count++;
        this.position = position;
        this.intensite = intensite;
    }

    public int getId() {
        return id;
    }

    public Coord getPosition() {
        return position;
    }

    public static int getRange() {
        return range;
    }

    public int getIntensite() {
        return intensite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(Coord position) {
        this.position = position;
    }

    public static void setRange(int range) {
        Capteur.range = range;
    }

    public void setIntensite(int intensite) {
        this.intensite = intensite;
    }
    
    public static Capteur getCapteurById(Capteur[] capteurs, int idRecherche){
        for(Capteur capteur : capteurs){
            if (capteur.id == idRecherche){
                return capteur;
            }
        }
        System.out.println("Le capteur n'existe pas frero");
        return null;
    }
    
    public String toString() {
        return "Capteur "+ getId() + " placé en " + getPosition() + " -> intensité : " + getIntensite();
    }
    
    
}
