package com.example.roberto.materialmeteo.Boundary;

import com.example.roberto.materialmeteo.Entity.City;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




public class CityBoundary {

    private  List<City> cityListFound;
    private  City citySelected;


    public CityBoundary(List<City> cityListFound){
        this.cityListFound = cityListFound;
        this.citySelected = cityListFound.get(0);
    }

    public CityBoundary(City citySelected) {this.citySelected = citySelected;};


    public boolean moreThanOneCity(){

        return (cityListFound.size()>1);

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to compare two City object                                              //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public boolean compareWithCitySelected(City cityToCompare){

        return ((cityToCompare.getWoeid().equals(citySelected.getWoeid()))
                && (cityToCompare.getName().equals(citySelected.getName()))
                && (cityToCompare.getCountry().equals(citySelected.getCountry()))
                && (cityToCompare.getDistrict().equals(citySelected.getDistrict()))
                && (cityToCompare.getLatitude().equals(citySelected.getLatitude()))
                && (cityToCompare.getLongitude().equals(citySelected.getLongitude())));
    }




    //Getter
    public  City getCitySelected() {

        if(citySelected != null)
            return citySelected;

        else
            return new City();
    }



    //Setter
    public  void setCitySelected(int citySelected) {
        this.citySelected = cityListFound.get(citySelected);
    }





    ///////////////////////////////////////////////////////////////////////////////////////////////
    //     Public method to dispatch City object to String objec                                 //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public  List<String> cityToString()  {

        List<String> listCity = new ArrayList<String>();
        Iterator<City> iteratorCity = cityListFound.iterator();
        City city;

        while (iteratorCity.hasNext()) {

            city = iteratorCity.next();
            listCity.add(city.getString());

        }

        return listCity;
        
    }


}


