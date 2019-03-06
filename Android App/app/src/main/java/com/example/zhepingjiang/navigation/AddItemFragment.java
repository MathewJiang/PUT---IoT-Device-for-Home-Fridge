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
import com.example.zhepingjiang.db.Brands;
import com.example.zhepingjiang.db.Categories;
import com.example.zhepingjiang.db.ContentUnits;
import com.example.zhepingjiang.db.DBAccess;
import com.example.zhepingjiang.db.GroceryStorage;
import com.example.zhepingjiang.db.PackageUnits;
import com.example.zhepingjiang.db.PurchaseHistory;
import com.example.zhepingjiang.db.Statuses;
import com.example.zhepingjiang.db.StdNames;
import com.example.zhepingjiang.db.Vendors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddItemFragment extends Fragment {
    private static final String TAG = "AddItemFragment";
    private Button add_button;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Add an item");

        final View cur_view = view;
        add_button = (Button)view.findViewById(R.id.addButton);

        //TODO: Check for the startdate automatically for the user
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText enterFoodNameEditText = cur_view.findViewById(R.id.enterFoodEditText);
                String foodName = enterFoodNameEditText.getText().toString();
                EditText enterDurationEditText = cur_view.findViewById(R.id.enterDurationEditText);
                String duration = enterDurationEditText.getText().toString();
                EditText enterEndDateEditText = cur_view.findViewById(R.id.enterEndDateEditText);
                String endDate = enterEndDateEditText.getText().toString();

                if (foodName == null || foodName.isEmpty()
                        || (duration == null || duration.isEmpty()
                            && (endDate == null || endDate.isEmpty()))) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Input Error");
                    alertDialog.setMessage("Missing mandatory fields");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    Log.i(TAG, "onClick: " + foodName);

                    // get the current Date
                    Calendar calendar = Calendar.getInstance();
                    Date curDate = calendar.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String startDate=dateFormat.format(curDate);

                    // compute the exipration date if only duration is given
                    if (duration != null && !duration.isEmpty()) {
                        Integer durationInt = Integer.parseInt(duration);

                        calendar.setTime(curDate);
                        calendar.add(Calendar.DATE, durationInt);
                        endDate = dateFormat.format(calendar.getTime());

                        calendar.setTime(curDate);
                    }


                    RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                    String url = "http://ece496puts.ddns.net:59496/raw_sql/" +
                            "use putsDB;" +
                            "insert into std_names values ('" + foodName + "', 'uncategorized');" +
                            "insert into purchase_history (std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date) " +
                            "VALUES ('" + foodName + "', 'default', 'default', '1', 'items', 'T', 'box', '" + startDate + "', '" + endDate + "');" +
                            "insert into grocery_storage VALUES (LAST_INSERT_ID(), '" + foodName + "', 1, 'items', '2019-01-04 00:16:32', '" + startDate +"', '" + endDate + "', 'good');";

                    Log.i(TAG, "onClick: " + url);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

//                    // Example construct of Business Objects
//                    final PurchaseHistory purchaseHistory = new PurchaseHistory(new StdNames(foodName));
//                    purchaseHistory.getStdName().setCategory(new Categories("drink"));
//                    purchaseHistory.setBrand(new Brands("Dole"));
//                    purchaseHistory.setVendor(new Vendors("Loblaws"));
//                    purchaseHistory.setContentQuantity(1000);
//                    purchaseHistory.setContentUnit(new ContentUnits("ml"));
//                    purchaseHistory.setPackaged(true);
//                    purchaseHistory.setPackageUnit(new PackageUnits("box"));
//                    purchaseHistory.setPurchaseDate("2019-01-08");
//                    purchaseHistory.setExpiryDate("2019-01-22");
//
//                    // UID unknown for now, next query will replace UID with LAST_INSERT_ID().
//                    final Statuses itemStatus = new Statuses("unopened");
//                    final String lastUpdatedTimeStamp = "2019-01-09 19:26:37";
//                    final GroceryStorage groceryStorage = new GroceryStorage(purchaseHistory, itemStatus, lastUpdatedTimeStamp);
//
//                    final String insertQueryStr = purchaseHistory.getInsertQuery() + groceryStorage.getInsertQuery();
//                    final String queryUrl = DBAccess.GetQueryLink(insertQueryStr);
//
//                    Log.i(TAG, "onClick: " + queryUrl);
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, queryUrl, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            ;//TODO: what do we need to do here?
                        }
                    }, error -> {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Internet Error");
                        alertDialog.setMessage("Internet Error: Please check your local network");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                (dialog, which) -> dialog.dismiss());
                        alertDialog.show();
                    });

                    queue.add(stringRequest);

                    //clear the text if user enter some info
                    enterFoodNameEditText.setText("");
                    enterEndDateEditText.setText("");
                    enterDurationEditText.setText("");

                    //hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(cur_view.getWindowToken(), 0);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.additem_layout, container, false);
    }
}
