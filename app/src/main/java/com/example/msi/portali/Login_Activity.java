package com.example.msi.portali;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login_Activity extends AppCompatActivity {
public static String nameTopass = "";
private EditText mEmail,mPassword;
private TextView mSignUp;
private Button mbutton;
private boolean Okk = false;
private TextInputLayout email_error,password_error;
/**/
    /**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        mEmail = (EditText)findViewById(R.id.input_email);
        mPassword =(EditText) findViewById(R.id.input_password);
        mSignUp = (TextView) findViewById(R.id.link_signup);
        mbutton =(Button) findViewById(R.id.btn_login);

        email_error = findViewById(R.id.email_error);
        password_error = findViewById(R.id.password_error);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this,Sign_Up_Activity.class);
                startActivity(intent);


            }
        });


        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase objDb = new Databaza(Login_Activity.this).getReadableDatabase();
                int content = 0;
                Cursor c = objDb.rawQuery("select * from SIGNUP",null);
                if(c.getCount()>0){
                    c.moveToFirst();

                    while (c.isAfterLast()==false){
                      /* String email = c.getString(2);*//*Breakpoint Test*//*
                        String password = c.getString(3);*/

                        if(c.getString(2).equals(mEmail.getText().toString().trim().toUpperCase())
                                &&c.getString(3).trim().equals
                                (getSHA((mPassword.getText().toString()),mEmail.getText().toString().trim().toUpperCase()))){

                            MainActivity.isLoggedIn = true;
                            nameTopass = getName(mEmail.getText().toString().trim().toUpperCase());
                            Intent intent = new Intent(Login_Activity.this,MainActivity.class);
                            startActivity(intent);
                            Okk = true;


                        }
                        c.moveToNext();

                    }
                    if(mEmail.getText().toString().trim().isEmpty()){
                        email_error.setError("This field cannot be Empty!");
                        password_error.setError(null);

                    }
                    else if(mPassword.getText().toString().trim().isEmpty())
                    {
                        password_error.setError("This field cannot be Empty!");
                        email_error.setError(null);
                    }
                    else if(!Okk){
                        StyleableToast.makeText(getApplicationContext(),"Password or Email is Wrong!",R.style.exampleToastError).show();
                        email_error.setError(null);
                        password_error.setError(null);
                    }




                }



            }
        });

    }
    //Hashing the password
    public static String getSHA(String input,String salt)
    {

        try {

            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest((input+salt).getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);

            return null;
        }
    }
    public String getName(String name)
    {
        SQLiteDatabase objDb = new Databaza(Login_Activity.this).getReadableDatabase();
        Cursor c = objDb.rawQuery("select * from SIGNUP WHERE EMAIL='"+name+"'",null);
        if(c.getCount()>0){
            c.moveToFirst();
            return c.getString(1);

        }
        else return "Not Known";



    }
/*    private void saveSession()
    {
        SQLiteDatabase objD = new Databaza(Login_Activity.this).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("SESSION",1);
        *//*objD.execSQL("DELETE From SAVE");*//*
        long ins = objD.insert("SAVE",null,cv);

    }*/


    @Override
    public void onBackPressed() {
        finish();
        startActivity(getIntent());
    }

}
