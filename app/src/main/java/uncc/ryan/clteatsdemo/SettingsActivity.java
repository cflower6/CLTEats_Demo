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
    ArrayList<String> spinSortByType = new ArrayList<>();

    Switch swRandomizeByConstraints;
    Switch swConstraintsFilter;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //start shared preferences and start editor
        sp = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sp.edit();

        //set OnClickListeners
        findViewById(R.id.btnSaveSettings).setOnClickListener(this);

        //initialize spinner
        ArrayAdapter spinSortByTypeAdapter = ArrayAdapter.createFromResource(this,R.array.sortBy_array,R.layout.spinner_item_custom_notbold);
        spinSortByTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortByType = (Spinner)findViewById(R.id.spSortByPref);
        sortByType.setAdapter(spinSortByTypeAdapter);

        //initialize switches
        swRandomizeByConstraints = (Switch) findViewById(R.id.swConstraintRandomize);
        swConstraintsFilter = (Switch) findViewById(R.id.swConstraintFilter);
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

            editor.commit();
        }
    }
}
