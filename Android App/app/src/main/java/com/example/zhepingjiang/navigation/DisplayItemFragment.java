package com.example.zhepingjiang.navigation;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

public class DisplayItemFragment extends Fragment {
    private final String TAG = DisplayItemFragment.class.getSimpleName();
    public String dbResult;
    public Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.displayitem_layout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("All items");

        final TextView textView = view.findViewById(R.id.displayAllText);

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        String url = "http://ece496puts.ddns.net:59496/raw_sql/use putsDB;select std_name from purchase_history;";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                textView.setText("response is: " + response);
                dbResult = response;
                Log.i(TAG, "onResponse: " + dbResult);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (dbResult != null) {
                            dbResult = dbResult.substring(dbResult.indexOf("<h1>") + 4, dbResult.indexOf("</h1>"));
                        }

                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(dbResult);
                            }
                        });
                    }
                });

                t.start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Not working");
            }
        });

        queue.add(stringRequest);


//        handler = new Handler(getContext().getMainLooper());
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                    dbResult = ServerRequestHandler.getAlltemp();
//                    Log.i(TAG, "The value for dbResult is: " + dbResult);
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setText("reached");
//                        }
//                    });
//            }
//        }).start();
        
    }
}
