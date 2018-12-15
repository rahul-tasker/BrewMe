package com.example.rahultasker.brewme;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context mContext;
    List<BrewRecord> items;
    AppDatabase db;
    BrewRecord fred;

    public UserAdapter(List<BrewRecord> items) {
        this.items = items;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
        ViewHolder holder = new ViewHolder(view);
        mContext = parent.getContext();
        db = Room.databaseBuilder(mContext, AppDatabase.class,AppDatabase.NAME).build();
        return holder;
    }

    @Override
    public void onBindViewHolder(final UserAdapter.ViewHolder holder, final int position) {
        holder.name.setText(" " + items.get(position).getName());
        holder.type.setText(" " + items.get(position).getBrewery_type());


        final int index = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expanded_text.getVisibility() == View.VISIBLE)
                {
                    holder.expanded_text.setVisibility(View.GONE); //this is used for shrinking
                    holder.expanded_text.setEnabled(false);
                    holder.expanded_text.setText("");
                    holder.phone_text.setVisibility(View.GONE); //this is used for shrinking
                    holder.phone_text.setEnabled(false);
                    holder.phone_text.setText("");
                }
                else
                {
                    holder.expanded_text.setVisibility(View.VISIBLE);  //this is used for expanding
                    holder.expanded_text.setEnabled(true);
                    holder.expanded_text.setText(" " + items.get(position).getStreet() + ", " + items.get(position).getCity()
                            + ", " + items.get(position).getState() + " " + items.get(position).getPostal_code() + " " + items.get(position).getCountry());
                    if(items.get(position).getPhone() != null || items.get(position).getPhone().equals("")) {
                        holder.phone_text.setVisibility(View.VISIBLE);  //this is used for expanding
                        holder.phone_text.setEnabled(true);
                        holder.phone_text.setText(setFormat(items.get(position).getPhone()));
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                items.get(index);
                fred = items.get(index);
                Intent map = new Intent(mContext, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("brewery",items.get(index));
                map.putExtras(bundle);
                String m = "latlon" + items.get(index).getLatitude() + " " + items.get(index).getLongitude();
                mContext.startActivity(map);
                return true;
            }
        });
    }

    public String setFormat(String str) {
        String outStr = "";
        int count = 0;
        for (char ch : str.toCharArray()){
            if (count == 0) {
                outStr += "(";
            }
            outStr += ch;
            count ++;
            if(count == 3) {
                outStr += ") ";
            }
            else if(count == 6) {
                outStr += " - ";
            }
        }
        return outStr;
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
        public TextView expanded_text;
        public TextView phone_text;
        public TextView type;
        public LinearLayout hidden_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_Name);
            type = itemView.findViewById(R.id.txt_Title);

            hidden_layout=(LinearLayout)itemView.findViewById(R.id.hidden_layout);
            expanded_text= itemView.findViewById(R.id.expanded_text);
            phone_text = itemView.findViewById(R.id.phone_text);
        }
    }
}
