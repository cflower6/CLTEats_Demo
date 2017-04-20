package uncc.ryan.clteatsdemo;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Ryan on 4/4/2017.
 */

public class RestaurantUtil {
    static public class RestaurantPullParser{
        static ArrayList<Restaurant> parseRestaurant(InputStream in) throws XmlPullParserException, IOException{
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in,"UTF-8");
            Restaurant restaurant = null;
            ArrayList<Restaurant> restaurantsList = new ArrayList<>();
            int event = parser.getEventType();

            boolean hasItem = false;

            while(event != XmlPullParser.END_DOCUMENT){
                String name;
                switch (event){
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if(name.equals("name")){
                            restaurant = new Restaurant();
                            restaurant.setName(parser.nextText().trim());
                            Log.d("debug","XMLparse: " + restaurant.getName());
                        }else if(name.equals("vicinity")){
                            restaurant.setAddress(parser.nextText().trim());
                            //Log.d("debug","XMLparse: " + restaurant.getAddress());
                        }else if(name.equals("lat")){
                            restaurant.setCoord_lat(Double.parseDouble(parser.nextText().trim()));
                            //Log.d("debug","XMLparse: " + restaurant.getCoord_lat());
                        }else if(name.equals("lng")){
                            restaurant.setCoord_long(Double.parseDouble(parser.nextText().trim()));
                            //Log.d("debug","XMLparse: " + restaurant.getCoord_long());
                        }else if(name.equals("rating")){
                            restaurant.setRating(Double.parseDouble(parser.nextText().trim()));
                            //Log.d("debug","XMLparse: " + restaurant.getRating());
                        }
                        else if(name.equals("place_id")){
                            restaurant.setPlace_id(parser.nextText().trim());
                            //Log.d("debug","XMLparse: " + restaurant.getPlace_id());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("result")){
                            restaurantsList.add(restaurant);
                            restaurant = null;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
            Log.d("restaurantsList",restaurantsList.toString()+"");
            return restaurantsList;
        }
    }
}
