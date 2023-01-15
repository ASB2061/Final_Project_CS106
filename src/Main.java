/***
 * Project: Identifying High Value Targets
 *
 * If we walk around a neighborhood – take Buena Vista, Miami, Florida for example – we can appreciate the attracting
 * and repelling features of that neighborhood. We have a sense of what the high value features are in a neighborhood,
 * but we can use a  data-driven approach to support our intuition with evidence. Our project computes statistics based
 * on the relationship between houses and features in a neighborhood. It provides the user with a graph of both the most
 * attractive and the most unattractive feature in the neighborhood. It also allows the user to estimate the ppsqft of a
 * desired latitude, longitude based on a weighted sum of the linear regression models predicting ppsqft for each feature.
 *
 * Note that code is not currently robust to duplicates. User trying to process data should be aware of these stipulations.
 *
 * @Authors: Daniel Bhatti, Jadyn Elliot, Adiel Benisty
 * @Version: 1.0
 * @LastUpdate: May 12, 2022
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import javafx.application.Application;
import javafx.scene.Scene;

import java.util.List;
import java.util.Scanner;
import java.io.FileReader;


public class Main {

    public static void main(String[] args) throws Exception {
        // Examples of Command Line Argument:
        // BuenaVistaHouses.csv, BuenaVistaFeatures.csv

        ArrayList<String> featureListsQueried = new ArrayList<String>();
        ArrayList<String> houseListsQueried = new ArrayList<String>();

        ArrayList<Feature> featuresCompiled = new ArrayList<Feature>();
        ArrayList<House> housesCompiled = new ArrayList<House>();

        Scanner scannerVar = new Scanner(System.in); // We use the scanner class to receive input from the user for the
        // .csv filenames

        System.out.println("Please input the names of the files you wish to use. Be sure to separate them by commas.");
        String lineInput = scannerVar.nextLine();
        List<String> inputtedFiles = new ArrayList<String>(Arrays.asList(lineInput.split(",")));
        // we create an array from the line inputted since each file is separated by comma.

        //for loop traverses through scanned list
        for (int i = 0; i < inputtedFiles.size(); i++) {
            inputtedFiles.set(i, inputtedFiles.get(i).replaceAll("\\s+", "").trim()); // this gets rid of any spaces
            // inside the string and on the outside of the string so the file can be read properly.
            // Example of this: "B u e naVist a .csv" - > "BuenaVista.csv"
            if (inputtedFiles.get(i).toLowerCase().contains("features")) {
                featureListsQueried.add(inputtedFiles.get(i));
            } else if (inputtedFiles.get(i).toLowerCase().contains("houses")) {
                houseListsQueried.add(inputtedFiles.get(i));
            }
        }

        // compiling each feature from each feature .csv
        for (String featureList : featureListsQueried) {
            try {
                CSVReaderHeaderAware reader1 = new CSVReaderHeaderAware(new FileReader(featureList));
                ArrayList<String[]> entriesFeatures = new ArrayList<>(reader1.readAll());
                reader1.close();
                // add each row from each .csv file (assuming each row contains minimum information) to ArrayList<Feature> featuresCompiled
                for (String[] rowScanFeatures : entriesFeatures) {
                    try {
                        Feature feature = new Feature(rowScanFeatures[0], rowScanFeatures[1], Double.parseDouble(rowScanFeatures[2]), Double.parseDouble(rowScanFeatures[3]));
                        featuresCompiled.add(feature);
                    } catch (NumberFormatException e) { // cannot parse empty String. Also, a feature without latitude and longitude is worthless
                        continue;
                    }
                }
            } catch (IOException | CsvException e) { // .csv filename not found in wd
                System.out.println(".csv file not found");
            }
        }

        // compiling each house from each house .csv
        for (String houseList : houseListsQueried) {
            try {
                CSVReaderHeaderAware reader2 = new CSVReaderHeaderAware(new FileReader(houseList));
                ArrayList<String[]> entries2 = new ArrayList<>(reader2.readAll());
                reader2.close();
                for (String[] rowScan2 : entries2) {
                    try {
                        House house = new House(rowScan2[3], Double.parseDouble(rowScan2[7]),
                                Double.parseDouble(rowScan2[15]), Double.parseDouble(rowScan2[12]),
                                Double.parseDouble(rowScan2[25]), Double.parseDouble(rowScan2[26]), 0);
                        house.setSumDistFrom(Calculations.sumDistFrom(house, featuresCompiled)); // each house has an associated sumDistFrom
                        housesCompiled.add(house);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            } catch (IOException | CsvException e) { //.csv filename not found in wd
                System.out.println(".csv file not found");
            }
        }

        // each feature has an associated r, beta-0, beta-1. Note: beta-1 = r*(Sy/Sx), beta-0 = y-bar - beta-1*(x-bar)
        // These statistics are to be found from ArrayLists of equal length. Each feature will hold both of these lists.
        // one arraylist will hold the distance of each house in our data set from the feature. the other will hold the
        // ppsqft of each house in our data set
        for (Feature feature : featuresCompiled) {
            for (House house : housesCompiled) {
                feature.getPpsqftHouses().add(house.getPpsqft());
                feature.getEachHousesDistFromFeature().add(Calculations.distance(feature.getLatitude(), house.getLatitude(), feature.getLongitude(), house.getLongitude()));
            }
            feature.setData();
        }

        // output plots for the features with highest r, lowest r
        // initializing “dummy” Features to help find featureWithMaxCorr, featureWithMinCorr
        double maxCorr = -1;
        double minCorr = 1;
        int maxCorrIndex = 0;
        int minCorrIndex = 0;
        for (Feature feature : featuresCompiled) {
            if (feature.getR() > maxCorr) { // if current value is greater than current max, replace current max with
                // current value
                maxCorr = feature.getR();
                maxCorrIndex = featuresCompiled.indexOf(feature);
            }
            if (feature.getR() < minCorr) { // if current value is less than current min, replace current min with
                // current value
                minCorr = feature.getR();
                minCorrIndex = featuresCompiled.indexOf(feature);
            }
        }

        ArrayHeap<House> housesSorted = new ArrayHeap<>(); // instantiate array heap
        housesSorted.sort(housesCompiled); // heap sort array
/*
This following code creates max heaps for the x values (the sum distance from feature) to organize that arraylist from
using the arrayheap.sort method. It then makes two arraylists that will hold just the lowest ten sum of distances
 */
        ArrayHeap<Double> maxCorrelationX = new ArrayHeap<>();
        ArrayList<Double> xAxisValuesMax = maxCorrelationX.sort(featuresCompiled.get(maxCorrIndex).getPpsqftHouses());
        ArrayHeap<Double> maxCorrelationY = new ArrayHeap<>();
        // not used ArrayList<Double> yAxisValuesMax = maxCorrelationY.sort(featuresCompiled.get(maxCorrIndex).getEachHousesDistFromFeature());
        ArrayHeap<Double> minCorrelationX = new ArrayHeap<>();
        ArrayList<Double> xAxisValuesMin = minCorrelationX.sort(featuresCompiled.get(minCorrIndex).getPpsqftHouses());
        ArrayHeap<Double> minCorrelationY = new ArrayHeap<>();
        // not used ArrayList<Double> yAxisValuesMin = minCorrelationY.sort(featuresCompiled.get(minCorrIndex).getEachHousesDistFromFeature());
        ArrayList<Double> firstTenMaxCorrX = new ArrayList<>();
        // not used ArrayList<Double> firstTenMaxCorrY = new ArrayList<>();
        ArrayList<Double> firstTenMinCorrX = new ArrayList<>();
        // not used ArrayList<Double> firstTenMinCorrY = new ArrayList<>();
//This code below iterates over the maxheap that holds the maxCorrelation xValues and the minCorrelation xValues
// and then takes the bottom ten(which repreents the closest homes) and adds them to arrraylists
        for (int i = xAxisValuesMax.size() - 1; i > (xAxisValuesMax.size() - 11); i--) {
            firstTenMaxCorrX.add(xAxisValuesMax.get(i));
            firstTenMinCorrX.add(xAxisValuesMin.get(i));
        }


        // Creates a csv file for features with minimum correlation and for features with maximum correlation by making
        //a string and then a file writer
        StringBuilder stringToFile1 = new StringBuilder();
        StringBuilder stringToFile2 = new StringBuilder();
        StringBuilder topTen1 = new StringBuilder();
        StringBuilder topTen2 = new StringBuilder();

        for (int i = 0; i < featuresCompiled.get(maxCorrIndex).getPpsqftHouses().size(); i++) {
            stringToFile1.append(featuresCompiled.get(maxCorrIndex).getEachHousesDistFromFeature().get(i));
            stringToFile1.append(",");
            stringToFile1.append(featuresCompiled.get(maxCorrIndex).getPpsqftHouses().get(i));
            stringToFile1.append("\n");

        }
        for (int i = 0; i < featuresCompiled.get(minCorrIndex).getPpsqftHouses().size(); i++) {
            stringToFile2.append(featuresCompiled.get(minCorrIndex).getEachHousesDistFromFeature().get(i));
            stringToFile2.append(",");
            stringToFile2.append(featuresCompiled.get(minCorrIndex).getPpsqftHouses().get(i));
            stringToFile2.append("\n");
        }

        for (int i = 0; i < 10; i++) {
            topTen1.append(firstTenMaxCorrX.get(i));
            topTen1.append("\n");
        }

        for (int i = 0; i < 10; i++) {
            topTen2.append(firstTenMinCorrX.get(i));
            topTen2.append("\n");
        }


        FileWriter writer1 = new FileWriter("graphMaxCorr.csv");
        FileWriter writer2 = new FileWriter("graphMinCorr.csv");
        FileWriter topTenMax = new FileWriter("topTenMaxCorr.csv");
        FileWriter topTenMin = new FileWriter("topTenMinCorr.csv");

        writer1.write(stringToFile1.toString());
        writer2.write(stringToFile2.toString());
        topTenMax.write(topTen1.toString());
        topTenMin.write(topTen2.toString());
        writer1.close();
        writer2.close();
        topTenMax.close();
        topTenMin.close();
//The following code prompts in the terminal to ask if the user wants a scatter plot or not and if so, which type.
        //HEADS UP, when the scatterplot when is exited out, the program also exits and stops.
        System.out.println("Do you want a scatterplot? Answer yes or no.");
        String userString = scannerVar.nextLine();
        if (userString.toLowerCase().contains("yes")) {
            System.out.println("Do you want a max or min scatterplot? Answer max or min.");
            String userString2 = scannerVar.nextLine();
            if(userString2.toLowerCase().contains("min")) {
                Scatterplot scatterplotMin = new Scatterplot
                        ("graphMinCorr.csv", "topTenMinCorr.csv");
            }else if(userString2.toLowerCase().contains("max")){
                Scatterplot scatterplotMax = new Scatterplot
                        ("graphMaxCorr.csv", "topTenMaxCorr.csv");
            }
        }
//The code below prompts the user to input their Longitude and Latitude coordinates and then estimates ppsqft
        System.out.println("Please enter coordinates for which you want to estimate price per square foot from?");
        String coordinateString = scannerVar.nextLine();
        List<String> coordinates = new ArrayList<>(Arrays.asList(coordinateString.split(",")));
        ArrayList<Double> longitude = new ArrayList<>();
        ArrayList<Double> latitude = new ArrayList<>();
        scannerVar.close();
        assert (coordinates.size() % 2 == 0); // concurrently asserts that latitude.size() == longitude.size()
        for (int i = 0; i < coordinates.size(); i++) {
            if (i % 2 == 0) {
                latitude.add(Double.parseDouble(coordinates.get(i).replaceAll("\\s+", "")));
            } else {
                longitude.add(Double.parseDouble(coordinates.get(i).replaceAll("\\s+", "")));
            }
        }

        //this code also creates a string with the estimated price per square foot and prints that string to a file
        StringBuilder stringToFile3 = new StringBuilder();
        stringToFile3.append("Estimated Price Per Square Foot for Given Latitude or Longitude \n");
        //Part II
        for (int i = 0; i < latitude.size(); i++) { // refer to project proposal (task 2))
            // initializing variables
            double sumPpsqft = 0;
            double inverseDistFrom;
            double sumInverseDistFrom = 0;
            // calculating iterated additions / sums for the weighted sum
            for (Feature feature : featuresCompiled) {
                inverseDistFrom = 1 / Calculations.distance(feature.getLatitude(), latitude.get(i), feature.getLongitude(), longitude.get(i));

                sumPpsqft += inverseDistFrom * (feature.getBeta0() +
                        feature.getBeta1() * Calculations.distance(feature.getLatitude(), latitude.get(i), feature.getLongitude(), longitude.get(i)));

                sumInverseDistFrom += inverseDistFrom;
            }
            sumPpsqft *= 1 / sumInverseDistFrom; // correction coefficient; makes the weights sum to 1
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String moneyString = formatter.format(sumPpsqft);
            stringToFile3.append(" (" + latitude.get(i) + ", " + longitude.get(i) + ')' + " : " + moneyString);
            stringToFile3.append("\n");
        }
        System.out.println(stringToFile3);
        FileWriter writer3 = new FileWriter("EstimatedPricePerSqft.csv");
        writer3.write(stringToFile3.toString());
        writer3.close();
    }
}
