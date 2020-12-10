package nure.Weather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import nure.weather.R;

import static java.util.Collections.max;
import static java.util.Collections.min;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncRequest request = new AsyncRequest();
        String city = "Kharkiv";
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            city = arguments.get("city").toString();
        }
        request.execute(city);
        try {
            APIFor5Days.LongTermForecast entity = request.get();
            setData(entity);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void onClick(View v) {
        Intent intent = new Intent(this, CitySelection.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    public void current(String city) {
        TextView clouds = (TextView) findViewById(R.id.clowds);
        TextView curTemp = (TextView) findViewById(R.id.curTemp);

        AsyncRequestCur request = new AsyncRequestCur();
        request.execute(city);

        try {
            Entity entity = request.get();
            clouds.setText(entity.weather.get(0).description);
            curTemp.setText(Math.round(entity.main.temp - 273.15) + "\u00B0");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    public String helper(String date, APIFor5Days.LongTermForecast forecast, boolean bool) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < forecast.list.size(); i++) {
            if (forecast.list.get(i).dt_txt.contains(date)) {
                if (bool) {
                    list.add(forecast.list.get(i).main.temp_max);
                } else {
                    list.add(forecast.list.get(i).main.temp_min);
                }
            }
        }
        double var;
        if (bool) {
            var = max(list);
        } else {
            var = min(list);
        }
        return Math.round(var - 273.15) + "\u00B0";
    }
    @SuppressLint("SetTextI18n")
    public void setData(APIFor5Days.LongTermForecast entity) {
        TextView city = (TextView) findViewById(R.id.city);
        TextView date1 = (TextView) findViewById(R.id.data1);
        TextView date2 = (TextView) findViewById(R.id.data2);
        TextView date3 = (TextView) findViewById(R.id.data3);
        TextView date4 = (TextView) findViewById(R.id.data4);
        TextView date5 = (TextView) findViewById(R.id.data5);
        TextView maxMin = (TextView) findViewById(R.id.maxMin);
        String max = helper(entity.list.get(0).dt_txt.substring(5,11), entity,true);
        String min =helper(entity.list.get(0).dt_txt.substring(5,11), entity,false);
        maxMin.setText("Max: " + max +", Min: " + min);

        city.setText(entity.city.name);
        current(entity.city.name);
        date1.setText(entity.list.get(0).dt_txt.substring(8, 10) + ". " + entity.list.get(0).dt_txt.substring(5, 7));
        date2.setText(entity.list.get(8).dt_txt.substring(8, 10) + ". " + entity.list.get(8).dt_txt.substring(5, 7));
        date3.setText(entity.list.get(16).dt_txt.substring(8, 10) + ". " + entity.list.get(16).dt_txt.substring(5, 7));
        date4.setText(entity.list.get(24).dt_txt.substring(8, 10) + ". " + entity.list.get(24).dt_txt.substring(5, 7));
        date5.setText(entity.list.get(32).dt_txt.substring(8, 10) + ". " + entity.list.get(32).dt_txt.substring(5, 7));
        setMaxMin(entity);
        setIcons(entity);
        setHorScroll(entity);
    }

    @SuppressLint("SetTextI18n")
    public void setHorScroll(APIFor5Days.LongTermForecast forecast) {
        TableRow first = (TableRow) findViewById(R.id.first);
        TableRow second = (TableRow) findViewById(R.id.second);
        TableRow third = (TableRow) findViewById(R.id.third);
        for (int i = 0; i < 25; i++) {
            TextView hour = new TextView(this);
            hour.setText(forecast.list.get(i).dt_txt.substring(10, 13));
            hour.setTextSize(24);
            hour.setWidth(100);
            hour.setGravity(Gravity.CENTER);
            first.addView(hour);
            TextView icon = new TextView(this);
            icon.setText(getIcon(forecast.list.get(i).weather.get(0).description));
            icon.setGravity(Gravity.CENTER);
            icon.setHeight(100);
            icon.setTextSize(24);
            icon.setWidth(100);
            second.addView(icon);
            TextView temp = new TextView(this);
            temp.setText(Math.round(forecast.list.get(i).main.temp - 273.15) + "\u00B0");
            temp.setTextSize(24);
            temp.setWidth(100);
            temp.setGravity(Gravity.CENTER);
            third.addView(temp);
        }
    }

    Map<Integer, String> icon = new HashMap<>();
    ArrayList<String> descriptions = new ArrayList<>();

    public void fillIcons() {
        icon.put(0, "⛅️");
        icon.put(1, "⛅️");
        icon.put(2, "⛅️");
        icon.put(3, "☁️️");
        icon.put(4, "☔️️");
        icon.put(5, "☀️");
        icon.put(6, "☔️️");
        icon.put(7, "❄️️");
        icon.put(8, "⚡️️");
        icon.put(9, "⚡️️");

        descriptions.add("scattered");
        descriptions.add("few");
        descriptions.add("broken");
        descriptions.add("overcast");
        descriptions.add("rain");
        descriptions.add("clear");
        descriptions.add("drizzle");
        descriptions.add("snow");
        descriptions.add("storm");
        descriptions.add("lightning");
    }

    public String getIcon(String string) {
        for (int i = 0; i < descriptions.size(); i++) {
            if (string.contains(descriptions.get(i))) {
                return icon.get(i);
            }
        }
        return icon.get(0);
    }

    public void setIcons(APIFor5Days.LongTermForecast entity) {
        TextView img1 = (TextView) findViewById(R.id.img1);
        TextView img2 = (TextView) findViewById(R.id.img2);
        TextView img3 = (TextView) findViewById(R.id.img3);
        TextView img4 = (TextView) findViewById(R.id.img4);
        TextView img5 = (TextView) findViewById(R.id.img5);

        fillIcons();
        img1.setText(icon.get(0));
        for (int i = 0; i < descriptions.size(); i++) {
            if (entity.list.get(0).weather.get(0).description.contains(descriptions.get(i))) {
                img1.setText(icon.get(i));
            }
        }
        img2.setText(icon.get(0));
        for (int i = 0; i < descriptions.size(); i++) {
            if (entity.list.get(8).weather.get(0).description.contains(descriptions.get(i))) {
                img2.setText(icon.get(i));
            }
        }
        img3.setText(icon.get(0));
        for (int i = 0; i < descriptions.size(); i++) {
            if (entity.list.get(16).weather.get(0).description.contains(descriptions.get(i))) {
                img3.setText(icon.get(i));
            }
        }
        img4.setText(icon.get(0));
        for (int i = 0; i < descriptions.size(); i++) {
            if (entity.list.get(24).weather.get(0).description.contains(descriptions.get(i))) {
                img4.setText(icon.get(i));
            }
        }
        img5.setText(icon.get(0));
        for (int i = 0; i < descriptions.size(); i++) {
            if (entity.list.get(32).weather.get(0).description.contains(descriptions.get(i))) {
                img5.setText(icon.get(i));
            }
        }

    }



    @SuppressLint("SetTextI18n")
    private void setMaxMin(APIFor5Days.LongTermForecast forecast) {
//        double max = forecast.list.get(0).main.temp_max;
//        double min = forecast.list.get(0).main.temp_min;
//        List<Double> maxList = new ArrayList<>();
//        List<Double> minList = new ArrayList<>();
//        String previous = forecast.list.get(0).dt_txt.substring(8, 10);
//
//
//        for (int i = 1; i < forecast.list.size() - 1; i++) {
//            APIFor5Days.Entity entity = forecast.list.get(i);
//            if (!entity.dt_txt.substring(8, 10).equals(previous)) {
//                maxList.add(max);
//                minList.add(min);
//                i++;
//                entity = forecast.list.get(i);
//                max = entity.main.temp_max;
//                min = entity.main.temp_min;
//                previous = entity.dt_txt.substring(8, 10);
//            } else {
//                max = Math.max(entity.main.temp_max, max);
//                min = Math.min(entity.main.temp_min, min);
//            }
//        }
//        if (maxList.size() != 5) {
//            maxList.add(max);
//            minList.add(min);
//        }
        TextView day1 = (TextView) findViewById(R.id.dayTemp1);
        TextView day2 = (TextView) findViewById(R.id.dayTemp2);
        TextView day3 = (TextView) findViewById(R.id.dayTemp3);
        TextView day4 = (TextView) findViewById(R.id.dayTemp4);
        TextView day5 = (TextView) findViewById(R.id.dayTemp5);
        TextView night1 = (TextView) findViewById(R.id.nightTemp1);
        TextView night2 = (TextView) findViewById(R.id.nightTemp2);
        TextView night3 = (TextView) findViewById(R.id.nightTemp3);
        TextView night4 = (TextView) findViewById(R.id.nightTemp4);
        TextView night5 = (TextView) findViewById(R.id.nightTemp5);

        night1.setText(helper(forecast.list.get(0).dt_txt.substring(5,11), forecast,false));
        night2.setText(helper(forecast.list.get(8).dt_txt.substring(5,11), forecast,false));
        night3.setText(helper(forecast.list.get(16).dt_txt.substring(5,11), forecast,false));
        night4.setText(helper(forecast.list.get(24).dt_txt.substring(5,11), forecast,false));
        night5.setText(helper(forecast.list.get(32).dt_txt.substring(5,11), forecast,false));


        day1.setText(helper(forecast.list.get(0).dt_txt.substring(5,11), forecast,true));
        day2.setText(helper(forecast.list.get(8).dt_txt.substring(5,11), forecast,true));
        day3.setText(helper(forecast.list.get(16).dt_txt.substring(5,11), forecast,true));
        day4.setText(helper(forecast.list.get(24).dt_txt.substring(5,11), forecast,true));
        day5.setText(helper(forecast.list.get(22).dt_txt.substring(5,11), forecast,true));


    }

    static class AsyncRequest extends AsyncTask<String, Void, APIFor5Days.LongTermForecast> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Start");
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected APIFor5Days.LongTermForecast doInBackground(String... city) {
            return new APIFor5Days().getData(city[0]);
        }


        @Override
        protected void onProgressUpdate(Void... params) {
            super.onProgressUpdate(params);
            System.out.println("In");

        }

        @Override
        protected void onPostExecute(APIFor5Days.LongTermForecast result) {
            super.onPostExecute(result);
            System.out.println("End");

        }

    }

    private int timeIndex(String time) {
        int hour = Integer.parseInt(time.substring(11, 13));
        switch (hour) {
            case 0:
            case 1:
            case 2: {
                return 0;
            }
            case 3:
            case 4:
            case 5: {
                return 1;
            }
            case 6:
            case 7:
            case 8: {
                return 2;
            }
            case 9:
            case 10:
            case 11: {
                return 3;
            }
            case 12:
            case 13:
            case 14: {
                return 4;
            }
            case 15:
            case 16:
            case 17: {
                return 5;
            }
            case 18:
            case 19:
            case 20: {
                return 6;
            }
            case 21:
            case 22:
            case 23: {
                return 7;
            }
        }
        return 0;
    }

    class AsyncRequestCur extends AsyncTask<String, Void, Entity> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Start");
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected Entity doInBackground(String... cities) {
            try {
                return new APICurrent().getData(cities[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... params) {
            super.onProgressUpdate(params);
            System.out.println("In");

        }

        @Override
        protected void onPostExecute(Entity result) {
            super.onPostExecute(result);
            System.out.println("End");

        }

    }
}