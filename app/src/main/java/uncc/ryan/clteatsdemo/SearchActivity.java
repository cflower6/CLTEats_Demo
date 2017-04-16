package uncc.ryan.clteatsdemo;

import android.Manifest;
import android.content.Context;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;

import static java.lang.Math.abs;
import static uncc.ryan.clteatsdemo.R.id.googleMap;


public class SearchActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener, PlacesAPIAsyncTask.AsyncResponse {

    public static GoogleMap mMap;
    protected LocationManager locationManager;
    protected double latitude, longitude;
    ArrayList<Address> addressList;
    static ArrayList<Restaurant> placesList;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    boolean awal = true;

    static float zoomLevel = 9.0f;

    GoogleApiClient mGoogleApiClient;

    Spinner spinDistance;
    Spinner spinCategory;
    Spinner spinPrice;

    TextView tvLat;
    TextView tvLong;

    boolean onSearchRandomized = false;

    SharedPreferences sp;
    String sortType;

    GetCurrentLocationTask locationListener = new GetCurrentLocationTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(googleMap);
        mapFragment.getMapAsync(this);

        sp = this.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        sortType = sp.getString("SORT_TYPE","Distance");//default sort type = distance
        Log.d("sp.SORT_TYPE",sortType+"");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

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
        findViewById(R.id.btnSearchRandomize).setOnClickListener(this);
        findViewById(R.id.btnRetryGPS).setOnClickListener(this);

        tvLat = (TextView)findViewById(R.id.tvLatitude);
        tvLong = (TextView)findViewById(R.id.tvLongitude);

        //initalize spinners
            ArrayAdapter spinDistanceAdapter = ArrayAdapter.createFromResource(this,R.array.distance_array,R.layout.spinner_item_custom);
            spinDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinDistance = (Spinner)findViewById(R.id.spinDistance);
            spinDistance.setAdapter(spinDistanceAdapter);

            ArrayAdapter spinCategoryAdapter = ArrayAdapter.createFromResource(this,R.array.cuisine_array,R.layout.spinner_item_custom);
            spinCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinCategory = (Spinner)findViewById(R.id.spinCategory);
            spinCategory.setAdapter(spinCategoryAdapter);

            ArrayAdapter spinPriceAdapter = ArrayAdapter.createFromResource(this,R.array.price_array,R.layout.spinner_item_custom);
            spinPriceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinPrice = (Spinner)findViewById(R.id.spinPrice);
            spinPrice.setAdapter(spinPriceAdapter);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 1, locationListener);

    }

    @Override
    public void onLocationChanged(Location location){
        locationManager.removeUpdates(this.locationListener);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btn2Search)){
            onSearch();
        }else if(v == findViewById(R.id.btnSearchRandomize)){
            onSearchRandomized = true;
            onSearch();
        }else if(v == findViewById(R.id.btnRetryGPS)){
            getGPS();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();

        LatLng latLngDefault = new LatLng(35.3056877,-80.7322255);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngDefault,9.0f));

        if(latitude != 0.0 && longitude != 0.0) {
            LatLng latlng = new LatLng(latitude, longitude);
            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latlng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,8.0f));
        }
    }

    public void moveMapCamera(){
        LatLng latlng = new LatLng(latitude,longitude);
        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latlng);
        //mMap.animateCamera(cameraUpdate,1000,null);
        if(latitude != 0.0 && longitude != 0.0){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoomLevel));
        }
    }

    public void onSearch(){
        //TO DO: populate spinners
        String maxDistance = spinDistance.getSelectedItem().toString();
            String radius = null;
            if(maxDistance.equals("5")) {
                radius = "8047";
                zoomLevel = 11.5f;
            }else if(maxDistance.equals("10")){
                radius = "16093";
                zoomLevel = 10.0f;
            }else if(maxDistance.equals("20")){
                radius = "32187";
                zoomLevel = 9.5f;
            }else if(maxDistance.equals("30")){
                radius = "48280";
                zoomLevel = 9.3f;
            }

            //String foodCategory = spinCategory.getSelectedItem().toString();

            //String maxPrice = spinPrice.getSelectedItem().toString();

        getGPS();

        Log.d("Set current coordinates",latitude + ", " + longitude);

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/xml?");
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&radius=" + radius);
        sb.append("&types=restaurant");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyBJM6hOHxff8dVxDn40_I6YBmlFVG0bhMQ");

        Log.d("Built Search Query: ",sb.toString());

        new PlacesAPIAsyncTask(this).execute(sb.toString());
    }

    public void getGPS(){ //has error correcting loops to retry get coordinates up to 5 times arbitrarily
        longitude = locationListener.thisLongitude;
        tvLong.setText(String.valueOf(longitude));
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
    public PlacesAPIAsyncTask.AsyncResponse processFinish(ArrayList<Restaurant> output) {
        placesList = output;
        Log.d("debug","placesList: " + placesList.toString());

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

        //randomized selection function
        if(onSearchRandomized && placesList.size() > 0) {
            Random random = new Random();
            int upperBound = placesList.size();
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

    public void clearLayout() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llSearch);
        linearLayout.removeAllViews();
        RecyclerView resultsView = new RecyclerView(this);
        resultsView.setHasFixedSize(true);
        resultsView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, resultsView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("onItemClick","position " + position);
                        //Fragment fragment = ItemShortTouchFragment.newInstance():
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d("onLongItemClick","position " + position);

                    }
                })
        );
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
}
