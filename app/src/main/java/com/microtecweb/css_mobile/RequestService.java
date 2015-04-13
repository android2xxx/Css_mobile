package com.microtecweb.css_mobile;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        Spinner spinnerBank = (Spinner) view.findViewById(R.id.spinnerBank);
        final Spinner spinnerBranch = (Spinner) view.findViewById(R.id.spinnerBranch);
        final QueryHttpGetServiceTask task = new QueryHttpGetServiceTask(this.getActivity());
        task.execute(EConstant.URL + "GetCustomer");
        Gson gson = new Gson();
        String json = null;
        try {
            json = task.get();
            Type collectionType = new TypeToken<List<ECustomer>>() {
            }.getType();
            final List<ECustomer> lstCustomer = gson.fromJson(json, collectionType);
            ArrayAdapter<ECustomer> adapter = new ArrayAdapter<ECustomer>(this.getActivity(), android.R.layout.simple_spinner_item, lstCustomer);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerBank.setAdapter(adapter);
            spinnerBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                   @Override
                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                       GetBranch(spinnerBranch, lstCustomer.get(position).getId());
                                                   }
                                               }
            );
        } catch (Exception ex) {


        }

        return view;
    }

    private void GetBranch(Spinner spinnerBranch, int customerId) {
        try {
            QueryHttpGetServiceTask task = new QueryHttpGetServiceTask(this.getActivity());
            task.execute(EConstant.URL + "GetBranch?customerId=" + customerId);
            Gson gson = new Gson();
            String json = task.get();
            Type collectionType = new TypeToken<List<ECustomer>>() {
            }.getType();
            final List<ECustomer> lstCustomer = gson.fromJson(json, collectionType);
            ArrayAdapter<ECustomer> adapter = new ArrayAdapter<ECustomer>(this.getActivity(), android.R.layout.simple_spinner_item, lstCustomer);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBranch.setAdapter(adapter);
        } catch (Exception ex) {
        }
    }
}
