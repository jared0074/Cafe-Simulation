/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class CustomerGenerator implements Runnable{
    CafeStatus cafeStatus;  //cafe status class
    Cafe cafe;  // cafe class
    int custID = 1; //customer id number
    int numCust = 20; //number of customer needed to be generated, can be modified
    boolean closingTime = false; //closing time is false, use to notify and stop generating customer
        
    public CustomerGenerator (CafeStatus cafeStatus,Cafe cafe){
        this.cafeStatus = cafeStatus; //pass the cafe status to customer generator
        this.cafe = cafe; //pass the cafe class to customer generator
    }
    
    public void run(){
        //while it is not closing time
        while(!closingTime){
            // and if customer id is not greater or equals to the number customer desired to be generated
            if (custID <= numCust){
                try {
                    // generate new customer class
                    Customer customer = new Customer(cafe, custID,cafeStatus);
                    // create customer thread
                    Thread thcust = new Thread(customer);
                    // start customer thread
                    thcust.start();
                    custID ++; //increment customer ID
                    //wait for a random time and generate again
                    TimeUnit.SECONDS.sleep((long)(Math.random()*3)); // can be modified
                }catch(Exception e){
                    System.out.println("Error");
                }
            }
            else{
                try {
                    Thread.sleep(2000); //wait for 2 seconds
                } catch (InterruptedException ex) {
                    Logger.getLogger(CustomerGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //if closing time is called, customer generator will stop
        if(closingTime){
            try {
                Thread.sleep(5000); //sleep for 5 seconds
            } catch (InterruptedException ex) {
                Logger.getLogger(CustomerGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
