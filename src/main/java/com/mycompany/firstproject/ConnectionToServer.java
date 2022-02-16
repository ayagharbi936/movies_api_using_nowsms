package com.mycompany.firstproject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dell
 */
public class ConnectionToServer {
    private static Connection connection;
    public Connection connection() {
        String url = "jdbc:postgresql://192.168.1.62/postgres";
        String username = "postgres";
        String password = "postgres";
        System.out.println("Connecting database to server...");

        try{
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected! to server");
           
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
 return connection;
    }    
}
