/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

/**
 *
 * @author jared
 */
public class ServingArea {
    Cupboard c; //creating cupboard
    boolean milkAvailable = true; //check whether milk is available, set to true at first
    boolean coffeeAvailable = true; //check whether coffee is available, set to true at first

    //putting cupboard inside serving area
    public ServingArea(Cupboard c){
        this.c = c; //cupboard c passed inside serving area class
    }
    
    //using juicetap to pour juice and create drink
    public synchronized void useJuiceTap(String position) throws InterruptedException{
        System.out.println(position + " is now using Juice Tap.");
        Thread.sleep(1000); //time to pour fruit juice, can be modified
    }
    
    //using cupboard to take ingredients or cup/glass
    public synchronized void useCupboard(String position, int order) throws InterruptedException{
        // order = 0 means its ordering juice
        if (order == 0){
            c.takeGlass(position); //take glass
            
        }
        // else order = 1 means its ordering coffee
        else{
            c.takeCup(position); //take cup
            c.takeMilk(position); //take milk
            c.takeCoffee(position); //take coffee
            
            milkAvailable = false;
            coffeeAvailable = false;
        }
    }
}

