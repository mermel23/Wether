package nure.Weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class APIFor5Days {
    public LongTermForecast getData(String city) {
        String url = new URLCreator().create(city);
        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHH" + url);
        StringBuilder response = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(response.toString(), LongTermForecast.class);
            //System.out.println("GSON", "id: " + myClass.id + "\njoke: " + myClass.joke);
            //return entity;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class City {
        public int id;
        public String name;
        public Coord coord = new Coord();
        public String country;
        public int timezone;
        public int sunrise;
        public int sunset;

        public City() {
        }
    }

    class Clouds {
        public double all;

        public Clouds() {
        }
    }

    class Coord {
        public double lon;
        public double lat;

        public Coord() {
        }
    }

    class Entity {
        public int dt;
        public MyMain main = new MyMain();
        public ArrayList<Weather> weather = new ArrayList<>();
        public Clouds clouds = new Clouds();
        public Wind wind = new Wind();
        public double visibility;
        public double pop;
        public Sys sys = new Sys();
        public String dt_txt;
        //    public Coord coord = new Coord();
        //    public String base;
        //
        //    public int timezone;
        //    public int id;
        //    public String name;
        //    public int cod;
    }

    public class LongTermForecast {
        public String cod;
        public String message;
        public int cnt;
        public List<Entity> list = new ArrayList<>();
        public City city = new City();

        public LongTermForecast() {
        }
    }

    class Wind {
        public double speed;
        public double deg;

        public Wind() {
        }
    }

    class MyMain {
        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
        public double pressure;
        public int sea_level;
        public int grnd_level;
        public double humidity;
        public double temp_kf;

        public MyMain() {
        }
    }

    class Rain {
        public String h;
    }

    class Sys {
        public String pod;
    }

    class URLCreator {
        public String create(String cityName) {
            String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&exclude=rain&appid=08c891a43467e96f3ed5c2824b35bb1d";
            System.out.println(url);
            return url;
        }
    }

    static class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;

        public Weather() {
        }
    }
}
