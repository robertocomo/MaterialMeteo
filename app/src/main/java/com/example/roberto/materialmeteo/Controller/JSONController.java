package com.example.roberto.materialmeteo.Controller;

import android.os.AsyncTask;
import com.example.roberto.materialmeteo.Boundary.CityBoundary;
import com.example.roberto.materialmeteo.Entity.City;
import com.example.roberto.materialmeteo.Boundary.WeatherBoundary;
import com.example.roberto.materialmeteo.MyException;
import com.example.roberto.materialmeteo.ServiceListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;




public class JSONController {

    private static JSONController instance = null;


    //Return Singleton Instance
    public static JSONController getSingletonInstance() {
        if (JSONController.instance == null)
            JSONController.instance = new JSONController();
        return instance;
    }

    //Return Singleton Instance
    public static JSONController getSingletonInstance(ServiceListener listener) {
        if (JSONController.instance == null)
            JSONController.instance = new JSONController(listener);
        return instance;
    }


    private JSONController(){};
    private JSONController(ServiceListener listener){this.listener=listener;}


    private ServiceListener listener;
    private JSONArray jsonPlace = new JSONArray();
    private JSONObject jsonChannel;




    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to decode the city information                                          //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void cityDispatch(JSONObject httpToJSON){

        int error_code = 0;

        try {

            JSONObject queryResults = httpToJSON.optJSONObject("query");
            int count = queryResults.optInt("count");

            if (count == 0) {
                error_code = MyException.STR_ERROR_INFO_CITY;
                throw new MyException();
            }

            if (count == 1)
                jsonPlace = new JSONArray().put(queryResults.optJSONObject("results").optJSONObject("place"));
            else
                jsonPlace = queryResults.optJSONObject("results").optJSONArray("place");


            if (jsonPlace == null)
                listener.serviceFailure(MyException.STR_ERROR_DECODE_DATA);
             else
                listener.placeListener(cityToList(jsonPlace));


        }
        catch (Exception e) {

            if(error_code == 0)
                error_code = MyException.STR_ERROR_DECODE_DATA;

            listener.serviceFailure(error_code);    }


    }





    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to decode the weather information                                       //
    //     Use the WeatherDispatchAsyncTask<>                                                    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void weatherDispatch(JSONObject httpToJSON){

        JSONObject queryResults = httpToJSON.optJSONObject("query");

        int count = queryResults.optInt("count");

        if (count == 0)
            listener.serviceFailure(MyException.STR_ERROR_INFO_WEATHER);
        else
        {
            AsyncTask weatherDispatchAsyncTask = new WeatherDispatchAsyncTask().execute(queryResults);
        }
    }





    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Custom AsyncTask to performing the decoding of the weather information                //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private class WeatherDispatchAsyncTask extends AsyncTask<JSONObject,Void,WeatherBoundary>{

        private Exception exception = null;

        @Override
        protected WeatherBoundary doInBackground(JSONObject... queryResults) {


            try
            {
                WeatherBoundary weatherBoundary = new WeatherBoundary();
                jsonChannel = queryResults[0].optJSONObject("results").optJSONObject("channel");

                JSONObject jsonUnits = jsonChannel.optJSONObject("units");

                weatherBoundary.units.setDistance(jsonUnits.optString("distance"));
                weatherBoundary.units.setPressure(jsonUnits.optString("pressure"));
                weatherBoundary.units.setSpeed(jsonUnits.optString("speed"));
                weatherBoundary.units.setTemperature(jsonUnits.optString("temperature"));


                JSONObject jsonWind = jsonChannel.optJSONObject("wind");

                weatherBoundary.wind.setChill(jsonWind.optInt("chill"));
                weatherBoundary.wind.setDirection(jsonWind.optInt("direction"));
                weatherBoundary.wind.setSpeed(jsonWind.optDouble("speed"));


                JSONObject jsonAtmosphere = jsonChannel.optJSONObject("atmosphere");

                weatherBoundary.atmosphere.setHumidity(jsonAtmosphere.optInt("humidity"));
                weatherBoundary.atmosphere.setPressure(BigDecimal.valueOf(jsonAtmosphere.optDouble("pressure")).floatValue());
                weatherBoundary.atmosphere.setRising(jsonAtmosphere.optInt("rising"));
                weatherBoundary.atmosphere.setVisibility(BigDecimal.valueOf(jsonAtmosphere.optDouble("visibility")).floatValue());


                JSONObject jsonAstronomy = jsonChannel.optJSONObject("astronomy");

                weatherBoundary.astronomy.setSunRise(jsonAstronomy.optString("sunrise"));
                weatherBoundary.astronomy.setSunSet(jsonAstronomy.optString("sunset"));


                JSONObject jsonCondition = jsonChannel.optJSONObject("item").optJSONObject("condition");

                weatherBoundary.condition.setCode(jsonCondition.optInt("code"));
                weatherBoundary.condition.setDate(jsonCondition.optString("date"));
                weatherBoundary.condition.setDescription(jsonCondition.optString("text"));
                weatherBoundary.condition.setTemp(jsonCondition.optInt("temp"));


                JSONArray jsonForecast = jsonChannel.optJSONObject("item").optJSONArray("forecast");
                JSONObject jsonForecastToday = jsonForecast.optJSONObject(0);

                weatherBoundary.forecastToday.setCode(jsonForecastToday.optInt("code"));
                weatherBoundary.forecastToday.setDescription(jsonForecastToday.optString("text"));
                weatherBoundary.forecastToday.setTempMax(jsonForecastToday.optInt("high"));
                weatherBoundary.forecastToday.setTempMin(jsonForecastToday.optInt("low"));
                weatherBoundary.forecastToday.setDate(jsonForecastToday.optString("date"));
                weatherBoundary.forecastToday.setDay(jsonForecastToday.optString("day"));


                JSONObject jsonForecastTomorrow = jsonForecast.optJSONObject(1);

                weatherBoundary.forecastTomorrow.setCode(jsonForecastTomorrow.optInt("code"));
                weatherBoundary.forecastTomorrow.setDescription(jsonForecastTomorrow.optString("text"));
                weatherBoundary.forecastTomorrow.setTempMax(jsonForecastTomorrow.optInt("high"));
                weatherBoundary.forecastTomorrow.setTempMin(jsonForecastTomorrow.optInt("low"));
                weatherBoundary.forecastTomorrow.setDate(jsonForecastTomorrow.optString("date"));
                weatherBoundary.forecastTomorrow.setDay(jsonForecastTomorrow.optString("day"));



                return weatherBoundary;

            }
            catch (RuntimeException e) {exception = new MyException() ; }

            return null;
        }


        @Override
        protected void onPostExecute(WeatherBoundary weatherBoundary) {
            super.onPostExecute(weatherBoundary);

            if((weatherBoundary == null) || (exception != null))
                listener.serviceFailure(MyException.STR_NOTE_WEATHER);
            else
                listener.weatherListener(weatherBoundary);
        }


    }




    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to Dispatch the City Class and returning an CityBoundary object         //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public CityBoundary cityToList(JSONArray jsonPlace) {

        List<City> elenco = new ArrayList<City>();
        JSONObject jsonObject;
        City city = null;

        int lenght = jsonPlace.length();

        for (int i = 0; i < lenght; i++) {

            jsonObject = jsonPlace.optJSONObject(i);

            city = new City();

            try {


                city.setWoeid(jsonObject.getString("woeid"));
                city.setName(jsonObject.getString("name"));
                city.setLatitude(jsonObject.getJSONObject("centroid").getString("latitude"));
                city.setLongitude(jsonObject.getJSONObject("centroid").getString("longitude"));
                city.setDistrict(jsonObject.getJSONObject("admin1").getString("content"));
                city.setCountry((jsonObject.getJSONObject("country")).getString("content"));

            } catch (Exception e) {  listener.serviceFailure(MyException.STR_NOTE_CITY);      }


            elenco.add(city);

        }

        return new CityBoundary(elenco);

    }


}