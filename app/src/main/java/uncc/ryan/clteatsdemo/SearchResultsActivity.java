package uncc.ryan.clteatsdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import static uncc.ryan.clteatsdemo.SearchActivity.placesList;

public class SearchResultsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RVAdapter mRVAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
    }

    //ArrayList<Restaurant> restaurantsList = getIntent().getParcelableExtra("placesList");

    public void createResultsView(Restaurant restaurants){

    Log.d("debug","arrayList: " + placesList.toString());
    //mRVAdapter = new RVAdapter(this, placesList);

    recyclerView = (RecyclerView)findViewById(R.id.rvRestaurantResults);
    recyclerView.setHasFixedSize(true);
    linearLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(linearLayoutManager);
    //mRVAdapter = new RVAdapter(this,placesList);
    recyclerView.setAdapter(mRVAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
