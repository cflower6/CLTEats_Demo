package uncc.ryan.clteatsdemo;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ryan on 4/19/2017.
 */

public class PlacesDetailsAPIAsyncTask extends AsyncTask<String, Void, ArrayList<Review>>{
    public interface AsyncResponse{
        void onLocationChanged(Location location);

        PlacesDetailsAPIAsyncTask.AsyncResponse onProcessFinish(ArrayList<Review> output);
    }
    public PlacesDetailsAPIAsyncTask.AsyncResponse delegate = null;

    public PlacesDetailsAPIAsyncTask(PlacesDetailsAPIAsyncTask.AsyncResponse delegate){
        this.delegate = delegate;
    }

    static int cursor = 0;

    @Override
    protected ArrayList<Review> doInBackground(String... params) {
        try{
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                InputStream in = con.getInputStream();
                return RestaurantDetailsUtil.DetailsPullParser.parseDetails(in);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> result) {
        super.onPostExecute(result);
        if (result != null) {
            //Log.d("onPostExecute:reviews:", result.toString());

            delegate.onProcessFinish(result);
        }
    }
}
