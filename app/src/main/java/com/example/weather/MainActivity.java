package com.example.weather;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button, button2;
    ImageView imageView, country_flag;
    TextView country_dt, city_dt, temp_dt, sky_dt;
    TextView latitude, longitude, humidity, sunrise, sunset, pressure, wind;
    TextView feels_like, max_temp, min_temp;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.user_input);
        button = findViewById(R.id.button);
        country_dt = findViewById(R.id.enter_country);
        city_dt = findViewById(R.id.enter_city);
        imageView = findViewById(R.id.imageView);
        temp_dt = findViewById(R.id.show_temp);
        sky_dt = findViewById(R.id.show_sky);
        latitude = findViewById(R.id.Latitude);
        longitude = findViewById(R.id.Longitude);
        humidity = findViewById(R.id.Humidity);
        sunrise = findViewById(R.id.Sunrise);
        sunset = findViewById(R.id.Sunset);
        pressure = findViewById(R.id.Pressure);
        wind = findViewById(R.id.Wind);
        feels_like = findViewById(R.id.feels_like);
        max_temp = findViewById(R.id.max_temp);
        min_temp = findViewById(R.id.min_temp);

        button2 = findViewById(R.id.forecast_button);
        country_flag = findViewById(R.id.flag);
        sv = findViewById(R.id.s_view);

        editText.addTextChangedListener(enteredTextWatcher);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWeather();
            }
        });
    }

    //disable the search weather button if the editText is empty
    private TextWatcher enteredTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String ct = editText.getText().toString().trim();
            button.setEnabled(!ct.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_gr1:
                sv.setBackgroundResource(R.drawable.bg1);
                return true;

            case R.id.back_gr2:
                sv.setBackgroundResource(R.drawable.bg2);
                return true;

            case R.id.back_gr3:
                sv.setBackgroundResource(R.drawable.bg3);
                return true;

            case R.id.back_gr4:
                sv.setBackgroundResource(R.drawable.bg4);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void findWeather() {
        String city = editText.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=462f445106adc1d21494341838c10019";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // calling api
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //find city
                    String city_find = jsonObject.getString("name");
                    city_dt.setText(city_find + ",");

                    //find country
                    JSONObject object1 = jsonObject.getJSONObject("sys");
                    String country_find = object1.getString("country");
                    country_dt.setText(country_find);

                    //flag find
                    String str = country_find.toLowerCase();
                    Picasso.get().load("https://openweathermap.org/images/flags/" + str + ".png").into(country_flag);

                    //find sky details
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject obj = jsonArray.getJSONObject(0);
                    String sky_find = obj.getString("description");
                    sky_dt.setText("Sky : " + sky_find);

                    //temp find
                    JSONObject object2 = jsonObject.getJSONObject("main");
                    double temp_find = object2.getDouble("temp");
                    temp_dt.setText(new DecimalFormat("##.##").format(temp_find - 273.15) + " °C");
                    //temp_dt.setText(temp_find - 273.00 +" °C");

                    //get weather icon
                    JSONArray jsonArray1 = jsonObject.getJSONArray("weather");
                    JSONObject objj = jsonArray1.getJSONObject(0);
                    String img = objj.getString("icon");
                    Picasso.get().load("https://openweathermap.org/img/wn/" + img + "@2x.png").into(imageView);

                    //find feels_like
                    JSONObject jsonObject8 = jsonObject.getJSONObject("main");
                    double feelslike = jsonObject8.getDouble("feels_like");
                    feels_like.setText(new DecimalFormat("##.##").format(feelslike - 273.15) + " °C");

                    //find max temp
                    JSONObject jsonObject9 = jsonObject.getJSONObject("main");
                    double maxtemp = jsonObject9.getDouble("temp_max");
                    max_temp.setText(new DecimalFormat("##.##").format(maxtemp - 273.15) + " °C");

                    //find min temp
                    JSONObject jsonObject10 = jsonObject.getJSONObject("main");
                    double mintemp = jsonObject10.getDouble("temp_min");
                    min_temp.setText(new DecimalFormat("##.##").format(mintemp - 273.15) + " °C");

                    //find latitude
                    JSONObject jsonObject1 = jsonObject.getJSONObject("coord");
                    double lat_find = jsonObject1.getDouble("lat");
                    latitude.setText(lat_find + "° N");

                    //find longitude
                    JSONObject jsonObject2 = jsonObject.getJSONObject("coord");
                    double long_find = jsonObject2.getDouble("lon");
                    longitude.setText(long_find + "° E");

                    //find humidity
                    JSONObject jsonObject3 = jsonObject.getJSONObject("main");
                    double hum_find = jsonObject3.getDouble("humidity");
                    humidity.setText(hum_find + " %");

                    //find sunrise
                    JSONObject jsonObject4 = jsonObject.getJSONObject("sys");
                    String sunrise_find = jsonObject4.getString("sunrise");
                    sunrise.setText(sunrise_find);

                    //find sunset
                    JSONObject jsonObject5 = jsonObject.getJSONObject("sys");
                    String sunset_find = jsonObject5.getString("sunset");
                    sunset.setText(sunset_find);

                    //find pressure
                    JSONObject jsonObject6 = jsonObject.getJSONObject("main");
                    String pressure_find = jsonObject6.getString("pressure");
                    pressure.setText(pressure_find + " hPa");

                    //find wind speed
                    JSONObject jsonObject7 = jsonObject.getJSONObject("wind");
                    String wind_find = jsonObject7.getString("speed");
                    wind.setText(wind_find + " km/h");


                    //forecasting more days
                    String city_id = jsonObject.getString("id");
                    final String forcast_url = "https://openweathermap.org/city/" + city_id;
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoUrl(forcast_url);
                        }

                        private void gotoUrl(String s) {
                            Uri uri = Uri.parse(s);
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}