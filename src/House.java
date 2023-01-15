/**
 *  House Object used for project.
 */
public class House implements Comparable<House> {
    // fields
    private String address;
    private double price;
    private double ppsqft;
    private double sqftLot;
    private double latitude;
    private double longitude;
    private double sumDistFrom;

    // default constructor
    public House(){
        address = "";
        price = 0;
        ppsqft = 0;
        sqftLot = 0;
        latitude = 0;
        longitude = 0;
        sumDistFrom = 0;
    }

    public House(String address, double price, double ppsqft, double sqftLot, double latitude, double longitude, double sumDistFrom){
        this.address = address;
        this.price = price;
        this.ppsqft = ppsqft;
        this.sqftLot = sqftLot;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sumDistFrom = sumDistFrom;
    }

    // setters
    public void setAddress(String address) {this.address = address;}
    public void setPrice(double price) {this.price = price;}
    public void setPpsqft(double ppsqft) {this.ppsqft = ppsqft;}
    public void setSqftLot(double sqftLot) {this.sqftLot = sqftLot;}
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public void setLongitude(double longitude) {this.longitude = longitude;}
    public void setSumDistFrom(double sumDistFrom) {this.sumDistFrom = sumDistFrom;}

    // getters
    public String getAddress(){return address;}
    public double getPrice(){return price;}
    public double getPpsqft(){return ppsqft;}
    public double getSqftLot(){return sqftLot;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public double getSumDistFrom(){return sumDistFrom;}

    // toString() override.
    public String toString(){
        return "Address: " + address + " Price: " + price + " Price per Square Foot: " + ppsqft + " Square Foot Lot: " +
                sqftLot + " Latitude: " + latitude + " Longitude " + longitude + " Sum of Distance from All Features " +
                sumDistFrom;
    }

    @Override
    public int compareTo(House house) {
        if(this.sumDistFrom > house.getSumDistFrom()){
            return 1;
        }else if(house.getSumDistFrom() > this.sumDistFrom){
            return -1;
        }else{
            return this.address.compareTo(house.getAddress());
            }
        }
    }


