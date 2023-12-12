package com.trodev.pennywise;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Loginpage extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        auth= FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            Intent intent= new Intent(Loginpage.this,login.class);
            startActivity(intent);
        }else {Intent intent1=new Intent(Loginpage.this,MainActivity.class);
            startActivity(intent1);
    }
}}