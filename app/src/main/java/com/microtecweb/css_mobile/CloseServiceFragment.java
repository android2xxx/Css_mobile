package com.microtecweb.css_mobile;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.MyExpandableListAdapter;
import entity.EConstant;
import entity.EDetailService;
import taskserver.MicFragment;
import taskserver.QueryHttpGetListenerServiceTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class CloseServiceFragment extends MicFragment {

    SimpleDateFormat dmyyyy = new SimpleDateFormat("dd-M-yyyy");
    SimpleDateFormat dmmyyyy = new SimpleDateFormat("dd-MMM-yyyy");
    private View rootView;
    private ExpandableListView expandableListViewService;
    private EditText txtFrom;
    private EditText txtTo;
    private int date_dialog_id;
    static final int DATE_DIALOG_ID_FROM = 999;
    static final int DATE_DIALOG_ID_TO = 888;

    private int yearFrom;
    private int monthFrom;
    private int dayFrom;

    private int yearTo;
    private int monthTo;
    private int dayTo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        yearTo = Calendar.getInstance().get(Calendar.YEAR);
        monthTo = Calendar.getInstance().get(Calendar.MONTH);
        dayTo = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, -1);

        yearFrom = calendar.get(Calendar.YEAR);
        monthFrom = calendar.get(Calendar.MONTH);
        dayFrom = calendar.get(Calendar.DAY_OF_MONTH);

        rootView = inflater.inflate(R.layout.fragment_close_service, container, false);

        txtFrom = (EditText) rootView.findViewById(R.id.txtFrom);
        txtFrom.setTextIsSelectable(true);
        txtFrom.setText(dmmyyyy.format(calendar.getTime()));

        txtTo = (EditText) rootView.findViewById(R.id.txtTo);
        txtTo.setTextIsSelectable(true);
        txtTo.setText(dmmyyyy.format(Calendar.getInstance().getTime()));
        //InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(txtFrom.getWindowToken(), 0);
        //imm.hideSoftInputFromWindow(txtTo.getWindowToken(), 0);
        //imm.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        txtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(DATE_DIALOG_ID_FROM).show();
            }
        });
        /*txtFrom.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });*/
        txtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(DATE_DIALOG_ID_TO).show();
            }
        });
        /*txtTo.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });*/
        ImageButton btSearch = (ImageButton) rootView.findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowService();
            }
        });
        return rootView;
    }

    private void ShowService() {
        final SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_USER_ID) && sharedpreferences.getInt(EConstant.MY_PREFERENCES_USER_ID, -1) != -1) {
            QueryHttpGetListenerServiceTask task = new QueryHttpGetListenerServiceTask(this.getActivity(), this);
            task.execute(EConstant.getURL(this.getActivity()) + "GetAllServicesClosedByUserAssignedId?userAssignedId=" + sharedpreferences.getInt(EConstant.MY_PREFERENCES_USER_ID, -1));
        }
    }

    protected Dialog onCreateDialog(int id) {
        date_dialog_id = id;
        if (date_dialog_id == DATE_DIALOG_ID_FROM)
            return new DatePickerDialog(this.getActivity(), datePickerListener, yearFrom, monthFrom, dayFrom);
        else
            return new DatePickerDialog(this.getActivity(), datePickerListener, yearTo, monthTo, dayTo);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            if (date_dialog_id == DATE_DIALOG_ID_FROM) {
                yearFrom = selectedYear;
                monthFrom = selectedMonth;
                dayFrom = selectedDay;
                String dateInString = new StringBuilder()
                        .append(dayFrom).append("-")
                        .append(monthFrom + 1).append("-")
                        .append(yearFrom).toString();
                try {
                    Date date = dmyyyy.parse(dateInString);
                    txtFrom.setText(dmmyyyy.format(date));
                } catch (Exception ex) {

                }

            } else if (date_dialog_id == DATE_DIALOG_ID_TO) {
                yearTo = selectedYear;
                monthTo = selectedMonth;
                dayTo = selectedDay;
                String dateInString = new StringBuilder()
                        .append(dayTo).append("-")
                        .append(monthTo + 1).append("-")
                        .append(yearTo).toString();
                try {
                    Date date = dmyyyy.parse(dateInString);
                    txtTo.setText(dmmyyyy.format(date));
                } catch (Exception ex) {

                }
            }
        }
    };

    public void onTaskCompleted(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        Gson gson = builder.create();
        expandableListViewService = (ExpandableListView) rootView.findViewById(R.id.listViewService);

        try {

            Type collectionType = new TypeToken<List<EDetailService>>() {
            }.getType();
            List<EDetailService> lstService = gson.fromJson(json, collectionType);
            MyExpandableListAdapter adapter = new MyExpandableListAdapter(getActivity(), lstService);
            expandableListViewService.setAdapter(adapter);
        } catch (Exception ex) {
            Log.i("Mic", ex.getMessage());
        }
        expandableListViewService.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                // TODO Auto-generated method stub
                if (groupPosition != previousGroup)
                    expandableListViewService.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }

}
