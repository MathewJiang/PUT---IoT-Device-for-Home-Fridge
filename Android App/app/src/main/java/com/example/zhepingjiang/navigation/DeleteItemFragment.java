package com.example.zhepingjiang.navigation;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

public class DeleteItemFragment extends Fragment {
    private static final String TAG = "DeleteItemFragment";
    private Button delete_button;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Delete an item");

        final View cur_view = view;
        delete_button = (Button)view.findViewById(R.id.deleteButton);


        delete_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText deleteItemEditText = cur_view.findViewById(R.id.deleteItemEditText);
                String foodName = deleteItemEditText.getText().toString();

                if (foodName == null || foodName.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Input Error");
                    alertDialog.setMessage("Food name cannot be empty");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    Log.i(TAG, "onClick: " + foodName);


                    RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                    String url = "http://ece496puts.ddns.net:59496/raw_sql/" +
                            "use putsDB;" +
                            "delete from std_names where std_name = '" + foodName + "';";

                    Log.i(TAG, "onClick: " + url);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ;//TODO: what do we need to do here?
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setTitle("Internet Error");
                            alertDialog.setMessage("Internet Error: Please check your local network");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });

                    queue.add(stringRequest);

                    //clear the text if user enter some info
                    deleteItemEditText.setText("");

                    //hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(cur_view.getWindowToken(), 0);
                }
            }
        } );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.deleteitem_layout, container, false);
    }
}