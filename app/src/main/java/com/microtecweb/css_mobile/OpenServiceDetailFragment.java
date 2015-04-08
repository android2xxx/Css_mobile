package com.microtecweb.css_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;;
import adapter.OpenServicePartAdapter;
import entity.EDetailService;
import entity.EPart;
import function.LoadFragment;
import taskserver.QueryHttpGetServiceTask;
import taskserver.QueryHttpPostUploadFileServiceTask;

public class OpenServiceDetailFragment extends Fragment {
    private static final String URL = "http://192.168.66.87:5559/Home/";


    public OpenServiceDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle extras = getArguments();
        long serviceId = extras.getLong(LoadFragment.PACKAGE_ID);
        View view = inflater.inflate(R.layout.fragment_open_service_detail, container, false);
        TextView tvATMID = (TextView) view.findViewById(R.id.tvATMID);
        TextView tvSerial = (TextView) view.findViewById(R.id.tvSerial);
        TextView tvBank = (TextView) view.findViewById(R.id.tvBank);
        TextView tvBranch = (TextView) view.findViewById(R.id.tvBranch);
        TextView tvServiceId = (TextView) view.findViewById(R.id.tvServiceId);
        TextView tvContract = (TextView) view.findViewById(R.id.tvContract);
        TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        TextView tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);

        EditText txtIssue = (EditText) view.findViewById(R.id.txtIssue);
        EditText txtSolution = (EditText) view.findViewById(R.id.txtSolution);

        QueryHttpGetServiceTask task = new QueryHttpGetServiceTask(this.getActivity());
        task.execute(URL + "GetServiceById?serviceId=" + serviceId);
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();
        try {
            String json = task.get();
            EDetailService objDetailService = gson.fromJson(json, EDetailService.class);
            tvATMID.setText(String.valueOf(objDetailService.getAtmId()));
            tvSerial.setText(objDetailService.getSerial());
            tvBank.setText(objDetailService.getBank());
            tvBranch.setText(objDetailService.getBranch());
            tvServiceId.setText(String.valueOf(serviceId));
            tvContract.setText(objDetailService.getContract());
            tvLocation.setText(objDetailService.getLocation());
            tvStartTime.setText(String.valueOf(objDetailService.getStartTime()));
            txtIssue.setText(objDetailService.getIssue());
            txtSolution.setText(objDetailService.getSolution());

            if(objDetailService.getParts().size() > 0) {
                ArrayList<HashMap<String, String>> servicePartList = new ArrayList<HashMap<String, String>>();
                for (EPart itemPart : objDetailService.getParts()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(OpenServicePartAdapter.KEY_PART_ID, String.valueOf(itemPart.getPartId()));
                    map.put(OpenServicePartAdapter.KEY_PART_NAME, itemPart.getPartName());
                    map.put(OpenServicePartAdapter.KEY_PART_QUANTITY, String.valueOf(itemPart.getQuantity()));
                    map.put(OpenServicePartAdapter.KEY_STORE_NAME, itemPart.getStore());
                    servicePartList.add(map);
                }
                ListView lstServicePart = (ListView) view.findViewById(R.id.lstServicePart);
                OpenServicePartAdapter  adapter = new OpenServicePartAdapter(this.getActivity(), servicePartList);
                lstServicePart.setAdapter(adapter);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final String MyPREFERENCES = "AtmLocationPrefs";
        final SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ImageButton btCheckIn = (ImageButton) view.findViewById(R.id.btCheckIn);
        ImageButton btTakePhoto = (ImageButton) view.findViewById(R.id.btTakePhoto);
        ImageButton btPhotoGallery = (ImageButton) view.findViewById(R.id.btPhotoGallery);
        ImageButton btCloseService = (ImageButton) view.findViewById(R.id.btCloseService);
        btCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_camera);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte [] byte_arr = stream.toByteArray();
                QueryHttpPostUploadFileServiceTask task = new QueryHttpPostUploadFileServiceTask(v.getContext());
                task.execute(URL + "UploadImage",  Base64.encodeToString(byte_arr, Base64.DEFAULT), "filename.abc", sharedpreferences.getString("userName", "") );
                String content = "";
                try {
                    content = task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(content.equals(""))
                    Toast.makeText(v.getContext(), "Upload file successfully", Toast.LENGTH_SHORT);
            }
        });


        return view;
    }


}
