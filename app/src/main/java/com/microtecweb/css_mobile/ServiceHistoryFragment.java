package com.microtecweb.css_mobile;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
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

import adapter.OpenServicePartAdapter;

/**
 * A simple {@link Fragment} subclass.
 */



public class ServiceHistoryFragment extends Fragment {

    private ListView lvSms;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> listAdapter ;
    private List<Map<String, Object>> data;
    public ServiceHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_service_history, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.listSms);

        Button btScan = (Button) view.findViewById(R.id.btScan);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listSms = new ArrayList<String>();
                listSms = GetListSms();
                DisplayListView(listView, listSms);
            }
        });

        return view;
    }

    private void PrepareData() {
        data = new ArrayList<Map<String, Object>>();
        Map<String, Object> item;
        item = new HashMap<String, Object>();
        item.put("1", "A");
        item.put("2", "B");
        data.add(item);
        item = new HashMap<String, Object>();
        item.put("3", "C");
        item.put("4", "D");
        data.add(item);
        item = new HashMap<String, Object>();
        item.put("5", "E");
        item.put("6", "F");
        data.add(item);
    }

    private void DisplayListView(ListView listView, List<String> listSms)
    {
        Toast.makeText(getActivity().getApplicationContext(),"abc", Toast.LENGTH_LONG).show();
        ArrayAdapter<String>adapter;
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listSms);
        listView.setAdapter(adapter);
    }

    private List<String> GetListSms() {
        List<String> listSms = new ArrayList<String>();
        Uri uri = Uri.parse("content://sms/");
        ContentResolver contentResolver = getActivity().getContentResolver();

        String phoneNumber = "+84983241066";
        String sms = "address='"+ phoneNumber + "'";
        Cursor cursor = contentResolver.query(uri, new String[] { "_id", "body", "date" }, sms, null,   null);

//        System.out.println ( cursor.getCount() );

        while (cursor.moveToNext())
        {
            //String strbody = cursor.getString( cursor.getColumnIndex("date") );
            String strbody = cursor.getString( cursor.getColumnIndex("body") );
            //System.out.println ( strbody );
            listSms.add(strbody);
        }
        return listSms;
    }
}

