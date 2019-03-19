package com.example.zhepingjiang.navigation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhepingjiang.db.ConsumptionHistory;
import com.example.zhepingjiang.db.DBAccess;
import com.example.zhepingjiang.db.GroceryStorage;
import com.example.zhepingjiang.db.PurchaseHistory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DisplayItemFragment extends Fragment {
    private final String TAG = DisplayItemFragment.class.getSimpleName();
    public String f_dbResult;
    public Handler handler;

    // DB results
    Map<Integer, PurchaseHistory> uidToPurchaseHistories = null;
    Map<Integer, GroceryStorage> uidToGroceryStorages = null;
    Map<Integer, List<ConsumptionHistory>> uidToConsumptionHistories = null;

    // DB load flags
    BooleanSupplier isPurchaseHistoryLoaded = () -> (uidToPurchaseHistories != null);
    BooleanSupplier isGroceryStorageLoaded = () -> (uidToGroceryStorages != null);
    BooleanSupplier isConsumptionHistoryLoaded = () -> (uidToConsumptionHistories != null);
    BooleanSupplier isEverythingLoaded = () -> (isPurchaseHistoryLoaded.getAsBoolean()
            && isGroceryStorageLoaded.getAsBoolean()
            && isConsumptionHistoryLoaded.getAsBoolean());

    int selectedUid = -1;


    LayoutInflater f_inflater;
    ViewGroup f_container;
    private LinearLayout parentLinearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        f_inflater = inflater;
        f_container = container;
        return inflater.inflate(R.layout.displayitem_layout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("All items");

        // final TextView textView = view.findViewById(R.id.displayAllText);
        final View cur_view = view;

        //Request towards the server
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        String groceryStorageUrl = DBAccess.GetQueryLink(GroceryStorage.GetSelectAllQueryStatic());
        String purchaseHistoryUrl = DBAccess.GetQueryLink(PurchaseHistory.GetSelectAllQueryStatic());
        String consumptionHistoryUrl = DBAccess.GetQueryLink(ConsumptionHistory.GetSelectAllQueryStatic());

        StringRequest groceryStorageRequest = new StringRequest(Request.Method.GET, groceryStorageUrl, response -> {
            f_dbResult = response;
            Log.i(TAG, "onResponse: " + f_dbResult);

//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    if (f_dbResult != null) {
//                        f_dbResult = f_dbResult.substring(f_dbResult.indexOf("<h1>") + 4, f_dbResult.indexOf("</h1>"));
//                    }
//                }
//            });
//
//            t.start();
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //Create new rows
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            parentLinearLayout = cur_view.findViewById(R.id.display_parent_layout);

            Set<GroceryStorage> groceryRecords = GroceryStorage.FromHTMLTableStr(response);
            // String[][] results =  strParseHelp(f_dbResult);
            int i;
            for (GroceryStorage record : groceryRecords) {
                final View rowView = inflater.inflate(R.layout.field, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

                TextView UIDText = (TextView) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.uid_text_view);
                UIDText.setText(String.valueOf(record.getUid()));
                LinearLayout childLayout = (LinearLayout) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1);
                setAhahaOnClickListener(UIDText, record.getUid(), cur_view);

                //int len = result.length;
                if (true) {
                    TextView nameText = (TextView) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.name_text_view);
                    nameText.setText(record.getStdName().getStdName());
                    setAhahaOnClickListener(nameText, record.getUid(), cur_view);
                }

                if (true) {
                    TextView dateText = (TextView) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.date_text_view);
                    dateText.setText(record.getExpiryDate());
                    setAhahaOnClickListener(dateText, record.getUid(), cur_view);
                }

                if (true) {
                    TextView statusText = (TextView) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.status_text_view);
                    statusText.setText(record.getStatus().getStatus());
                    setAhahaOnClickListener(statusText, record.getUid(), cur_view);
                }

                Button rowButton1 = (Button) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.delete_button);
                    rowButton1.setOnClickListener(v -> {
                        Log.i(TAG, "onClick: " + "deleteButton: " + v.toString());

                        RequestQueue queue1 = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                        String cur_uid = ((TextView)((View)v.getParent()).findViewById(R.id.uid_text_view)).getText().toString();
                        String url1 = "http://ece496puts.ddns.net:59496/raw_sql_br/use putsDB;delete from grocery_storage where uid=" + cur_uid + ";";
                        Log.i(TAG, "onClick: " + "url for deletion is: " + url1);
                        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response1) {
                                Log.i(TAG, "onResponse: " + "Deletion successful!\n");
//                                        textView.setText("Deletion successful\n");
                            }
                        }, error -> Toast.makeText(getActivity(),"Deletion failed\n Please check the network", Toast.LENGTH_LONG).show());

                        queue1.add(stringRequest1);
                        parentLinearLayout.removeView((View) v.getParent());
                    });
            }
            uidToGroceryStorages = groceryRecords.stream().collect(Collectors.toMap(gr->gr.getUid(), gr->gr));
        }, error -> Toast.makeText(getActivity(), "No response from the server\n Please check the network", Toast.LENGTH_LONG).show());
        queue.add(groceryStorageRequest);

        // Now query all purchase histories
        StringRequest purchaseHistoryRequest = new StringRequest(Request.Method.GET, purchaseHistoryUrl, response -> {
            Set<PurchaseHistory> purchaseHistories = PurchaseHistory.FromHTMLTableStr(response);
            uidToPurchaseHistories = purchaseHistories.stream().collect(Collectors.toMap(ph->ph.getUid(), ph->ph));
        }, error -> Toast.makeText(getActivity(), "No response from the server\n Please check the network", Toast.LENGTH_LONG).show());
        queue.add(purchaseHistoryRequest);

        // Now query all consumption histories
        StringRequest consumptionHistoryRequest = new StringRequest(Request.Method.GET, consumptionHistoryUrl, response -> {
            Set<ConsumptionHistory> consumptionHistories = ConsumptionHistory.FromHTMLTableStr(response);
            uidToConsumptionHistories = consumptionHistories.stream()
                    .sequential()
                    .sorted(Comparator.comparing(ConsumptionHistory::getTimeStamp).reversed())
                    .collect(Collectors.groupingBy(ConsumptionHistory::getUid));
        }, error -> Toast.makeText(getActivity(), "No response from the server\n Please check the network", Toast.LENGTH_LONG).show());
        queue.add(consumptionHistoryRequest);
        
    }


    private String[][] strParseHelp(String input) {
        String[] input_str_line = input.split("<br>");


        int input_str_line_size = input_str_line.length;
        String[][] output = new String[input_str_line_size][];

        int i = 0;
        for (String input_str : input_str_line) {
            input_str = input_str.replaceFirst("\\|", "");
            input_str = input_str.replaceAll(" ", "");
            output[i] = input_str.split("\\|");
            i++;
        }

        return output;
    }

    private void setAhahaOnClickListener(View view, int uid, View topLevelView) {
        view.setOnClickListener(v -> {
            if (!isEverythingLoaded.getAsBoolean()) {
                return;
            }
            // Populate fields
            PurchaseHistory ph = uidToPurchaseHistories.getOrDefault(uid, null);
            TextView name_text = (TextView) (topLevelView.findViewById(R.id.item_name_text_view));
            TextView remaining_quantity_text = (TextView) (topLevelView.findViewById(R.id.remaining_quantity_text_view));
            TextView purchased_date_text = (TextView) (topLevelView.findViewById(R.id.purchase_date_text_view));
            TextView expiry_date_text = (TextView) (topLevelView.findViewById(R.id.expiry_date_text_view));

            if (ph != null) {
                GroceryStorage gs = uidToGroceryStorages.get(uid);
                name_text.setText("Name: " + ph.getStdName().getStdName());
                remaining_quantity_text.setText("Remaining Quantity: " + gs.getContentQuantity() + " " + gs.getContentUnit().getUnit());
                purchased_date_text.setText("Purchase Date: " + ph.getPurchaseDate());
                expiry_date_text.setText("Expiry Date: " + ph.getExpiryDate());
            } else {
                name_text.setText("Name: Unknown");
                remaining_quantity_text.setText("Remaining Quantity: Unknown");
                purchased_date_text.setText("Purchase Date: Unknown");
                expiry_date_text.setText("Expiry Date: Unknown");
            }

            // populate consumption history
            ListView consumption_history_list_view = topLevelView.findViewById(R.id.consumption_history_list_view);
            List<ConsumptionHistory> ch = uidToConsumptionHistories.getOrDefault(uid, Lists.newArrayList());
            List<Map<String, String>> chContent = ch.stream().map(ch_entry -> {
                Map<String, String> entryMap = Maps.newHashMap();
                entryMap.put("Opid", String.valueOf(ch_entry.getOpid()));
                entryMap.put("Quantity", String.valueOf(ch_entry.getConsumedQuantity()) + " " + ph.getContentUnit().getUnit());
                entryMap.put("Action", ch_entry.getAction().getUserAction());
                entryMap.put("Timestamp", ch_entry.getTimeStamp());
                return entryMap;
            }).collect(Collectors.toList());
            consumption_history_list_view.setAdapter(new ConsumptionHistoryAdapter(getActivity(), chContent));

            selectedUid = uid;
        });
    }
}
