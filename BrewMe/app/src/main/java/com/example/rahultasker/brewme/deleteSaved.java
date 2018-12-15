package com.example.rahultasker.brewme;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class deleteSaved extends AsyncTask<String,Void,List<SavedBrewRecord>> {
    private SavedDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;
    String brewID;
    List<SavedBrewRecord> items;
    int index;

    public deleteSaved(SavedDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context , List<SavedBrewRecord> items, int index) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
        this.items = items;
        this.index = index;
    }

    @Override
    protected List<SavedBrewRecord> doInBackground(String... params) {
        brewID  = params[0];
        db.savedDatabaseInterface().deleteItems(brewID);
        return items;
    }

    @Override
    protected void onPostExecute(List<SavedBrewRecord> s) {
        super.onPostExecute(s);
        if(items.size() == 1) {
            items = new ArrayList<>();
        } else {
            items.remove(items.get(index));
        }
        adapter= new SavedBrewAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}