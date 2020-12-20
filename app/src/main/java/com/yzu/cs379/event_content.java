package com.yzu.cs379;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class event_content extends AppCompatActivity {
    private snipper mysnipper;
    private TextView title;
    private RecyclerView catalogRecyclerView;
    private catlogAdapter myAdapter;
    private WebView myWeb;
    private String myLink;
    private ImageButton heart;
    private eventContentClass myEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_content);

        Intent myIntent = getIntent();
        mysnipper = new snipper();
        title = (TextView)findViewById(R.id.title);
        catalogRecyclerView = (RecyclerView)findViewById(R.id.catlogView);
        myWeb = (WebView)findViewById(R.id.webviewer);
        myLink = myIntent.getExtras().getString("link");
        heart = (ImageButton)findViewById(R.id.heart);

        mysnipper.setting(myIntent.getExtras().getString("token"),myIntent.getExtras().getString("account"));
        myEvent = mysnipper.getEventContent(myLink);



        title.setText(myEvent.eventName);
        if(myEvent.inMyList){
            heart.setImageResource(R.drawable.heart);
        }else{
            heart.setImageResource(R.drawable.holo_heart);
        }


        catalogRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        myAdapter = new catlogAdapter(myEvent.catalog);
        catalogRecyclerView.setAdapter(myAdapter);

        myWeb.getSettings().setJavaScriptEnabled(true);
        myWeb.setWebViewClient(new WebViewClient());
        myWeb.loadData(myEvent.webContent,"text/html","UTF-8");

    }

    public void OnClickShare(View view){
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT,myLink);
        share.setType("text/plain");
        startActivity(share);
    }

    public void onClickOpenInBrowser(View view){
        Intent openBrowser = new Intent(Intent.ACTION_VIEW);
        openBrowser.setData(Uri.parse(myLink));
        startActivity(openBrowser);
    }
    public void onClickAddToMyList(View view){

        if(myEvent.inMyList){
            heart.setImageResource(R.drawable.holo_heart);
            mysnipper.removeFromMyList(myLink);
        }else{
            heart.setImageResource(R.drawable.heart);
            mysnipper.addToMyList(myLink);
        }
        myEvent.inMyList = !myEvent.inMyList;
    }

    class catlogAdapter extends RecyclerView.Adapter<catlogAdapter.ViewHolder>{
        private List<String> myData;

        catlogAdapter(List<String> data){
            myData = data;
        }

        class ViewHolder extends  RecyclerView.ViewHolder{
            private TextView textItem;

            ViewHolder(View itemView){
                super(itemView);
                textItem = (TextView)itemView.findViewById(R.id.txtItem);

                textItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_content_catlog,parent,false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            holder.textItem.setText(myData.get(position));
        }
        public int getItemCount(){
            return myData.size();
        }
    }
}