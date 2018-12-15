package com.example.rahultasker.brewme;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class displayAll extends AsyncTask<Void,Void,List<BrewRecord>>{
    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;
    List<BrewRecord> items;

    public displayAll(AppDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context ) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }


    @Override
    protected List<BrewRecord> doInBackground(Void... m) {

        items = db.databaseInterface().getAllItems();
        System.out.println("in displayAll item size: " + items.size() );

        return items;
    }

    @Override
    protected void onPostExecute(List<BrewRecord> s) {
        super.onPostExecute(s);
        adapter= new UserAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        MainActivity.items = items;
    }
}
