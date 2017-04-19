package uncc.ryan.clteatsdemo;

import java.util.Comparator;

/**
 * Created by Ryan on 4/19/2017.
 */

public class RestaurantRatingComparator implements Comparator<Restaurant> {
    public int compare(Restaurant rest1, Restaurant rest2){
        String rest1rating = String.valueOf(rest1.getRating());
        String rest2rating = String.valueOf(rest2.getRating());
        return rest2rating.compareTo(rest1rating);
    }
}
