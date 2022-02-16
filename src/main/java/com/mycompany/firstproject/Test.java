/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.firstproject;

/**
 *
 * @author dell
 */
public class Test {


    public static void main(String[] args) {
        Test t = new Test();
        t.displayMSG();
    }

    private synchronized void displayMSG() {
        while(true){
            System.out.println("running");
        }
        
    }

}
