package uncc.ryan.clteatsdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ryrid on 3/31/2017.
 */

public class Restaurant {
    String name, food_category, address, place_id, price;
    double coord_lat, coord_long, distance_meters, distance_miles, rating;

    public Restaurant(){

    }

    private Restaurant(Parcel in) {
        name = in.readString();
        address = in.readString();
        place_id = in.readString();
        coord_lat = in.readDouble();
        coord_long = in.readDouble();
        distance_miles = in.readDouble();
        rating = in.readDouble();
    }


    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", food_category='" + food_category + '\'' +
                ", address='" + address + '\'' +
                ", coord_lat=" + coord_lat +
                ", coord_long=" + coord_long +
                //", distance_meters=" + distance_meters +
                ", distance_miles=" + distance_miles +
                ", price=" + price +
                ", rating=" + rating +
                '}';
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFood_category() {
        return food_category;
    }

    public void setFood_category(String food_category) {
        this.food_category = food_category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getCoord_lat() {
        return coord_lat;
    }

    public void setCoord_lat(double coord_lat) {
        this.coord_lat = coord_lat;
    }

    public double getCoord_long() {
        return coord_long;
    }

    public void setCoord_long(double coord_long) {
        this.coord_long = coord_long;
    }

    public double getDistance_meters() {
        return distance_meters;
    }

    public void setDistance_meters(double distance_meters) {
        this.distance_meters = distance_meters;
    }

    public double getDistance_miles() {
        return distance_miles;
    }

    public void setDistance_miles(double distance_miles) {
        this.distance_miles = distance_miles;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
}
