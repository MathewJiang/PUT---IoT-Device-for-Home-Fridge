package com.example.zhepingjiang.navigation;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Range;
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
import com.example.zhepingjiang.db.ConsumptionHistory;
import com.example.zhepingjiang.db.DBAccess;
import com.example.zhepingjiang.db.GroceryStorage;
import com.example.zhepingjiang.db.PurchaseHistory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;

// Heuristic of Timeline widget Ref: https://www.jianshu.com/p/655ea359e3db
public class TimelineFragment extends Fragment {
    private static final String TAG = "TimelineFragment";
    private RecyclerView rv;
    private ArrayList<Map<String,Object>> listItem;
    private TimelineAdapter timelineAdapter;

    @Data
    @AllArgsConstructor
    private class TimeLineItem {
        private String action;
        private String name;
        private DateTime timeStamp;
        private int quantity;
        private String unit;
    }

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Grocery Timeline");

        //Request towards the server
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        String groceryStorageUrl = DBAccess.GetQueryLink(GroceryStorage.GetSelectAllQueryStatic());
        String purchaseHistoryUrl = DBAccess.GetQueryLink(PurchaseHistory.GetSelectAllQueryStatic());
        String consumptionHistoryUrl = DBAccess.GetQueryLink(ConsumptionHistory.GetSelectAllQueryStatic());

        // First query all grocery records
        StringRequest groceryStorageRequest = new StringRequest(Request.Method.GET, groceryStorageUrl, response -> {
            uidToGroceryStorages = GroceryStorage.FromHTMLTableStr(response).stream()
                    .collect(Collectors.toMap(gr -> gr.getUid(), gr -> gr));
            checkDataLoaded();
        }, error -> {});
        queue.add(groceryStorageRequest);
        // Then query all purchase histories
        StringRequest purchaseHistoryRequest = new StringRequest(Request.Method.GET, purchaseHistoryUrl, pResponse -> {
            Set<PurchaseHistory> purchaseHistories = PurchaseHistory.FromHTMLTableStr(pResponse);
            uidToPurchaseHistories = purchaseHistories.stream().collect(Collectors.toMap(ph->ph.getUid(), ph->ph));
            checkDataLoaded();
        }, error -> {});
        queue.add(purchaseHistoryRequest);
        // Then query all consumption histories
        StringRequest consumptionHistoryRequest = new StringRequest(Request.Method.GET, consumptionHistoryUrl, cResponse -> {
            Set<ConsumptionHistory> consumptionHistories = ConsumptionHistory.FromHTMLTableStr(cResponse);
            uidToConsumptionHistories = consumptionHistories.stream()
                    .sequential()
                    .sorted(Comparator.comparing(ConsumptionHistory::getTimeStamp).reversed())
                    .collect(Collectors.groupingBy(ConsumptionHistory::getUid));
            checkDataLoaded();
        }, error -> {});
        queue.add(consumptionHistoryRequest);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timeline_layout, container, false);
    }


    public void checkDataLoaded() {
        if (!isEverythingLoaded.getAsBoolean()) return;
        initData();
        initView();
    }

    public void initData(){
        // process ch and ph into timelineItem objects
        Set<TimeLineItem> tlItems = Sets.newHashSet();
        tlItems.addAll(uidToConsumptionHistories.values().stream()
                .flatMap(i -> i.stream())
                .map(ch -> new TimeLineItem(ch.getAction().getUserAction(), ch.getStdName().getStdName(),
                        DateTime.parse(ch.getTimeStamp(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")),
                        ch.getConsumedQuantity(),
                        uidToPurchaseHistories.get(ch.getUid()).getContentUnit().getUnit()))
                .collect(Collectors.toSet()));

        tlItems.addAll(uidToPurchaseHistories.values().stream()
                .map(ph -> new TimeLineItem("purchase", ph.getStdName().getStdName(),
                        DateTime.parse(ph.getPurchaseDate(), DateTimeFormat.forPattern("yyyy-MM-dd")),
                        ph.getContentQuantity(),
                        ph.getContentUnit().getUnit()))
                .collect(Collectors.toSet()));

        listItem = Lists.newArrayList(tlItems.stream()
                .sorted((i1, i2) -> DateTimeComparator.getInstance().reversed().compare(i1.getTimeStamp(), i2.getTimeStamp()))
                .map(i -> ImmutableMap.<String, Object>of(
                        "ItemTitle", i.getName(),
                        "ItemText", i.getAction() + ": " + String.valueOf(i.getQuantity()) + " " + i.getUnit(),
                        "ItemAction", i.getAction(),
                        "ItemDateTime", i.getTimeStamp().toString("yyyy-MM-dd HH:mm:ss")))
                .collect(Collectors.toList()));
    }

    public void initView(){
        rv = getView().findViewById(R.id.timeline_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        rv.addItemDecoration(new TimelineItemDecoration(requireContext()));

        timelineAdapter = new TimelineAdapter(requireContext(), listItem);
        rv.setAdapter(timelineAdapter);
    }
}