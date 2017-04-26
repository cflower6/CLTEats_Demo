package uncc.ryan.clteatsdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.BoolRes;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner sortByType;

    Switch swRandomizeByConstraints;
    Switch swConstraintsFilter;
    Switch swPushNotifications;
    Switch swInAppNoise;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String lastSortType;
    String lastConstrainsFilter;
    String lastRandomizeByConstraints;
    String lastPushNotification;
    String lastAppNoise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //start shared preferences and start editor
        sp = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sp.edit();

        //sync settings last state
        lastSortType = sp.getString("SORT_TYPE","default");
        lastConstrainsFilter = sp.getString("FILTER_CONST", "default");
        lastRandomizeByConstraints = sp.getString("RANDOM_TYPE", "default");
        lastPushNotification = sp.getString("PUSH_NOTIF", "default");
        lastAppNoise = sp.getString("APP_NOISE","default");

        //set OnClickListeners
        findViewById(R.id.btnSaveSettings).setOnClickListener(this);

        //initialize spinner
        ArrayAdapter spinSortByTypeAdapter = ArrayAdapter.createFromResource(this,R.array.sortBy_array,R.layout.spinner_item_custom_notbold);
        spinSortByTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortByType = (Spinner)findViewById(R.id.spSortByPref);
        sortByType.setAdapter(spinSortByTypeAdapter);

        //initialize switches
        swRandomizeByConstraints = (Switch) findViewById(R.id.swConstraintRandomize);
            if(lastRandomizeByConstraints.equals("true")){
                swConstraintsFilter.setChecked(true);
            }
        swConstraintsFilter = (Switch) findViewById(R.id.swConstraintFilter);
            if(lastConstrainsFilter.equals("true")) {
                swConstraintsFilter.setChecked(true);
            }
        swPushNotifications = (Switch) findViewById(R.id.swPushNotifications);
            if(lastPushNotification.equals("true")){
                swPushNotifications.setChecked(true);
            }
        swInAppNoise = (Switch) findViewById(R.id.swInAppNoise);
            if(lastAppNoise.equals("true")){
                swInAppNoise.setChecked(true);
            }
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnSaveSettings)){
            String sortTypeSTR = sortByType.getSelectedItem().toString();
            if(sortTypeSTR.equals("Distance")){
                editor.putString("SORT_TYPE",sortTypeSTR);
                Log.d("sp.SORT_TYPE",sortTypeSTR+"");
            }else if(sortTypeSTR.equals("Rating")){
                editor.putString("SORT_TYPE",sortTypeSTR);
                Log.d("sp.SORT_TYPE",sortTypeSTR+"");
            }else if(sortTypeSTR.equals("A-Z")){
                editor.putString("SORT_TYPE",sortTypeSTR);
                Log.d("sp.SORT_TYPE",sortTypeSTR+"");
            }else{
                editor.putString("SORT_TYPE","Distance");
                Log.d("sp.SORT_TYPE","SET TO DEFAULT");
            }

            if(swRandomizeByConstraints.isChecked()){
                Log.d("RandomizeByConstraints","true");
                editor.putString("RANDOM_TYPE","true");
            }else if(!swRandomizeByConstraints.isChecked()){
                Log.d("RandomizeByConstraints","false");
                editor.putString("RANDOM_TYPE","false");
            }else{
                Log.d("RandomizeByConstraints","default");
                editor.putString("RANDOM_TYPE","default");
            }

            if(swConstraintsFilter.isChecked()){
                Log.d("ConstraintFilter","true");
                editor.putString("FILTER_CONST","true");
            }else if(!swConstraintsFilter.isChecked()){
                Log.d("ConstraintFilter","false");
                editor.putString("FILTER_CONST","false");
            }else {
                Log.d("ConstraintFilter","default");
                editor.putString("FILTER_CONST","default");
            }

            if(swPushNotifications.isChecked()){
                Log.d("PUSH_NOTIF","true");
                editor.putString("PUSH_NOTIF","true");
            }else if(!swPushNotifications.isChecked()){
                Log.d("PUSH_NOTIF","false");
                editor.putString("PUSH_NOTIF","false");
            }else{
                Log.d("PUSH_NOTIF","default");
                editor.putString("PUSH_NOTIF","default");
            }

            if(swInAppNoise.isChecked()){
                Log.d("APP_NOISE","true");
                editor.putString("APP_NOISE","true");
            }else if(!swInAppNoise.isChecked()){
                Log.d("APP_NOISE","false");
                editor.putString("APP_NOISE","false");
            }else{
                Log.d("AP_NOISE","default");
                editor.putString("APP_NOISE","default");
            }

            editor.commit();
        }
    }
}
