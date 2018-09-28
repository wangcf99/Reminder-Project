package com.example.cynthiawang.dailyreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddActivity extends AppCompatActivity {

    public static final String ADDED_REMINDER = "com.example.cynthiawang.dailyreminder.ViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Spinner mySpinner = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.dropdown_times));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);


    }

    public void backView(View view){
        EditText RemName = (EditText)findViewById(R.id.editText);
        EditText RemTime = (EditText)findViewById(R.id.editText2);
        EditText RemNotes = (EditText)findViewById(R.id.editText3);


        Spinner getNumtimes = (Spinner)findViewById(R.id.spinner1);

        String name = RemName.getText().toString();
        String time = RemTime.getText().toString();
        String notes = RemNotes.getText().toString();
        String numtimes = getNumtimes.getSelectedItem().toString();

        String reminder = name + ",,," + time + ",,," + numtimes + ",,," + notes;

        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra(ADDED_REMINDER, reminder);
        intent.putExtra("activity","add");
        startActivity(intent);

    }
}
