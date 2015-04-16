package com.microtecweb.css_mobile;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import android.database.Cursor;
import android.net.Uri;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.*;

import adapter.OpenServicePartAdapter;
import entity.EConstant;
import entity.ESmsRep;
import function.Function;
import android.widget.RadioButton;
import android.widget.RadioGroup;
/**
 * A simple {@link Fragment} subclass.
 */



public class ServiceHistoryFragment extends Fragment {

    private ListView lvSms;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> listAdapter ;
    private List<Map<String, Object>> data;
    private EditText txtFrom;
    private EditText txtTo;
    private int date_dialog_id;
    static final int DATE_DIALOG_ID_FROM = 999;
    static final int DATE_DIALOG_ID_TO = 888;
    static final int ONE_DAY_TICKET = 1000 * 60 * 60 * 24;

    SimpleDateFormat dmyyyy = new SimpleDateFormat("dd-M-yyyy");
    SimpleDateFormat dmmyyyy = new SimpleDateFormat("dd-MMM-yyyy");
    private int yearFrom;
    private int monthFrom;
    private int dayFrom;

    private int yearTo;
    private int monthTo;
    private int dayTo;

    private long dateFrom, dateTo;

    public ServiceHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View view = inflater.inflate(R.layout.fragment_service_history, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.listSms);

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


        txtFrom = (EditText) view.findViewById(R.id.txtFrom);
        txtFrom.setTextIsSelectable(true);
        txtFrom.setText(dmmyyyy.format(calendar.getTime()));

        txtTo = (EditText) view.findViewById(R.id.txtTo);
        txtTo.setTextIsSelectable(true);
        txtTo.setText(dmmyyyy.format(Calendar.getInstance().getTime()));


        txtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(DATE_DIALOG_ID_FROM).show();
            }
        });

        txtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(DATE_DIALOG_ID_TO).show();
            }
        });


        ImageButton btScan = (ImageButton) view.findViewById(R.id.btScan);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplaySms(view, listView);
            }
        });

        return view;
    }

    private void DisplaySms(View view, ListView listView)
    {
        dateFrom = ConvertToDate(txtFrom.getText().toString()).getTime();
        dateTo = ConvertToDate(txtTo.getText().toString()).getTime() + ONE_DAY_TICKET;
        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioDirection);
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == R.id.radioInbox) {
            List<String> listSms = new ArrayList<String>();
            listSms = GetListSms(dateFrom, dateTo);
            DisplayListView(listView, listSms);
        }
        else
        {
            List<String> lstDemo = Function.getOutboxSms((Context)getActivity(), dateFrom, dateTo);
            DisplayOutboxView(listView, lstDemo);
        }
    }

    private Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
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

    private void DisplayListView(ListView listView, List<String> listSms)
    {
        ArrayAdapter<String>adapter;
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listSms);
        listView.setAdapter(adapter);
    }

    private List<String> GetListSms(long from, long to) {
        List<String> listSms = new ArrayList<String>();
        Uri uri = Uri.parse("content://sms/");
        ContentResolver contentResolver = ((Context)getActivity()).getContentResolver();

        String phoneNumber = EConstant.SERVICE_NUMBER_PHONE ;
        if (phoneNumber.length() > EConstant.LAST_NUM_QUANLITY)
            phoneNumber = phoneNumber.substring(phoneNumber.length() - EConstant.LAST_NUM_QUANLITY);
        String sms = "address like '%"+ phoneNumber + "'";
        Cursor cursor = contentResolver.query(uri, new String[] { "_id", "body", "date" }, sms, null,   null);
        while (cursor.moveToNext())
        {
            String strbody = cursor.getString( cursor.getColumnIndex("body") );
            String d = cursor.getString( cursor.getColumnIndex("date") );
            long l = (long)Long.parseLong(d);
            Date date =new Date(l);
            if ( (from <= l) && (l <= to) ){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s = formatter.format(date);
                listSms.add(s + " | " + strbody);
            }
        }
        return listSms;
    }

    private void DisplayOutboxView(ListView listView, List<String> listSms)
    {
        ArrayAdapter<String>adapter;
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listSms);
        listView.setAdapter(adapter);
    }
}

