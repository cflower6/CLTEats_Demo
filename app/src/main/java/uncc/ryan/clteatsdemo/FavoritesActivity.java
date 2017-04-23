package uncc.ryan.clteatsdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;

import static uncc.ryan.clteatsdemo.FileUtil.writeXml;
import static uncc.ryan.clteatsdemo.FileUtil.xmlToObject;

public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String sortType;
    String spFilePath;
    String filepath;

    static ArrayList<Restaurant> favRestaurantsList;

    LinearLayout linearLayout;
    RecyclerView resultsView;
    RVAdapter mRVAdapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        sp = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        sortType = sp.getString("SORT_TYPE","Distance");
        spFilePath = sp.getString("FILE_PATH","favoriteslist.xml");
        Log.d("sp.SORT_TYPE",sortType + "");
        Log.d("sp.FILE_PATH",spFilePath + "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading from file...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        if(sp.contains("FILE_PATH")) {
            filepath = spFilePath;
        }else{
            filepath = getFilePath();
        }
        Log.d("getFilePath()",filepath + "");

        favRestaurantsList = new ArrayList<Restaurant>(xmlToObject(filepath));

        onSortingList();
        Log.d("favRestList.size()",favRestaurantsList.size()+"");
        Log.d("favRestList.toString()",favRestaurantsList.toString()+"");

        //setonclicklisteners


        //build programmatic layout
        linearLayout = (LinearLayout) findViewById(R.id.llFavoritesList);
        cleanLayout();
        populateLayout();


        progressDialog.dismiss();
    }

    private void cleanLayout(){
        linearLayout.removeAllViews();
    }

    private void populateLayout(){
        resultsView = new RecyclerView(this);
        resultsView.setHasFixedSize(true);
        resultsListClickListeners();
        linearLayout.addView(resultsView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        resultsView.setLayoutManager(llm);
        mRVAdapter = new RVAdapter(this, favRestaurantsList);
        resultsView.setAdapter(mRVAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume(){
        super.onResume();

        Log.d("INFO","refresh layout");
        mRVAdapter.notifyDataSetChanged();
        cleanLayout();
        populateLayout();
    }

    public void resultsListClickListeners(){
        resultsView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, resultsView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("onItemClick","position " + position);
                        //Toast.makeText(SearchActivity.this, "" + placesList.get(position).getName(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(FavoritesActivity.this, PopupWindow.class);
                        intent.putExtra("INDEX",position);

                        if(favRestaurantsList.get(position).getReview(0) != null) {
                            startActivity(intent);
                        }else{
                            Toast.makeText(FavoritesActivity.this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d("onLongItemClick","position " + position);

                        Intent intent = new Intent(FavoritesActivity.this, PopupWindowFavActions.class);
                        intent.putExtra("INDEX",position);
                        intent.putExtra("FILE_PATH",filepath);

                        if(favRestaurantsList.get(position) != null){
                            startActivity(intent);
                        }else{
                            Toast.makeText(FavoritesActivity.this, "Error: Couldn't find object!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }

    private String getFilePath(){
        File path = getApplicationContext().getFilesDir();
        StringBuilder sb = new StringBuilder(String.valueOf(path));
        sb.append("/favoriteslist.xml");
        return sb.toString();
    }

    public void onSortingList(){
        if(sortType.equals("A-Z")){
            Collections.sort(favRestaurantsList, new RestaurantNameComparator());
            Log.d("favRestList","A-Z Sorted");
        }else if(sortType.equals("Distance")){
            Collections.sort(favRestaurantsList, new RestaurantDistanceComparator());
            Log.d("favRestList","Distance Sorted");
        }else if(sortType.equals("Rating")){
            Collections.sort(favRestaurantsList, new RestaurantRatingComparator());
            Log.d("favRestList","Rating Sorted");
        }
    }
}
