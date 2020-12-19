package com.yzu.cs379;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;


//
//ever should have simple readme like this
//this activity only do once thing, loading manpage activity
public class MainActivity extends AppCompatActivity {

    private snipper mysnipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mysnipper = new snipper();
        if(mysnipper.isNetWorkAvailable(this)){
            mysnipper.login("","");

            mysnipper.getCFPcatalogPageList("machine learning");
            mysnipper.getCFPMyListPageList(1);
            List<cfpMetaClass> test = mysnipper.getCFPMainPageList();
            for(int i = 0 ; i < test.size() ; i++){
                mysnipper.getEventContent(test.get(1).Link);
            }
        }

        else
            Log.d("state","network error");
    }
}