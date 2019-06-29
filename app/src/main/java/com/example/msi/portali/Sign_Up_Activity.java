package com.example.msi.portali;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.PatternMatcher;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class Sign_Up_Activity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATERN =Pattern.compile("^(?=.*[0-9])" /*a digit must occur at least once*/
            +
            "(?=.*[a-z])" +/*# a lower case letter must occur at least once*/
            "(?=.*[A-Z])" +/*an upper case letter must occur at least once*/
            "(?=\\S+$)" +/*no whitespace allowed in the entire string*/
            ".{4,}$");/*anything, at least six places though*/


    private EditText mEmail,mPassword,mName;
    private TextInputLayout textInputLayout,textInputLayoutEmail,textInputLayoutPassword;
    private Button btn_signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up_);

        mEmail = findViewById(R.id.input_email);
        mName = findViewById(R.id.input_name);
        mPassword = findViewById(R.id.input_password);
        btn_signUp = findViewById(R.id.btn_signup);

        //textInputForMistakes
        textInputLayout = findViewById(R.id.textInput1);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        textInputLayoutPassword = findViewById(R.id.InputLayoutPassword);
        /**//**//**/

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail()==true&&validatePassword()==true)
                {
                    SQLiteDatabase objDb = new Databaza(Sign_Up_Activity.this).getWritableDatabase();
                    ContentValues cv = new ContentValues();



                    cv.put("NAME",mName.getText().toString().trim());
                    cv.put("EMAIL",mEmail.getText().toString().trim().toUpperCase());
                    //getting Hash of Password//Salt is the email cause it is Unique
                    cv.put("PASSWORD",getSHA(mPassword.getText().toString().trim(),mEmail.getText().toString().trim().toUpperCase()));

                    if(tupleExist(mEmail.getText().toString().trim().toUpperCase())==false){

                        StyleableToast.makeText(getApplicationContext(),"This Email Already Exist!",R.style.exampleToastError).show();
                    }
                    else {

                    long it = objDb.insert("SIGNUP",null,cv);
                    if(it==-1){
                        StyleableToast.makeText(getApplicationContext(),"Not Registered",R.style.exampleToastError).show();
                    }
                    else
                    StyleableToast.makeText(getApplicationContext(),"Successfully Registered",R.style.exampleToast).show();
                    Intent intent = new Intent(Sign_Up_Activity.this,Login_Activity.class);
                    startActivity(intent);
                }
                }

            }
        });

    }
    private boolean validateEmail(){
        String emailInput = mEmail.getText().toString().trim();
        if(emailInput.isEmpty()){
            textInputLayoutEmail.setError("Fields cannot be Empty!");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            textInputLayoutEmail.setError("Please Enter a valid Email!");
        }
        else
            textInputLayoutEmail.setError(null);


        return true;
    }
    private boolean validatePassword(){
        String passwordInput = mPassword.getText().toString().trim();
        if(passwordInput.isEmpty()){
            textInputLayoutPassword.setError("Field Cannot be empty!");
            return false;
        }
        else if(!PASSWORD_PATERN.matcher(passwordInput).matches()){
            textInputLayoutPassword.setError("Password To Weak!");
            return false;
        }
        else
            textInputLayoutPassword.setError(null);
        return true;

    }
    //Method for checking if Email exist//
    public boolean tupleExist(String tuple) {
        SQLiteDatabase objDb = new Databaza(Sign_Up_Activity.this).getReadableDatabase();
        int content = 0;
        boolean unique = true;
        Cursor c = objDb.rawQuery("select * from SIGNUP", null);
        if (c.getCount() > 0) {
            c.moveToFirst();

            while (c.isAfterLast() == false) {
                /*  String email = c.getString(2);   BreakPoint Testing*//**//*
                        String password = c.getString(3);*/

                if (c.getString(2).equals(tuple)) {
                    unique = false;
                }
                c.moveToNext();

            }

        }
        return unique;
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

    // Driver code
}
