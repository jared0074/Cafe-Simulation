/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class Owner implements Runnable{
    String name = "Thread-Owner :"; //owners name
    Cafe cafe; //cafe class
    Customer customer; //customer class
    Statistics stats; //statistics class
    public boolean closingTime = false; //closing time is false, use for notifying closing time
    boolean available = true; //available is true, use for checking availability of owner
    boolean nearClosing = false; //near closing is false, use to notify owner to make last call
    private List<Customer> allCustomers ; //private list of customer
    int numCust = 20; // number of customer desired to be generated, can be modified
    
    public Owner(Cafe cafe)
    {
        this.cafe = cafe; // pass cafe class to owner
        
    }
    
 
    public void run() {
        
        //when it is not near closing serve customer
        while (!nearClosing) {
            // when number of customer desired to be generate not equals to total customer served
            if( numCust != stats.customerServed.intValue()){
                cafe.Serve(this.name); //serve customer
                allCustomers = cafe.allCustomer; //set all customer array list to owner all customer list
            }else{ //else when owner had served all the customer, sleep to wait clock to call near closing time and closing time
                try {
                    Thread.sleep(2000); //sleep for 2 second
                } catch (InterruptedException ex) {
                    Logger.getLogger(Owner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //while not closing time, but it is near closing
        while(!closingTime){
            cafe.forceLeave(); //cafe force leave will notify waiter if stuck waiting for customer
            System.out.println(name + " LAST CALL!!!"); //owner will shout last call
            //if number of customer not equals to number of customer served, means not all customer is served
            if (numCust != stats.customerServed.intValue()) {
                //force all customers to set closing time
                for (int i = 0; i < allCustomers.size(); i++) {
                    allCustomers.get(i).setClosingTime(); //set all closing time
                    stats.addTotalCust(); //set temporary all customer number
                }
                //while cafe customer list is not empty continue to serve
                while (!cafe.listCustomer.isEmpty()) {
                    cafe.Serve(this.name); //serve function for owner,continue serve
                }
            }
            else{ //else all the customer is served
                try {
                    //force all the customer to set closing time
                    for (int i = 0; i < allCustomers.size(); i++) {
                        allCustomers.get(i).setClosingTime();
                        stats.addTotalCust(); //set temporary all customer number
                    }
                    Thread.sleep(10000); //wait for 10 seconds
                } catch (InterruptedException ex) {
                    Logger.getLogger(Owner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //if it is closing time
        if(closingTime){
            //while cafe customer list is not empty, owner needs to serve all customers
            while (!cafe.listCustomer.isEmpty()) {
                cafe.Serve(this.name); //serve customer function
            }
            //once done, owner will print out he had served all customers and wait for customers to leave
            System.out.println(name + " has served all the Customer");
            try {
                Thread.sleep(5000); //wait for 5 seconds
            } catch (InterruptedException ex) {
                Logger.getLogger(Owner.class.getName()).log(Level.SEVERE, null, ex);
            }

            stats.displayTotalStatistics(); //display the total statistics of the day
            try {
                Thread.sleep(2000); //wait for 2 seconds
            } catch (InterruptedException ex) {
                Logger.getLogger(Owner.class.getName()).log(Level.SEVERE, null, ex);
            }
            //owner will now leave the cafe 
            System.out.println(name + " will now leave the Cafe");
            try {
                Thread.sleep(2000); //wait for 2 seconds and proceed

            } catch (InterruptedException ex) {
                Logger.getLogger(Owner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //function used to pass the latest statistics for owner
    public void addStats(Statistics stats){
        this.stats = stats; //statistic stats pass to owner
    }
}
