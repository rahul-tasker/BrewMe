package com.example.rahultasker.brewme;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class savedDisplayAll extends AsyncTask<Void,Void,List<SavedBrewRecord>>{
    private SavedDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;  //done as a hack to get app context
    List<SavedBrewRecord> items;

    public savedDisplayAll(SavedDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context ) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }


    @Override
    protected List<SavedBrewRecord> doInBackground(Void... m) {
        items = db.savedDatabaseInterface().getAllItems();
        return items;
    }

    @Override
    protected void onPostExecute(List<SavedBrewRecord> s) {
        super.onPostExecute(s);
        adapter= new SavedBrewAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
