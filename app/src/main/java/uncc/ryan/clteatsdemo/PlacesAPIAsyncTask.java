package uncc.ryan.clteatsdemo;

<<<<<<< HEAD
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

=======
import android.os.AsyncTask;
import android.util.Log;

>>>>>>> origin/master
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
<<<<<<< HEAD
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
=======
import java.net.URL;
>>>>>>> origin/master

/**
 * Created by Ryrid on 3/31/2017.
 */

<<<<<<< HEAD
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
=======
public class PlacesAPIAsyncTask extends AsyncTask<String, Void, String> {
    String data = null;

    @Override
    protected String doInBackground(String... url){
        try{
            data = downloadUrl(url[0]);
        }catch (Exception e){
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result){
        JSONParser parserTask = new JSONParser();
        parserTask.execute(result);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while get url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
>>>>>>> origin/master
