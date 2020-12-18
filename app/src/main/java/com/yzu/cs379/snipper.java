package com.yzu.cs379;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

//https://www.itread01.com/content/1541933583.html HTTP NO SSL

public class snipper {
    private Map<String, String> cookie;
    private String account;
    public void snipper(){

    }
    public void login(String account, String password){

        Runnable runable = () -> {
            try {
                String Url = "http://www.wikicfp.com/cfp/servlet/user.regin";

                Connection conn = Jsoup.connect(Url);
                conn.data("accountsel",account);
                conn.data("password",password);
                conn.data("keepin","on");
                conn.data("mode","login");
                conn.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
                conn.method(Connection.Method.POST);
                conn.timeout(30000);
                Connection.Response response = conn.execute();
                //Connection.Response response = Jsoup.connect(Url).data("accountsel",account).data("password",password).data("keepin","on").data("mode","login").header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0").method(Connection.Method.POST).execute();
                Log.d("STATE",response.body());
                this.cookie = response.cookies();

            }
            catch (IOException e){
                e.printStackTrace();

            }

        };
        new Thread(runable).start();



    }
    public boolean isNetWorkAvailable(Activity activity){
        Context context = activity.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null){
            return false;
        }else{
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            if(netInfo != null && netInfo.length > 0){
                for(int i = 0 ; i < netInfo.length ; i++){
                    if(netInfo[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }



}
