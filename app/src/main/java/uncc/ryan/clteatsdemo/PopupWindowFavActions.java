package uncc.ryan.clteatsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static uncc.ryan.clteatsdemo.FileUtil.writeXml;

/**
 * Created by Ryan on 4/23/2017.
 */

public class PopupWindowFavActions extends Activity implements View.OnClickListener {

    int index;
    String filepath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow_favactions_list);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null){
            index = (int)b.get("INDEX");
            filepath = b.getString("FILE_PATH");
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .6),(int)(height * .2));

        //onclicklisteners
        findViewById(R.id.btnPopActionViewFavDetails).setOnClickListener(this);
        findViewById(R.id.btnPopActionDeleteFavorite).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnPopActionDeleteFavorite)){
            //Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
            Log.d("Item Removed",FavoritesActivity.favRestaurantsList.get(index)+"");
            FavoritesActivity.favRestaurantsList.remove(FavoritesActivity.favRestaurantsList.get(index));

            writeXml(FavoritesActivity.favRestaurantsList,filepath);
            Log.d("file updated","list size written: " + FavoritesActivity.favRestaurantsList.size());

            finish();
        }else if(v == findViewById(R.id.btnPopActionViewFavDetails)){
            Toast.makeText(this, "View Details", Toast.LENGTH_SHORT).show();
        }
    }
}
