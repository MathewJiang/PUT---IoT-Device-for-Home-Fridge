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
        final View cur_view = view;

        //Request towards the server
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        String url = "http://ece496puts.ddns.net:59496/raw_sql_br/use putsDB;select uid, std_name, expiry_date, status from grocery_storage;";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
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
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Create new rows
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            parentLinearLayout = cur_view.findViewById(R.id.display_parent_layout);

            String[][] results =  strParseHelp(f_dbResult);
            int i;
            for (String[] result : results) {
                final View rowView = inflater.inflate(R.layout.field, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

                TextView UIDText = (TextView) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.uid_text_view);
                UIDText.setText(result[0]);

                int len = result.length;
                if (len > 1) {
                    TextView nameText = (TextView) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.name_text_view);
                    nameText.setText(result[1]);
                }

                if (len > 2) {
                    TextView dateText = (TextView) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.date_text_view);
                    dateText.setText(result[2]);
                }

                if (len > 3) {
                    TextView statusText = (TextView) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.status_text_view);
                    statusText.setText(result[3]);
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
                        }, error -> textView.setText("Deletion failed\n Please check the network"));

                        queue1.add(stringRequest1);
                        parentLinearLayout.removeView((View) v.getParent());
                    });
            }
        }, error -> textView.setText("No response from the server\n Please check the network"));

        queue.add(stringRequest);

//        //Create new rows
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        parentLinearLayout = view.findViewById(R.id.display_parent_layout);
//
//        int i = 0;
//        for (i = 0; i < num_row; i++) {
//            final View rowView = inflater.inflate(R.layout.field, null);
//            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
//            Button rowButton1 = (Button) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1).findViewById(R.id.delete_button);
//            rowButton1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i(TAG, "onClick: " + "rowButton");
//                    parentLinearLayout.removeView((View) v.getParent());
//                }
//            });
//        }


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
}
