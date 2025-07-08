package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;


public class App {
    public static void main(String[] args) {

        String myAPIkey = "";
        String city = "Moenchengladbach";


//get API DATA
        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + myAPIkey + "&units=metric";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader openWeatherInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuilder APIdata = new StringBuilder();

            while ((inputLine = openWeatherInput.readLine()) != null) {
                APIdata.append(inputLine);
            }

            openWeatherInput.close();
            connection.disconnect();

            System.out.println("Antwort von API:\n" + APIdata.toString());

//convert API data to JSON
            String jsonString = APIdata.toString();
            JSONObject myJSON = new JSONObject(jsonString);

//get CityName
            String name = myJSON.getString("name");
            System.out.println("\n" + name);

//get "main subclass openWeatherInput JSON as object and get data
            double temp = myJSON.getJSONObject("main").getDouble("temp");
            double tempMax = myJSON.getJSONObject("main").getDouble("temp_max");
            double tempMin = myJSON.getJSONObject("main").getDouble("temp_min");

//get "wind" subclass openWeatherInput JSON as object and get wind speed
            double windspeed = myJSON.getJSONObject("wind").getDouble("speed");

//get "weather" subclass Array with data ..... why it`s an Array? dont know !?
            JSONObject weatherObj = myJSON.getJSONArray("weather").getJSONObject(0);
            String wetterTyp = weatherObj.getString("main");
            String wetterTyp2 = weatherObj.getString("description");

// get the timestamp -> chatGPT converts timestamp -> format angepasst an den rest
            long timestamp = myJSON.getJSONObject("sys").getLong("sunset");
            ZonedDateTime time = Instant.ofEpochSecond(timestamp).atZone(ZoneId.of("Europe/Berlin")); // deine Zeitzone
            String formattedTime = time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

//Print the selected data
            System.out.println("Temperatur: " + temp + " c");
            System.out.println("Temperatur max: " + tempMax + " c");
            System.out.println("Temperatur min: " + tempMin + " c");
            System.out.println("Wetter allgemein: " + wetterTyp + ", " + wetterTyp2);
            System.out.println("Wind speed: " + windspeed + " km/h");
            System.out.println("Sonnenuntergang: " + formattedTime);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}