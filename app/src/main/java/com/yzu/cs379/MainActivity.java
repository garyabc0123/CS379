package com.yzu.cs379;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
            /*
            List<cfpMetaClass> test = mysnipper.getCFPcatalogPageList("machine learning",1);
            for(int i = 0 ; i < test.size() ; i++){
                mysnipper.getEventContent(test.get(1).Link);
            }
            test = mysnipper.getCFPMyListPageList(1);
            for(int i = 0 ; i < test.size() ; i++){
                mysnipper.getEventContent(test.get(1).Link);
            }
            test = mysnipper.getCFPMainPageList();
            for(int i = 0 ; i < test.size() ; i++){
                mysnipper.getEventContent(test.get(1).Link);
            }
            */
            Intent intentToEventContent = new Intent(this,event_content.class);
            Bundle bag = new Bundle();
            bag.putString("link","http://www.wikicfp.com/cfp/servlet/event.showcfp?eventid=120560&copyownerid=31491");
            bag.putString("token",mysnipper.getToken());
            bag.putString("account",mysnipper.getAccount());
            intentToEventContent.putExtras(bag);
            startActivity(intentToEventContent);
        }

        else
            Log.d("state","network error");
    }




}