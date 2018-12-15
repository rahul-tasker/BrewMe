package com.example.rahultasker.brewme;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class loadDataBase extends AsyncTask<List<BrewRecord>,Void,List<BrewRecord>> {

    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;  //done as a hack to get app context
    List<BrewRecord> items;

    public loadDataBase(AppDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context ) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }


    @Override
    protected List<BrewRecord> doInBackground(List<BrewRecord>... params) {
        items = params[0];
        db.databaseInterface().dropTheTable();
        for (int i =0; i< items.size(); i++) {
            db.databaseInterface().insertAll(items.get(i));
        }
        System.out.println("item size: " + items.size() );
        return items;
    }

    @Override
    protected void onPostExecute(List<BrewRecord> s) {
        super.onPostExecute(s);
        adapter= new UserAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}

