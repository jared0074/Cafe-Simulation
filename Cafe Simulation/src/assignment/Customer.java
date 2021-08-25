/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class Customer implements Runnable{
    public static final List<Customer> allCustomer = new ArrayList<Customer>();
    int custID; //customer ID
    String custName; //customer name
    public boolean closingTime =false; //closing time for customer
    boolean leave = false; //customer leave
    long duration = 0; //duration for drinking time
    CafeStatus cafeStatus; //shop status for checking available seat
    Cafe cf; //cafe class
    CustomerGenerator cg = new CustomerGenerator(cafeStatus,cf); //customer generator class 
   
    
    public Customer (Cafe c,int customerID,CafeStatus cafeStatus){
        this.cf = c; //cafe class passing
        this.custID = customerID; //customer id passing
        this.cafeStatus = cafeStatus; //
        this.custName = ("Thread-Customer "+ customerID + " :");
    }
    

    public void run() {
        cf.goIn(this);
        // going to enter the cafe
        System.out.println(custName + " attempt getting in Cafe.");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        // synchronize with cafe seats
        synchronized (cafeStatus.seat) {
            // if seats is not enough 
           
            //if the seat is not empty
            while (cafeStatus.tableSeat < 1) {
                //show that customer is waiting on queue
                
                try {
                    //waiting for a available seat
                    System.out.println(custName + " waiting in queue.");
                    cafeStatus.seat.wait();
                    
                    //check whether or not it is closing time
                    if(closingTime){
                        leave = true; //if it is closing time, leave equals to true
                        System.out.println(custName + " are forced to leave because cafe is closing"); //customer will be forced to leave because cafe is closing
                        break; //exit the loop
                    }
                    //needs to queue to enter
                } catch (InterruptedException ex) {
                    Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        //check whether leave condition is false
        //if it is not false, customer can continue to enter
        if (!leave) {
            // if there's seat
            try {
                cafeStatus.tableSeat--; //one person has occupied the seat
            } catch (Exception e) {
                e.getMessage();
            }
            // customer enters the cafe

            cf.enter(this); //customer enters the cafe

            cf.leave(this); //customer leaves the cafe

            // synchronized with cafe seats
            synchronized (cafeStatus.seat) {

                //release 1 seat and notify
                cafeStatus.tableSeat++;
                //if it not closing time
                if(!closingTime){
                    cafeStatus.seat.notify(); //notify there's available seat
                }
            }

            
        }

    }
    
    //set closing time for customer class and force customer to leave
    public void setClosingTime() {
        //synchronized cafe status seat
        synchronized(cafeStatus.seat){
            this.closingTime = true; //set customer closing time to true
            cafeStatus.seat.notifyAll(); //notify all the waiting customer
        }
    }
    

    
}
