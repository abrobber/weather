package com.abrobber.weather;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class WeatherApp implements CommandLineRunner {

    //private static final Logger LOG = LoggerFactory.getLogger("Data Clima");
    private final String city1 = "http://api.openweathermap.org/data/2.5/weather?q=Heredia,cr&APPID=7930795e0a49e14a221dc3d8ed0a108b";
    private final String city2 = "http://api.openweathermap.org/data/2.5/weather?q=Alajuela,cr&APPID=7930795e0a49e14a221dc3d8ed0a108b";
    private final String city3 = "http://api.openweathermap.org/data/2.5/weather?q=Cartago,cr&APPID=7930795e0a49e14a221dc3d8ed0a108b";
    private final String city4 = "http://api.openweathermap.org/data/2.5/weather?q=Guapiles,cr&APPID=7930795e0a49e14a221dc3d8ed0a108b";
    private final String city5 = "http://api.openweathermap.org/data/2.5/weather?q=Guanacaste,cr&APPID=7930795e0a49e14a221dc3d8ed0a108b";
    private final String city6 = "http://api.openweathermap.org/data/2.5/weather?q=Puntarenas,cr&APPID=7930795e0a49e14a221dc3d8ed0a108b";
    private final String city7 = "http://api.openweathermap.org/data/2.5/weather?q=San Jose,cr&APPID=7930795e0a49e14a221dc3d8ed0a108b";

    @Autowired
    WeatherRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(WeatherApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String hora = dateFormat.format(new Date());
        //LOG.info(">>Aplication run: {}", hora);

        readWeather(city1);
        readWeather(city2);
        readWeather(city3);
        readWeather(city4);
        readWeather(city5);
        readWeather(city6);
        readWeather(city7);
        
        Iterable<Weather> iterator = repository.findAll();

        System.out.println("\nTodos los registros: ");
        System.out.println("\nCiudad\t\tTemp\tPresion\tHumedad\tFecha");
        iterator.forEach(name -> System.out.println(name));

        List<Weather> city = repository.findByName("Heredia");
        System.out.println("\nBusqueda por Ciudad : ");
        city.forEach(name -> System.out.println(name));

        List<Weather> tempMaxItems = repository.listItemsWithTempOver(295);
        System.out.println("\nMaximas temperaturas: ");
        tempMaxItems.forEach(name -> System.out.println(name));

    }

    //@Scheduled(fixedRate = 60000)
    public void readWeather(String urlCity) throws FileNotFoundException, ParseException, IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String hora = dateFormat.format(new Date());
        System.out.println("Start aplication : " + hora);

        try {
            URL url = new URL(urlCity);
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
//            LOG.info("Temp : " + temp);
//            LOG.info("Pressure : " + pressure);
//            LOG.info("Humidity : " + humidity);
//            LOG.info("City : " + name);
            repository.save(new Weather("" + name, temp, pressure, humidity, hora));
            conn.disconnect();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
