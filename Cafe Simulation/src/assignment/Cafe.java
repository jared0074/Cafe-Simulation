/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author jared
 */
public class Cafe {
    int order; // variable for the order, if 0 means it's fruit juice, if 1 means it's cappucino
    int numCust = 20; // number of customer desired to be generated, can be modified
    Owner owner = new Owner(this); // owner class
    Waiter waiter = new Waiter(this); // waiter class
    CafeStatus shopStatus; //status for checking whether there is still available seat in cafe, it is a class
    ServingArea serveArea; //serving area class
    Statistics stats; //statistics class
    String orderName; //the name of the order
    Object serving = new Object(); //object created for letting thread wait for serving when there's customer
    boolean closingTime = false; //closing time for cafe, set to false
    long duration = 0; //duration of the drinking time for customers
    
    LinkedList<Customer> listCustomer = new LinkedList<Customer>(); //list of customers
    public final List<Customer> allCustomer = new ArrayList<>(); //array list to contain all the customers
    
    //cafe class will pass the serving area, shop status and statistics class
    public Cafe(ServingArea serveArea,CafeStatus shopStatus,Statistics stats){
        this.serveArea = serveArea; //serving area
        this.shopStatus = shopStatus; //shop status
        this.stats = stats; //statistics
        owner.addStats(stats); //include statistics inside owner
        waiter.addStats(stats); //include statistics inside waiter
        CustomerGenerator custGen = new CustomerGenerator(shopStatus,this); //customer generator
        Thread thCustGen = new Thread(custGen); // Thread customer generator
        Thread thown = new Thread(owner); //thread owner
        Thread thwait = new Thread(waiter); //thread waiter
        Clock clock = new Clock(owner,waiter,custGen,this); //clock class
        Thread thclock = new Thread(clock); //thread clock
        
        
        thCustGen.start(); //start customer generator thread
        thown.start(); //start owner thread
        //thwait.start(); //start waiter thread
        thclock.start(); //start clock thread
    }
    
    
    //add customers into array list when customer thread runs 
    public void goIn(Customer customer){
        allCustomer.add(customer); //add customer inside array list
    }
    
    //function for customers to enter the cafe
    public void enter(Customer customer){
       
        System.out.println(customer.custName  + " has entered the Cafe.");
        listCustomer.offer(customer); //will insert customer into a list
        System.out.println("Available Seats :" + shopStatus.tableSeat); //print
        //synchronized serving
        synchronized(serving){ 
            serving.notify(); //notify owner or waiter thread that was waiting to serve a customer
        }
        synchronized(customer){
            try {
                customer.wait(); //customer waits to be served by waiter or owner
            } catch (InterruptedException ex) {
                Logger.getLogger(Cafe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //function for customer to leave
    public void leave(Customer customer){
        // leaving
        duration = (long) (Math.random() * 10); 
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(customer.custName + " has finished drinking in " + duration + " seconds");
        System.out.println(customer.custName + " is now leaving.");
    }
    
    //function for owner and waiter serving
    void Serve(String position) {
        

        //synchronize serving
        synchronized (serving){
            try{
                //check whether there's customer in list and check whether customer served is the total amount of customer today
                while (listCustomer.size() <= 0 && stats.customerServed.intValue() != numCust) {

                    serving.wait(); //wait if there's no customer and not customer served is not the total customer today

                }
            }catch(Exception e){
                System.out.println(e.toString());
            }
        }
        
        /*when closing time owner or waiter will force notify all
         if customer served is equals to number of customer today which indicates all customer has been served
         then i exits the function.*/
        if(stats.customerServed.intValue() == numCust){
            return;
        }
        
        //if not all customer is served, customer will be taken out from the list
        Customer customer = listCustomer.poll();

        
        //check whether owner or waiter is available
        if (owner.available == true) {
            // synchronized owner
            synchronized (owner) {
                try {
                    owner.available = false; //set owner available to false meaning owner is occupied
                    takeOrder(position, customer); //take order from customer
                    owner.addStats(stats); //update latest statistics for owner
                    waiter.addStats(stats); //update latest statistics for waiter
                    owner.available = true; //after taking order and making order is done, owner be available once again
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        } else {
            //synchronized waiter
            synchronized (waiter) {
                try {
                    
                  
                    waiter.available = false; //set waiter available to false meaning waiter is occupied
                    takeOrder(position, customer); //take order from customer
                    owner.addStats(stats); //update latest statistics for owner
                    waiter.addStats(stats); //update latest statistics for waiter
                    waiter.available = true;  //after taking order and making order is done, waiter be available once again
                   
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }

        synchronized (customer) {
            customer.notify(); /*once waiter and owner successfully served the customer, 
                                customer will notify and it will then leave the enter function*/
        }
    }
    
    //take order from customer function
    public void takeOrder(String position, Customer customer){
        System.out.println(position + " is now taking order from " + customer.custName);
        makeOrder(position,customer); //make order function
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cafe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //customer making order
    public void makeOrder(String position, Customer customer) {
        order = new Random().nextInt(2); //generate 0 or 1
        //if order is 0 means customer had ordered fruit juice
        //else is ordering coffee
        if (order == 0) {
            System.out.println(customer.custName + " had ordered Fruit Juice");
            stats.addJuiceOrder(); //add total juice ordered 
            try {
                makeJuice(position, order,customer);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cafe.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.orderName = "FRUIT JUICE";
            return;
        } else {
            System.out.println(customer.custName + " had ordered Cappucino");
            stats.addCappucinoOrder(); // add total cappucino ordered
            try {
                makeCappucino(position, order,customer);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cafe.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.orderName = "CAPPUCINO";
            return;
        }
    }
    
    //making fruit juice for customer
    public void makeJuice(String position, int order,Customer customer) throws InterruptedException {
        //take glass from cupboard
        serveArea.useCupboard(position, order);
        System.out.println(position + " is now making Fruit Juice");
        //using juice tap to make fruit juice
        serveArea.useJuiceTap(position); //can be modified in serving area
        System.out.println(position + " had finished making Fruit Juice and served to "+ customer.custName);
        stats.addCustomerServed();
    }

    //making cappucino for customer
    public void makeCappucino(String position, int order,Customer customer) throws InterruptedException {
        //check if resources are available
        if (serveArea.coffeeAvailable == true && serveArea.milkAvailable == true){
            serveArea.useCupboard(position, order);
        }else{
            System.out.println(position + " is waiting for cupboard to be available");
            //synchronized serving
            synchronized(serving){
                serving.wait(); //wait for serving
                serveArea.useCupboard(position, order); //use cupboard and take cup,milk and coffee for making cappucino
            }
        }
        System.out.println(position + " is now making Cappucino");
        //wait time for mixing cappucino
        Thread.sleep(1000); //can be modified
        //display 
        System.out.println(position + " had finished making Cappucino and served to " +customer.custName);
        System.out.println(position + " returning ingredients back to cupboard");
        stats.addCustomerServed();
        serveArea.coffeeAvailable = true; serveArea.milkAvailable = true;
        synchronized(serving){
            serving.notify();
        }
    }
    
    /*function to force owner or waiter to leave the class once all customer is served
      or once it is closing time*/
    public void forceLeave(){
        synchronized(serving){
            serving.notifyAll(); //calling all waiting threads waiter or owner
        }
    }
    
}
