package uncc.ryan.clteatsdemo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setOnClickListeners
        findViewById(R.id.btnSearch).setOnClickListener(this);
        findViewById(R.id.btnFavorites).setOnClickListener(this);
        findViewById(R.id.btnSettings).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnSearch)){
            //TO DO:start searchActivity
            if(isConnectedOnline()){
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "ERROR: No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }else if(v == findViewById(R.id.btnSettings)) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    private boolean isConnectedOnline(){ //checks if device has internet access
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }
    }
}
