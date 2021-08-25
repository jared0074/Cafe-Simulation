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
public class Cupboard{ 
    
    //function for taking cup
    public void takeCup(String position) throws InterruptedException{
        System.out.println(position + " taking cup from Cupboard");
        Thread.sleep(500);
    }
    
    //function for take glass
    public void takeGlass(String position) throws InterruptedException{
        System.out.println(position + " taking glass from Cupboard");
        Thread.sleep(500);
    }
    
    //function for take milk
    public void takeMilk(String position) throws InterruptedException{
        System.out.println(position + " taking milk from Cupboard");
        Thread.sleep(500);
    }
    
    //function for take coffee
    public void takeCoffee(String position) throws InterruptedException{
        System.out.println(position + " taking coffee from Cupboard");
        Thread.sleep(500);
    }


}
