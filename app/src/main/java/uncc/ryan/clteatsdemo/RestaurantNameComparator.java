package uncc.ryan.clteatsdemo;

import java.util.Comparator;

/**
 * Created by Ryan on 4/19/2017.
 */

public class RestaurantNameComparator implements Comparator<Restaurant> {
    public int compare(Restaurant rest1, Restaurant rest2){
        return rest1.getName().compareTo(rest2.getName());
    }
}
