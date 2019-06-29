package com.example.msi.portali;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msi.portali.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeMeasure extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView mBota,mTeknologjia,mBitcoin,mTop;
    private static String _dayy = "";
    private boolean hepls = false;
    float rainfall[] = {MainActivity.timeBota,MainActivity.timeTeknologjia,MainActivity.timeBitcoin,MainActivity.timeTop};
    String monthNames[] = {"Earth","Technology","Bitcoin","Top"};
    private Spinner mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_measure);
        getSupportActionBar().setTitle("Time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.days,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);



        mBota = findViewById(R.id.bota);
        mTeknologjia = findViewById(R.id.teknologjia);
        mBitcoin = findViewById(R.id.bitcoin);
        mTop = findViewById(R.id.top);
        TextView[] textViews = {mBota,mTeknologjia,mBitcoin,mTop};





        /*mBota.setText(String.valueOf(MainActivity.timeBota));
        mTeknologjia.setText(String.valueOf(MainActivity.timeTeknologjia));
        mBitcoin.setText(String.valueOf(MainActivity.timeBitcoin));
        mTop.setText(String.valueOf(MainActivity.timeTop));*/


        /*If _day is not filled take the current day*/
        if(_dayy.equals("")){
        _dayy = getDay();}
        fillDatabaseWithDayilyTime();
        rainfall = fillValues();
        for(int i=0;i<4;i++)
        {
            textViews[i].setText(String.valueOf(takeTime(i)/1000)+"s") ;
        }


        setupPieChart();


    }
    private void setupPieChart(){
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i=0;i<rainfall.length;i++){
            pieEntries.add(new PieEntry(rainfall[i],monthNames[i]));

        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"Time to Measure");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart = findViewById(R.id.chart);
        chart.setData(data);
        chart.invalidate();
    }

    public long takeTime(int tuple){
        SQLiteDatabase objDb = new Databaza(TimeMeasure.this).getReadableDatabase();
        long content = 0;
        Cursor c = objDb.rawQuery("select * from Koha where DAY='"+_dayy+"'",null);
        if(c.getCount()>0)
        {
            c.moveToFirst();

            content = c.getLong(tuple);

        }
        return content;

    }
    public void fillDatabaseWithDayilyTime(){
        SQLiteDatabase objDb = new Databaza(TimeMeasure.this).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("DAY",getDay());
        long finalTime = takeTime(0)+MainActivity.timeBota;
        MainActivity.timeBota = 0;
        cv.put("BOTATIME",finalTime);
        finalTime = takeTime(1)+MainActivity.timeTeknologjia;
        MainActivity.timeTeknologjia = 0;
        cv.put("TEKNOLOGJIATIME",finalTime);
        finalTime = takeTime(2) +MainActivity.timeBitcoin;
        MainActivity.timeBitcoin = 0;
        cv.put("BITCOINTIME",finalTime);
        finalTime = takeTime(3)+MainActivity.timeTop;
        MainActivity.timeTop = 0;
        cv.put("TOPTIME",finalTime);

        if(_dayy.equals(getDay())){
        objDb.execSQL("DELETE FROM KOHA WHERE DAY='"+getDay()+"'");}

        long retValue = objDb.insert("Koha",null,cv);

    }
    public float[] fillValues(){
        float[] koha = new float[4];
        for(int i=0;i<koha.length;i++){
            koha[i]= (float)takeTime(i);
        }
        return koha;
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TimeMeasure.this,MainActivity.class);
        startActivity(intent);
        /*fillDatabaseWithDayilyTime();*/
       /* intent.putExtra("StartFrom",System.currentTimeMillis());*/

    }
    public String getDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        //no need to Trim() string or to use toLower()
        if(!text.equals(getDay())){
        if(text.equals("Saturday")) {
            _dayy = "Saturday";
            finish();
            startActivity(getIntent());
        }
        else if(text.equals("Sunday")) {
            _dayy = "Sunday";
            finish();
            startActivity(getIntent());
        }
        else if(text.equals("Monday")) {
            _dayy = "Monday";
            finish();
            startActivity(getIntent());
        }
        else if(text.equals("Tuesday")) {
            _dayy = "Tuesday";
            finish();
            startActivity(getIntent());
        }
        else if(text.equals("Wednesday")) {
            _dayy = "Wednesday";
            finish();
            startActivity(getIntent());
        }
        else if(text.equals("Thursday")) {
            _dayy = "Thursday";
            finish();
            startActivity(getIntent());
        }
        else if(text.equals("Friday")) {
            _dayy = "Friday";
            finish();
            startActivity(getIntent());
        }
        }
        else if(hepls==false){
            _dayy = getDay();
            finish();
            startActivity(getIntent());
            hepls = true;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
