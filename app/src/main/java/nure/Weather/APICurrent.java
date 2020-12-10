package nure.Weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

class Entity {
    public Coord coord = new Coord();
    public ArrayList<Weather> weather = new ArrayList<>();
    public String base;
    public MyMain main = new MyMain();
    public double visibility;
    public Wind wind = new Wind();
    public Clouds clouds = new Clouds();
    public int dt;
    public Sys sys = new Sys();
    public int timezone;
    public int id;
    public String name;
    public int cod;
}

class Sys {
    public int type;
    public int id;
    public String country;
    public int sunrise;
    public int sunset;
}

class Coord {
    public double lon;
    public double lat;

    public Coord() {
    }
}

class Weather {
    public int id;
    public String main;
    public String description;
    public String icon;

    public Weather() {
    }
}

class MyMain {
    public double temp;
    public double feels_like;
    public double temp_min;
    public double temp_max;
    public double pressure;
    public double humidity;

    public MyMain() {
    }
}

class Wind {
    public double speed;
    public double deg;

    public Wind() {
    }
}

class Clouds {
    public double all;

    public Clouds() {
    }
}

class URLCreator {
    public String create(String cityName) {
        return "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=ce974aa927ea6f121ca5fa89402214e1";
    }
}

public class APICurrent {

    public Entity getData(String name) throws IOException {
        String url = new URLCreator().create(name);
        StringBuilder response = new StringBuilder();

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuilder();
            System.out.println(response);
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(response.toString(), Entity.class);
    }

}
