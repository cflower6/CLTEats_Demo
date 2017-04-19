package uncc.ryan.clteatsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryrid on 4/18/2017.
 */

public class PopupWindow extends Activity {

    ArrayList<Restaurant> placesList = SearchActivity.placesList;
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

        getWindow().setLayout((int)(width * .6),(int)(height * .4));

        TextView tvPrice = (TextView)findViewById(R.id.tvPopPriceVar);
        tvPrice.setText(placesList.get(index).getPrice());
    }
}
