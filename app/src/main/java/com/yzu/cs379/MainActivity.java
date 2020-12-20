package com.yzu.cs379;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;


//
//ever should have simple readme like this
//this activity only do once thing, loading manpage activity
public class MainActivity extends AppCompatActivity {

    private snipper mysnipper;
    private EditText account, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = findViewById(R.id.main_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.start();
        mysnipper = new snipper();
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.pass);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mysnipper.isNetWorkAvailable(this)) {
                    mysnipper.login(account.getText().toString(), password.getText().toString());
                    Intent sendIntent = new Intent();
                    sendIntent.setType(Intent.ACTION_MAIN);
                    sendIntent.putExtra("account", account.getText().toString());
                    sendIntent.putExtra("password", password.getText().toString());
                }
                else
                    Log.d("state","network error");
            }
        });


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

            Intent intentToEventContent = new Intent(this,event_content.class);
            Bundle bag = new Bundle();
            bag.putString("link","http://www.wikicfp.com/cfp/servlet/event.showcfp?eventid=120560&copyownerid=31491");
            bag.putString("token",mysnipper.getToken());
            bag.putString("account",mysnipper.getAccount());
            intentToEventContent.putExtras(bag);
            startActivity(intentToEventContent);*/
        //}


    }
}