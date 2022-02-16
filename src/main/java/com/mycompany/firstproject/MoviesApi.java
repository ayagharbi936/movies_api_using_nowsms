/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.firstproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class MoviesApi {

    private String api_key = "28bc65213ce1b06d53fd9101782126ce";
    private JSONArray categories = null;
   
//    public void Get_movies() throws Exception {
//
//        try {
//            String url = "https://jsonplaceholder.typicode.com/todos";
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            // optional default is GET
//            con.setRequestMethod("GET");
//            //add request header
//            
//            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'GET' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            //print in String
//            System.out.println(response.toString());
//            //Read JSON response and print
////            JSONObject myResponse = new JSONObject(response.toString());
//            JSONArray myResponse = new JSONArray(response.toString());
//            for (int i = 0; i < myResponse.length(); i++) {
//                JSONObject object = (myResponse.getJSONObject(i));
//                System.out.println("Thank you Hama"+object);
//                     
//            }
//        } catch (ProtocolException ex) {
//            Logger.getLogger(MoviesApi.class.getName()).log(Level.SEVERE, null, ex);
//        }
//   }

    public static int get_Random(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static List<Integer> get_AllMoviesID(int[] array) {

        return null;
    }

    public JSONObject[] get_randomMovies(JSONArray movies) {
        int[] randomMoviesID = new int[3];
        int[] moviesID = new int[movies.length()];
        JSONObject[] randomMovies = new JSONObject[3];

        for (int i = 0; i < movies.length(); i++) {
            JSONObject object = (movies.getJSONObject(i));
            moviesID[i] = object.getInt("id");

        }
        int r = 0;
        while (r < 3) {
            int i = 0;
            boolean exist = false;
            int randomId = get_Random(moviesID);
            while (exist == false && i <= r) {

                if (randomMoviesID[i] == randomId) {
                    exist = true;
                } else {
                    i++;
                }
            }
            if (exist == false) {
                randomMoviesID[r] = randomId;
                r++;
            }

        }

        for (int i = 0; i < randomMoviesID.length; i++) {
            boolean found = false;
            int j = 0;
            while (found == false && j < movies.length()) {

                if (randomMoviesID[i] == movies.getJSONObject(j).getInt(("id"))) {
                    found = true;
                    randomMovies[i] = movies.getJSONObject(j);

                } else {
                    j++;
                }

            }

        }
        return randomMovies;
    }

    public String get_CategoryNameById(int category_id) throws Exception {
        get_Allcategoris();
        boolean found = false;
        int i = 0;
        String categoryName = "";
        while (found == false && i < categories.length()) {

            if (categories.getJSONObject(i).getInt(("id")) == category_id) {
                found = true;
                categoryName = categories.getJSONObject(i).getString("name");

            } else {
                i++;
            }

        }

        return categoryName;
    }

     
    public List<String> get_movies_ByCatgory(int category_id) throws Exception {
        JSONArray movies = null;
        List<String> mov = null;

        try {
            String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + api_key + "&with_genres=" + category_id;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header

            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'GET' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print in String
//            System.out.println(response.toString());
            //Read JSON response and print
            JSONObject myResponse = new JSONObject(response.toString());
            movies = myResponse.getJSONArray("results");
            JSONObject[] randomMovies = get_randomMovies(movies);

            mov = new ArrayList<String>();
            for (int i = 0; i < randomMovies.length; i++) {
                JSONObject object = (randomMovies[i]);
                mov.add((i + 1) + ")\nmovie title:" + object.getString("title") + "\n" + "movie rate:" + object.getDouble("vote_average") + "\n" + "release date:" + object.getString("release_date"));
//                System.out.println("movie-" + (i + 1) + ") movie title:" + object.getString("title") + " - " + "movie rate:" + object.getDouble("vote_average") + " - " + "release date:" + object.getString("release_date"));
//                System.out.println("******************");
            }

        } catch (ProtocolException ex) {
            Logger.getLogger(MoviesApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mov;
    }

    public List<String> get_Allcategoris() throws Exception {

        List<String> catg = null;
        try {
            String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + api_key;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header

            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'GET' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print in String

            //Read JSON response and print
            JSONObject myResponse = new JSONObject(response.toString());
            categories = myResponse.getJSONArray("genres");
            catg = new ArrayList<String>();
            for (int i = 0; i < categories.length(); i++) {
                JSONObject object = (categories.getJSONObject(i));
                catg.add(object.getInt("id") + "-" + object.getString("name"));
            }
        } catch (ProtocolException ex) {
            Logger.getLogger(MoviesApi.class.getName()).log(Level.SEVERE, null, ex);
        }

        return catg;
    }

}
