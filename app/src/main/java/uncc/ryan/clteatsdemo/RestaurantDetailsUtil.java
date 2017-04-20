package uncc.ryan.clteatsdemo;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Ryan on 4/19/2017.
 */

public class RestaurantDetailsUtil {
    static public class DetailsPullParser{
        static ArrayList<Review> parseDetails(InputStream in)throws XmlPullParserException, IOException {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            int event = parser.getEventType();

            Review review = null;
            boolean hasItem = false;

            ArrayList<Review> reviews = new ArrayList<>();

            while(event != XmlPullParser.END_DOCUMENT){
                String name;
                switch (event){
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if(name.equals("text")){
                            review = new Review(); hasItem = true;
                            review.setUserComment(parser.nextText().trim());
                            Log.d("debug","XMLparse: UserComment: " + review.getUserComment());
                        }else if(name.equals("author_name")){
                            if(hasItem){
                                review.setUserName(parser.nextText().trim());
                            }
                            Log.d("debug","XMLparse: UserName: " + review.getUserName());
                        }else if(name.equals("aspect")){
                            parser.nextTag();
                            if(name.equals("rating")){
                                review.setUserRating(Integer.parseInt(parser.nextText().trim()));
                            }
                            Log.d("debug","XMLparse: UserRating: " + review.getUserRating());
                        }else if(name.equals("place_id")){
                            if(hasItem){
                                review.setPlace_id(parser.nextText().trim());
                            }
                            else{
                                review = new Review(); hasItem = true;
                                review.setPlace_id(parser.nextText().trim());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("result")){
                            reviews.add(review);
                            review = null;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }

            //Log.d("XMLparse.toString",reviews.toString()+"");
            return reviews;
        }
    }
}
