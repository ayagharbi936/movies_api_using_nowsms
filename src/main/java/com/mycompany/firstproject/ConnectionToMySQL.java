/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.firstproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author dell
 */
public class ConnectionToMySQL {
    
   private static Connection connection;
    public Connection connection() {
        String url = "jdbc:mysql://localhost:3306/sendsms";
        String username = "root";
        String password = "";
        System.out.println("Connecting database...");

        try{
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
           
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
 return connection;
    }

}
