package com.example.roberto.materialmeteo.Controller;

import android.net.Uri;
import android.os.AsyncTask;
import com.example.roberto.materialmeteo.MyException;
import com.example.roberto.materialmeteo.ServiceListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import android.os.Handler;




public class HTTPController {

    private static HTTPController instance = null;
    private ServiceListener listener;
    private Exception error = null;
    private final int METRIC_UNIT = 0;
    private final int IMPERIAL_UNIT = 1;
    private final int HTTP_TIMEOUT = 10000;
    private final String YAHOO_API = "https://query.yahooapis.com/v1/public/yql?q=%s&format=json";
    private String unitConfiguration;


    //Return Singleton Instance
    public static HTTPController getSingletonInstance(ServiceListener listener) {
        if (HTTPController.instance == null) {
            HTTPController.instance = new HTTPController(listener);
            HTTPController.instance.setDefaultUnitConfiguration();
        }
        return instance;
    }

    //Return Singleton Instance
    public static HTTPController getSingletonInstance() {
        if (HTTPController.instance == null){
            HTTPController.instance = new HTTPController();
        }
        return instance;
    }

    private HTTPController() {} ;

    private HTTPController(ServiceListener listener) {
        this.listener = listener;
    }




    //setter
    public void setUnitConfiguration(int unit){

      switch (unit){

          case METRIC_UNIT: this.unitConfiguration = "c";
              break;
          case IMPERIAL_UNIT: this.unitConfiguration = "f";
              break;

          default: setDefaultUnitConfiguration();

      }
    }


    //setter
    public void setDefaultUnitConfiguration(){

        if((Locale.getDefault().getCountry()).equals("US"))
            this.unitConfiguration = "f";
        else
            this.unitConfiguration = "c";
    }





    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to get the data related to the cities                                   //
    //     Use the PlaceDataAsyncTrask<>                                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void getPlaceData( String city) {

        if (city.isEmpty())
            listener.serviceFailure(MyException.STR_ERROR_NOTHING_SEARCH);

        else {

            AsyncTask httpPlaceCompute = new PlaceDataAsyncTrask().execute(city);

            //Handler to kill the PlaceDataAsyncTrask<> after HTTP_TIMEOUT seconds
            Handler timerHTTP = new Handler();
            timerHTTP.postDelayed(new TimerRunnable(httpPlaceCompute,timerHTTP),HTTP_TIMEOUT);

        }
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to get the weather data                                                 //
    //     Use the WeatherDataAsyncTask<>                                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void getWeatherData(String city) {

        if (city.isEmpty())
            listener.serviceFailure(MyException.STR_ERROR_FETCHING);


        else {

            AsyncTask httpWeatherCompute = new WeatherDataAsyncTask().execute(city);

            //Handler to kill the PlaceDataAsyncTrask<> after HTTP_TIMEOUT seconds
            Handler timerHTTP = new Handler();
            timerHTTP.postDelayed(new TimerRunnable(httpWeatherCompute,timerHTTP),HTTP_TIMEOUT);

        }
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Custom AsyncTask to performing the download of the city information                   //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private class PlaceDataAsyncTrask extends AsyncTask<String,Void,JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... citytoSearch) {


            String YQL = String.format("select * from geo.places where text=\"%s\" and lang=\"%s\"", citytoSearch[0], Locale.getDefault().getLanguage());
            String endpoint = String.format(YAHOO_API, Uri.encode(YQL));
            error = null;


            try
            {
                JSONObject data = computeHttpConnetion(endpoint);
                return data;

            } catch (Exception e) { error = e; }

            return null;

        }


        // Use of the listeners to communicate with the Main Thread UI
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if(error == null)
                JSONController.getSingletonInstance().cityDispatch(jsonObject);
            else
                listener.serviceFailure(MyException.STR_ERROR_DOWNLOADING);
        }


        // Use of the listeners to communicate with the Main Thread UI
        // onCancelled is invoked only if the AsyncTask is killed by the Handler
        @Override
        protected void onCancelled(JSONObject jsonObject) {
            super.onCancelled(jsonObject);
            listener.serviceFailure(MyException.STR_ERROR_TIMEOUT);
        }

    }





    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Custom class to implementing the Handler behavior                                     //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private class TimerRunnable implements Runnable{

        private AsyncTask asyncTaskToControl;
        private Handler handlerFather;


        private TimerRunnable(AsyncTask asyncTaskToControl, Handler handlerFather){
            this.asyncTaskToControl = asyncTaskToControl;
            this.handlerFather = handlerFather;
        }

        @Override
        public void run() {

            if(asyncTaskToControl.getStatus() != AsyncTask.Status.FINISHED)
                asyncTaskToControl.cancel(true);

            handlerFather.removeCallbacks(this);
        }
    }





    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Custom AsyncTask to performing the download of weather data                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public class WeatherDataAsyncTask extends AsyncTask<String,Void,JSONObject>{


        @Override
        protected JSONObject doInBackground(String... citytoSearch) {

            String YQL = String.format("select * from weather.forecast where woeid=%s and u=\"%s\"", citytoSearch[0], unitConfiguration);
            String endpoint = String.format(YAHOO_API, Uri.encode(YQL));
            error = null;

            try
            {
                JSONObject data = computeHttpConnetion(endpoint);
                return data;

            } catch (Exception e) {error = e;  }


            return null;
        }


        // Use of the listeners to communicate with the Main Thread UI
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if(error == null)
                JSONController.getSingletonInstance().weatherDispatch(jsonObject);
            else
                listener.serviceFailure(MyException.STR_ERROR_WEATHER_DATA);

        }


        // Use of the listeners to communicate with the Main Thread UI
        // onCancelled is invoked only if the AsyncTask is killed by the Handler
        @Override
        protected void onCancelled(JSONObject jsonObject) {
            super.onCancelled(jsonObject);
            listener.serviceFailure(MyException.STR_ERROR_TIMEOUT);
        }


    }




    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Private method to perform the HTTP Request Connections                                //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private JSONObject computeHttpConnetion(String endpoint) throws Exception {

        URL url = new URL(endpoint);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setConnectTimeout(HTTP_TIMEOUT);
        connection.setReadTimeout(HTTP_TIMEOUT);
        connection.connect();

        InputStream inputStream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return new JSONObject(result.toString());

    }





    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to perform the ImageRequest forwarded by the DraweeController           //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public DraweeController download(DraweeController oldController, ControllerListener controllerListener, Uri uri){



        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(oldController)
                .setControllerListener(controllerListener)

                .build();

        return controller;

    }


}


