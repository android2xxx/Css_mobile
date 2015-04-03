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
    public static final String KEY_SERVICE = "Service";
    public static final String KEY_ATMID = "ATMID";
    public static final String KEY_BANK = "Bank";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_ISSUE = "Issue";
    private static final String URL = "test";

    ListView lstOpenService;
    OpenServiceAdapter adapter;
    LoadFragment loadFragmentObj;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open, container, false);

        ArrayList<HashMap<String, String>> openServerList = new ArrayList<HashMap<String, String>>();
        /*
        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
        Document doc = parser.getDomElement(xml); // getting DOM element

        NodeList nl = doc.getElementsByTagName(KEY_SERVICE);
        // looping through all song nodes &lt;song&gt;
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key =&gt; value
            map.put(KEY_ATMID, parser.getValue(e, KEY_ATMID));
            map.put(KEY_BANK, parser.getValue(e, KEY_BANK));
            map.put(KEY_LOCATION, parser.getValue(e, KEY_LOCATION));
            map.put(KEY_ISSUE, parser.getValue(e, KEY_ISSUE));


            // adding HashList to ArrayList
            openServerList.add(map);
        }*/

        GetOpenServiceTask task = new GetOpenServiceTask(this.getActivity());
        task.execute("http://192.168.66.87:5559/Home/LOL");
        Gson gson = new Gson();
        try {
            String json = task.get();
            Type collectionType = new TypeToken<List<EService>>(){}.getType();
            List<EService> lstService = gson.fromJson(json, collectionType);

            for(EService itemService : lstService)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(KEY_ATMID, itemService.getAtmid());
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
                loadFragmentObj.initializeFragment(new OpenServiceDetailFragment());
            }
        });

        loadFragmentObj = new LoadFragment(getFragmentManager());
        return view;
    }


}
