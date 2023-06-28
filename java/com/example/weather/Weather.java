package com.example.weather;

public class Weather {

    private String high;
    private String low;
    private int id;
    private String time;
    private boolean morning;

    public Weather(String h, String l, int i, String t)
    {
        high = h;
        low = l;
        id = i;
        setTime(t);
        if(Integer.parseInt(t.substring(0, 2)) - 5 >= 12 || Integer.parseInt(t.substring(0, 2)) - 5 <= 0)
        {
            morning = false;
        }
        else
        {
            morning = true;
        }
    }
    public void setTime(String t)
    {
        int initTime = Integer.parseInt(t.substring(0, 2));
        int finalTime = Integer.parseInt(t.substring(0, 2)) + 3;
        initTime -= 5;
        finalTime -= 5;
        if(initTime > 12)
        {
            initTime -= 12;
        }
        else if(initTime <= 0)
        {
            initTime += 12;
        }
        if(finalTime > 12)
        {
            finalTime -= 12;
        }
        else if(finalTime <= 0)
        {
            finalTime += 12;
        }
        time = initTime + ":00 - " + finalTime + ":00";
    }
    public String getTime()
    {
        return time;
    }

    public int getId()
    {
        return id;
    }

    public String getHigh()
    {
        return "H: " + high + "°";
    }

    public String getLow()
    {
        return "L: " + low + "°";
    }
    public boolean isMorning()
    {
        return morning;
    }
}
