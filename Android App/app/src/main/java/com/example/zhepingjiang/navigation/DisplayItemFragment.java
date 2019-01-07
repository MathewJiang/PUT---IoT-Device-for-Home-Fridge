package com.example.zhepingjiang.navigation;

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
    public String f_dbResult;
    public Handler handler;

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

        final TextView textView = view.findViewById(R.id.displayAllText);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentLinearLayout = view.findViewById(R.id.display_parent_layout);
        final View rowView = inflater.inflate(R.layout.field, null);
        final View rowView2 = inflater.inflate(R.layout.field, null);
        final View rowView3 = inflater.inflate(R.layout.field, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
        Button rowButton1 = (Button) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.delete_button);
        rowButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + "rowButton1");
                parentLinearLayout.removeView((View) v.getParent());
            }
        });
        parentLinearLayout.addView(rowView2, parentLinearLayout.getChildCount());
        parentLinearLayout.addView(rowView3, parentLinearLayout.getChildCount());

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        String url = "http://ece496puts.ddns.net:59496/raw_sql/use putsDB;select std_name from purchase_history;";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                f_dbResult = response;
                Log.i(TAG, "onResponse: " + f_dbResult);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (f_dbResult != null) {
                            f_dbResult = f_dbResult.substring(f_dbResult.indexOf("<h1>") + 4, f_dbResult.indexOf("</h1>"));
                        }

                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(f_dbResult);
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
//                    f_dbResult = ServerRequestHandler.getAlltemp();
//                    Log.i(TAG, "The value for f_dbResult is: " + f_dbResult);
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
