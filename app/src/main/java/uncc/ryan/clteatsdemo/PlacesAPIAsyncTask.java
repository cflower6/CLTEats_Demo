package uncc.ryan.clteatsdemo;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ryrid on 3/31/2017.
 */

public class PlacesAPIAsyncTask extends AsyncTask<String, Void, ArrayList<Restaurant>> {

    public interface AsyncResponse{
        void onLocationChanged(Location location);

        AsyncResponse processFinish(ArrayList<Restaurant> output);
    }
    public AsyncResponse delegate = null;

    public PlacesAPIAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Restaurant> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                return RestaurantUtil.RestaurantPullParser.parseRestaurant(in);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Restaurant> result) {
        super.onPostExecute(result);
        if (result != null) {
            Log.d("demo", result.toString());
            delegate.processFinish(result);
        }
    }
}