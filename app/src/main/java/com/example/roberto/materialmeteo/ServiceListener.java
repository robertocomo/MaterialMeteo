package com.example.roberto.materialmeteo;

import com.example.roberto.materialmeteo.Boundary.CityBoundary;
import com.example.roberto.materialmeteo.Boundary.WeatherBoundary;


public interface ServiceListener {

    void placeListener(CityBoundary jsonPlace);

    void weatherListener(WeatherBoundary weatherBoundary);

    void serviceFailure(Exception exception);

    //Overloading method
    void serviceFailure(int error);
}