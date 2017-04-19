package uncc.ryan.clteatsdemo;

import java.util.Comparator;

/**
 * Created by Ryan on 4/19/2017.
 */

public class RestaurantDistanceComparator implements Comparator<Restaurant> {
    public int compare(Restaurant rest1, Restaurant rest2){
        String rest1dist = String.valueOf(rest1.getDistance_miles());
        String rest2dist = String.valueOf(rest2.getDistance_miles());
        return rest1dist.compareTo(rest2dist);
    }
}
