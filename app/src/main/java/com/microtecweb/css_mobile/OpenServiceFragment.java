package com.microtecweb.css_mobile;

import android.os.Bundle;
import android.app.Fragment;
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
import java.util.concurrent.ExecutionException;

import adapter.OpenServiceAdapter;
import entity.EService;
import function.LoadFragment;
import taskserver.GetOpenServiceTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OpenServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OpenServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String KEY_SERVICE_ID = "ServiceId";
    public static final String KEY_ATM_ID = "ATMId";
    public static final String KEY_BANK = "Bank";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_ISSUE = "Issue";
    private static final String URL = "http://192.168.66.87:5559/Home/GetAllServicesByUserAssignedId?userAssignedId=";

    ListView lstOpenService;
    OpenServiceAdapter adapter;
    LoadFragment loadFragmentObj;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open, container, false);

        ArrayList<HashMap<String, String>> openServerList = new ArrayList<HashMap<String, String>>();

        GetOpenServiceTask task = new GetOpenServiceTask(this.getActivity());
        task.execute(URL + 6);
        Gson gson = new Gson();
        try {
            String json = task.get();
            Type collectionType = new TypeToken<List<EService>>(){}.getType();
            List<EService> lstService = gson.fromJson(json, collectionType);

            for(EService itemService : lstService)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(KEY_SERVICE_ID, itemService.getServiceId());
                map.put(KEY_ATM_ID, itemService.getAtmId());
                map.put(KEY_BANK, itemService.getBank());
                map.put(KEY_LOCATION, itemService.getLocation());
                map.put(KEY_ISSUE, itemService.getIssue());
                openServerList.add(map);
                lstOpenService = (ListView) view.findViewById(R.id.lstOpenService);
                adapter = new OpenServiceAdapter(this.getActivity(), openServerList);
                lstOpenService.setAdapter(adapter);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Click event for single list row
        lstOpenService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadFragmentObj.initializeFragment(new OpenServiceDetailFragment(), id);
            }
        });

        loadFragmentObj = new LoadFragment(getFragmentManager());
        return view;
    }


}
