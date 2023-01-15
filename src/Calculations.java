/**
 *  Calculations class does the math behind our project. There are five methods; the three most important are distance,
 *  correlation and sumDistFrom. Each one is explained below. The snapshot for them is that distance calculates the
 *  spherical distance between two pairs of coordinates, correlation outputs a correlation coefficient through the analysis
 *  of two datasets. This is also known as Pearson's correlation coefficient. Finally, sumDistFrom adds up the distance
 *  from a house to the features as a total distance and can be used to evaluate the centrality of a home.
 */

import java.util.ArrayList;

public class Calculations {


    /***
     * Class Constructor. Meant to receive two pairs of coordinates with latitudes and then longitudes. Currently prints
     * the distance.
     * @param lat1
     * @param lat2
     * @param lon1
     * @param lon2
     */
    public Calculations(double lat1, double lat2, double lon1,
                                double lon2){

        //System.out.println(distance(lat1, lat2, lon1, lon2, 0, 0));
        System.out.println(distance(lat1, lat2, lon1, lon2));
    }

    /**
     * Haversine Formula for calculating Spherical Distance:
     *
     * lat1, lon1 Start point lat2, lon2 End point el1
     * @returns Distance in Meters
     *
     * These are the three steps of the Haversine Formula in symbolic terms where φ is latitude, λ is longitude
     *
     * a = sin²(φB - φA/2) + cos φA * cos φB * sin²(λB - λA/2)
     * c = 2 * atan2( √a, √(1−a) )
     * d = R ⋅ c
     *
     *  - Portions of this method we credit to David George from Stack Overflow. Some of it was modified since there was
     *  an addition for height that was unnecessary.
     *  URL: https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     *
     *   - In addition, R.W. Sinnott, "Virtues of the Haversine" Sky and Telescope, vol. 68, no. 2, 1984, p. 159 was
     *   typically credited for the Haversine Formula in readings that we found while developing the method.
     */
     public static double distance(double latitudeOne, double latitudeTwo, double longitudeOne, double longitudeTwo){
        final int R = 6371; // This is the radius of the earth in meters.

        double latitudalDistance = Math.toRadians(latitudeTwo - latitudeOne);
        double longitudalDistance = Math.toRadians(longitudeTwo - longitudeOne);

        double a = Math.sin(latitudalDistance / 2) * Math.sin(latitudalDistance / 2)
                + Math.cos(Math.toRadians(latitudeOne)) * Math.cos(Math.toRadians(latitudeTwo))
                * Math.sin(longitudalDistance / 2) * Math.sin(longitudalDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // this is the spherical distance between the
         // points 'mathematically'

        double distance = R * c * 1000; // meter conversion

        return distance;
    }
    /***
     * Simply used to give us an average double from an ArrayList of doubles.
     * @param doubleArrayList
     * @return double
     */
    public static double avg(ArrayList<Double> doubleArrayList){
         double avg = 0;
         double listSum = 0;

         for (Double aDouble : doubleArrayList){
             listSum += aDouble;
         }

         avg = listSum / (double) doubleArrayList.size();
         return avg;
    }

    /***
     * Gives the standard Deviation of a list of doubles.
     * @param doubleArrayList requires an ArrayList<Double>
     * @return standard deviation as a double.
     */
    public static double stDev(ArrayList<Double> doubleArrayList){
        double stDev = 0;
        double listSum = 0;

        for (Double aDouble : doubleArrayList) {
            listSum += aDouble;
        }

        double listAvg = listSum / (double) doubleArrayList.size();
        for (Double aDouble : doubleArrayList) {
            stDev += Math.pow((aDouble - listAvg), 2);
        }

        stDev /= (double) doubleArrayList.size() - 1;
        stDev = Math.sqrt(stDev);

        return stDev;
    }
    /**
     * Uses Pearson correlation coefficient which processes two data sets of equal length and finds a correlation value
     * r. This is used multiple times, typically with price per square foot and some type of distance metric in the
     * features.java class
     *
     * Symbolically, Pearson's Correlation coefficient looks like this:
     *
     * rₓᵧ = (Σxᵢyᵢ - nx̄ȳ) / ((n-1)sₓsᵧ)
     *
     * where xᵢ and yᵢ are individual sample points indexed with i. n is the sample size. x̄ and ȳ are the sample means
     * for x and y respectively. Finally, sₓ and sᵧ are the standard deviations for the x sample and y samples.
     *
     * precondition: x is the same length as y
     * @param xInput
     * @param yInput
     * src: Butler, Lynne. "(Least Squares) Linear Regression" STATH203: Introduction to Stat. Methods, 26 January 2022.
     * Hilles Hall, Haverford College, Haverford.
     * @return
     */
    public static double corr(ArrayList<Double> xInput, ArrayList<Double> yInput) {
        if (xInput.size() != yInput.size()) throw new ArrayIndexOutOfBoundsException("Arrays are not of equal length!");

        double xN = xInput.size();
        double yN = yInput.size();
        double summationOfProductDifferences = 0, sumX = 0, sumY = 0, avgX = 0, avgY = 0, stdX = 0, stdY = 0;

        for (int i = 0; i < xN; i++) {
            sumX += xInput.get(i);
            sumY += yInput.get(i);
        }

        avgX = sumX / xN;
        avgY = sumY / yN;

        for (int i = 0; i < xN; i++) summationOfProductDifferences += (xInput.get(i) - avgX) * (yInput.get(i) - avgY);

        for (int i = 0; i < xN; i++) {
            stdX += Math.pow((xInput.get(i) - avgX), 2);
            stdY += Math.pow((yInput.get(i) - avgY), 2);
        }

        stdX /= (xN - 1.0);
        stdY /= (yN - 1.0);

        stdX = Math.sqrt(stdX);
        stdY = Math.sqrt(stdY);

        double denominator = (xN - 1.0) * stdX * stdY;

        return summationOfProductDifferences / denominator;
    }

    /***
     * Gives the distance of a house from multiple features and adds them together. The lower the value, the more 'central'
     * the house is to the features that are processed.
     * @param houseInput we use the house object for a specific house
     * @param listOfFeaturesInput a list of features
     * @return the 'total' distance adding up house's distance from each feature and returned as a double.
     */
    public static double sumDistFrom (House houseInput, ArrayList<Feature> listOfFeaturesInput){
        double sumDistFrom = 0;
        for (Feature feature : listOfFeaturesInput) {
            sumDistFrom = sumDistFrom + distance(houseInput.getLatitude(), feature.getLatitude(), houseInput.getLongitude(), feature.getLongitude());
        }
        return sumDistFrom;
    }


}
