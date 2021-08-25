/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jared
 */
public class Statistics {
    
    AtomicInteger juiceOrder = new AtomicInteger(); //total fruit juice ordered
    AtomicInteger coffeeOrder = new AtomicInteger(); //total coffee ordered
    AtomicInteger customerServed = new AtomicInteger(); //total customer served
    AtomicInteger totalCustomer = new AtomicInteger(); //total customer left because of cafe closed
    int finalCustLeft; //final value to be calculated
    int finalCustServed;
    
    //function to increment juice order
    public synchronized void addJuiceOrder(){
        juiceOrder.getAndAdd(1);
    }
    
    //function to increment cappucino order
    public synchronized void addCappucinoOrder(){
        coffeeOrder.getAndAdd(1);
    }
    
    //function to increment cappucino order
    public synchronized void addCustomerServed(){
        customerServed.getAndAdd(1);
    }
    
    //function to increment add total customer
    public synchronized void addTotalCust(){
        totalCustomer.getAndAdd(1);
    }
    
    //function for displaying last statistics when all customers had left
    public void displayTotalStatistics() {
        finalCustLeft = totalCustomer.intValue(); //turn atomic integer total customer to integer value and pass to finalCusLeft
        finalCustServed = customerServed.intValue();  //turn atomic integer total customer to integer value and pass to finalCusLeft
        finalCustLeft = finalCustLeft - finalCustServed; //calculation for total amount of customer forcefully left
        System.out.println("--------------------STATISTICS----------------------");
        System.out.println("Total amount of fruit juice ordered : " + juiceOrder);
        System.out.println("Total amount of coffee ordered      : " + coffeeOrder);
        System.out.println("Total amount of customer served     : " + customerServed);
        System.out.println("Total amount of customer left       : " + finalCustLeft);

    }

}
