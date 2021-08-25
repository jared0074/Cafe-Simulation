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
public class CafeStatus {
    //seat object
    Object seat = new Object();
    int tableSeat; //number of seat

    Object serving = new Object(); //serving object

    CafeStatus(int seatSize) {
        tableSeat = seatSize; //pass the number of seat to identify seat size of cafe
    }
}
