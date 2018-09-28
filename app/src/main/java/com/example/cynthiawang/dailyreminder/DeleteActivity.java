package com.example.cynthiawang.dailyreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DeleteActivity extends AppCompatActivity {
    public static final String DELETED_THING = "com.example.cynthiawang.dailyreminder.ViewActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        List<String> namelist  = new ArrayList<>();
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        String remString = settings.getString("RL", "");
        String[] itemReminders = remString.split(";");

        System.out.println("load list!!!!!");

        if (remString!=""){
            for (String name: itemReminders){
                String[] desList = name.split(",", 2);
                System.out.println("name: " + name);
                if (name != "NO REMINDERS!!"){
                    namelist.add(desList[0]);
                }
            }
        }

        Spinner mySpinner = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DeleteActivity.this,
                android.R.layout.simple_list_item_1, namelist);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

    }

    public void toView(View view){
        Spinner getDelete = (Spinner)findViewById(R.id.spinner2);
        String deletethis = getDelete.getSelectedItem().toString();
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra(DELETED_THING, deletethis);
        intent.putExtra("activity","delete");
        startActivity(intent);
    }
}
