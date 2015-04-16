package com.microtecweb.css_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.OpenServiceAdapter;
import entity.EConstant;
import entity.ESummaryService;
import function.LoadFragment;
import taskserver.MicFragment;
import taskserver.QueryHttpGetListenerServiceTask;

public class OpenServiceFragment extends MicFragment {

    ListView lstOpenService;
    OpenServiceAdapter adapter;
    LoadFragment loadFragmentObj;
    OpenServiceDetailFragment openServiceDetailFragment;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_open_service, container, false);
        try {
            QueryHttpGetListenerServiceTask task = new QueryHttpGetListenerServiceTask(this.getActivity(), this);
            final SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
            if (sharedpreferences.contains(EConstant.MY_PREFERENCES_USER_ID) && sharedpreferences.getInt(EConstant.MY_PREFERENCES_USER_ID, -1) != -1) {
                task.execute(EConstant.getURL(this.getActivity()) + "GetAllServicesByUserAssignedId?userAssignedId=" + sharedpreferences.getInt(EConstant.MY_PREFERENCES_USER_ID, -1));
            }
        } catch (Exception e) {
        }
        return view;
    }

    public void onTaskCompleted(String json) {
        final OpenServiceFragment currentFragment = this;
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<ESummaryService>>() {
        }.getType();
        List<ESummaryService> lstService = gson.fromJson(json, collectionType);
        ArrayList<HashMap<String, String>> openServerList = new ArrayList<HashMap<String, String>>();
        for (ESummaryService itemService : lstService) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(OpenServiceAdapter.KEY_SERVICE_ID, itemService.getServiceId());
            map.put(OpenServiceAdapter.KEY_ATM_ID, itemService.getAtmId());
            map.put(OpenServiceAdapter.KEY_BANK, itemService.getBank());
            map.put(OpenServiceAdapter.KEY_LOCATION, itemService.getLocation());
            map.put(OpenServiceAdapter.KEY_ISSUE, itemService.getIssue());
            openServerList.add(map);
        }
        lstOpenService = (ListView) view.findViewById(R.id.lstOpenService);
        adapter = new OpenServiceAdapter(this.getActivity(), openServerList);
        lstOpenService.setAdapter(adapter);

        lstOpenService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                openServiceDetailFragment = new OpenServiceDetailFragment("OpenServiceDetailFragment");
                loadFragmentObj.initializeFragment(currentFragment, openServiceDetailFragment, openServiceDetailFragment.getTextName(), (int) id);
            }
        });
        loadFragmentObj = new LoadFragment(getFragmentManager());
    }
}
