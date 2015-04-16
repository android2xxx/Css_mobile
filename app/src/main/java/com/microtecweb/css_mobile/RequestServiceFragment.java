package com.microtecweb.css_mobile;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import entity.EATM;
import entity.EBranch;
import entity.EConstant;
import entity.ECustomer;
import entity.EResponse;
import function.Function;
import taskserver.QueryHttpGetServiceTask;
import taskserver.QueryHttpPostServiceTask;
import taskserver.QueryHttpPostUploadFileServiceTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestServiceFragment extends Fragment {
     List<EBranch> lstBranch;
    private File _file;
    private int indexImage = 1;
    private ImageView _imageView2;
    private ImageView _imageView3;
    private ImageView _imageView;
    private List<String> lstPathFileImage = new ArrayList<>();

    public RequestServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        Spinner spinnerBank = (Spinner) view.findViewById(R.id.spinnerBank);
        final Spinner spinnerBranch = (Spinner) view.findViewById(R.id.spinnerBranch);
        final Spinner spinnerATM = (Spinner) view.findViewById(R.id.spinnerATM);
        final EditText txtIssue = (EditText) view.findViewById(R.id.txtIssue);
        ImageButton btTakePhoto = (ImageButton) view.findViewById(R.id.btTakePhoto);
        ImageButton btPhotoGallery = (ImageButton) view.findViewById(R.id.btPhotoGallery);
        Button btSubmit = (Button) view.findViewById(R.id.btSubmit);
        _imageView = (ImageView) view.findViewById(R.id.imgCap);
        _imageView2 = (ImageView) view.findViewById(R.id.imgCap2);
        _imageView3 = (ImageView) view.findViewById(R.id.imgCap3);
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
            spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    GetBranch(spinnerBranch, lstCustomer.get(position).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    GetATM(spinnerATM, lstBranch.get(position).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
                    startActivityForResult(i, EConstant.REQUEST_CODE_PHOTO_GALLERY_FOR_REQUESET_SERVICE);
                }
            });
            for (int i = 0; i <= 2; i++)
                lstPathFileImage.add("");

            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flagImage = false;
                    for (String pathFileImage : lstPathFileImage) {
                        if (!pathFileImage.isEmpty() && !pathFileImage.equals("")) {
                            flagImage = true;
                            break;
                        }
                    }
                    if (flagImage) {
                        if( spinnerATM.getSelectedItem() != null) {
                            EATM atm = (EATM) spinnerATM.getSelectedItem();
                            showProgressDialog(atm.getSerial(), txtIssue.getText().toString());
                        }
                        else {
                            Toast.makeText(getActivity(), "Please select ATM ID !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "Please take photo or chose photo from gallery !", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception ex) {

        }

        return view;
    }

    private void TakePhoto() {
        File _dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), EConstant.FOLDER_NAME);
        if (!_dir.exists()) {
            _dir.mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        _file = new File(_dir, String.format("myPhoto_%s.jpg", System.currentTimeMillis()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(_file));
        this.getActivity().startActivityForResult(intent, EConstant.REQUEST_CODE_TAKE_PHOTO_FOR_REQUESET_SERVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (requestCode == EConstant.REQUEST_CODE_TAKE_PHOTO_FOR_REQUESET_SERVICE && resultCode == EConstant.RESULT_OK) {
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
            } else if (requestCode == EConstant.REQUEST_CODE_PHOTO_GALLERY_FOR_REQUESET_SERVICE && resultCode == EConstant.RESULT_OK) {
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

    private void GetBranch(Spinner spinnerBranch, int customerId) {
        try {
            QueryHttpGetServiceTask task = new QueryHttpGetServiceTask(this.getActivity());
            task.execute(EConstant.URL + "GetBranch?customerId=" + customerId);
            Gson gson = new Gson();
            String json = task.get();
            Type collectionType = new TypeToken<List<EBranch>>() {
            }.getType();
            lstBranch = gson.fromJson(json, collectionType);
            ArrayAdapter<EBranch> adapter = new ArrayAdapter<EBranch>(this.getActivity(), android.R.layout.simple_spinner_item, lstBranch);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBranch.setAdapter(adapter);
        } catch (Exception ex) {
        }
    }

    private void GetATM(Spinner spinnerATM, int branchId) {
        try {
            QueryHttpGetServiceTask task = new QueryHttpGetServiceTask(this.getActivity());
            task.execute(EConstant.URL + "GetAtmIdSerial?branchId=" + branchId);
            Gson gson = new Gson();
            String json = task.get();
            Type collectionType = new TypeToken<List<EATM>>() {
            }.getType();
            final List<EATM> lstATM = gson.fromJson(json, collectionType);
            ArrayAdapter<EATM> adapter = new ArrayAdapter<EATM>(this.getActivity(), android.R.layout.simple_spinner_item, lstATM);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerATM.setAdapter(adapter);
        } catch (Exception ex) {
        }
    }

    private void showProgressDialog(final String serial, final String issue) {
        // TODO Auto-generated method stub
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Uploading", "Please wait...", true);

        new Thread() {
            public void run() {
                try {
                    Integer index = 0;
                    final SharedPreferences sharedpreferences = getActivity().getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
                    EResponse objEResponse = new EResponse();
                    for (String pathFileImage : lstPathFileImage) {
                        if (!pathFileImage.isEmpty() && !pathFileImage.equals("")) {
                            File fileUpload = new File(pathFileImage);
                            if (fileUpload.exists()) {
                                try {
                                    String[] arrayPathFiles = pathFileImage.split("/");
                                    String fileName = arrayPathFiles[arrayPathFiles.length - 1];
                                    byte[] byte_arr = Function.getBytesFromFile(fileUpload);
                                    QueryHttpPostUploadFileServiceTask task = new QueryHttpPostUploadFileServiceTask();
                                    task.execute(EConstant.URL + "UploadImage", Base64.encodeToString(byte_arr, Base64.DEFAULT), fileName, sharedpreferences.getString(EConstant.MY_PREFERENCES_USER_NAME, ""));
                                    objEResponse = task.get();
                                    index++;
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                    if (objEResponse.getStatus()) {
                        QueryHttpPostServiceTask taskPost = new QueryHttpPostServiceTask(getActivity());
                        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                        nameValuePair.add(new BasicNameValuePair("URL", EConstant.URL + "EntryService"));
                        nameValuePair.add(new BasicNameValuePair("userName", sharedpreferences.getString(EConstant.MY_PREFERENCES_USER_NAME, "")));
                        nameValuePair.add(new BasicNameValuePair("serial", serial));
                        nameValuePair.add(new BasicNameValuePair("issue", issue));
                        taskPost.execute(nameValuePair);
                        objEResponse = taskPost.get();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }.start();
    }
}
