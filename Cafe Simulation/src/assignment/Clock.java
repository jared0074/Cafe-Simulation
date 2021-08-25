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
 * @author jared
 */
public class Clock implements Runnable{
    private CustomerGenerator customerGenerator; //customer generator class
    private Owner owner; //owner class
    private Waiter waiter; //waiter class
    private Cafe cafe; //cafe class
    
    public Clock(Owner owner, Waiter waiter,CustomerGenerator customerGenerator,Cafe cafe){
       this.owner = owner; //pass owner to clock
       this.waiter = waiter; //pass waiter to clock
       this.customerGenerator = customerGenerator; //pass customer generator to clock
       this.cafe = cafe; //pass cafe to clock
    }
    
    public void run() {
        try {
            //operating duration
            Thread.sleep(30000);
            //notify last call for owner and waiter
            NotifyLastCall();
            //operating duration after last call
            Thread.sleep(7000);
            //notify closing time for owner, customer generator and cafe
            NotifyClosingTime();
        } catch (InterruptedException ex) {
            Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //call last call function to notify owner and waiter
    public synchronized void NotifyLastCall(){
        System.out.println("----------------IT IS ALMOST CLOSING TIME----------------");
        waiter.nearClosing = true; //set near closing time for waiter to be true
        owner.nearClosing = true; //set near closing time for owner to be true
    }
    
    //call closing time function to notify owner, customer generator and cafe
    public synchronized void NotifyClosingTime(){
        System.out.println("------------------CAFE IS NOW CLOSED !---------------------");
        owner.closingTime = true; //set closing time for owner to be true
        customerGenerator.closingTime = true; //set closing time for customer generator to be true
        cafe.closingTime = true; //set closing time for cafe to be true
    }
}
