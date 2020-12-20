package com.yzu.cs379;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


//
//ever should have simple readme like this
//this activity only do once thing, loading manpage activity

//DONOT ADD MAIN PROGRAM IN THIS
//USING TEST
public class MainActivity extends AppCompatActivity {

    private snipper mysnipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentToLogin = new Intent(this,Login.class);
        startActivity(intentToLogin);

    }
}