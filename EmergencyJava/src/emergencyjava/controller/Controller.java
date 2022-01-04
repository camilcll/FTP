/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emergencyjava.controller;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Sami
 */
public class Controller {
    
    public void askData(){
        
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                System.out.println("Ask to receive data");
            }
            
        }, 5000, 10000);
    }
    
}
