package com.yzu.cs379;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class lobby extends AppCompatActivity {
    private RecyclerView recyclerView;
    private myListAdapter adapter;
    private snipper mysnipper;
    private ProgressBar myProgressBar;
    private DBhelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Intent myIntent = getIntent();
        mysnipper = new snipper();
        mysnipper.setting(myIntent.getExtras().getString("token"),myIntent.getExtras().getString("account"));
        myDB = new DBhelper(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        recyclerView = (RecyclerView)findViewById(R.id.list_item);
        myProgressBar = (ProgressBar)findViewById(R.id.prohressBarLogin);




        toolbar.inflateMenu(R.menu.memu_main);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        myProgressBar.setVisibility(View.VISIBLE);

        Runnable runnable = () -> {
            List<CatalogClass> cat = myDB.getUserAllCatalog(mysnipper.getAccount());
            List<cfpMetaClass> meta = mysnipper.getCFPMainPageList();
            List<eventContentClass> event = new ArrayList<>();
            for(int i = 0 ; i < meta.size() ; i++){
                event.add(mysnipper.getEventContent(meta.get(i).Link));
            }


            adapter = new myListAdapter(meta,event,cat);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(adapter);
                }
            });

            myProgressBar.post(new Runnable() {
                @Override
                public void run() {
                    myProgressBar.setVisibility(View.GONE);
                }
            });
        };

        Thread  td = new Thread(runnable);
        td.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memu_main, menu);

        return true;
    }

    @Override
    public boolean  onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.my_search){
            Toast.makeText(this,"TEST",Toast.LENGTH_SHORT).show();

        }else if(item.getItemId() == R.id.reflash){
            adapter = null;
            myProgressBar.setVisibility(View.VISIBLE);
            Runnable runnable = () -> {
                List<cfpMetaClass> meta = mysnipper.getCFPMainPageList();
                List<eventContentClass> event = new ArrayList<>();
                for(int i = 0 ; i < meta.size() ; i++){
                    event.add(mysnipper.getEventContent(meta.get(i).Link));
                }
                List<CatalogClass> cat = myDB.getUserAllCatalog(mysnipper.getAccount());

                adapter = new myListAdapter(meta,event,cat);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });

                myProgressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        myProgressBar.setVisibility(View.GONE);
                    }
                });
            };

            Thread  td = new Thread(runnable);
            td.start();

        }
        return true;
    }


    public class myListAdapter extends RecyclerView.Adapter<myListAdapter.ViewHolder> {
        private List<cfpMetaClass> myData;
        private List<eventContentClass> contentClasses;
        private List<CatalogClass> catalog;
        myListAdapter(List<cfpMetaClass> input ,List<eventContentClass> content , List<CatalogClass> cata){
            myData = input;
            contentClasses = content;
            catalog = cata;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView eventName;
            private TextView when;
            private TextView where;
            private TextView eventContent;
            private ImageView imageView;

            ViewHolder(View item){
                super(item);
                eventName = (TextView)item.findViewById(R.id.event_name);
                when = (TextView)item.findViewById(R.id.when);
                where = (TextView)item.findViewById(R.id.where);
                eventContent = (TextView)item.findViewById(R.id.event_content);
                imageView = (ImageView) item.findViewById(R.id.imageView);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = getAdapterPosition();
                        Intent intent = new Intent(lobby.super.getBaseContext(),event_content.class);
                        Bundle bag = new Bundle();
                        bag.putString("token",mysnipper.getToken());
                        bag.putString("account",mysnipper.getAccount());
                        bag.putString("link",myData.get(i).Link);
                        intent.putExtras(bag);
                        startActivity(intent);
                    }
                });
            }


        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card,parent,false);

            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position){
            holder.eventName.setText(myData.get(position).Event);
            holder.eventContent.setText(myData.get(position).Brief);
            holder.when.setText(myData.get(position).When);
            holder.where.setText(myData.get(position).Where);

            holder.imageView.setImageResource(R.drawable.magic_dot);
            if(myData.get(position).inMyList){
                holder.imageView.setColorFilter(Color.rgb(0xEB,0x57,0x57));
                holder.imageView.setVisibility(View.VISIBLE);
            }else{
                holder.imageView.setVisibility(View.INVISIBLE);
                boolean complete = false;
                for(int i = 0 ; i < contentClasses.get(position).catalog.size() ; i++){
                    for(int j = 0 ; j < catalog.size() ; j++){
                        if(contentClasses.get(position).catalog.get(i).equals(catalog.get(j))){
                            holder.imageView.setColorFilter(Color.rgb(catalog.get(i).r,catalog.get(i).g,catalog.get(i).b));
                            holder.imageView.setVisibility(View.VISIBLE);
                            complete = true;
                        }
                        if(complete)
                            break;
                    }
                    if(complete)
                        break;
                }

            }

            //holder.imageView.setImageDrawable(R.drawable.ic_notification_overlay);
        }
        @Override
        public  int getItemCount(){
            return  myData.size();
        }
    }

}