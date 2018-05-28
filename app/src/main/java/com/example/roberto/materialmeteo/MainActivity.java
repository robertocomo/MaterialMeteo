package com.example.roberto.materialmeteo;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.example.roberto.materialmeteo.Boundary.CityBoundary;
import com.example.roberto.materialmeteo.Controller.HTTPController;
import com.example.roberto.materialmeteo.Controller.JSONController;
import com.example.roberto.materialmeteo.Controller.PreferencesController;
import com.example.roberto.materialmeteo.Boundary.WeatherBoundary;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.util.Locale;



///////////////////////////////////////////////////////////////////////////////////////////////
//   This project to work and run must have the following dependencies:                      //
//                                                                                           //
//                                                                                           //
//            compile 'com.jaredrummler:material-spinner:1.1.0'                              //
//            compile 'com.android.support:appcompat-v7:24.2.0'                              //
//            compile 'com.android.support:design:24.2.0'                                    //
//            compile 'com.android.support:cardview-v7:24.2.0'                               //
//            compile 'com.elmargomez.typer:typerlib:1.0.0'                                  //
//            compile 'com.facebook.fresco:fresco:0.13.0'                                    //
//                                                                                           //
//                                                                                           //
//                                                                                           //
///////////////////////////////////////////////////////////////////////////////////////////////






///////////////////////////////////////////////////////////////////////////////////////////////
//      Main Actityvy Thread UI                                                              //
///////////////////////////////////////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity implements ServiceListener {

    //private fields
    private JSONController jsonController;
    private HTTPController httpController;
    private PreferencesController preferencesController;
    private CityBoundary myCityBoundary;

    private MaterialSpinner spinner;

    private SearchView searchViewAndroidActionBar;

    private TextView displayCoordinateLatitudeTextView;
    private TextView displayCoordinateLongitudeTextView;
    private TextView displayTemperatureTextView;
    private TextView displayWindTextView;
    private TextView displayPressureTextView;
    private TextView displayHumidityTextView;
    private TextView displaySunsetTextView;
    private TextView displaySunriseTextView;
    private TextView displayDateTodayTextView;
    private TextView displayDayTodayTextView;
    private TextView displayDateTomorrowTextView;
    private TextView displayDayTomorrowTextView;
    private TextView displayMaxTemperatureTodayTextView;
    private TextView displayMinTemperatureTodayTextView;
    private TextView displayMinTemperatureTomorrowTextView;
    private TextView displayMaxTemperatureTomorrowTextView;
    private TextView displayDescriptionTodayTextView;
    private TextView displayDescriptionTomorrowTextView;
    private TextView displayDescriptionConditionTextView;
    private SimpleDraweeView displayImageConditionSimpleDraweeView;
    private SimpleDraweeView displayForecastTodaySimpleDraweeView;
    private SimpleDraweeView displayForecastTomorrowSimpleDraweeView;
    private ProgressBar displayImageConditionProgressBar;
    private ControllerListener controllerListenerDraweeController;
    private CoordinatorLayout homeLayout;
    private DraweeController frescoControllerCondition;
    private DraweeController frescoControllerToday;
    private DraweeController frescoControllerTomorrow;
    private AlertDialog dialog;
    private MenuItem searchViewItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int tempUnitConfiguration;
    private LinearLayout todayConditionLinearLayout;
    private CardView forecastCardViewBottom;
    private TextView displayDegreesTextView;
    private TextView displayVisibilityTextView;
    private boolean firstRunUpdateGUIBoolean = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inizialize fresco controller using Singleton Pattern
        Fresco.initialize(this);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Inizialize HTTP and JSON Controller using Singleton Pattern
        jsonController = JSONController.getSingletonInstance(this);

        httpController = HTTPController.getSingletonInstance(this);
        preferencesController = new PreferencesController(this);


        ////////////////////////////////////////////////////////////////////////////////////////
        //  Block FindViewByID                                                                //
        ////////////////////////////////////////////////////////////////////////////////////////
        displayCoordinateLongitudeTextView = (TextView) findViewById(R.id.displayCoordinateLongitudeTextView);
        displayCoordinateLatitudeTextView = (TextView) findViewById(R.id.displayCoordinateLatitudeTextView);
        displayTemperatureTextView = (TextView) findViewById(R.id.displayTemperatureTextView);
        displayTemperatureTextView = (TextView) findViewById(R.id.displayTemperatureTextView);
        displayWindTextView = (TextView) findViewById(R.id.displayWindTextView);
        displayPressureTextView = (TextView) findViewById(R.id.displayPressureTextView);
        displayHumidityTextView = (TextView) findViewById(R.id.displayHumidityTextView);
        displaySunsetTextView = (TextView) findViewById(R.id.displaySunsetTextView);
        displaySunriseTextView = (TextView) findViewById(R.id.displaySunriseTextView);
        displayDateTodayTextView = (TextView) findViewById(R.id.displayDateTodayTextView);
        displayDayTodayTextView = (TextView) findViewById(R.id.displayDayTodayTextView);
        displayMaxTemperatureTodayTextView = (TextView) findViewById(R.id.displayMaxTemperatureTodayTextView);
        displayMinTemperatureTodayTextView = (TextView) findViewById(R.id.displayMinTemperatureTodayTextView);
        displayMaxTemperatureTomorrowTextView = (TextView) findViewById(R.id.displayMaxTemperatureTomorrowTextView);
        displayMinTemperatureTomorrowTextView = (TextView) findViewById(R.id.displayMinTemperatureTomorrowTextView);
        displayDescriptionTodayTextView = (TextView) findViewById(R.id.displayDescriptionTodayTextView);
        displayDescriptionTomorrowTextView = (TextView) findViewById(R.id.displayDescriptionTomorrowTextView);
        displayDescriptionConditionTextView = (TextView) findViewById(R.id.displayDescriptionConditionTextView);
        displayDateTomorrowTextView = (TextView) findViewById(R.id.displayDateTomorrowTextView);
        displayDayTomorrowTextView = (TextView) findViewById(R.id.displayDayTomorrowTextView);
        displayDegreesTextView = (TextView) findViewById(R.id.displayDegreesTextView);
        displayVisibilityTextView = (TextView) findViewById(R.id.displayVisibilityTextView);

        displayImageConditionSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.displayImageConditionSimpleDraweeView);
        displayForecastTodaySimpleDraweeView = (SimpleDraweeView) findViewById(R.id.displayForecastTodaySimpleDraweeView);
        displayForecastTomorrowSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.displayForecastTomorrowSimpleDraweeView);

        forecastCardViewBottom = (CardView) findViewById(R.id.forecastCardViewBottom);

        displayImageConditionProgressBar = (ProgressBar)findViewById(R.id.displayImageConditionProgressBar);

        todayConditionLinearLayout = (LinearLayout) findViewById(R.id.todayConditionLinearLayout);
        homeLayout = (CoordinatorLayout) findViewById(R.id.homeLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);

        // Personalize the SwipeRefreshLayout
        swipeRefreshLayout.setColorSchemeResources(
                R.color.materialGreen700,
                R.color.colorPrimaryDark,
                R.color.colorPrimaryDark);


        // Handle the refresh gesture by performing refreshWeatherData()
        swipeRefreshLayout.setOnRefreshListener(new     SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWetherData();
            }
        });


        // Graphical inizial setup
        todayConditionLinearLayout.setAlpha(0.0f);
        forecastCardViewBottom.setAlpha(0.0f);

        // Initialization of the first setup
        controllerListenerDraweeController = new MybaseControllListener();
        httpController.setUnitConfiguration(preferencesController.getUnitPreferences());

        displayTemperatureTextView.setTypeface(Typer.set(getApplicationContext()).getFont(Font.ROBOTO_THIN));
        displayDegreesTextView.setTypeface(Typer.set(getApplicationContext()).getFont(Font.ROBOTO_LIGHT));


    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Handle the Voice to Text features request                                                     //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onNewIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {

            String query = intent.getStringExtra(SearchManager.QUERY);
            searchViewAndroidActionBar.setQuery(query,true);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Setup the menu layout to improve the searching features                                        //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchViewItem = menu.findItem(R.id.searchToolbar);

        searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchViewAndroidActionBar.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchViewAndroidActionBar.setSubmitButtonEnabled(true);


        //Setting the onQueryTextListenet to the SearchView Widget
        searchViewAndroidActionBar.setOnQueryTextListener(new mySearchViewOnQueryTextListener());


        //////////////////////////////////////////////////////////////////////////
        //      Initial choice of the behavior to be performed on the basis     //
        //      of the presence of any saved preferences                        //
        //////////////////////////////////////////////////////////////////////////
        if(preferencesController.verifyCityPreferencesExistence())
        {
            myCityBoundary= new CityBoundary(preferencesController.getCityPreferences());
            swipeRefreshLayout.setRefreshing(true);
            refreshWetherData();
        }

        else
            if(firstRunUpdateGUIBoolean)
                showFirstRunDialog();

        return super.onCreateOptionsMenu(menu);
    }




    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Manage the click of the elements of the menu                                                   //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.actionRefresh: {
                swipeRefreshLayout.setRefreshing(true);
                refreshWetherData();
            }   break;

            case R.id.action_settings: showSettingDialog();
                break;

            case R.id.actionLike: showLikeDialog();
                break;

            case R.id.actionErase: {

                if((preferencesController.getUnitPreferences() != PreferencesController.UNIT_NOT_FOUND) || (preferencesController.verifyCityPreferencesExistence()))
                    showEraseDialog();
                else
                    Snackbar.make(homeLayout,getResources().getString(R.string.strNoSavedSetting), Snackbar.LENGTH_SHORT).show();
            }
            break;

        }

        return super.onOptionsItemSelected(item);
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Customized Listener by the implementation of the ServiceListener interface                     //
    //      to receive from the controller the results of the city found                                   //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void placeListener(CityBoundary cityBoundary) {

        myCityBoundary = cityBoundary;


        //case which were found multiple matches conform to the city inserted
        if (myCityBoundary.moreThanOneCity()) {

            ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.list_item,R.id.textView,myCityBoundary.cityToString());
            ListView listView=new ListView(this);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    displayImageConditionProgressBar.setVisibility(View.VISIBLE);
                    myCityBoundary.setCitySelected(position);

                    // first checking if there is at least one internet connection before performing the access
                    if(isNetworkAvailable()) {
                        dialog.dismiss();
                        swipeRefreshLayout.setRefreshing(true);
                        httpController.getWeatherData(myCityBoundary.getCitySelected().getWoeid());
                    }
                    else
                        dialog.dismiss();

                }

            });


            AlertDialog.Builder builder = new  AlertDialog.Builder(MainActivity.this);

            builder.setCancelable(true);

            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            });



            builder.setView(listView);

            dialog = builder.create();

            dialog.show();


        }

        //if the found corresponding city is one
        else
        {
            // first checking if there is at least one internet connection before performing the access
            if(isNetworkAvailable()) {
                displayImageConditionProgressBar.setVisibility(View.VISIBLE);
                httpController.getWeatherData(myCityBoundary.getCitySelected().getWoeid());
             }
        }
    }





    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Customized Listener by the implementation of the ServiceListener interface to receive          //
    //      from the controller the results weather data related to the city selected                      //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void weatherListener(WeatherBoundary weatherBoundary) {


        //Configuration of the controllers related to fresco SimpleDraweeView
        frescoControllerCondition = httpController.download(frescoControllerCondition, controllerListenerDraweeController, weatherBoundary.getUriCondition());
        frescoControllerToday = httpController.download(frescoControllerToday, controllerListenerDraweeController, weatherBoundary.getUriForecastToday());
        frescoControllerTomorrow = httpController.download(frescoControllerTomorrow, controllerListenerDraweeController, weatherBoundary.getUriForecastTomorrow());

        //Graphical effect
        if(firstRunUpdateGUIBoolean){

            todayConditionLinearLayout.animate().alpha(1.0f).setDuration(700);
            forecastCardViewBottom.animate().alpha(1.0f).setDuration(700);

            firstRunUpdateGUIBoolean= false;

        }


        //Unpacking of the weather information found, to can be represented on the graphics
        String strTempOutput = "";

        try {
            getSupportActionBar().setTitle(myCityBoundary.getCitySelected().getName());
            getSupportActionBar().setSubtitle(myCityBoundary.getCitySelected().getPartialDescription());

            displayCoordinateLatitudeTextView.setText(myCityBoundary.getCitySelected().getLatitude());
            displayCoordinateLongitudeTextView.setText(myCityBoundary.getCitySelected().getLongitude());
            displayTemperatureTextView.setText(String.format(Locale.getDefault(), "%d", weatherBoundary.condition.getTemp()) );


            strTempOutput = String.format(Locale.getDefault(), "%.2f", weatherBoundary.wind.getSpeed()).concat(" ").concat(weatherBoundary.units.getSpeed());
            displayWindTextView.setText(strTempOutput);

            displaySunsetTextView.setText(weatherBoundary.astronomy.getSunSet());
            displaySunriseTextView.setText(weatherBoundary.astronomy.getSunRise());
            displayMaxTemperatureTodayTextView.setText(String.format(Locale.getDefault(), "%d", weatherBoundary.forecastToday.getTempMax()));
            displayMinTemperatureTodayTextView.setText(String.format(Locale.getDefault(), "%d", weatherBoundary.forecastToday.getTempMin()));
            displayMaxTemperatureTomorrowTextView.setText(String.format(Locale.getDefault(), "%d", weatherBoundary.forecastTomorrow.getTempMax()));
            displayMinTemperatureTomorrowTextView.setText(String.format(Locale.getDefault(), "%d", weatherBoundary.forecastTomorrow.getTempMin()));
            displayDescriptionTodayTextView.setText(weatherBoundary.forecastToday.getDescription());
            displayDescriptionTomorrowTextView.setText(weatherBoundary.forecastTomorrow.getDescription());
            displayDescriptionConditionTextView.setText(weatherBoundary.condition.getDescription());

            strTempOutput = String.format(Locale.getDefault(), "%.2f", weatherBoundary.atmosphere.getPressure()).concat(" ").concat(weatherBoundary.units.getPressure());
            displayPressureTextView.setText(strTempOutput);

            strTempOutput = String.format(Locale.getDefault(), "%d", weatherBoundary.atmosphere.getHumidity()).concat("%");
            displayHumidityTextView.setText(strTempOutput);

            strTempOutput = String.format(Locale.getDefault(), "%.2f", weatherBoundary.atmosphere.getVisibility()).concat(" ").concat(weatherBoundary.units.getDistance());
            displayVisibilityTextView.setText(strTempOutput);


            displayDegreesTextView.setText(String.format("° %s", weatherBoundary.units.getTemperature()));

            displayDateTodayTextView.setText(weatherBoundary.forecastToday.getDate());
            displayDayTodayTextView.setText(weatherBoundary.forecastToday.getDay());
            displayDateTomorrowTextView.setText(weatherBoundary.forecastTomorrow.getDate());
            displayDayTomorrowTextView.setText(weatherBoundary.forecastTomorrow.getDay());


            displayImageConditionSimpleDraweeView.setVisibility(View.VISIBLE);

            displayForecastTomorrowSimpleDraweeView.setController(frescoControllerTomorrow);
            displayForecastTodaySimpleDraweeView.setController(frescoControllerToday);
            displayImageConditionSimpleDraweeView.setController(frescoControllerCondition);

        }
        catch (NullPointerException e){serviceFailure(new MyException(getResources().getString(R.string.strErrorGui)));};

    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Customized Listener by the implementation of the ServiceListener interface to                  //
    //      handle various situations in which an exception is thrown                                      //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void serviceFailure(Exception exception) {
        swipeRefreshLayout.setRefreshing(false);
        displayImageConditionProgressBar.setVisibility(View.GONE);
        Snackbar.make(homeLayout, exception.getMessage(), Snackbar.LENGTH_LONG).show();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Customized Listener by the implementation of the ServiceListener interface to                  //
    //      manage the various errors of controller                                                        //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void serviceFailure(int error) {

        String strErrorMessage ="";

        switch (error){

            case MyException.STR_ERROR_NOTHING_SEARCH: strErrorMessage = getResources().getString(R.string.strNothingSearch);
                break;

            case MyException.STR_ERROR_FETCHING: strErrorMessage = getResources().getString(R.string.strErrorFetching);
                break;

            case MyException.STR_ERROR_DOWNLOADING: strErrorMessage = getResources().getString(R.string.strErrorDownloading);
                break;

            case MyException.STR_ERROR_TIMEOUT: strErrorMessage = getResources().getString(R.string.strTimeout);
                break;

            case MyException.STR_ERROR_WEATHER_DATA: strErrorMessage = getResources().getString(R.string.strWeatherError);
                break;

            case MyException.STR_ERROR_INFO_CITY: strErrorMessage = getResources().getString(R.string.strNoCityInformation);
                break;

            case MyException.STR_ERROR_DECODE_DATA: strErrorMessage = getResources().getString(R.string.strErrorDecoding);
                break;

            case MyException.STR_ERROR_INFO_WEATHER: strErrorMessage = getResources().getString(R.string.strNoWeatherInfo);
                break;

            case MyException.STR_NOTE_WEATHER: strErrorMessage = getResources().getString(R.string.strNote);
                break;

            case MyException.STR_NOTE_CITY: strErrorMessage = getResources().getString(R.string.strInformationPartial);
                break;

        }
            serviceFailure(new MyException(strErrorMessage));

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Implementing a custom  SearchView.OnQueryTextListener to handle the submit of the query        //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class mySearchViewOnQueryTextListener implements SearchView.OnQueryTextListener{


        @Override
        public boolean onQueryTextSubmit(String query) {
            searchViewAndroidActionBar.clearFocus();
            searchViewItem.collapseActionView();
            if(isNetworkAvailable()) {
                swipeRefreshLayout.setRefreshing(true);
                httpController.getPlaceData(query);

            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Custom  BaseControllerListener  to handle any error of the fresco libray  action download      //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class MybaseControllListener extends BaseControllerListener {

        @Override
        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
            //super.onFinalImageSet(id, imageInfo, animatable);
            displayImageConditionProgressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            Snackbar.make(homeLayout, getResources().getString(R.string.strDownloadNoComplete), Snackbar.LENGTH_LONG).show();
        }


    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Private method to first check is a Internet Access is avaible                                  //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean trueConnetion = (activeNetworkInfo != null) && activeNetworkInfo.isConnected();

        if (trueConnetion)
            return true;

        else {
            serviceFailure(new MyException(getResources().getString(R.string.strNetworkNotAvaible)));

            return false;
        }

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Private method to start the refreshing feature                                                 //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void refreshWetherData(){

        searchViewItem.collapseActionView();

        if(isNetworkAvailable())
            if(myCityBoundary != null)
                httpController.getWeatherData(myCityBoundary.getCitySelected().getWoeid());
            else {
                swipeRefreshLayout.setRefreshing(false);
                serviceFailure(new MyException(getResources().getString(R.string.strSelectCity)));
            }

    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Private method to show a dialog to be able to change the set of units of measure               //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void showSettingDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle(getResources().getString(R.string.strSelectSet));
        String[] items = {getResources().getString(R.string.strMetric_set), getResources().getString(R.string.strImperial_set)};


        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempUnitConfiguration=which;
            }
        });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpController.setUnitConfiguration(tempUnitConfiguration);
                preferencesController.saveUnitPreferences(tempUnitConfiguration);
                swipeRefreshLayout.setRefreshing(true);
                refreshWetherData();
            }
        });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Private method to show a dialog to erase any configuration setting                             //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void showEraseDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle(getResources().getString(R.string.strRestoreSetting));

        builder.setMessage(getResources().getString(R.string.strRestoreSettingMessage));

        String positiveText = getString(android.R.string.ok);

        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferencesController.eraseData();
                Snackbar.make(homeLayout, getResources().getString(R.string.strDeletedData), Snackbar.LENGTH_LONG).show();
            }
        });


        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, null);

        AlertDialog dialog = builder.create();

        dialog.show();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Private method to show a dialog to set a city as favorite                                      //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void showLikeDialog() {

        if (myCityBoundary != null) {

            if (preferencesController.verifyCityPreferencesExistence())
            {
                if (myCityBoundary.compareWithCitySelected(preferencesController.getCityPreferences()))
                    Snackbar.make(homeLayout, getResources().getString(R.string.strAlreadyFavorite), Snackbar.LENGTH_LONG).show();

                else {
                    createLikeDialog(String.format(getResources().getString(R.string.strConfirmReplace),
                            preferencesController.getCityPreferences().getString(),
                            myCityBoundary.getCitySelected().getString()));
                }
            }

            else{
                createLikeDialog(String.format(getResources().getString(R.string.strConfirmCity),
                        myCityBoundary.getCitySelected().getString()));
                }
        }

        else
            serviceFailure(new MyException(getResources().getString(R.string.strSelectCity)));

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Private method invoked by showLikeDialog() to create the dialog interface                      //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void createLikeDialog(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle(getResources().getString(R.string.strSetCityFavorite));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferencesController.saveCityPreferences(myCityBoundary.getCitySelected());
                Snackbar.make(homeLayout,getResources().getString(R.string.strCitySet), Snackbar.LENGTH_LONG).show();
            }
        });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,null);

        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();

    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      Private method to show a dialog to handle the first run  behavior                              //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showFirstRunDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, homeLayout,false);
        dialogBuilder.setView(dialogView);


        spinner = (MaterialSpinner) dialogView.findViewById(R.id.firstRunSpinner);

        final EditText firstRunEditText = (EditText) dialogView.findViewById(R.id.firstRunEditText);

        dialogBuilder.setTitle(getResources().getString(R.string.strWelcome));
        dialogBuilder.setMessage(getResources().getString(R.string.strEnterCityAndSet)).setCancelable(false);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}});

        final AlertDialog dialog = dialogBuilder.create();

        dialog.show();

        spinner.setItems(getResources().getString(R.string.strMetric), getResources().getString(R.string.strImperial));


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(firstRunEditText.getText().toString().isEmpty())
                    firstRunEditText.findFocus();

                else{
                    if(isNetworkAvailable()) {
                        dialog.dismiss();
                        swipeRefreshLayout.setRefreshing(true);
                        httpController.setUnitConfiguration(spinner.getSelectedIndex());
                        preferencesController.saveUnitPreferences(spinner.getSelectedIndex());
                        httpController.getPlaceData(firstRunEditText.getText().toString());

                    }

                }
            }
        });
    }





}

