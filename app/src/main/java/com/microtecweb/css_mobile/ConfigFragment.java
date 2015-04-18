package com.microtecweb.css_mobile;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import entity.EConstant;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {

    View view;
    EditText txtURL;
    EditText txtChatServer;
    EditText txtChatPort;
    EditText txtServiceNumberPhone;
    EditText txtUserToDoList;
    EditText txtPasswordToDoList;
    SharedPreferences sharedpreferences;

    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_config, container, false);
        txtURL = (EditText) view.findViewById(R.id.txtURL);
        txtChatServer = (EditText) view.findViewById(R.id.txtChatServer);
        txtChatPort = (EditText) view.findViewById(R.id.txtChatPort);
        txtServiceNumberPhone = (EditText) view.findViewById(R.id.txtServiceNumberPhone);
        txtUserToDoList = (EditText) view.findViewById(R.id.txtUserToDoList);
        txtPasswordToDoList = (EditText) view.findViewById(R.id.txtPasswordToDoList);
        Button btSaveConfig = (Button) view.findViewById(R.id.btSaveConfig);
        Button btRestoreDefault = (Button) view.findViewById(R.id.btRestoreDefault);

        sharedpreferences = this.getActivity().getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        LoadData();
        final ConfigFragment fragment = this;
        btSaveConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.getActivity());
                alertDialogBuilder.setTitle("Confirm save");
                alertDialogBuilder.setMessage("Are you sure you want to save this config ?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(EConstant.MY_PREFERENCES_URL, txtURL.getText().toString());
                        editor.putString(EConstant.MY_PREFERENCES_SERVER_CHAT, txtChatServer.getText().toString());
                        editor.putInt(EConstant.MY_PREFERENCES_SERVER_PORT, Integer.parseInt(txtChatPort.getText().toString()));
                        editor.putString(EConstant.MY_PREFERENCES_SERVICE_NUMBER_PHONE, txtServiceNumberPhone.getText().toString());
                        editor.putString(EConstant.MY_PREFERENCES_USER_TO_DO_LIST, txtUserToDoList.getText().toString());
                        editor.putString(EConstant.MY_PREFERENCES_PASSWORD_TO_DO_LIST, txtPasswordToDoList.getText().toString());
                        editor.commit();

                        ParseUser currentUser = ParseUser.getCurrentUser();
                        if (currentUser == null) {
                            ParseUser.logInInBackground(txtUserToDoList.getText().toString(), txtPasswordToDoList.getText().toString() , new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if(user == null)
                                    {
                                        Toast.makeText(fragment.getActivity(), "User Password TO DO list không chính xác.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }

                        Toast.makeText(fragment.getActivity(), "Save successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        btRestoreDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.getActivity());
                alertDialogBuilder.setTitle("Confirm restore");
                alertDialogBuilder
                        .setMessage("Are you sure you want to restore default config ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.remove(EConstant.MY_PREFERENCES_URL);
                                editor.remove(EConstant.MY_PREFERENCES_SERVER_CHAT);
                                editor.remove(EConstant.MY_PREFERENCES_SERVER_PORT);
                                editor.remove(EConstant.MY_PREFERENCES_SERVICE_NUMBER_PHONE);
                                editor.remove(EConstant.MY_PREFERENCES_USER_TO_DO_LIST);
                                editor.remove(EConstant.MY_PREFERENCES_PASSWORD_TO_DO_LIST);
                                editor.commit();
                                LoadData();
                                Toast.makeText(fragment.getActivity(), "Restore default successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    private void LoadData() {
        txtURL.setText(EConstant.getURL(this.getActivity()));
        txtChatServer.setText(EConstant.getSERVER_CHAT(this.getActivity()));
        txtChatPort.setText(String.valueOf(EConstant.getSERVER_PORT(this.getActivity())));
        txtServiceNumberPhone.setText(EConstant.getSERVICE_NUMBER_PHONE(this.getActivity()));
        txtUserToDoList.setText(String.valueOf(EConstant.getUserToDoList(this.getActivity())));
        txtPasswordToDoList.setText(EConstant.getPasswordToDoList(this.getActivity()));
        /*
        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_URL))
            txtURL.setText(sharedpreferences.getString(EConstant.MY_PREFERENCES_URL, EConstant.URL));
        else
            txtURL.setText(EConstant.URL);

        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_SERVER_CHAT))
            txtChatServer.setText(sharedpreferences.getString(EConstant.MY_PREFERENCES_SERVER_CHAT, EConstant.SERVER_CHAT));
        else
            txtChatServer.setText(EConstant.SERVER_CHAT);

        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_SERVER_PORT))
            txtChatPort.setText(String.valueOf(sharedpreferences.getInt(EConstant.MY_PREFERENCES_SERVER_PORT, EConstant.SERVER_PORT)));
        else
            txtChatPort.setText(String.valueOf(EConstant.SERVER_PORT));

        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_SERVICE_NUMBER_PHONE))
            txtServiceNumberPhone.setText(sharedpreferences.getString(EConstant.MY_PREFERENCES_SERVICE_NUMBER_PHONE, EConstant.SERVICE_NUMBER_PHONE));
        else
            txtServiceNumberPhone.setText(EConstant.SERVICE_NUMBER_PHONE);*/
    }


}
