package com.example.msi.portali.nointernet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.msi.portali.Databaza;
import com.example.msi.portali.MainActivity;
import com.example.msi.portali.R;
import com.example.msi.portali.models.Article;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OfflineActivity extends AppCompatActivity {
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mAuthors = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mDescriptions = new ArrayList<>();
    private ArrayList<String> mSources = new ArrayList<>();
    private ArrayList<String> mPublishedAts = new ArrayList<>();
    private ArrayList<String> mTimes = new ArrayList<>();
    private AdapterOffline recyclerViewAdapter;

    private Map<Integer,String> article = new HashMap<Integer, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        initNews();
        RecyclerView recyclerView  = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new AdapterOffline(OfflineActivity.this,mImages,mAuthors,mTitles,mDescriptions,mSources,mPublishedAts,mTimes);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(getApplicationContext(),"Mberriva",Toast.LENGTH_SHORT).show();
    }
    private void initNews()
    {
        SQLiteDatabase objD = new Databaza(OfflineActivity.this).getReadableDatabase();
        Cursor c = objD.rawQuery("select *from Lajmet",null);

        if(c.getCount()>0){
            c.moveToFirst();

            while (c.isAfterLast()==false){
                File storageDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
                mImages.add(storageDir+"/"+c.getString(1)+".jpg");
                mTitles.add(c.getString(1));
                mDescriptions.add(c.getString(2));
                mAuthors.add(c.getString(3));

                mPublishedAts.add(c.getString(4));
                mSources.add(c.getString(5));
                mTimes.add(c.getString(6));
                c.moveToNext();
            }
        }
    }
/*    private void initListener(){
        recyclerViewAdapter.setOnItemClickListener(new AdapterOffline.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

                *//*Article article = getAr*//*

            }
        });

    }*/

    @Override
    public void onBackPressed() {
        if(isNetworkAvailable()){
            Intent intent = new Intent(OfflineActivity.this, MainActivity.class);
            startActivity(intent);
            StyleableToast.makeText(getApplicationContext()," Be happy,You're Online Now!",R.style.exampleToast).show();
        }
        else{
            super.onBackPressed();
            StyleableToast.makeText(getApplicationContext(),"Offline again!",R.style.offline).show();

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
