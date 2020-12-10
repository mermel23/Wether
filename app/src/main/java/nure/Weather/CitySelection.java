package nure.Weather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import nure.weather.R;

public class CitySelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sity_selection);
        AsyncRequestCur request = new AsyncRequestCur();
        final Button kharkiv = (Button) findViewById(R.id.Kharkiv);
        request.execute("Kharkiv","Kiev", "London", "Paris", "Tokyo" );
        kharkiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click(kharkiv.getText().toString());
            }
        });
        final Button london = (Button) findViewById(R.id.London);
        london.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click(london.getText().toString());
            }
        });
        final Button paris = (Button) findViewById(R.id.Paris);
        paris.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click(paris.getText().toString());
            }
        });
        final Button tokyo = (Button) findViewById(R.id.Tokyo);
        tokyo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click(tokyo.getText().toString());
            }
        });
        final Button kiev = (Button) findViewById(R.id.Kiev);
        kiev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click(kiev.getText().toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    public void find(View v) {
        EditText editText = (EditText) findViewById(R.id.CityName);
        String city = editText.getText().toString();
        editText.setText("");
        AsyncRequestCur request = new AsyncRequestCur();
        request.execute(city);
        try {
            Optional<Entity> entity = request.get();
            if(entity.isPresent()) {
                TableRow tableRow = new TableRow(this);
                final Button button = new Button(this);
                button.setText(entity.get().name);
                button.setBackgroundColor(00000000);
                button.setTextSize(24);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        click(button.getText().toString());
                    }
                });
                int pixels = Math.round(convertDpToPixel(278, getApplicationContext()));
                button.setLayoutParams(new TableRow.LayoutParams(pixels, TableRow.LayoutParams.WRAP_CONTENT));
                button.setGravity(Gravity.LEFT);
                button.setTextColor(Color.parseColor("#453E3E"));
                button.setPadding(25, 0, 0, 0);
                TextView textView = new TextView(this);
                textView.setText(Math.round(entity.get().main.temp - 273.15) + "\u00B0");
                textView.setTextSize(40);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(25, 0, 0, 0);
                tableRow.addView(button);
                tableRow.addView(textView);
                LinearLayout list = (LinearLayout) findViewById(R.id.CityList);
                list.addView(tableRow);
            }
            else {
                Toast toast = Toast.makeText(this, "No such city exist!", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (ExecutionException | InterruptedException e) {
            Toast toast = Toast.makeText(this, "No such city exist!", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public void click(String name) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("city", name);
        startActivity(intent);
    }

     class AsyncRequestCur extends AsyncTask<String, Void, Optional<Entity>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Start");
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @SuppressLint("SetTextI18n")
        @Override
        protected Optional<Entity> doInBackground(String... cities) {
            if(cities.length>1) {
                for (int i = 0; i < cities.length; i++) {
                    Double temp = null;
                    try {
                        temp = new APICurrent().getData(cities[i]).main.temp;
                        System.out.println("TTTTTTTT" + temp + cities[i]);
                        LinearLayout list = (LinearLayout) findViewById(R.id.CityList);
                        TableRow tableRow = (TableRow) list.getChildAt(i);
                        TextView curTemp = (TextView) tableRow.getChildAt(1);
                        curTemp.setText(Math.round(temp - 273.15) + "\u00B0");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            try {
                return Optional.of(new APICurrent().getData(cities[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  Optional.empty();
        }


        @Override
        protected void onProgressUpdate(Void... params) {
            super.onProgressUpdate(params);
            System.out.println("In");

        }

        @Override
        protected void onPostExecute(Optional<Entity> result) {
            super.onPostExecute(result);
            System.out.println("End");

        }

    }
}