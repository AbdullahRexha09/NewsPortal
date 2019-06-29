package com.example.msi.portali;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.msi.portali.api.ApiClient;
import com.example.msi.portali.api.ApiInterface;
import com.example.msi.portali.models.Article;
import com.example.msi.portali.models.News;
import com.example.msi.portali.nointernet.NoInternetConnection;
import com.example.msi.portali.nointernet.OfflineActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    public static long timeBota = 0,timeTeknologjia =0,timeBitcoin=0,timeTop=0;
    public static boolean isLoggedIn = false;
    public static final String API_KEY ="3879fb19debb4ccfbe55ea5cd36d1c80";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    String q="",whereFrom = "";
    private static String imageName = "";
    int startTime = 0,endTime=0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView mName;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Name Sign Out like Google*/
        mName = findViewById(R.id.name);
        mName.setText(Login_Activity.nameTopass);
        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();

            }
        });
        /**/
        if(!isNetworkAvailable()){

            /*Intent intent = new Intent(MainActivity.this, OfflineActivity.class);*/
            Intent intent = new Intent(MainActivity.this, NoInternetConnection.class);
            startActivity(intent);

        }
        /*isLoggedIn = true;//test version*/
        if(!isLoggedIn){
        Intent intenti = new Intent(MainActivity.this,Login_Activity.class);
        startActivity(intenti);
        finish();

        }
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        /*whereFrom = "top";*/
        if(whereFrom!=""){
        endTime =(int)System.currentTimeMillis();
        }
        whereFrom = "top";
        takeTime();


        startTime = (int)System.currentTimeMillis();

        /*whereFrom = "top";*/

        onLoadingSwipeRefresh(false);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = new MenuInflater(MainActivity.this);
        mi.inflate(R.menu.main_menu,menu);

        MenuInflater m2 = new MenuInflater(MainActivity.this);
        m2.inflate(R.menu.time_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    public void LoadJson(boolean trego){

        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();

        Call<News> call;
        if(trego==false)
        call = apiInterface.getNews(country,API_KEY);
        else
            call = apiInterface.changeNews(q,API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()&&response.body().getArticle()!=null){
                    if(!articles.isEmpty()){
                        articles.clear();
                    }
                    articles = response.body().getArticle();
                    adapter = new Adapter(articles,MainActivity.this);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                    initListener();
                    swipeRefreshLayout.setRefreshing(false);


                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });


    }

    private void initListener(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);

                Article article = articles.get(position);

                Glide.with(MainActivity.this).asBitmap()
                        .load(article.getUrlToImage())
                        .into(new SimpleTarget<Bitmap>(100,100) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                saveImage(resource);
                            }
                        });

                /*Name of photo*/
                  /**/
                storeDataNewsToDatabase(article);///saving data to database
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("img", article.getUrlToImage());



                /*intent.putExtra("title", article.getTitle());



                intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("author", article.getAuthor());*/

                Pair<View, String> pair = Pair.create((View) imageView, ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,
                        pair
                );


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, optionsCompat.toBundle());
                } else {
                    startActivity(intent);
                }
                startActivity(intent);


            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.bota)
        {
             endTime =(int)System.currentTimeMillis();
            takeTime();
             startTime =(int)System.currentTimeMillis();
             whereFrom = "bota";
            q = "earth";
            onLoadingSwipeRefresh(true);
            getSupportActionBar().setTitle("Earth");

        }
        else if(item.getItemId()==R.id.teknologjia)
        {
            endTime =(int)System.currentTimeMillis();
            takeTime();
            startTime = (int)System.currentTimeMillis();
            whereFrom = "teknologjia";
            q = "technology";
            onLoadingSwipeRefresh(true);
            getSupportActionBar().setTitle("Technology");

        }
        else if(item.getItemId()==R.id.bitcoin)
        {
            endTime =(int)System.currentTimeMillis();
            takeTime();
            startTime = (int)System.currentTimeMillis();
            whereFrom = "bitcoin";
            q = "bitcoin";
            onLoadingSwipeRefresh(true);
            getSupportActionBar().setTitle("Bitcoin");

        }
        else if(item.getItemId()==R.id.time){
            endTime =(int)System.currentTimeMillis();
            takeTime();
            String whereTo = whereFrom;
            whereFrom = "";
            Intent intent = new Intent(MainActivity.this,TimeMeasure.class);
            startActivity(intent);

          /*  startTime = (int)System.currentTimeMillis();

            whereFrom = whereTo;//Kujdes!!!!*/

        }

        return super.onOptionsItemSelected(item);
    }
    public void takeTime(){

        endTime = endTime - startTime;
        if(whereFrom.equals("bota")){

            timeBota = timeBota + endTime;

        }
        else if(whereFrom.equals("teknologjia"))
        {

            timeTeknologjia = timeTeknologjia + endTime;
        }
        else if(whereFrom.equals("bitcoin")){

           timeBitcoin = timeBitcoin + endTime;
        }
        else if(whereFrom.equals("top")) {
            timeTop = timeTop + endTime;
        }




    }



    @Override
    public void onRefresh() {


        LoadJson(false);



    }
    private void onLoadingSwipeRefresh(final boolean value){

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {


                LoadJson(value);


            }
        });

    }
    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = imageName+".jpg";
        File storageDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(MainActivity.this, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
    //check network connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
   public void storeDataNewsToDatabase(Article article){
       imageName = article.getTitle();
       /**/
       SQLiteDatabase objD = new Databaza(MainActivity.this).getWritableDatabase();
       ContentValues cv = new ContentValues();

       cv.put("TITLE",article.getTitle());
       cv.put("DESCRIPTION",article.getDescription());
       cv.put("AUTHOR",article.getAuthor());
       cv.put("PUBLISHEDAT",article.getPublishedAt());
       cv.put("SOURCE",article.getSource().getName());
       cv.put("TIME",article.getPublishedAt());
       /*cv.put("IMAGE",article.getUrlToImage());*/

       long result = objD.insert("Lajmet",null,cv);

   }

    @Override
    public void onBackPressed() {
        if(whereFrom.equals("top")){
            showAlert();
        }
        else
        {
            //back from a back
           onLoadingSwipeRefresh(false);
           getSupportActionBar().setTitle("Portali");

        }

    }
    public void showAlert(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        a_builder.setMessage("Do you wanna Sign Out")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this,Login_Activity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Confirmation");
        alert.show();
    }
    private boolean session(){

    return true;
    }
}
