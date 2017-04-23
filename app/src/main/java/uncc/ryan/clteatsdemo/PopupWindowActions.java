package uncc.ryan.clteatsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Ryan on 4/22/2017.
 */

public class PopupWindowActions extends Activity implements View.OnClickListener {

    int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow_actions_list);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null){
            index = (int)b.get("INDEX");
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .6),(int)(height * .2));

        //onclicklisteners
        findViewById(R.id.btnPopActionAddFavorite).setOnClickListener(this);
        findViewById(R.id.btnPopActionViewDetails).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnPopActionAddFavorite)){
            //Toast.makeText(this, "Add To Favorites", Toast.LENGTH_SHORT).show();
            //TODO: add current restaurant to arrayList<Restaurant> favoritesList in SearchActivity

            SearchActivity.favoritesList.add(SearchActivity.placesList.get(index)); //Error line
            if(SearchActivity.favoritesList.size() != 0) {
                Log.d("favoritesList Status", "Size(): " + SearchActivity.favoritesList.size() + "\ntoString: " + SearchActivity.favoritesList.toString());
            }
            finish();
        }else if(v == findViewById(R.id.btnPopActionViewDetails)){
            Toast.makeText(this, "View Details", Toast.LENGTH_SHORT).show();
            //TODO: not sure what this will do yet
            finish();
        }

    }
}
