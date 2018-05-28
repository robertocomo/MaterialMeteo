package com.example.roberto.materialmeteo.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.roberto.materialmeteo.Entity.City;
import com.facebook.drawee.backends.pipeline.Fresco;


public class PreferencesController {



    public static final String PREFS_NAME = "MaterialMeteoPreferences";
    public static final String CITY_WOEID_KEY = "woeid";
    public static final String CITY_NAME_KEY = "name";
    public static final String CITY_DISTRICT_KEY = "district";
    public static final String CITY_COUNTRY_KEY = "country";
    public static final String CITY_LATITUDE_KEY = "latitude";
    public static final String CITY_LONGITUDE_KEY = "longitude";
    public static final int UNIT_NOT_FOUND = -1;

    public static final String UNIT_KEY = "unit";


    private City preferencesCity;

    private SharedPreferences preferences;

    public PreferencesController(Context context){
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferencesCity = new City();
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to get any saved preferences and then verify the complete integrity     //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public boolean verifyCityPreferencesExistence() {

        preferencesCity = new City();

        preferencesCity.setWoeid(preferences.getString(CITY_WOEID_KEY,null));
        preferencesCity.setCountry(preferences.getString(CITY_COUNTRY_KEY,null));
        preferencesCity.setDistrict(preferences.getString(CITY_DISTRICT_KEY,null));
        preferencesCity.setName(preferences.getString(CITY_NAME_KEY,null));
        preferencesCity.setLatitude(preferences.getString(CITY_LATITUDE_KEY,null));
        preferencesCity.setLongitude(preferences.getString(CITY_LONGITUDE_KEY,null));



            return !((preferencesCity.getName() == null)
                    || (preferencesCity.getWoeid() == null)
                    || (preferencesCity.getDistrict() == null)
                    || (preferencesCity.getCountry() == null)
                    || (preferencesCity.getLongitude() == null)
                    || (preferencesCity.getLatitude() == null));

    }


    //Getter
    public City getCityPreferences(){

        return preferencesCity;

    }


    //Getter
    public int getUnitPreferences(){

        return preferences.getInt(UNIT_KEY,UNIT_NOT_FOUND);}




    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to save city preferences                                                //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void saveCityPreferences(City cityToSave) {


        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(CITY_WOEID_KEY, cityToSave.getWoeid());
        editor.putString(CITY_DISTRICT_KEY, cityToSave.getDistrict());
        editor.putString(CITY_COUNTRY_KEY, cityToSave.getCountry());
        editor.putString(CITY_NAME_KEY, cityToSave.getName());
        editor.putString(CITY_LATITUDE_KEY, cityToSave.getLatitude());
        editor.putString(CITY_LONGITUDE_KEY, cityToSave.getLongitude());
        editor.apply();
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to save unit preferences                                                //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void saveUnitPreferences(int unitToSave) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(UNIT_KEY, unitToSave);

        editor.apply();
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to erase all preferences                                                //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void eraseData(){

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

    }




}


