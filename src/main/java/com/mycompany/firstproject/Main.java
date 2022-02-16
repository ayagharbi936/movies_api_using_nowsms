/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.firstproject;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dell
 */
public class Main extends Thread {

    public static String FullSMS;
    public static String sender1;
    public static String phoneNumber;
    public static String idreq;
    public static String flagSent;
    public static ConnectionToPostgreSQL connPS = new ConnectionToPostgreSQL();
    public static Connection connectionPS = connPS.connection();
    public static ConnectionToServer conn = new ConnectionToServer();
    public static Connection connection = conn.connection();

    public static void send_sms(String msg, String phoneNumber) {
        Sendsms.server = "http://192.168.1.80:8800/";
        Sendsms.user = "somatel";
        Sendsms.password = "sicap";
        Sendsms.smscroute = "omar";
        Sendsms.receiptrequested = "yes";
        Sendsms.text = msg;
        Sendsms.sender = sender1;
        Sendsms.phonenumber = phoneNumber;
        idreq = Sendsms.send();
        flagSent = "oui";
    }

    public static void send_pub() throws SQLException, MalformedURLException, Exception {

        String pub_msg = "It's Friday! You know what that means: It's movie night.\nSend 'movie' word to 85098 to help you with movies suggestions according to your desired category!\nfirst 3 days are free, Harry up!";
        send_sms(pub_msg, "21692744706");

    }

    public static Date add_daysToDate(int nbday, long initialDate) {

        long daysToAdd = 60 * 60 * 24 * nbday * 1000;
        Date newDate = new Date(initialDate + daysToAdd);
        return newDate;
    }

    public static boolean check_subscriptionFreeTrial(String num_tel) throws Exception {

        String selectQueryy = "SELECT * from subscriptions WHERE status= 1 and service=1 and phone_number= '" + num_tel + "' ";
        Statement stmPS = connectionPS.createStatement();
        ResultSet rsPS = stmPS.executeQuery(selectQueryy);

        while (rsPS.next()) {
            Date created_at_plus3days = add_daysToDate(3, rsPS.getTimestamp("created_at").getTime());
            System.out.println("inital: " + rsPS.getTimestamp("created_at").getTime() + "\nnewDate: " + created_at_plus3days);
            System.out.println(created_at_plus3days.equals(new Date()));
        }
        return false;
    }

    public static void display_categories_toUser(String num_user) throws Exception {
        MoviesApi m = new MoviesApi();
        List<String> catg = m.get_Allcategoris();

        String title = "Please choose and enter ctg- + the number of one of the categories displayed below:\nexemple:ctg-28\n";
        System.out.println(title);

        String catgToString = "";
//      convert table of categories to String seperated by "\n"
        catgToString = catg.stream().collect(Collectors.joining("\n"));
//      manbkch m fi mo5k 
        System.out.println(catgToString);

//        send_sms(title + catgToString, num_user);
    }

    public static void save_userSubscription(String phone_number) throws Exception {

        String query = "INSERT INTO subscriptions (phone_number, created_at, service) VALUES ('" + phone_number + "', '" + new Timestamp(System.currentTimeMillis()) + "', 1)";

        Statement stmPS = connectionPS.createStatement();
        int rows = stmPS.executeUpdate(query);
        System.out.println(rows + "rows inserted");
        String msg = "Hola! We're so excited you are a part of our service Movie night!\nif you want to unsubscribe please send 'STOP MOVIE' to 85098";
        System.out.println(msg);
//        send_sms(msg, phone_number);
    }

    public static int check_UserSubscription(String num_tel, int status) throws Exception {
        String selectQueryy = "SELECT COUNT(*) from subscriptions WHERE status='" + status + "'and service=1 and phone_number= '" + num_tel + "' ";
        Statement stmPS = connectionPS.createStatement();
        ResultSet rsPS = stmPS.executeQuery(selectQueryy);
        int count = 0;
        while (rsPS.next()) {
            count = rsPS.getInt("count");
        }
        System.out.println(count);
        return count;
    }

    public static void update_userSubscriptionStatus(String num_tel, int status) throws Exception {
        String updateQuery = "UPDATE subscriptions SET status = '" + status + "' WHERE service=1 and phone_number= '" + num_tel + "' ";
        Statement update_stm = connectionPS.createStatement();
        update_stm.executeUpdate(updateQuery);
    }

    public static void get_userSubscription() throws Exception {

        String selectQuery = "SELECT * from received WHERE LOWER(message)= LOWER('movie') and traite ='non'  ";
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(selectQuery);

        while (rs.next()) {
            int id = rs.getInt("recid");
            String num_tel = rs.getString("num_tel");
            int count = check_UserSubscription(num_tel, 1);
            int countStatus0 = check_UserSubscription(num_tel, 0);

            if (count >= 1) {
                System.out.println("the asshole with phone number " + num_tel + " already exist");
                String msg = "You already member in this service";
//                send_sms(msg, num_tel);
            } else if (countStatus0 > 0) {

                update_userSubscriptionStatus(num_tel, 1);
                System.out.println("the asshole with phone number " + num_tel + " resubscribe to movies services");
                String msg = "Welcom back! we're happy for your return";
//                send_sms(msg, num_tel);
                display_categories_toUser(num_tel);

            } else {

                System.out.println("not treated users :\n" + num_tel + "-id-:" + rs.getInt("recid"));
                save_userSubscription(num_tel);
                display_categories_toUser(num_tel);
            }
            check_subscriptionFreeTrial(num_tel);
            String updateQuery = "UPDATE received SET traite = 'oui' WHERE recid ='" + id + "'";
            Statement update_stm = connection.createStatement();
            update_stm.executeUpdate(updateQuery);
        }

    }

    public static ResultSet get_subscribedUsersChoice() throws Exception {

        String selectQuery = "SELECT * from received WHERE LOWER(message)like LOWER('ctg-%') and traite ='non' ";
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(selectQuery);
        return rs;
    }

    public static void send_movies() throws Exception {
        ResultSet rs = get_subscribedUsersChoice();

        while (rs.next()) {
            int id = rs.getInt("recid");
            String num_tel = rs.getString("num_tel");
            int count = check_UserSubscription(num_tel, 1);
            int countStatus0 = check_UserSubscription(num_tel, 0);

            if (count == 0) {
                if (countStatus0 > 0) {
                    System.out.println("the asshole with phone number: " + num_tel + " is not subscribed anymore");
                    String msg = "You're not member in this service anymore to resubscribe please send 'MOVIE' word to 85098 ";
//                    send_sms(msg, num_tel);
                } else {
                    System.out.println("the asshole with phone number:" + num_tel + "is not subscribed");
                    String msg = "You're not member in this service";
//                    send_sms(msg, num_tel);
                }
            } else {
                int category = Integer.parseInt(rs.getString("message").substring(4));
                MoviesApi m = new MoviesApi();
                System.out.println(category);
                String categoryName = m.get_CategoryNameById(category);
                if (categoryName.length() != 0) {
                    String title = "Movies in " + categoryName + " category\n";
                    List<String> mov = m.get_movies_ByCatgory(category);

//            convert table of movies to String seperated by "\n"
                    String movToString = mov.stream().collect(Collectors.joining("\n****\n"));
                    System.out.println(movToString);
//                    send_sms(title + movToString, num_tel);
                } else {
                    System.out.println("the asshole with phone number: " + num_tel + " entred invalid category");
                    String msg = "Please enter a valid category";
//                    send_sms(msg, num_tel);
                    display_categories_toUser(num_tel);

                }
            }
            String updateQuery = "UPDATE received SET traite = 'oui' WHERE recid ='" + id + "'";
            Statement update_stm = connection.createStatement();
            update_stm.executeUpdate(updateQuery);
        }
    }

    public static void remove_userSubscription() throws Exception {

        String selectQuery = "SELECT * from received WHERE LOWER(message)like LOWER('%stop movie%') and traite ='non' ";
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(selectQuery);
        while (rs.next()) {
            int id = rs.getInt("recid");
            String num_tel = rs.getString("num_tel");
            int count = check_UserSubscription(num_tel, 1);
            if (count == 0) {
                System.out.println("the asshole with phone number: " + num_tel + " is not subscribed");
                String msg = "You're not member in this service";
//                send_sms(msg, num_tel);
            } else {

                update_userSubscriptionStatus(num_tel, 0);
                System.out.println("the asshole with phone number: " + num_tel + " quit the service! shit!!!");
                String msg = "You're now unsbscribed from this service! we hope you'll subscribe again :) !";
//                send_sms(msg, num_tel);

            }
            String updateQuery = "UPDATE received SET traite = 'oui' WHERE recid ='" + id + "' ";
            Statement update_stm = connection.createStatement();
            update_stm.executeUpdate(updateQuery);
        }
    }

    private synchronized static void runProgram() throws Exception {
        //send_pub();
        while (true) {
            get_userSubscription();
            send_movies();
            remove_userSubscription();

        }

    }

    public static void main(String[] args) throws SQLException, MalformedURLException, Exception {

        runProgram();

    }

}
