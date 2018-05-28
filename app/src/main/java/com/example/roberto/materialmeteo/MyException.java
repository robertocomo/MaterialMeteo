package com.example.roberto.materialmeteo;



public class MyException extends Exception {

    public static final int STR_ERROR_NOTHING_SEARCH = -10;
    public static final int STR_ERROR_FETCHING = -20;
    public static final int STR_ERROR_DOWNLOADING = -30;
    public static final int STR_ERROR_TIMEOUT = -40;
    public static final int STR_ERROR_WEATHER_DATA = -50;
    public static final int STR_ERROR_INFO_CITY = -60;
    public static final int STR_ERROR_DECODE_DATA = -70;
    public static final int STR_ERROR_INFO_WEATHER = -80;
    public static final int STR_NOTE_WEATHER = -90;
    public static final int STR_NOTE_CITY = -100;



    public MyException(String error){
        super(error);
    }

    public  MyException(){}


}
