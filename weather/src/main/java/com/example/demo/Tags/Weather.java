/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Tags;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author roberto
 */
@Component
public class Weather {

    //private static final Logger LOG = LoggerFactory.getLogger(Weather.class);
    private static final Logger LOG = LoggerFactory.getLogger("Data Clima");

    @Scheduled(fixedRate = 60000)
    public void readWeather() throws FileNotFoundException, ParseException, IOException {        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String hora = dateFormat.format(new Date());
        LOG.info(">>Aplication run: {}", hora);
        
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Heredia,cr&APPID=7930795e0a49e14a221dc3d8ed0a108b");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error # " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }

            String output = sb.toString();
            JsonObject json = new Gson().fromJson(output, JsonObject.class);
            
            //System.out.println(">>Salida como JSON" + json);
            //System.out.println(">>Salida como String : " + output);
            
            JsonObject main = json.get("main").getAsJsonObject();
            float temp = main.get("temp").getAsFloat();
            float pressure = main.get("pressure").getAsFloat();
            float humidity = main.get("humidity").getAsFloat();
            String name = json.get("name").getAsString();
            
//            System.out.println("Temp : " + temp);
//            System.out.println("Pressure : " + pressure);
//            System.out.println("Humidity : " + humidity);
//            System.out.println("City : " + name);

            LOG.info("Temp : " + temp);
            LOG.info("Pressure : " + pressure);
            LOG.info("Humidity : " + humidity);
            LOG.info("City : " + name);
            conn.disconnect();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }   

}
