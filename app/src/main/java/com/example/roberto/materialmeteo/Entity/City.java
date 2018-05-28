package com.example.roberto.materialmeteo.Entity;


public class City {

    private String woeid;
    private String name;
    private String country;
    private String district;
    private String latitude;
    private String longitude;
    private String fullDescription;
    private String partialDescription;


    public City(){

        this.woeid="";
        this.country ="";
        this.latitude ="";
        this.longitude ="";
        this.name ="";
        this.fullDescription ="";
        this.partialDescription="";
        this.district="";


    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWoeid() {
        return woeid;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) { this.latitude = latitude;
    }

    public void setLongitude(String longitude) { this.longitude = longitude;
    }


    public String getString(){

       if(fullDescription.equals(""))
           computeDescriptions();

        return fullDescription;
    }



    public String getPartialDescription(){

        if(partialDescription.isEmpty())
            computeDescriptions();

        return partialDescription;
    }




    private void computeDescriptions(){

        fullDescription = "null";
        partialDescription = "null";

        if(!district.isEmpty()) {
            if (!country.isEmpty()) {
                if(!district.equals(name))
                {
                    partialDescription = district + ", " + country;
                    fullDescription = name + ", " + partialDescription;

                }
                else
                {
                    partialDescription = country;
                    fullDescription = name + ", " + country;
                }
            }

            else
            if(!district.equals(name))
            {
                partialDescription = district;
                fullDescription = name + ", " + district;
            }

            else
            {
                partialDescription = name;
                fullDescription = name;
            }

        }
        else
        if(!country.isEmpty())
        {
            partialDescription = country;
            fullDescription = name + ", " + country;
        }
        else
        if(!name.isEmpty()) {
            partialDescription = name;
            fullDescription = name;
        }

    }


}
