package com.microtecweb.css_mobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import adapter.OpenServicePartAdapter;
import entity.EConstant;
import entity.EDetailService;
import entity.EPart;
import function.Function;
import function.LoadFragment;
import taskserver.QueryHttpGetServiceTask;
import taskserver.QueryHttpPostUploadFileServiceTask;


public class OpenServiceDetailFragment extends Fragment {

    private File _file;
    private int indexImage = 1;
    private ImageView _imageView2;
    private ImageView _imageView3;
    private ImageView _imageView;
    private List<String> lstPathFileImage = new ArrayList<>();

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
        task.execute(EConstant.URL + "GetServiceById?serviceId=" + serviceId);
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

            if (objDetailService.getParts().size() > 0) {
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
                OpenServicePartAdapter adapter = new OpenServicePartAdapter(this.getActivity(), servicePartList);
                lstServicePart.setAdapter(adapter);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        ImageButton btCheckIn = (ImageButton) view.findViewById(R.id.btCheckIn);
        ImageButton btTakePhoto = (ImageButton) view.findViewById(R.id.btTakePhoto);
        ImageButton btPhotoGallery = (ImageButton) view.findViewById(R.id.btPhotoGallery);
        ImageButton btCloseService = (ImageButton) view.findViewById(R.id.btCloseService);
        _imageView = (ImageView) view.findViewById(R.id.imgCap);
        _imageView2 = (ImageView) view.findViewById(R.id.imgCap2);
        _imageView3 = (ImageView) view.findViewById(R.id.imgCap3);
        btCloseService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer index = 0;


                //asyncDialog.setMessage("Loading...");
                //asyncDialog.show();
                openprogresdialog();
/*
                for (String pathFileImage : lstPathFileImage) {
                    if (!pathFileImage.isEmpty() && !pathFileImage.equals("")) {
                        File fileUpload = new File(pathFileImage);
                        if (fileUpload.exists()) {
                            try {
                                String[] arrayPathFiles = pathFileImage.split("/");
                                String fileName = arrayPathFiles[arrayPathFiles.length - 1];
                                byte[] byte_arr = Function.getBytesFromFile(fileUpload);
                                QueryHttpPostUploadFileServiceTask task = new QueryHttpPostUploadFileServiceTask(getActivity(), asyncDialog);
                                task.execute(EConstant.URL + "UploadImage", Base64.encodeToString(byte_arr, Base64.DEFAULT), fileName, sharedpreferences.getString(EConstant.MY_PREFERENCES_USER_NAME, ""));
                                String content = task.get();
                                index++;

                            } catch (Exception e) {

                            }
                        }
                    }
                }*/
                if (index > 0)
                    Toast.makeText(getActivity(), "Upload " + index + (index > 1 ? " files " : "file ") + "successfully", Toast.LENGTH_SHORT).show();
                /*
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_camera);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte[] byte_arr = stream.toByteArray();
                QueryHttpPostUploadFileServiceTask task = new QueryHttpPostUploadFileServiceTask(v.getContext());
                task.execute(EConstant.URL + "UploadImage", Base64.encodeToString(byte_arr, Base64.DEFAULT), "filename.abc", sharedpreferences.getString("userName", ""));
                String content = "";
                try {
                    content = task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (content.equals(""))
                    Toast.makeText(v.getContext(), "Upload file successfully", Toast.LENGTH_SHORT).show();
                    */
            }
        });

        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto();
            }
        });

        btPhotoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EConstant.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, EConstant.REQUEST_CODE_PHOTO_GALLERY);
            }
        });
        for (int i = 0; i <= 2; i++)
            lstPathFileImage.add("");
        return view;
    }
    //ProgressDialog asyncDialog = new ProgressDialog(getActivity());
    private void openprogresdialog() {
        // TODO Auto-generated method stub
        final ProgressDialog progDailog = ProgressDialog.show(
                getActivity(), "Uploading", "Please wait...", true);
        //asyncDialog.show();
        new Thread() {
            public void run() {
                try {
                    final SharedPreferences sharedpreferences = getActivity().getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);

                    for (String pathFileImage : lstPathFileImage) {
                        if (!pathFileImage.isEmpty() && !pathFileImage.equals("")) {
                            File fileUpload = new File(pathFileImage);
                            if (fileUpload.exists()) {
                                try {
                                    String[] arrayPathFiles = pathFileImage.split("/");
                                    String fileName = arrayPathFiles[arrayPathFiles.length - 1];
                                    byte[] byte_arr = Function.getBytesFromFile(fileUpload);
                                    QueryHttpPostUploadFileServiceTask task = new QueryHttpPostUploadFileServiceTask(getActivity());
                                    task.execute(EConstant.URL + "UploadImage", Base64.encodeToString(byte_arr, Base64.DEFAULT), fileName, sharedpreferences.getString(EConstant.MY_PREFERENCES_USER_NAME, ""));
                                    String content = task.get();


                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
                progDailog.dismiss();
            }
        }.start();
    }

    private void TakePhoto() {
        File _dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), EConstant.FOLDER_NAME);
        if (!_dir.exists()) {
            _dir.mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        _file = new File(_dir, String.format("myPhoto_%s.jpg", System.currentTimeMillis()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(_file));
        this.getActivity().startActivityForResult(intent, EConstant.REQUEST_CODE_TAKE_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (requestCode == EConstant.REQUEST_CODE_TAKE_PHOTO && resultCode == EConstant.RESULT_OK) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(_file.getPath(),
                        options);
                if (indexImage == 1) {
                    _imageView.setImageBitmap(bitmap);
                    _imageView.setVisibility(View.VISIBLE);
                    indexImage = 2;
                    lstPathFileImage.set(0, _file.getPath());
                    TakePhoto();
                } else if (indexImage == 2) {
                    _imageView2.setImageBitmap(bitmap);
                    _imageView2.setVisibility(View.VISIBLE);
                    indexImage = 3;
                    lstPathFileImage.set(1, _file.getPath());
                    TakePhoto();
                } else if (indexImage == 3) {
                    _imageView3.setImageBitmap(bitmap);
                    _imageView3.setVisibility(View.VISIBLE);
                    indexImage = 1;
                    lstPathFileImage.set(2, _file.getPath());
                }
            } else if (requestCode == EConstant.REQUEST_CODE_PHOTO_GALLERY && resultCode == EConstant.RESULT_OK) {
                if (data != null) {
                    String[] all_path = data.getStringArrayExtra("all_path");
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    for (String pathFile : all_path) {

                        final Bitmap bitmap = BitmapFactory.decodeFile(pathFile,
                                options);
                        if (indexImage == 1) {
                            _imageView.setImageBitmap(bitmap);
                            _imageView.setVisibility(View.VISIBLE);
                            indexImage = 2;
                            lstPathFileImage.set(0, pathFile);
                        } else if (indexImage == 2) {
                            _imageView2.setImageBitmap(bitmap);
                            _imageView2.setVisibility(View.VISIBLE);
                            indexImage = 3;
                            lstPathFileImage.set(1, pathFile);
                        } else if (indexImage == 3) {
                            _imageView3.setImageBitmap(bitmap);
                            _imageView3.setVisibility(View.VISIBLE);
                            indexImage = 1;
                            lstPathFileImage.set(2, pathFile);
                        }
                    }
                }
            }
        }
        //in fragment class callback
    }
}
