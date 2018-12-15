package com.example.rahultasker.brewme;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;


public class SavedActivity extends AppCompatActivity {

    private BrewRecord br;
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public Context context;
    public SavedDatabase db;
    public static List<SavedBrewRecord> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Saved");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter= new SavedBrewAdapter(items);
        context = getApplicationContext();
        db = Room.databaseBuilder(context, SavedDatabase.class, db.NAME).fallbackToDestructiveMigration().build();
        new savedDisplayAll(db, recyclerView, adapter, context).execute();


    }

    @Override
    protected void onStop(){
        super.onStop();
        db.close();
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        db.close();
    }

}
