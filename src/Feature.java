/**
 *  Feature Object used for Project
 */

import java.util.ArrayList;

public class Feature {
    // fields
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    // vars needed for part I
    private final ArrayList<Double> eachHousesDistFromFeature = new ArrayList<>(); // corresponds to x-axis on s.plot
    private final ArrayList<Double> ppsqftHouses = new ArrayList<>(); // corresponds to y-axis on s.plot
    private double r = Calculations.corr(ppsqftHouses, eachHousesDistFromFeature);
    private double avgPpsqftHouses = Calculations.avg(ppsqftHouses);
    private double avgEachHousesDistFromFeature = Calculations.avg(eachHousesDistFromFeature);
    private double stDevPpsqftHouses = Calculations.stDev(ppsqftHouses);
    private double stDevEachHousesDistFromFeature = Calculations.stDev(eachHousesDistFromFeature);
    private double beta1 = Calculations.corr(ppsqftHouses, eachHousesDistFromFeature) * (stDevPpsqftHouses / stDevEachHousesDistFromFeature);
    private double beta0 = avgPpsqftHouses - beta1*avgEachHousesDistFromFeature;

    // needed for part II
    private final ArrayList<Double> eachNewInputsDistFromFeature = new ArrayList<>();

    // default constructor
    public Feature (){
        name = "";
        address = "";
        latitude = 0;
        longitude = 0;
    }

    public Feature (String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // base setters
    public void setName(String name){this.name = name;}
    public void setAddress(String address){this.address = address;}
    public void setLatitude(double latitude){this.latitude = latitude;}
    public void setLongitude(double longitude){this.longitude = longitude;}

    // base getters
    public String getName(){return name;}
    public String getAddress(){return address;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}

    // computations setters
    public void setR(){r = Calculations.corr(ppsqftHouses, eachHousesDistFromFeature);}
    public void setAvgPpsqftHouses(){avgPpsqftHouses = Calculations.avg(ppsqftHouses);}
    public void setAvgEachHousesDistFromFeature(){avgEachHousesDistFromFeature = Calculations.avg(eachHousesDistFromFeature);}
    public void setStDevPpsqftHouses(){stDevPpsqftHouses = Calculations.stDev(ppsqftHouses);}
    public void setStDevEachHousesDistFromFeature(){stDevEachHousesDistFromFeature = Calculations.stDev(eachHousesDistFromFeature);}
    public void setBeta1(){beta1 = Calculations.corr(ppsqftHouses, eachHousesDistFromFeature) * (stDevPpsqftHouses / stDevEachHousesDistFromFeature);}
    public void setBeta0(){beta0 = avgPpsqftHouses - beta1*avgEachHousesDistFromFeature;}

    // computations getters
    public ArrayList<Double> getPpsqftHouses(){return ppsqftHouses;}
    public ArrayList<Double> getEachHousesDistFromFeature(){return eachHousesDistFromFeature;}
    public double getR(){return r;}
    public double getAvgPpsqftHouses(){return avgPpsqftHouses;}
    public double getAvgEachHousesDistFromFeature(){return avgEachHousesDistFromFeature;}
    public double getStDevPpsqftHouses(){return stDevPpsqftHouses;}
    public double getStDevEachHousesDistFromFeature(){return stDevEachHousesDistFromFeature;}
    public double getBeta1(){return beta1;}
    public double getBeta0(){return beta0;}
    public ArrayList<Double> getEachNewInputsDistFromFeature() {return eachNewInputsDistFromFeature;}

    // sets all computations
    public void setData(){
        setR();
        setAvgPpsqftHouses();
        setAvgEachHousesDistFromFeature();
        setStDevPpsqftHouses();
        setStDevEachHousesDistFromFeature();
        setBeta1();
        setBeta0();
    }

    // toString() override. Doesn't output ArrayLists, standard deviations, or averages (too much clutter)
    public String toString(){
        return "Name of Feature: " + name + " Address: " + address + " Latitude: " + latitude + " Longitude: " +
                longitude + " r: " + r + " beta1: " + beta1 + " beta0: " + beta0;
    }
}
