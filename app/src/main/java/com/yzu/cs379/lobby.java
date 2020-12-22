package com.yzu.cs379;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class lobby extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView slideRecycle;
    private myListAdapter adapter;
    private slideAdapter adapterslide;
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
        String findStr = myIntent.getExtras().getString("cat");
        myDB = new DBhelper(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        recyclerView = (RecyclerView)findViewById(R.id.list_item);
        slideRecycle = (RecyclerView)findViewById(R.id.slidebar);
        myProgressBar = (ProgressBar)findViewById(R.id.prohressBarLogin);




        toolbar.inflateMenu(R.menu.memu_main);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        slideRecycle.setLayoutManager(new LinearLayoutManager(this));
        slideRecycle.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        List<CatalogClass> db = myDB.getUserAllCatalog(mysnipper.getAccount());
        if (db.size() == 0){
            CatalogClass temp =  new CatalogClass();
            temp.userName = mysnipper.getAccount();
            temp.name = "mylist";
            temp.r = 0xEB;
            temp.g = 0x57;
            temp.b = 0x57;
            myDB.addCatalog(temp);
            db = myDB.getUserAllCatalog(mysnipper.getAccount());
        }
        adapterslide = new slideAdapter(db);
        slideRecycle.setAdapter(adapterslide);
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

        Runnable runnablefind = () -> {
            List<CatalogClass> cat = myDB.getUserAllCatalog(mysnipper.getAccount());
            List<cfpMetaClass> meta = mysnipper.getCFPcatalogPageList(findStr,1);
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
        Runnable runnablemyList = () -> {
            List<CatalogClass> cat = myDB.getUserAllCatalog(mysnipper.getAccount());
            List<cfpMetaClass> meta = mysnipper.getCFPMyListPageList(1);
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

        if(findStr.equals("")){
            Thread  td = new Thread(runnable);
            td.start();
        }else if(findStr.equals("mylist")){
            Thread  td = new Thread(runnablemyList);
            td.start();
        } else{
            Thread td = new Thread(runnablefind);
            td.start();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memu_main, menu);

        return true;
    }

    @Override
    public boolean  onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.my_search)
            Toast.makeText(this,"TEST",Toast.LENGTH_SHORT).show();
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);

        return true;
    }


    public void onClickAdd(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(lobby.this);
        dialog.setTitle("New Categories");
        LayoutInflater lf = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = lf.inflate(R.layout.add_dialog,null);
        dialog.setView(layout);
        AlertDialog ad = dialog.create();

        Button enter = (Button)layout.findViewById(R.id.slidebarAddEnter);
        Button cancel = (Button)layout.findViewById(R.id.slidebarAddCancel);
        SeekBar r = (SeekBar) layout.findViewById(R.id.seek_r);
        SeekBar g = (SeekBar)layout.findViewById(R.id.seek_g);
        SeekBar b = (SeekBar)layout.findViewById(R.id.seek_b);
        TextView name = (TextView)layout.findViewById(R.id.slidebarCata);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogClass temp = new CatalogClass();
                if(name.getText().equals(""))
                    return;
                temp.name = name.getText().toString();
                temp.userName = mysnipper.getAccount();
                temp.r = r.getProgress();
                temp.g = g.getProgress();
                temp.b = b.getProgress();
                myDB.addCatalog(temp);
                adapterslide.addItem(temp);
                ad.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ad.dismiss();

            }
        });



        ad.show();
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
                        if(contentClasses.get(position).catalog.get(i).equals(catalog.get(j).name)){
                            holder.imageView.setColorFilter(Color.rgb(catalog.get(j).r,catalog.get(j).g,catalog.get(j).b));
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

    public class slideAdapter extends RecyclerView.Adapter<slideAdapter.ViewHolder>{
        private List<CatalogClass> catalog;
        slideAdapter( List<CatalogClass> cata){
            catalog = cata;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView eventContent;
            private ImageView imageView;
            private ImageButton deleteImg;
            ViewHolder(View item) {
                super(item);
                eventContent = (TextView)item.findViewById(R.id.slidebarText);
                imageView = (ImageView)item.findViewById(R.id.SlideBarImg);
                deleteImg = (ImageButton) item.findViewById(R.id.deleteImg);

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentToEventContent = new Intent(lobby.this,lobby.class);
                        Bundle bag = new Bundle();
                        bag.putString("token",mysnipper.getToken());
                        bag.putString("account",mysnipper.getAccount());
                        bag.putString("cat",catalog.get(getPosition()).name);
                        intentToEventContent.putExtras(bag);
                        startActivity(intentToEventContent);
                    }
                });
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(catalog.get(getPosition()).name.equals("mylist"))
                            return;
                        myDB.deleteCatalog(mysnipper.getAccount(),catalog.get(getAdapterPosition()).name);
                        catalog.remove(getAdapterPosition());;
                        notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slidebar_item,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position){
            holder.eventContent.setText(catalog.get(position).name);
            holder.imageView.setImageResource(R.drawable.magic_dot);
            holder.imageView.setColorFilter(Color.rgb(catalog.get(position).r,catalog.get(position).g,catalog.get(position).b));

        }
        public  int getItemCount(){
            return  catalog.size();
        }
        public void addItem(CatalogClass item){
            catalog.add(item);
            notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }

    }

}