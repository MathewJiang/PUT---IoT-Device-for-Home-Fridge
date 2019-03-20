package com.example.zhepingjiang.navigation;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogActionDialogFragment extends DialogFragment {

    private EditText actionQuantity;
    private int uid;
    private int remainingQuantity;
    private String name;
    private String contentUnit;
    private String action;

    public interface LogActionListener {
        void onOKLogAction(int uid, int actionQuantity, String action);
    }


    static LogActionDialogFragment newInstance(int uid, int remainingQuantity, String contentUnit, String name, String action) {
        LogActionDialogFragment f = new LogActionDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("uid", uid);
        args.putInt("remainingQuantity", remainingQuantity);
        args.putString("contentUnit", contentUnit);
        args.putString("name", name);
        args.putString("action", action);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uid = getArguments().getInt("uid");
        remainingQuantity = getArguments().getInt("remainingQuantity");
        name = getArguments().getString("name");
        contentUnit = getArguments().getString("contentUnit");
        action = getArguments().getString("action");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.log_action_dialog, container);

        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.log_action_dialog, null);

        TextView log_action_prompt_text = view.findViewById(R.id.log_action_prompt_text);
        log_action_prompt_text.setText(" Enter " + action + " quantity for "+ name + ": (1-" + remainingQuantity + ")");
        TextView log_action_unit_text = view.findViewById(R.id.log_action_unit_text);
        log_action_unit_text.setText(contentUnit);

        actionQuantity = view.findViewById(R.id.log_action_quantity_text);

        builder.setView(view)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                int aQ = Integer.valueOf(actionQuantity.getText().toString());
                                if (aQ < 1 || aQ > remainingQuantity) {
                                    Toast.makeText(getActivity(), "Invalid value entered, try again", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                ((LogActionListener)getTargetFragment()).onOKLogAction(uid, aQ, action);
                            }
                        })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }
}
