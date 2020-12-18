package com.yzu.cs379;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//
//ever should have simple readme like this
//this activity only do once thing, loading manpage activity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}