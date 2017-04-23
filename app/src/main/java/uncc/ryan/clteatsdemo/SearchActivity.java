package uncc.ryan.clteatsdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.ULocale;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Handler;

import static java.lang.Math.abs;
import static uncc.ryan.clteatsdemo.FileUtil.writeXml;
import static uncc.ryan.clteatsdemo.FileUtil.xmlToObject;
import static uncc.ryan.clteatsdemo.R.id.dark;
import static uncc.ryan.clteatsdemo.R.id.googleMap;
import static uncc.ryan.clteatsdemo.R.id.start;
import static uncc.ryan.clteatsdemo.R.styleable.MenuItem;


public class SearchActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener, PlacesAPIAsyncTask.AsyncResponse, PlacesDetailsAPIAsyncTask.AsyncResponse {

    public static GoogleMap mMap;
    protected LocationManager locationManager;
    protected double latitude, longitude;
    static ArrayList<Restaurant> favoritesList = new ArrayList<>();
    static ArrayList<Restaurant> placesList;
    static ArrayList<Review> reviewsList = new ArrayList<>();
    final String API_KEY = "AIzaSyDQR8gmJvYApRaEcepi9SZ4L9_TY1oOnMY";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    static float zoomLevel = 9.0f;

    GoogleApiClient mGoogleApiClient;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    LatLng latLng;
    Location mLocation;

    ProgressDialog progressDialog;
    ProgressDialog progressDialogReviews;

    Spinner spinDistance;
    Spinner spinCategory;
    Spinner spinPrice;

    TextView tvLat;
    TextView tvLong;

    boolean onSearchRandomized = false;
    boolean onSearchRandomizedBranch = false;

    int randomizedListSize;
    static int reviewsMergeSync;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String sortType;
    String randomType;
    String filterConst;

    RecyclerView resultsView;

    GetCurrentLocationTask locationListener = new GetCurrentLocationTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(googleMap);
        mapFragment.getMapAsync(this);

        sp = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sp.edit();
        sortType = sp.getString("SORT_TYPE", "Distance");//default sort type = distance
        randomType = sp.getString("RANDOM_TYPE", "TRUE");//default randomize = use constraints
        filterConst = sp.getString("FILTER_CONST", "TRUE");//default filter = remove places without constraint data
        Log.d("sp.SORT_TYPE", sortType + "");
        Log.d("sp.RANDOM_TYPE", randomType + "");
        Log.d("sp.FILTER_CONST", filterConst + "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Search Results...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialogReviews = new ProgressDialog(this);
        progressDialogReviews.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialogReviews.setMessage("Loading reviews...");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location != null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, this.locationListener);*/

        //setOnClickListeners
        findViewById(R.id.btn2Search).setOnClickListener(this);
        findViewById(R.id.btnSearchRandomize).setOnClickListener(this);
        findViewById(R.id.btnRetryGPS).setOnClickListener(this);

        tvLat = (TextView) findViewById(R.id.tvLatitude);
        tvLong = (TextView) findViewById(R.id.tvLongitude);

        //initalize spinners
        ArrayAdapter spinDistanceAdapter = ArrayAdapter.createFromResource(this, R.array.distance_array, R.layout.spinner_item_custom);
        spinDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDistance = (Spinner) findViewById(R.id.spinDistance);
        spinDistance.setAdapter(spinDistanceAdapter);

        ArrayAdapter spinCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.cuisine_array, R.layout.spinner_item_custom);
        spinCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory = (Spinner) findViewById(R.id.spinCategory);
        spinCategory.setAdapter(spinCategoryAdapter);

        ArrayAdapter spinPriceAdapter = ArrayAdapter.createFromResource(this, R.array.price_array, R.layout.spinner_item_custom);
        spinPriceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPrice = (Spinner) findViewById(R.id.spinPrice);
        spinPrice.setAdapter(spinPriceAdapter);


        /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 1, locationListener);*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("GPS Provider", "not enabled");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS Not Enabled")
                    .setMessage("Would you like to enable the GPS settings?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    tvLat.setText(String.valueOf(latitude));
                    tvLong.setText(String.valueOf(longitude));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, mLocationListener);

            //mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            //latitude = latLng.latitude;
            //longitude = latLng.longitude;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.btn2Search)) {
            getGPS();
            onSearch();
        } else if (v == findViewById(R.id.btnSearchRandomize)) {
            onSearchRandomized = true;
            onSearchRandomizedBranch = true;
            onSearch();
        } else if (v == findViewById(R.id.btnRetryGPS)) {
            getGPS();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();

        LatLng latLngDefault = new LatLng(35.3056877, -80.7322255);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngDefault, 9.0f));

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8.0f));

        if (latitude != 0.0 && longitude != 0.0) {
            LatLng latlng = new LatLng(latitude, longitude);
            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latlng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 8.0f));
        }
    }

    public void moveMapCamera() {
        LatLng latlng = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));

        /*if(latitude != 0.0 && longitude != 0.0){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoomLevel));
        }*/
    }

    public void onSearch() {
        //TO DO: populate spinners
        String maxDistance = spinDistance.getSelectedItem().toString();
        String radius = null;
        if (maxDistance.equals("5")) {
            radius = "8047";
            zoomLevel = 11.5f;
        } else if (maxDistance.equals("10")) {
            radius = "16093";
            zoomLevel = 10.0f;
        } else if (maxDistance.equals("20")) {
            radius = "32187";
            zoomLevel = 9.5f;
        } else if (maxDistance.equals("30")) {
            radius = "48280";
            zoomLevel = 9.3f;
        }

        String foodCategory = spinCategory.getSelectedItem().toString();

        String maxPrice = spinPrice.getSelectedItem().toString();

        getGPS();

        Log.d("Set current coordinates", latitude + ", " + longitude);

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/xml?");
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&radius=" + radius);
        sb.append("&types=restaurant");
        sb.append("&sensor=true");
        sb.append("&key=" + API_KEY);


        Log.d("Built Search Query: ", sb.toString());

        progressDialog.show();

        new PlacesAPIAsyncTask(this).execute(sb.toString());
    }

    @Override
    public PlacesDetailsAPIAsyncTask.AsyncResponse onProcessFinish(ArrayList<Review> output) {
        reviewsList = output;
        Log.d("debug", "reviewsList: " + reviewsList.toString());

        for (int k = 0; k < placesList.size(); k++) {
            for (int j = 0; j < reviewsList.size(); j++) {
                String placesListID = placesList.get(k).getPlace_id();
                String reviewsListID = reviewsList.get(j).getPlace_id();

                progressDialogReviews.setMessage("Loading: " + placesList.get(k).getName());

                //Log.d("placesList.size()",placesList.size()+"");
                //Log.d("list matchup", "hereID:" + placesListID + "\nthereID:" + reviewsListID);
                //Log.d("reviewsList.size()",reviewsList.size()+"");

                if (placesListID.equals(reviewsListID)) {
                    ArrayList<Review> tempListReviews = placesList.get(k).getReviews();
                    //tempListReviews.add(reviewsList.get(j));
                    placesList.get(k).setReview(reviewsList.get(j));
                    Log.d("ID matchup", "success");
                } else {
                    //Log.d("ID matchup","fail");
                }
            }
        }

        progressDialogReviews.hide();

        return null;
    }

    public void getGPS() { //has error correcting loops to retry get coordinates up to 5 times arbitrarily
        //longitude = locationListener.thisLongitude;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        latitude = latLng.latitude;
        longitude = latLng.longitude;

        tvLong.setText(String.valueOf(longitude));
        tvLat.setText(String.valueOf(latitude));

        if(longitude == 0.0 || latitude == 0.0){
            for(int a = 0;a < 6;a++) {
                if(longitude == 0.0) {
                    longitude = locationListener.thisLongitude;
                    Log.d("LOCATION PROBLEM", "TRIED REFETCH LONGITUDE");
                }
                if(latitude == 0.0){
                    latitude = locationListener.thisLatitude;
                    tvLat.setText(String.valueOf(latitude));
                    Log.d("LOCATION PROBLEM","TRIED REFETCH LATITUDE");
                }
                if(a == 5) {
                    if(latitude == 0.0 || longitude == 0.0) {
                        Toast.makeText(this, "GPS ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }else if(longitude != 0.0 && latitude != 0.0){
                    moveMapCamera();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public PlacesAPIAsyncTask.AsyncResponse processFinish(ArrayList<Restaurant> output) {
        placesList = output;
        Log.d("debug","placesList: " + placesList.toString());

        Log.d("1st randomizedListSize",randomizedListSize+"");
        if(onSearchRandomizedBranch){
            randomizedListSize = 1;
            onSearchRandomizedBranch = false;
            Log.d("placesList Bound Mode","Randomized");
        }else{
            randomizedListSize = placesList.size();
            Log.d("placesList Bound Mode","Standard");
        }

        Log.d("getPlaceDetails","method called");
        getPlaceDetails();

        reviewsMergeSync = 0;
        while(reviewsMergeSync < placesList.size()){
            StringBuilder sb2 = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/xml?");
            sb2.append("placeid=" + placesList.get(reviewsMergeSync).getPlace_id());
            sb2.append("&key=" + API_KEY);

            Log.d("Built Search Query:",""+sb2.toString());

            new PlacesDetailsAPIAsyncTask(this).execute(sb2.toString());

            //Log.d("reviewsMergeSync",reviewsMergeSync+"");
            reviewsMergeSync++;
        }

        Location globalLocation = new Location("global");
        globalLocation.setLatitude(latitude);
        globalLocation.setLongitude(longitude);

        for(int j = 0; j < placesList.size(); j++){
            Location newLocation = new Location("newLocation");
            newLocation.setLatitude(placesList.get(j).getCoord_lat());
            newLocation.setLongitude(placesList.get(j).getCoord_long());

            double distance = newLocation.distanceTo(globalLocation);
            placesList.get(j).setDistance_meters(distance);
            placesList.get(j).setDistance_miles(distance*0.000621371);
        }

        Log.d("onSortingList()","method called");
        onSortingList();

        //randomized selection function
        if(onSearchRandomized && placesList.size() > 0) {
            Random random = new Random();
            int upperBound = (placesList.size() - 1);
            int randomSelection = abs((random.nextInt(upperBound) + 1));

            Log.d("randomSelection",randomSelection+"");
            Log.d("upperBound",upperBound+"");

            placesList.add(0,placesList.get(randomSelection));

            while(placesList.size() >= 2){
                Log.d("placesList removed",placesList.get(1).toString()+"");
                placesList.remove(placesList.get(1));
            }
            Log.d("placesList",placesList.size()+"");
        }

        clearLayout();

        return null;
    }

    public void getPlaceDetails(){
        Log.d("2nd randomizedListSize",randomizedListSize+"");
        for(int i = 0;i<randomizedListSize;i++){
            //Log.d("Place name",placesList.get(i).getName()+"");
            //Log.d("Place id",placesList.get(i).getPlace_id()+"");
            final int finalI = i;
            Places.GeoDataApi.getPlaceById(mGoogleApiClient,
                        placesList.get(i).getPlace_id())
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                if(places.getStatus().isSuccess() && places.getCount() > 0){
                                    final Place myPlace = places.get(0);
                                    //Log.i("Info","Place found: " + myPlace.getName());
                                    //Log.i("Info","Place found price: " + myPlace.getPriceLevel()+"");

                                    //Log.i("Info","Place found phone#:" + myPlace.getPhoneNumber());

                                    //Log.d("finalI",finalI+"");

                                    int tempPriceLevel = myPlace.getPriceLevel();
                                    if(tempPriceLevel == 1){
                                        //tempPrice = "$";
                                        placesList.get(finalI).setPrice("$");
                                    }else if(tempPriceLevel == 2){
                                        //tempPrice = "$$";
                                        placesList.get(finalI).setPrice("$$");
                                    }else if(tempPriceLevel == 3){
                                        //tempPrice = "$$$";
                                        placesList.get(finalI).setPrice("$$$");
                                    }else if(tempPriceLevel == -1){
                                        placesList.get(finalI).setPrice("?");
                                    }

                                    placesList.get(finalI).setPhone_number((String) myPlace.getPhoneNumber());
                                }else{
                                    Log.e("Error","Place not found");
                                }
                                //Log.d("Set Place price",placesList.get(finalI).getPrice()+"");
                                places.release();
                            }
                        });
            //placesList.get(i).setPrice(tempPrice);
            Log.d("Place price",placesList.get(i).getPrice()+"");
        }
    }

    public void clearLayout() {
        progressDialog.hide();
        progressDialogReviews.show();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llSearch);
        linearLayout.removeAllViews();
        resultsView = new RecyclerView(this);
        resultsView.setHasFixedSize(true);
        resultsListClickListeners();
        linearLayout.addView(resultsView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        resultsView.setLayoutManager(llm);
        RVAdapter mRVAdapter = new RVAdapter(this, placesList);
        resultsView.setAdapter(mRVAdapter);

        for (int k = 0; k < placesList.size(); k++){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(placesList.get(k).getCoord_lat(), placesList.get(k).getCoord_long()))
                    .title(placesList.get(k).getName()));
                    //.setSnippet(k + "");
        }

        moveMapCamera();
    }

    private void enableMyLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    public void onSortingList(){
        if(sortType.equals("A-Z")){
            Collections.sort(placesList, new RestaurantNameComparator());
            Log.d("placesList","A-Z Sorted");
        }else if(sortType.equals("Distance")){
            Collections.sort(placesList, new RestaurantDistanceComparator());
            Log.d("placesList","Distance Sorted");
        }else if(sortType.equals("Rating")){
            Collections.sort(placesList, new RestaurantRatingComparator());
            Log.d("placesList","Rating Sorted");
        }
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, "onBackPressed", Toast.LENGTH_SHORT).show();

        //TODO:if file exists read from ... else write favoritesList to file

        if(sp.contains("FILE_PATH")){
            Log.d("sp.FILE_PATH","true");
            String spFilePath = sp.getString("FILE_PATH",String.valueOf(getApplicationContext().getFilesDir())+"/favorites.xml");
            Log.d("sp.FILE_PATH",spFilePath+"");
            File file = new File(spFilePath);
            if(file.exists()){
                //TODO:read ... parse ... to favoritesList ... destroy file then write new file
                //parse file
                ArrayList<Restaurant> storageList = new ArrayList<Restaurant>(xmlToObject(spFilePath));
                for(int i=0;i<storageList.size();i++){
                    if(!favoritesList.contains(storageList.get(i))) {
                        favoritesList.add(storageList.get(i));
                        Log.d("StorageList","Element copied to favList");
                    }else{
                        Log.d("StorageList","Element ignored as duplicate");
                    }
                }
                Log.d("Info","xmlToObject called...");
                //destroy file
                Log.d("DeleteThisFile()","called...");
                Log.d("pre-del:if-exists",String.valueOf(file.exists())+"");
                deleteThisFile();
                Log.d("post-del:if-exists",String.valueOf(file.exists())+"");
                //write file
                Log.d("checkForDuplicates()","called...");
                checkForDuplicates();
                writeXml(favoritesList,spFilePath);
                Log.d("post-write:if-exists",String.valueOf(file.exists())+"");
            }else{
                //create file
                Log.d("checkForDuplicates()","called...");
                checkForDuplicates();
                writeXml(favoritesList,spFilePath);
                Log.d("Info","writeXml() called...");
                //write file
            }
        }else{
            Log.d("sp.FILE_PATH","false");
            File path = getApplicationContext().getFilesDir();
            StringBuilder filePath = new StringBuilder(String.valueOf(path));
            filePath.append("/favoriteslist.xml");
            String filepathSTR = filePath.toString();

            Log.d("sp.FILE_PATH",filepathSTR+"");

            editor.putString("FILE_PATH", filepathSTR+"");
            editor.commit();

            File file = new File(filepathSTR);
            if(file.exists()){
                //TODO:read ... parse ... to favoritesList ... destroy file then write new file
                //parse file
                ArrayList<Restaurant> storageList = new ArrayList<Restaurant>(xmlToObject(filepathSTR));
                for(int i=0;i<storageList.size();i++){
                    favoritesList.add(storageList.get(i));
                }
                Log.d("Info","xmlToObject called...");
                //destroy file
                Log.d("DeleteThisFile()","called...");
                Log.d("pre-del:if-exists",String.valueOf(file.exists())+"");
                deleteThisFile();
                Log.d("post-del:if-exists",String.valueOf(file.exists())+"");
                //write file
                Log.d("checkForDuplicates()","called...");
                checkForDuplicates();
                writeXml(favoritesList, filepathSTR);
                Log.d("Info","writeXml() called...");
                Log.d("post-write:if-exists",String.valueOf(file.exists())+"");
            }else{
                //create file
                Log.d("checkForDuplicates()","called...");
                checkForDuplicates();
                writeXml(favoritesList,filepathSTR);
                Log.d("Info","writeXml() called...");
                //write file
            }
        }

        finish();
    }

    public void checkForDuplicates(){
        Log.d("checkForDuplicates()","running...");
        Log.d("cfd().fLizt.size()",favoritesList.size()+"");
        for(int k=0;k<favoritesList.size();k++){
            for(int j=0;j<favoritesList.size();j++) {
                if (favoritesList.get(j).getPlace_id().equals(favoritesList.get(k).getPlace_id()) && k != j) {
                    Log.d("Duplicate Removed", favoritesList.get(k).toString() + "");
                    favoritesList.remove(k);
                    k = 1;
                }
            }
        }
        Log.d("cfd().fLizt.size()",favoritesList.size()+"");
    }

    private void deleteThisFile(){
        File dir = getFilesDir();
        File dirFile = new File(dir, "favoriteslist.xml");
        boolean deletedFile = dirFile.delete();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if(progressDialogReviews.isShowing()) {
            progressDialogReviews.dismiss();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if(progressDialogReviews.isShowing()) {
            progressDialogReviews.dismiss();
        }
    }

    public void resultsListClickListeners(){
        resultsView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, resultsView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("onItemClick","position " + position);
                        //Toast.makeText(SearchActivity.this, "" + placesList.get(position).getName(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SearchActivity.this, PopupWindow.class);
                        intent.putExtra("INDEX",position);

                        if(placesList.get(position).getReview(0) != null) {
                            startActivity(intent);
                        }else{
                            Toast.makeText(SearchActivity.this, "Still Loading...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d("onLongItemClick","position " + position);

                        Intent intent = new Intent(SearchActivity.this, PopupWindowActions.class);
                        intent.putExtra("INDEX",position);

                        if(placesList.get(position) != null){
                            startActivity(intent);
                        }else{
                            Toast.makeText(SearchActivity.this, "Error: Couldn't find object!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }
}
