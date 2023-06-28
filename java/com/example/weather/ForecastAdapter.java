package com.example.weather;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ForecastAdapter extends ArrayAdapter<Weather>
{
    int xml;
    ArrayList<Weather> forecast;
    Context mainActivityContext;
    boolean start = true;

    public ForecastAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Weather> objects)
    {
        super(context, resource, objects);
        mainActivityContext = context;
        xml = resource;
        forecast = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View view;
        LayoutInflater inflater = (LayoutInflater)mainActivityContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(xml, null);

        TextView time = view.findViewById(R.id.Time);
        TextView high = view.findViewById(R.id.HighTemperature);
        TextView low = view.findViewById(R.id.LowTemperature);
        ImageView icon = view.findViewById(R.id.Icon);


        time.setText(forecast.get(position).getTime());
        high.setText(forecast.get(position).getHigh());
        low.setText(forecast.get(position).getLow());

        if(forecast.get(position).isMorning())
        {
            time.setTextColor(Color.parseColor("#024E8A"));
            time.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }

        if(forecast.get(position).getId() >= 200 && forecast.get(position).getId() < 300)
        {
            icon.setImageResource(R.drawable.thunder);
        }
        else if(forecast.get(position).getId() >= 300 && forecast.get(position).getId() < 400)
        {
            icon.setImageResource(R.drawable.drizzle);
        }
        else if(forecast.get(position).getId() >= 500 && forecast.get(position).getId() < 600)
        {
            icon.setImageResource(R.drawable.rain);
        }
        else if(forecast.get(position).getId() == 511)
        {
            icon.setImageResource(R.drawable.hail);
        }
        else if(forecast.get(position).getId() >= 600 && forecast.get(position).getId() < 700)
        {
            icon.setImageResource(R.drawable.snow);
        }
        else if(forecast.get(position).getId() >= 700 && forecast.get(position).getId() < 800)
        {
            icon.setImageResource(R.drawable.wind);
        }
        else if(forecast.get(position).getId() == 800 || forecast.get(position).getId() == 801)
        {
            icon.setImageResource(R.drawable.sun);
        }
        else if(forecast.get(position).getId() == 802 || forecast.get(position).getId() == 803)
        {
            icon.setImageResource(R.drawable.partialcloud);
        }
        else
        {
            icon.setImageResource(R.drawable.cloud);
        }

        return view;
    }
}
