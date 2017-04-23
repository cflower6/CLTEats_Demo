package uncc.ryan.clteatsdemo;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Ryan on 4/23/2017.
 */

public class FileUtil {
    public static ArrayList<Restaurant> xmlToObject(String filename){
        try{
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            String data = new String(inputBuffer);
            isr.close();
            fis.close();

            InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
            return XmlStorageUtil.XmlStoragePullParser.parseXmlStorage(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeXml(ArrayList<Restaurant> restaurants, String filename){
        try{
            Log.d("Info","writeXml() running...");

            FileOutputStream fos = new FileOutputStream(new File(filename));
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            //serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true");

            Log.d("Write to file", "Size(): " + restaurants.size() + "\ntoString: " + restaurants.toString());

            serializer.startTag(null, "root");
            for(int j=0;j<restaurants.size();j++){
                serializer.startTag(null,"restaurant");
                serializer.startTag(null,"name");
                serializer.text(restaurants.get(j).getName());
                serializer.endTag(null, "name");

                serializer.startTag(null, "food_category");
                serializer.text("null");
                serializer.endTag(null, "food_category");

                serializer.startTag(null,"address");
                serializer.text(restaurants.get(j).getAddress());
                serializer.endTag(null, "address");

                serializer.startTag(null, "place_id");
                serializer.text(restaurants.get(j).getPlace_id());
                serializer.endTag(null, "place_id");

                serializer.startTag(null, "price");
                serializer.text(restaurants.get(j).getPrice());
                serializer.endTag(null, "price");

                serializer.startTag(null, "phone_number");
                serializer.text(restaurants.get(j).getPhone_number());
                serializer.endTag(null, "phone_number");

                serializer.startTag(null, "coord_lat");
                serializer.text(String.valueOf(restaurants.get(j).getCoord_lat()));
                serializer.endTag(null, "coord_lat");

                serializer.startTag(null, "coord_long");
                serializer.text(String.valueOf(restaurants.get(j).getCoord_long()));
                serializer.endTag(null, "coord_long");

                serializer.startTag(null, "distance_miles");
                serializer.text(String.valueOf(restaurants.get(j).getDistance_miles()));
                serializer.endTag(null, "distance_miles");

                serializer.startTag(null, "rating");
                serializer.text(String.valueOf(restaurants.get(j).getRating()));
                serializer.endTag(null, "rating");
                serializer.endTag(null,"restaurant");
            }
            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
