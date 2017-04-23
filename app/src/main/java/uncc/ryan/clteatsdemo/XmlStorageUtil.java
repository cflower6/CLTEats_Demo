package uncc.ryan.clteatsdemo;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Ryan on 4/22/2017.
 */

public class XmlStorageUtil {
    static public class XmlStoragePullParser{
        static ArrayList<Restaurant> parseXmlStorage(InputStream in) throws XmlPullParserException, IOException{
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            Restaurant restaurant = null;
            ArrayList<Restaurant> favoriteRestaurantsList = new ArrayList<>();
            int event = parser.getEventType();

            boolean hasItem = false;

            while(event != XmlPullParser.END_DOCUMENT){
                String name = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equals("name")) {
                            restaurant = new Restaurant();
                            restaurant.setName(parser.nextText().trim());
                        } else if (name.equals("food_category")) {
                            restaurant.setFood_category(parser.nextText().trim());
                        } else if (name.equals("address")) {
                            restaurant.setAddress(parser.nextText().trim());
                        } else if (name.equals("place_id")) {
                            restaurant.setPlace_id(parser.nextText().trim());
                        } else if (name.equals("price")) {
                            restaurant.setPrice(parser.nextText().trim());
                        } else if (name.equals("phone_number")) {
                            restaurant.setPhone_number(parser.nextText().trim());
                        } else if (name.equals("coord_lat")) {
                            restaurant.setCoord_lat(Double.parseDouble(parser.nextText().trim()));
                        } else if (name.equals("coord_long")) {
                            restaurant.setCoord_long(Double.parseDouble(parser.nextText().trim()));
                        } else if (name.equals("distance_miles")) {
                            restaurant.setDistance_miles(Double.parseDouble(parser.nextText().trim()));
                        } else if (name.equals("rating")) {
                            restaurant.setRating(Double.parseDouble(parser.nextText().trim()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("restaurant")){
                            favoriteRestaurantsList.add(restaurant);
                            Log.d("XmlStorageUtil:added:",restaurant.toString()+"");
                            restaurant = null;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
            return favoriteRestaurantsList;
        }
    }
}
