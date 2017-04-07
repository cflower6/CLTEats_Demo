package uncc.ryan.clteatsdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.ULocale;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
<<<<<<< HEAD
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
=======
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
>>>>>>> origin/master
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
<<<<<<< HEAD
import com.google.android.gms.maps.CameraUpdate;
=======
>>>>>>> origin/master
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import java.util.ArrayList;

import static uncc.ryan.clteatsdemo.R.id.googleMap;

<<<<<<< HEAD
public class SearchActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener, PlacesAPIAsyncTask.AsyncResponse {

    public static GoogleMap mMap;
    protected LocationManager locationManager;
    protected double latitude = 35.32, longitude = -80.78;
    ArrayList<Address> addressList;
    static ArrayList<Restaurant> placesList;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    boolean awal = true;

    GoogleApiClient mGoogleApiClient;

    Spinner spinDistance;
    ArrayList<String> spinDistanceOptions = new ArrayList<String>();

=======
public class SearchActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static GoogleMap mMap;
    protected LocationManager locationManager;
    protected double latitude, longitude;
    ArrayList<Address> addressList;
    ArrayList<Restaurant> placesList;

    GoogleApiClient mGoogleApiClient;

    Spinner spinDistance;
    ArrayList<String> spinDistanceOptions = new ArrayList<String>();

>>>>>>> origin/master
    GetCurrentLocationTask locationListener = new GetCurrentLocationTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(googleMap);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

<<<<<<< HEAD
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location != null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, this.locationListener);

        //setOnClickListeners
        findViewById(R.id.btn2Search).setOnClickListener(this);
        //initalize spinners
            spinDistanceOptions.add("5");
            spinDistanceOptions.add("10");
            spinDistanceOptions.add("20");
            spinDistanceOptions.add("30");
        ArrayAdapter<String> spinDistanceAdapter = new ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, spinDistanceOptions);
        spinDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDistance = (Spinner)findViewById(R.id.spinDistance);
        spinDistance.setAdapter(spinDistanceAdapter);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 1, locationListener);
        if(locationListener.getThisLatitude() != 0.0) {
            latitude = locationListener.getThisLatitude();
        }
        if(locationListener.getThisLongitude() != 0.0) {
            longitude = locationListener.getThisLongitude();
        }
=======
        //setOnClickListeners
        findViewById(R.id.btn2Search).setOnClickListener(this);
        //initalize spinners
            spinDistanceOptions.add("5");
            spinDistanceOptions.add("10");
            spinDistanceOptions.add("20");
            spinDistanceOptions.add("30");
        ArrayAdapter<String> spinDistanceAdapter = new ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, spinDistanceOptions);
        spinDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDistance = (Spinner)findViewById(R.id.spinDistance);
        spinDistance.setAdapter(spinDistanceAdapter);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 1, locationListener);
        latitude = locationListener.getThisLatitude();
        longitude = locationListener.getThisLongitude();
>>>>>>> origin/master
        Log.d("Set current coordinates",latitude + ", " + longitude);
    }

    @Override
<<<<<<< HEAD
    public void onLocationChanged(Location location){
        //moveMapCamera();
        locationManager.removeUpdates(this.locationListener);
        //moveMapCamera();
    }

    @Override
=======
>>>>>>> origin/master
    public void onClick(View v) {
        if(v == findViewById(R.id.btn2Search)){

            //TODO: load search parameters into search asynctask, launch searchResultsActivity
            onSearch();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

<<<<<<< HEAD
        enableMyLocation();
    }

    public void moveMapCamera(){
        LatLng latlng = new LatLng(latitude,longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latlng);
        mMap.animateCamera(cameraUpdate);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
=======
>>>>>>> origin/master
    }

    public void onSearch(){
        //TO DO: populate spinners
        String maxDistance = spinDistance.getSelectedItem().toString();
            String radius = null;
<<<<<<< HEAD
            if(maxDistance.equals("5")) {
                radius = "8047";
            }else if(maxDistance.equals("10")){
                radius = "16093";
            }else if(maxDistance.equals("20")){
                radius = "32187";
            }else if(maxDistance.equals("30")){
=======
            if(maxDistance.equals(5)) {
                radius = "8047";
            }else if(maxDistance.equals(10)){
                radius = "16093";
            }else if(maxDistance.equals(20)){
                radius = "32187";
            }else if(maxDistance.equals(30)){
>>>>>>> origin/master
                radius = "48280";
            }
        Spinner spinCategory = (Spinner)findViewById(R.id.spinCategory);
            //String foodCategory = spinCategory.getSelectedItem().toString();
        Spinner spinPrice = (Spinner)findViewById(R.id.spinPrice);
            //String maxPrice = spinPrice.getSelectedItem().toString();

<<<<<<< HEAD

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/xml?");
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&radius=" + radius);
        sb.append("&types=restaurant");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyBJM6hOHxff8dVxDn40_I6YBmlFVG0bhMQ");

        Log.d("Built Search Query: ",sb.toString());

        new PlacesAPIAsyncTask(this).execute(sb.toString());

=======
        //TODO: clear example values
        latitude = 35.32; longitude = -80.78;
        radius = "48280";

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&radius=" + radius);
        sb.append("&types=restaurant");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyBJM6hOHxff8dVxDn40_I6YBmlFVG0bhMQ");

        Log.d("Built Search Query: ",sb.toString());

        PlacesAPIAsyncTask placesTask = new PlacesAPIAsyncTask();
        placesTask.execute(sb.toString());
        addToPlacesList(sb.toString());
    }

    public void addToPlacesList(String sb){
>>>>>>> origin/master
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
<<<<<<< HEAD

    }

    @Override
    public PlacesAPIAsyncTask.AsyncResponse processFinish(ArrayList<Restaurant> output) {
        placesList = output;
        Log.d("debug","placesList: " + placesList.toString());
        clearLayout();
        //Intent intent = new Intent(this, SearchResultsActivity.class);
        //intent.putExtra("placesList", placesList);
        //startActivity(intent);
        return null;
    }

    public void clearLayout() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llSearch);
        linearLayout.removeAllViews();
        RecyclerView resultsView = new RecyclerView(this);
        resultsView.setHasFixedSize(true);
        linearLayout.addView(resultsView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        resultsView.setLayoutManager(llm);
        RVAdapter mRVAdapter = new RVAdapter(this, placesList);
        resultsView.setAdapter(mRVAdapter);

        for (int k = 0; k < placesList.size(); k++){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(placesList.get(k).getCoord_lat(), placesList.get(k).getCoord_long()))
                    .title(placesList.get(k).getName()));
        }

        moveMapCamera();
    }
=======
>>>>>>> origin/master

    }
}
