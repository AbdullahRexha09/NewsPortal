package com.example.msi.portali.nointernet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.msi.portali.MainActivity;
import com.example.msi.portali.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class NoInternetConnection extends AppCompatActivity {
    private Button mRetry,mOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);

        mRetry = findViewById(R.id.btn_retry);
        mOffline = findViewById(R.id.btn_offline);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable())
                {
                    Intent intent = new Intent(NoInternetConnection.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                    {
                        StyleableToast.makeText(getApplicationContext(),"No Internet!",R.style.exampleToastError).show();
                    }

            }
        });
        mOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoInternetConnection.this, OfflineActivity.class);
                startActivity(intent);
                StyleableToast.makeText(getApplicationContext(),"Offline Mode!",R.style.offline).show();
            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
