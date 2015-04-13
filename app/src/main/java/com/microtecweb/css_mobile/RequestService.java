package com.microtecweb.css_mobile;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entity.EConstant;
import entity.ECustomer;
import taskserver.QueryHttpGetServiceTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestService extends Fragment {


    public RequestService() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open_service_detail, container, false);
        Spinner spinnerBank = (Spinner)view.findViewById(R.id.spinnerBank);
        QueryHttpGetServiceTask task = new QueryHttpGetServiceTask(this.getActivity());
        task.execute(EConstant.URL + "GetCustomer");
        Gson gson = new Gson();

        try {
            String json = task.get();
            Type collectionType = new TypeToken<List<ECustomer>>() {            }.getType();
            List<ECustomer> lstCustomer = gson.fromJson(json, collectionType);
            ArrayAdapter<ECustomer> adapter = new ArrayAdapter<ECustomer>(this.getActivity(), android.R.layout.simple_spinner_item, lstCustomer);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerBank.setAdapter(adapter);
        } catch (Exception ex) {

            
        }

        return view;
    }

}
