package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText input;
    TextView city;
    TextView submit;
    TextView temperature;
    TextView error;
    TextView center;
    TextView quote;
    TextView forecastBackground;
    ListView forecast;
    ArrayList<Weather> forecastList;
    ForecastAdapter adapter;
    ImageView tl[];
    ImageView tr[];
    ImageView bl[];
    ImageView br[];
    AnimatorSet frontTL;
    AnimatorSet backTL;
    AnimatorSet frontTR;
    AnimatorSet backTR;
    AnimatorSet frontBL;
    AnimatorSet backBL;
    AnimatorSet frontBR;
    AnimatorSet backBR;
    AnimatorSet cityAnim;
    AnimatorSet errorAnim;
    AnimatorSet quoteAnim;
    AnimatorSet tempAnim;
    AnimatorSet listAnim;
    int imageState = 1;
    String currentZipCode = "10001";
    public static final String ZIP = "zip";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ZIP, currentZipCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tl = new ImageView[2];
        tr = new ImageView[2];
        bl = new ImageView[2];
        br = new ImageView[2];

        input = findViewById(R.id.Input);
        error = findViewById(R.id.Error);
        city = findViewById(R.id.City);
        submit = findViewById(R.id.Submit);
        temperature = findViewById(R.id.Temperature);
        forecast = findViewById(R.id.Forecast);
        tl[0] = findViewById(R.id.IconTL);
        tr[0] = findViewById(R.id.IconTR);
        bl[0] = findViewById(R.id.IconBL);
        br[0] = findViewById(R.id.IconBR);
        tl[1] = findViewById(R.id.IconTLBack);
        tr[1] = findViewById(R.id.IconTRBack);
        bl[1] = findViewById(R.id.IconBLBack);
        br[1] = findViewById(R.id.IconBRBack);
        center = findViewById(R.id.AtomCircle);
        quote = findViewById(R.id.Quote);
        forecastBackground = findViewById(R.id.ListAnim);

        frontTL = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.front_animator_tl));
        backTL = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.back_animator_tl));
        frontTR = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.front_animator_mid));
        backTR = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.back_animator_mid));
        frontBL = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.front_animator_mid));
        backBL = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.back_animator_mid));
        frontBR = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.front_animator_br));
        backBR = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.back_animator_br));
        cityAnim = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.fade_in));
        errorAnim = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.fade_in));
        tempAnim = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.fade_in));
        quoteAnim = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.fade_in));
        listAnim = (AnimatorSet)(AnimatorInflater.loadAnimator(this, R.animator.fade_out));
        AnimationDrawable animationDrawable = (AnimationDrawable)center.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        forecastList = new ArrayList<Weather>();
        adapter = new ForecastAdapter(this, R.layout.forecast, forecastList);
        forecast.setAdapter(adapter);

        if(savedInstanceState != null) {
            currentZipCode = savedInstanceState.getString(ZIP);
        }
        Search init = new Search();
        init.setZipCode(currentZipCode);
        try {
            init.execute().get();
        } catch (Exception e) { }
        city.setText(init.getCity());
        temperature.setText(init.getTemperature());
        if(init.getId() >= 200 && init.getId() < 300)
        {
            tl[0].setImageResource(R.drawable.thundertl);
            tr[0].setImageResource(R.drawable.thundertr);
            bl[0].setImageResource(R.drawable.thunderbl);
            br[0].setImageResource(R.drawable.thunderbr);
            quote.setText("Lightning is a discharge of electricity");
        }
        else if(init.getId() >= 300 && init.getId() < 400)
        {
            tl[0].setImageResource(R.drawable.drizzletl);
            tr[0].setImageResource(R.drawable.drizzletr);
            bl[0].setImageResource(R.drawable.drizzlebl);
            br[0].setImageResource(R.drawable.drizzlebr);
            quote.setText("Raindrops' potential energy gets converted to kinetic during free fall");
        }
        else if(init.getId() >= 500 && init.getId() < 600)
        {
            tl[0].setImageResource(R.drawable.raintl);
            tr[0].setImageResource(R.drawable.raintr);
            bl[0].setImageResource(R.drawable.rainbl);
            br[0].setImageResource(R.drawable.rainbr);
            quote.setText("Rain falls from saturated clouds");
        }
        else if(init.getId() == 511)
        {
            tl[0].setImageResource(R.drawable.hailtl);
            tr[0].setImageResource(R.drawable.hailtr);
            bl[0].setImageResource(R.drawable.hailbl);
            br[0].setImageResource(R.drawable.hailbr);
            quote.setText("Hail shards fall at 9.8 m/s/s");
        }
        else if(init.getId() >= 600 && init.getId() < 700)
        {
            tl[0].setImageResource(R.drawable.snowtl);
            tr[0].setImageResource(R.drawable.snowtr);
            bl[0].setImageResource(R.drawable.snowbl);
            br[0].setImageResource(R.drawable.snowbr);
            quote.setText("Integrating the size of all the snowflakes gets total volume");
        }
        else if(init.getId() >= 700 && init.getId() < 800)
        {
            tl[0].setImageResource(R.drawable.windtl);
            tr[0].setImageResource(R.drawable.windtr);
            bl[0].setImageResource(R.drawable.windbl);
            br[0].setImageResource(R.drawable.windbr);
            quote.setText("Air resistance always opposes motion");
        }
        else if(init.getId() == 800 || init.getId() == 801)
        {
            tl[0].setImageResource(R.drawable.suntl);
            tr[0].setImageResource(R.drawable.suntr);
            bl[0].setImageResource(R.drawable.sunbl);
            br[0].setImageResource(R.drawable.sunbr);
            quote.setText("Light travels from the Sun at 300000 km/s");
        }
        else if(init.getId() == 802 || init.getId() == 803)
        {
            tl[0].setImageResource(R.drawable.partialcloudtl);
            tr[0].setImageResource(R.drawable.partialcloudtr);
            bl[0].setImageResource(R.drawable.partialcloudbl);
            br[0].setImageResource(R.drawable.partialcloudbr);
            quote.setText("Clouds deflect sunlight in all directions");
        }
        else
        {
            tl[imageState].setImageResource(R.drawable.cloudtl);
            tr[imageState].setImageResource(R.drawable.cloudtr);
            bl[imageState].setImageResource(R.drawable.cloudbl);
            br[imageState].setImageResource(R.drawable.cloudbr);
            quote.setText("The electron cloud is a region of negative charge around the nucleus");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentZipCode = String.valueOf(input.getText());
                if(input.getText().length() == 5)
                {
                    Search search = new Search();
                    search.setZipCode(String.valueOf(input.getText()));
                    try {
                        search.execute().get();
                    } catch (Exception e) { }
                    if(!search.getCity().equals(""))
                    {
                        error.setText("");
                        city.setText(search.getCity());
                        cityAnim.setTarget(city);
                        cityAnim.start();
                        temperature.setText(search.getTemperature());
                        tempAnim.setTarget(temperature);
                        tempAnim.start();
                        if (search.getId() >= 200 && search.getId() < 300) {
                            tl[imageState].setImageResource(R.drawable.thundertl);
                            tr[imageState].setImageResource(R.drawable.thundertr);
                            bl[imageState].setImageResource(R.drawable.thunderbl);
                            br[imageState].setImageResource(R.drawable.thunderbr);
                            quote.setText("Lightning is a discharge of electricity");
                        } else if (search.getId() >= 300 && search.getId() < 400) {
                            tl[imageState].setImageResource(R.drawable.drizzletl);
                            tr[imageState].setImageResource(R.drawable.drizzletr);
                            bl[imageState].setImageResource(R.drawable.drizzlebl);
                            br[imageState].setImageResource(R.drawable.drizzlebr);
                            quote.setText("Raindrops' potential energy gets converted to kinetic during free fall");
                        } else if (search.getId() >= 500 && search.getId() < 600) {
                            tl[imageState].setImageResource(R.drawable.raintl);
                            tr[imageState].setImageResource(R.drawable.raintr);
                            bl[imageState].setImageResource(R.drawable.rainbl);
                            br[imageState].setImageResource(R.drawable.rainbr);
                            quote.setText("Rain falls from saturated clouds");
                        } else if (search.getId() == 511) {
                            tl[imageState].setImageResource(R.drawable.hailtl);
                            tr[imageState].setImageResource(R.drawable.hailtr);
                            bl[imageState].setImageResource(R.drawable.hailbl);
                            br[imageState].setImageResource(R.drawable.hailbr);
                            quote.setText("Hail shards fall at 9.8 m/s/s");
                        } else if (search.getId() >= 600 && search.getId() < 700) {
                            tl[imageState].setImageResource(R.drawable.snowtl);
                            tr[imageState].setImageResource(R.drawable.snowtr);
                            bl[imageState].setImageResource(R.drawable.snowbl);
                            br[imageState].setImageResource(R.drawable.snowbr);
                            quote.setText("Integrating the size of all the snowflakes gets total volume");
                        } else if (search.getId() >= 700 && search.getId() < 800) {
                            tl[imageState].setImageResource(R.drawable.windtl);
                            tr[imageState].setImageResource(R.drawable.windtr);
                            bl[imageState].setImageResource(R.drawable.windbl);
                            br[imageState].setImageResource(R.drawable.windbr);
                            quote.setText("Air resistance always opposes motion");
                        } else if (search.getId() == 800 || search.getId() == 801) {
                            tl[imageState].setImageResource(R.drawable.suntl);
                            tr[imageState].setImageResource(R.drawable.suntr);
                            bl[imageState].setImageResource(R.drawable.sunbl);
                            br[imageState].setImageResource(R.drawable.sunbr);
                            quote.setText("Light travels from the Sun at 300000 km/s");
                        } else if (search.getId() == 802 || search.getId() == 803) {
                            tl[imageState].setImageResource(R.drawable.partialcloudtl);
                            tr[imageState].setImageResource(R.drawable.partialcloudtr);
                            bl[imageState].setImageResource(R.drawable.partialcloudbl);
                            br[imageState].setImageResource(R.drawable.partialcloudbr);
                            quote.setText("Clouds deflect sunlight in all directions");
                        } else {
                            tl[imageState].setImageResource(R.drawable.cloudtl);
                            tr[imageState].setImageResource(R.drawable.cloudtr);
                            bl[imageState].setImageResource(R.drawable.cloudbl);
                            br[imageState].setImageResource(R.drawable.cloudbr);
                            quote.setText("The electron cloud is a region of negative charge around the nucleus");
                        }
                        quoteAnim.setTarget(quote);
                        quoteAnim.start();
                        if(imageState == 0)
                        {
                            frontTL.setTarget(tl[1]);
                            backTL.setTarget(tl[0]);
                            frontTL.start();
                            backTL.start();
                            frontTR.setTarget(tr[1]);
                            backTR.setTarget(tr[0]);
                            frontTR.start();
                            backTR.start();
                            frontBL.setTarget(bl[1]);
                            backBL.setTarget(bl[0]);
                            frontBL.start();
                            backBL.start();
                            frontBR.setTarget(br[1]);
                            backBR.setTarget(br[0]);
                            frontBR.start();
                            backBR.start();
                            imageState = 1;
                        }
                        else
                        {
                            frontTL.setTarget(tl[0]);
                            backTL.setTarget(tl[1]);
                            frontTL.start();
                            backTL.start();
                            frontTR.setTarget(tr[0]);
                            backTR.setTarget(tr[1]);
                            frontTR.start();
                            backTR.start();
                            frontBL.setTarget(bl[0]);
                            backBL.setTarget(bl[1]);
                            frontBL.start();
                            backBL.start();
                            frontBR.setTarget(br[0]);
                            backBR.setTarget(br[1]);
                            frontBR.start();
                            backBR.start();
                            imageState = 0;
                        }
                        listAnim.setTarget(forecastBackground);
                        listAnim.start();
                    }
                    else
                    {
                        error.setText("Please enter a valid zip code");
                        errorAnim.setTarget(error);
                        errorAnim.start();
                    }
                }
                else
                {
                    error.setText("Please enter a valid zip code");
                    errorAnim.setTarget(error);
                    errorAnim.start();
                }
                input.setText("");
            }
        });
    }

    public class Search extends AsyncTask<Void, Void, Void>
    {
        private String json = "";
        private String zipCode = "10001";
        private String city = "";
        private String temperature = "";
        private int id = 0;

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
                URL url;
                url = new URL("https://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + ",us&appid=c5eeb19b31954587f25ec9288502c4fb");
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                json = reader.readLine();
                try {
                    JSONObject weather = new JSONObject(json);
                    city = weather.getString("name");
                    JSONObject main = weather.getJSONObject("main");
                    temperature = Double.toString(toFahrenheit(main.getDouble("temp"))) + "° | H:" + Double.toString(toFahrenheit(main.getDouble("temp_max"))) + "° | L:" + Double.toString(toFahrenheit(main.getDouble("temp_min"))) + "°";
                    String lat = Double.toString(weather.getJSONObject("coord").getDouble("lat"));
                    String lon = Double.toString(weather.getJSONObject("coord").getDouble("lon"));
                    URL forecastUrl = new URL("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=c5eeb19b31954587f25ec9288502c4fb");
                    URLConnection forecastConnect = forecastUrl.openConnection();
                    InputStream forecastStream = forecastConnect.getInputStream();
                    BufferedReader forecastReader = new BufferedReader(new InputStreamReader(forecastStream));
                    String forecastJson = forecastReader.readLine();
                    forecastList.clear();
                    for(int i = 0; i < 5; i++)
                    {
                        id = new JSONObject(forecastJson).getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
                        String max = String.valueOf(toFahrenheit(new JSONObject(forecastJson).getJSONArray("list").getJSONObject(i).getJSONObject("main").getDouble("temp_max")));
                        String min = String.valueOf(toFahrenheit(new JSONObject(forecastJson).getJSONArray("list").getJSONObject(i).getJSONObject("main").getDouble("temp_min")));
                        String time = new JSONObject(forecastJson).getJSONArray("list").getJSONObject(i).getString("dt_txt").substring(11);
                        forecastList.add(new Weather(max, min, id, time));
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) { }
            } catch (Exception e) { }
            return null;
        }

        public void setZipCode(String z)
        {
            zipCode = z;
        }

        public String getCity()
        {
            return city;
        }

        public String getTemperature()
        {
            return temperature;
        }
        private int getId()
        {
            return id;
        }

        private double toFahrenheit(double kelvin)
        {
            double result = (kelvin - 273.15) * 9/5 + 32;
            result *= 100;
            result = (int)result;
            return result / 100;
        }
    }
}