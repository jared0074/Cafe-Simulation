/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vazeer
 */
public class Waiter implements Runnable {
    String name = "Thread-Waiter :"; //waiter name
    Cafe cafe; //cafe class
    Statistics stats; //statistic class
    public boolean nearClosing = false; //near closing set to false
    boolean available = true; // check for availability of waiter, set to true
    int numCust = 20; // number of customer desired to be generated, can be modified
    
    public Waiter(Cafe cafe)
    {
        this.cafe = cafe; //pass cafe class to waiter
    }

    public void run()
    {
        //while not near closing, customer will continue to serve
        while(!nearClosing){
            // when number of customer desired to be generate not equals to total customer served, meaning not all customer served
            if(numCust != stats.customerServed.intValue()){
                cafe.Serve(this.name); //serve function
            }else{ //else when waiter had served all the customer, sleep to wait clock to call near closing time
                try {
                    Thread.sleep(4000); //wait for 4 seconds
                } catch (InterruptedException ex) {
                    Logger.getLogger(Waiter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //if it is near closing
        if(nearClosing){
            cafe.forceLeave(); //cafe force leave will notify owner if stuck waiting for customer
            System.out.println(name + " will now leave the cafe"); //after finish serving after near closing is called
            //waiter will leave
            try {
                Thread.sleep(5000); //wait for 5 seconds
            } catch (InterruptedException ex) {
                Logger.getLogger(Waiter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //function used to pass the latest statistics for owner
    public void addStats(Statistics stats){
        this.stats = stats;   //statistic stats pass to owner
    }
}
