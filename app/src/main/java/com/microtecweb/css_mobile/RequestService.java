package com.microtecweb.css_mobile;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.ArrayList;

import android.widget.Spinner;
import android.widget.ArrayAdapter;

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
        //Spinner spinnerBank = (Spinner)view.findViewById(R.id.spinnerBank);
        List<String> list = new ArrayList<String>();
        list.add("BIDV");
        list.add("VCB");
        list.add("VietinBank");


        String[] array = new String[list.size()];
        list.toArray(array);

        Spinner spinnerBank = (Spinner) view.findViewById(R.id.spinnerBank);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, array) ;

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBank.setAdapter(adapter);
        return view;
    }

}
