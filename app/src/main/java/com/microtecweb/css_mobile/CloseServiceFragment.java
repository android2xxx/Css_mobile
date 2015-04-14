package com.microtecweb.css_mobile;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    View rootView;
    private ExpandableListView expandableListViewService;

    public CloseServiceFragment() {
        // Required empty public constructor
    }

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
            Log.i("VCL", ex.getMessage());

        }
        expandableListViewService.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                // TODO Auto-generated method stub
                if (groupPosition != previousGroup)
                    expandableListViewService.collapseGroup(previousGroup);
                previousGroup = groupPosition;

                /*MyExpandableListAdapter adapter = (MyExpandableListAdapter) expandableListViewService.getExpandableListAdapter();
                adapter.RemoveAllChild(groupPosition);
                adapter.AddChild(groupPosition, "Bus số " + new Random().nextInt(2000));
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();*/
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_close_service, container, false);
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
            task.execute(EConstant.URL + "GetAllServicesClosedByUserAssignedId?userAssignedId=" + sharedpreferences.getInt(EConstant.MY_PREFERENCES_USER_ID, -1));
        }
    }
}
