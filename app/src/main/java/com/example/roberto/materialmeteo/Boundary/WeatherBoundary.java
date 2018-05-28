package com.example.roberto.materialmeteo.Boundary;

import android.net.Uri;

import com.example.roberto.materialmeteo.Entity.Astronomy;
import com.example.roberto.materialmeteo.Entity.Atmosphere;
import com.example.roberto.materialmeteo.Entity.Condition;
import com.example.roberto.materialmeteo.Entity.Forecast;
import com.example.roberto.materialmeteo.Entity.Units;
import com.example.roberto.materialmeteo.Entity.Wind;

import java.util.Locale;


public class WeatherBoundary {

    public Condition condition = new Condition();
    public Wind wind = new Wind();

    public Forecast forecastToday = new Forecast();
    public Forecast forecastTomorrow = new Forecast();

    public Astronomy astronomy = new Astronomy();
    public Units units = new Units();

    public Atmosphere atmosphere = new Atmosphere();



    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to create a valid truthful Uri string download                          //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final String URI_TO_PARSE="http://l.yimg.com/a/i/us/we/52/%s.gif";
    public Uri getUriCondition(){

        return Uri.parse(String.format(URI_TO_PARSE,String.format(Locale.getDefault(),"%d",condition.getCode())));
    }

    public Uri getUriForecastToday(){

        return Uri.parse(String.format(URI_TO_PARSE,String.format(Locale.getDefault(),"%d",forecastToday.getCode())));
    }

    public Uri getUriForecastTomorrow(){

        return Uri.parse(String.format(URI_TO_PARSE,String.format(Locale.getDefault(),"%d",forecastTomorrow.getCode())));
    }




}
