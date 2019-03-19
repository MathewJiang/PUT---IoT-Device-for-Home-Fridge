package com.example.zhepingjiang.navigation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ConsumptionHistoryAdapter extends BaseAdapter {
    private Activity activity;
    private List<Map<String, String>> chContents;

    public ConsumptionHistoryAdapter(Activity activity, List<Map<String, String>> chContents) {
        super();
        this.activity = activity;
        this.chContents = chContents;
    }

    @Override
    public int getCount() {
        return chContents.size();
    }

    @Override
    public Object getItem(int position) {
        return chContents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView firstCol;
        TextView secondCol;
        TextView thirdCol;
        TextView fourthCol;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.ch_col, null);
            holder=new ViewHolder();

            holder.firstCol=(TextView) convertView.findViewById(R.id.ch_opid);
            holder.secondCol=(TextView) convertView.findViewById(R.id.ch_quantity);
            holder.thirdCol=(TextView) convertView.findViewById(R.id.ch_action);
            holder.fourthCol=(TextView) convertView.findViewById(R.id.ch_timestamp);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        Map<String, String> map = chContents.get(position);
        holder.firstCol.setText(map.get("Opid"));
        holder.secondCol.setText(map.get("Quantity"));
        holder.thirdCol.setText(map.get("Action"));
        holder.fourthCol.setText(map.get("Timestamp"));

        return convertView;
    }

}