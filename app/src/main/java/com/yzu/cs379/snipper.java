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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//https://www.itread01.com/content/1541933583.html HTTP NO SSL

public class snipper {
    private Map<String, String> cookie;
    private String account;
    private final String cfpLink = "http://www.wikicfp.com";
    public void snipper(){

    }
    public void snipper(String token,String account){
        cookie.put("accountkey",token);
        this.account = account;
    }
    public boolean login(String account, String password){

        Runnable runable = () ->{


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
                    this.account = account;

                }
                catch (IOException e){
                    e.printStackTrace();

                }


        };
        Thread  td = new Thread(runable);
        td.start();
        try{
            td.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return cookie.containsKey("accountkey"); //if login success return true



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
    public String getToken(){
        return cookie.get("accountkey");
    }
    public String getAccount(){return this.account;}
    public void setting(String token,String account){
        cookie.put("accountkey",token);
        this.account = account;
    }
    public List<cfpMetaClass> getCFPMainPageList(){
        List<cfpMetaClass> ret = new ArrayList<>();
        Runnable runable = () -> {
            try {
                String url = "http://www.wikicfp.com/cfp/";
                Connection conn = Jsoup.connect(url);
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
                if(cookie != null && cookie.containsKey("accountkey")){
                    conn.cookie("accountkey",cookie.get("accountkey"));
                }
                conn.method(Connection.Method.POST);
                conn.timeout(30000);
                Connection.Response response = conn.execute();
                Document document = response.parse();

                Elements mainTable = document.getElementsByClass("contsec").last().select("form > table > tbody > tr").last().select("td > table > tbody").first().select("tr");

                int iterator = 1;

                while(true){
                    if(iterator + 1 > mainTable.size()){
                        break;
                    }
                    Element tempDocFirst = mainTable.get(iterator);
                    Element tempDocSecond = mainTable.get(iterator + 1);
                    cfpMetaClass temp = new cfpMetaClass();
                    temp.Link = cfpLink + tempDocFirst.select("a").first().attr("href");
                    temp.Brief = tempDocFirst.getElementsByAttributeValue("colspan","4").text();
                    temp.Event = tempDocFirst.select("a").first().text();
                    temp.When = tempDocSecond.select("td").get(0).text();
                    temp.Where = tempDocSecond.select("td").get(1).text();
                    temp.Deadline = tempDocSecond.select("td").get(2).text();
                    temp.inMyList = tempDocSecond.getElementsByAttributeValue("align","center").size() != 0;
                    ret.add(temp);
                    iterator += 2;




                }

            }catch (IOException e){
                e.printStackTrace();
            }

        };

        Thread  td = new Thread(runable);
        td.start();
        try{
            td.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return ret;
    }
    public List<cfpMetaClass> getCFPMyListPageList(int page){
        if(cookie != null && cookie.containsKey("accountkey")){}
        else
            return null;
        if(page < 0){
            return null;
        }
        List<cfpMetaClass> ret = new ArrayList<>();
        Runnable run1 = () -> {
            try{
                String url = cfpLink + "/cfp/";
                Connection conn = Jsoup.connect(url);
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
                conn.cookie("accountkey",cookie.get("accountkey"));
                conn.method(Connection.Method.POST);
                conn.timeout(30000);
                Connection.Response response = conn.execute();
                Document document = response.parse();

                String url2 = cfpLink +  document.getElementsByClass("menusec").last().getElementsByClass("nav").get(9).attr("href") + "&page=" + Integer.toString(page);

                conn = null;
                response = null;
                document = null;
                conn = Jsoup.connect(url2);
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
                conn.cookie("accountkey",cookie.get("accountkey"));
                conn.method(Connection.Method.POST);
                conn.timeout(30000);
                response = conn.execute();
                document = response.parse();

                Elements mainTable = document.getElementsByClass("contsec").first().select("center > table > tbody > tr").get(2).select("td > table > tbody > tr");
                int iterator = 1;
                while(iterator < mainTable.size()){
                    Element tempDocFirst = mainTable.get(iterator);
                    Element tempDocSecond = mainTable.get(iterator + 1);
                    cfpMetaClass temp = new cfpMetaClass();
                    temp.Link =  cfpLink + tempDocFirst.select("a").first().attr("href");
                    temp.Brief = tempDocFirst.getElementsByAttributeValue("colspan","4").text();
                    temp.Event = tempDocFirst.select("a").first().text();
                    temp.When = tempDocSecond.select("td").get(0).text();
                    temp.Where = tempDocSecond.select("td").get(1).text();
                    temp.Deadline = tempDocSecond.select("td").get(2).text();
                    temp.inMyList = tempDocSecond.getElementsByAttributeValue("align","center").size() != 0;
                    ret.add(temp);
                    iterator += 2;




                }

            }catch (IOException e){
                e.printStackTrace();
            }
        };

        Thread  td = new Thread(run1);
        td.start();
        try{
            td.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return ret;

    }
    public List<cfpMetaClass> getCFPcatalogPageList(String catalogName){
        List<cfpMetaClass> ret = new ArrayList<>();
        Runnable runable = () -> {
            try {
                String url =  cfpLink + "/cfp/call?conference=" + catalogName;
                Connection conn = Jsoup.connect(url);
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
                if(cookie != null && cookie.containsKey("accountkey")){
                    conn.cookie("accountkey",cookie.get("accountkey"));
                }
                conn.method(Connection.Method.POST);
                conn.timeout(30000);
                Connection.Response response = conn.execute();
                Document document = response.parse();

                Elements mainTable = document.getElementsByClass("contsec").last().select("center > form > table > tbody > tr").get(2).select("table > tbody > tr");
                for(int iterator = 1 ; iterator < mainTable.size() ; iterator += 2){
                    Element tempDocFirst = mainTable.get(iterator);
                    Element tempDocSecond = mainTable.get(iterator + 1);
                    cfpMetaClass temp = new cfpMetaClass();
                    temp.Link =  cfpLink + tempDocFirst.select("a").first().attr("href");
                    temp.Brief = tempDocFirst.getElementsByAttributeValue("colspan","4").text();
                    temp.Event = tempDocFirst.select("a").first().text();
                    temp.When = tempDocSecond.select("td").get(0).text();
                    temp.Where = tempDocSecond.select("td").get(1).text();
                    temp.Deadline = tempDocSecond.select("td").get(2).text();
                    temp.inMyList = tempDocSecond.getElementsByAttributeValue("align","center").size() != 0 && tempDocSecond.getElementsByAttributeValue("align","center").text().equals("in My List");
                    ret.add(temp);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        };

        Thread  td = new Thread(runable);
        td.start();
        try{
            td.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }



        return ret;
    }

}
