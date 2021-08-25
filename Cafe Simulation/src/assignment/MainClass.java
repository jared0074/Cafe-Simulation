/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

/**
 *
 * @author vazeer
 */
public class MainClass {
    public static void main(String ar[])
    {
        //if customer number required to be generated needs to be modified
        //it need to be modified in cafe, waiter, owner and customer generator class
        int numSeats = 10; //number of seats in the cafe
        
        CafeStatus shopStatus = new CafeStatus(numSeats); //pass the number of seats into the cafe
        
        Cupboard c = new Cupboard(); //initialize cupboard
        ServingArea s = new ServingArea(c); //initialize serving area
        Statistics stats = new Statistics(); //initialize statistics
        Cafe cafe = new Cafe(s,shopStatus,stats); //initialize cafe, all threads will run inside the cafe.
        
    }
}

