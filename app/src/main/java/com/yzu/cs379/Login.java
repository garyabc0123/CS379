package com.yzu.cs379;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class Login extends AppCompatActivity {
    private snipper mysnipper;
    private EditText account;
    private EditText password;
    private ProgressBar myProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ConstraintLayout constraintLayout = findViewById(R.id.main_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.start();
        mysnipper = new snipper();
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.pass);
        myProgressBar = (ProgressBar)findViewById(R.id.prohressBarLogin);
    }

    public void onCLickLogin(View view){
        if(mysnipper.isNetWorkAvailable(this)){
            myProgressBar.setVisibility(View.VISIBLE);
            Runnable run = () -> {
                if(!mysnipper.login(account.getText().toString(),password.getText().toString())){


                    return;
                }
                myProgressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        myProgressBar.setVisibility(View.GONE);
                    }
                });
                //大便用程式碼
                List<cfpMetaClass> test = mysnipper.getCFPMainPageList();
                mysnipper.GoogleSearch("test");
                for(int i = 0 ; i < test.size() ; i++){
                    Intent intentToEventContent = new Intent(this,event_content.class);
                    Bundle bag = new Bundle();
                    bag.putString("link",test.get(i).Link);
                    bag.putString("token",mysnipper.getToken());
                    bag.putString("account",mysnipper.getAccount());
                    intentToEventContent.putExtras(bag);
                    startActivity(intentToEventContent);

                }
                //end
            };

            Thread  td = new Thread(run);
            td.start();


            try{
                td.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            myProgressBar.setVisibility(View.GONE);
            if(!mysnipper.iflogin())
                Toast.makeText(this,"Account Not found",Toast.LENGTH_SHORT).show();

        }

        else
            Toast.makeText(this,"Please check your Network",Toast.LENGTH_SHORT).show();
    }
}