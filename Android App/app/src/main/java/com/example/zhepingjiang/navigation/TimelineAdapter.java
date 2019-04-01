package com.example.zhepingjiang.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimelineAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private ArrayList<Map<String, Object>> listItem;

    public TimelineAdapter(Context context, ArrayList<Map<String, Object>> listItem) {
        inflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }

    class Viewholder extends RecyclerView.ViewHolder  {
        private TextView Title, Text;

        public Viewholder(View root) {
            super(root);
            Title = (TextView) root.findViewById(R.id.Itemtitle);
            Text = (TextView) root.findViewById(R.id.Itemtext);

        }

        public TextView getTitle() {
            return Title;
        }

        public TextView getText() {
            return Text;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(inflater.inflate(R.layout.timeline_cell, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Viewholder vh = (Viewholder) holder;
        vh.Title.setText((String) listItem.get(position).get("ItemTitle"));
        vh.Text.setText((String) listItem.get(position).get("ItemText"));
        ((TextView)vh.itemView.findViewById(R.id.ItemDateTime)).setText(
                (String) listItem.get(position).get("ItemDateTime")
        );
        ((TextView)vh.itemView.findViewById(R.id.ItemAction)).setText(
                (String) listItem.get(position).get("ItemAction")
        );
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }


}