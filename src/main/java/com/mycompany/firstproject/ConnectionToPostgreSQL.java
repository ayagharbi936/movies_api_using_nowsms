/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.firstproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author dell
 */
public class ConnectionToPostgreSQL {
private static Connection connection;
    public Connection connection() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "postgres";
        System.out.println("Connecting database to postgres...");

        try{
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected! to postgres");
           
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
 return connection;
    }    
}
