package com.microtecweb.css_mobile;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.OpenServiceAdapter;
import function.LoadFragment;
import function.XMLParser;
import taskserver.GetOpenService;
import taskserver.LoginToWS;


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

        GetOpenService task = new GetOpenService(this.getActivity());
        task.execute("12345", "1111", "");

          //  result = task.get();


        for (int i = 0; i < 20; i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();

            map.put(KEY_ATMID, KEY_ATMID + "__" +  i);
            map.put(KEY_BANK, KEY_BANK + "__" +  i);
            map.put(KEY_LOCATION, KEY_LOCATION + "__" +  i);
            map.put(KEY_ISSUE, KEY_ISSUE + "__" +  i);


            // adding HashList to ArrayList
            openServerList.add(map);
        }

        lstOpenService = (ListView) view.findViewById(R.id.lstOpenService);

        // Getting adapter by passing xml data ArrayList
        adapter = new OpenServiceAdapter(this.getActivity(), openServerList);
        lstOpenService.setAdapter(adapter);

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
