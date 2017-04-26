package uncc.ryan.clteatsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryrid on 4/18/2017.
 */

public class PopupWindow extends Activity {

    ArrayList<Restaurant> placesList = SearchActivity.placesList;
    ArrayList<Review> reviewsList;
    int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null){
            index = (int)b.get("INDEX");
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .7),(int)(height * .6));

        TextView tvHeader = (TextView) findViewById(R.id.tvPopHeader);
        tvHeader.setText(placesList.get(index).getName());

        TextView tvPrice = (TextView)findViewById(R.id.tvPopPriceVar);
        tvPrice.setText(placesList.get(index).getPrice());

        TextView tvPhoneNumber = (TextView) findViewById(R.id.tvPopPhoneNUmber);
        tvPhoneNumber.setText(placesList.get(index).getPhone_number());

        reviewsList = placesList.get(index).getReviews();
        Log.d("reviewsList.size()",reviewsList.size()+"");
        Log.d("SA.pList.reviews.size()",SearchActivity.placesList.get(index).reviews.size()+"");

        //Log.d("reviewsList",reviewsList.toString()+"");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        ReviewAdapter adapter = new ReviewAdapter(this, SearchActivity.placesList.get(index).reviews);
        RecyclerView rvReviewsList = (RecyclerView)findViewById(R.id.rvReviewsList);
        rvReviewsList.setHasFixedSize(true);
        rvReviewsList.setLayoutManager(layoutManager);
        rvReviewsList.setAdapter(adapter);
    }
}
