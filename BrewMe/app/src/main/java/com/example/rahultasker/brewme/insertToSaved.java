package com.example.rahultasker.brewme;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class insertToSaved extends AsyncTask<List<SavedBrewRecord>,Void,List<SavedBrewRecord>> {

    private SavedDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;
    List<SavedBrewRecord> items;

    public insertToSaved(SavedDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context ) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }


    @Override
    protected List<SavedBrewRecord> doInBackground(List<SavedBrewRecord>... params) {
        items = params[0];
        for (int i =0; i< items.size(); i++) {
            db.savedDatabaseInterface().insertAll(items.get(i));
        }
        System.out.println("item size: " + items.size() );
        return items;
    }

    @Override
    protected void onPostExecute(List<SavedBrewRecord> s) {
        super.onPostExecute(s);
        Toast.makeText(context, "saved item", Toast.LENGTH_LONG).show();
    }
}

