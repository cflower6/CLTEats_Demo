package uncc.ryan.clteatsdemo;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    Spinner sortByType;
    ArrayList<String> spinSortByType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //initialize spinner
        spinSortByType.add("Name");
        spinSortByType.add("Rating");
        spinSortByType.add("Distance");
        spinSortByType.add("Price");
        ArrayAdapter<String> spinSortByTypeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinSortByType);
        spinSortByTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortByType = (Spinner)findViewById(R.id.spSortByPref);
        sortByType.setAdapter(spinSortByTypeAdapter);
    }
}
