package com.example.rahultasker.brewme;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static com.example.rahultasker.brewme.SavedActivity.adapter;

public class SavedBrewAdapter extends RecyclerView.Adapter<SavedBrewAdapter.ViewHolder> {

    Context mContext;
    List<SavedBrewRecord> items;
    SavedDatabase db;

    public SavedBrewAdapter(List<SavedBrewRecord> items) {
        this.items = items;
    }

    @Override
    public SavedBrewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_saved, null);
        SavedBrewAdapter.ViewHolder holder = new SavedBrewAdapter.ViewHolder(view);
        mContext = parent.getContext();
        db = Room.databaseBuilder(mContext, SavedDatabase.class, SavedDatabase.NAME).build();
        return holder;
    }

    @Override
    public void onBindViewHolder(final SavedBrewAdapter.ViewHolder holder, final int position) {
        holder.name.setText(" " + items.get(position).getName());
        holder.address.setText(" " + items.get(position).getStreet() + ", " + items.get(position).getCity()
                + ", " + items.get(position).getState() + " " + items.get(position).getPostal_code() + " " + items.get(position).getCountry());


        final int index = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String final_url = items.get(position).getWebsite_url();
                Uri uri = Uri.parse(final_url);
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                new  deleteSaved(db, SavedActivity.recyclerView, adapter,  mContext ,  items, index).execute(Integer.toString(items.get(index).getId()));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else
            return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView address;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            address = itemView.findViewById(R.id.txtTitle);


        }
    }

}
