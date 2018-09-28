package com.example.cynthiawang.dailyreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ViewActivity extends AppCompatActivity {
    HashMap<String, String> nameTime = new HashMap<>();
    List<HashMap<String, String>> REMINDERS_LIST = new ArrayList<>();

    public static String title;
    public static String notes;
    public static long when;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        alarmManager();

        if(getIntent().getStringExtra("activity").equals("main")){
            System.out.println("here!");
            load_list();
        }else if (getIntent().getStringExtra("activity").equals("add")){
            System.out.println("add");
            load_list();
            addNew();
        }  else if (getIntent().getStringExtra("activity").equals("delete")){
            System.out.println("delete");
            deleteItem();
        }else{
            load_list();
        }
        save_list();
        ontoListView();

        //alarmManager();
    }

    public void registerAlarm(String t, String n){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 4);
        today.set(Calendar.MINUTE, 51);
        today.set(Calendar.SECOND, 0);

        Intent intent = new Intent(ViewActivity.this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ViewActivity.this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        title = t;
        notes = n;

        System.out.println("registerAlarm: " + title + notes);
        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,today.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        System.out.println("shoulda been done with rA");

    }
    public void tomilli(String hour, String minute){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        Date objDate = new Date();

        String dayString = objDate.getDay() + "";
        String MonthString = objDate.getMonth() + "";
        if (dayString.length() == 1){
            dayString = "0" + dayString;
        }
        if (MonthString.length()==1){
            MonthString="0"+MonthString;
        }

        String dateString = dayString + "-" + MonthString + "-" + (objDate.getYear()+1900) +" " + hour + ":" + minute + ":00";
        System.out.println("dateString: " + dateString);
        try{
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(dateString);
            when = date.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
    }

    public void alarmManager(){

        load_list();

        for (String s: nameTime.keySet()){
            System.out.println("alarm manager: " + s);
            String ss = nameTime.get(s);
            String[] sl = ss.split(",");
            String time = sl[0];
            String often = sl[1];
            String[] timelist = time.split(":", 2);

            tomilli(timelist[0], timelist[1]);
            registerAlarm(s, sl[2]);

            if (often != "Everyday"){
                tomilli(timelist[0] + 10, timelist[1]);
                registerAlarm(s, sl[2]);
            }

        }
    }
    public void ontoListView(){
        SimpleAdapter adapter = new SimpleAdapter(this, REMINDERS_LIST,
                R.layout.reminder_list,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.listtext1, R.id.listtext2});

        System.out.println(nameTime);
        Iterator it = nameTime.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            REMINDERS_LIST.add(resultsMap);
        }

        ListView lv = (ListView)findViewById(R.id.listview);
        lv.setAdapter(adapter);

    }
    public void addNew(){

        Intent intent = getIntent();
        String reminder = intent.getStringExtra(AddActivity.ADDED_REMINDER);
        String[] itemReminders = reminder.split(",,,");
        nameTime.put(itemReminders[0], itemReminders[1] + ", " + itemReminders[2] + ", " + itemReminders[3]);

        SimpleAdapter adapter = new SimpleAdapter(this, REMINDERS_LIST,
                R.layout.reminder_list,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.listtext1, R.id.listtext2});
        //save_list();
    }

    public void deleteItem(){
        Intent intent = getIntent();
        String todelete = intent.getStringExtra(DeleteActivity.DELETED_THING);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        String remString = settings.getString("RL", "");
        System.out.println("remString: " + remString);
        String[] itemReminders = remString.split(";");

        if (remString!=""){
            for (String name: itemReminders){
                String[] desList = name.split(",", 2);
                System.out.println("desList[0]: " + desList[0]);
                System.out.println("todelete: " + todelete);
                System.out.println("name: " + name);
                System.out.println(desList[0].toString().contains(todelete.toString()));

                if (name != "NO REMINDERS!!" && !desList[0].toString().contains(todelete.toString())){
                    nameTime.put(desList[0], desList[1]);
                }
            }

        }

    }

    public void load_list(){

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        String remString = settings.getString("RL", "");
        System.out.println(remString);
        String[] itemReminders = remString.split(";");

        System.out.println("load list!!!!!");

        if (remString!=""){
            for (String name: itemReminders){
                String[] desList = name.split(",", 2);
                System.out.println("name: " + name);
                if (name != "NO REMINDERS!!"){
                    nameTime.put(desList[0], desList[1]);
                }
            }

        }else{
            System.out.println("nothing");
            nameTime.put("NO REMINDERS!!", "");

        }

    }

    public void save_list(){
        StringBuilder sB = new StringBuilder();
        Set<String> keys = nameTime.keySet();
        for (String key : keys){
            sB.append(key + "," + nameTime.get(key));
            sB.append(";");
        }

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("RL", sB.toString());
        editor.commit();

        System.out.println("saved");
    }

    public void startAdd(View view){
        save_list();
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    public void toMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toDelete(View view){
        Intent intent = new Intent(this, DeleteActivity.class);
        startActivity(intent);
    }

}
