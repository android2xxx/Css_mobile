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
import android.support.v7.app.ActionBarActivity;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import adapter.OpenServicePartAdapter;
import entity.EConstant;
import entity.EDetailService;
import entity.EPart;
import entity.EResponse;
import function.Function;
import function.LoadFragment;
import taskserver.QueryHttpGetServiceTask;
import taskserver.QueryHttpPostServiceTask;
import taskserver.QueryHttpPostUploadFileServiceTask;


public class OpenServiceDetailFragment extends Fragment {

    private File _file;
    private int indexImage = 1;
    private ImageView _imageView2;
    private ImageView _imageView3;
    private ImageView _imageView;
    private List<String> lstPathFileImage = new ArrayList<>();
    private String textName;

    public OpenServiceDetailFragment()
    {

    }
    public OpenServiceDetailFragment(String textName) {
        this.textName = textName;
    }

    public String getTextName()
    {
        return  textName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle extras = getArguments();

        final int serviceId = extras.getInt(LoadFragment.PACKAGE_ID);
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle("ServiceId: " + serviceId);
        View view = inflater.inflate(R.layout.fragment_open_service_detail, container, false);
        try {
            TextView tvATMID = (TextView) view.findViewById(R.id.tvATMID);
            TextView tvSerial = (TextView) view.findViewById(R.id.tvSerial);
            TextView tvBank = (TextView) view.findViewById(R.id.tvBank);
            TextView tvBranch = (TextView) view.findViewById(R.id.tvBranch);
            TextView tvServiceId = (TextView) view.findViewById(R.id.tvServiceId);
            TextView tvContract = (TextView) view.findViewById(R.id.tvContract);
            TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            TextView tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);
            final EditText txtIssue = (EditText) view.findViewById(R.id.txtIssue);
            final EditText txtSolution = (EditText) view.findViewById(R.id.txtSolution);

            QueryHttpGetServiceTask task = new QueryHttpGetServiceTask(this.getActivity());
            task.execute(EConstant.URL + "GetServiceById?serviceId=" + serviceId);
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            Gson gson = builder.create();

            String json = task.get();
            EDetailService objDetailService = gson.fromJson(json, EDetailService.class);
            tvATMID.setText(objDetailService.getAtmId());
            tvSerial.setText(objDetailService.getSerial());
            tvBank.setText(objDetailService.getBank());
            tvBranch.setText(objDetailService.getBranch());
            tvServiceId.setText(String.valueOf(serviceId));
            tvContract.setText(objDetailService.getContract());
            tvLocation.setText(objDetailService.getLocation());

            tvStartTime.setText(df.format(objDetailService.getStartTime()));
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


            final SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
            ImageButton btCheckIn = (ImageButton) view.findViewById(R.id.btCheckIn);
            ImageButton btTakePhoto = (ImageButton) view.findViewById(R.id.btTakePhoto);
            ImageButton btPhotoGallery = (ImageButton) view.findViewById(R.id.btPhotoGallery);
            ImageButton btCloseService = (ImageButton) view.findViewById(R.id.btCloseService);
            _imageView = (ImageView) view.findViewById(R.id.imgCap);
            _imageView2 = (ImageView) view.findViewById(R.id.imgCap2);
            _imageView3 = (ImageView) view.findViewById(R.id.imgCap3);

            btCheckIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QueryHttpPostServiceTask taskPost = new QueryHttpPostServiceTask(getActivity());
                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                    nameValuePair.add(new BasicNameValuePair("URL", EConstant.URL + "CheckInService"));
                    nameValuePair.add(new BasicNameValuePair("serviceId", String.valueOf(serviceId)));
                    nameValuePair.add(new BasicNameValuePair("dateTimeAssign", df.format(Calendar.getInstance().getTime())));
                    nameValuePair.add(new BasicNameValuePair("username", sharedpreferences.getString(EConstant.MY_PREFERENCES_USER_NAME, "")));
                    taskPost.execute(nameValuePair);
                }
            });

            btCloseService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flagImage = false;
                    for (String pathFileImage : lstPathFileImage) {
                        if (!pathFileImage.isEmpty() && !pathFileImage.equals("")) {
                            flagImage = true;
                            break;
                        }
                    }
                    if (flagImage)
                        showProgressDialog(serviceId, txtIssue.getText().toString(), txtSolution.getText().toString());
                    else
                        Toast.makeText(getActivity(), "Please take photo or chose photo from gallery !", Toast.LENGTH_SHORT).show();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    //ProgressDialog asyncDialog = new ProgressDialog(getActivity());
    //String contentUpload = "zzzz";

    private void showProgressDialog(final int serviceId, final String issue, final String solution) {
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
                        nameValuePair.add(new BasicNameValuePair("URL", EConstant.URL + "CloseService"));
                        nameValuePair.add(new BasicNameValuePair("serviceId", String.valueOf(serviceId)));
                        nameValuePair.add(new BasicNameValuePair("issue", issue));
                        nameValuePair.add(new BasicNameValuePair("solution", solution));
                        nameValuePair.add(new BasicNameValuePair("username", sharedpreferences.getString(EConstant.MY_PREFERENCES_USER_NAME, "")));
                        taskPost.execute(nameValuePair);
                        objEResponse = taskPost.get();
                        /*
                        if (objEResponse.getStatus()) {
                            if (index > 0) {
                                contentUpload = "Upload " + index + (index > 1 ? " files " : "file ") + "successfully";
                            }
                            //Toast.makeText(getActivity(), "Upload " + index + (index > 1 ? " files " : "file ") + "successfully", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(), "Upload file succesully !", Toast.LENGTH_SHORT).show();
                        } else {
                            contentUpload = "Error: " + objEResponse.getMessage();
                            //Toast.makeText(getActivity(), "Error: " + objEResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }*/
                    }
                } catch (Exception e) {
                    //contentUpload = "Error: " + e.getMessage();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                //Toast.makeText(getActivity(), "Error: test", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), contentUpload, Toast.LENGTH_SHORT).show();
            }
        }.start();
        //Toast.makeText(getActivity(), contentUpload, Toast.LENGTH_SHORT).show();
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
