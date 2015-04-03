package com.microtecweb.css_mobile;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OpenServiceDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OpenServiceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenServiceDetailFragment extends Fragment {

    public OpenServiceDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open_service_detail, container, false);
    }

}
