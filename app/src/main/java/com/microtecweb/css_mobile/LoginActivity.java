package com.microtecweb.css_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import entity.EConstant;
import function.Function;
import taskserver.QueryHttpGetServiceTask;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText txtAccount = (EditText) findViewById(R.id.txtAccount);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        final CheckBox ckRememberMe = (CheckBox) findViewById(R.id.ckRememberMe);

        final SharedPreferences sharedpreferences = getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAccount.getText().toString().trim().length() == 0) {
                    txtAccount.setError("Invalid User");
                } else if (txtPassword.getText().toString().trim().length() == 0) {
                    txtPassword.setError("Invalid Password");
                } else {
                    String result = "";
                    if (!Function.isOnline(LoginActivity.this)) {
                        result = "Network is not available";
                    } else {
                        QueryHttpGetServiceTask task = new QueryHttpGetServiceTask(LoginActivity.this);
                        task.execute(EConstant.URL + "Authenticate?UserName=" + txtAccount.getText().toString() + "&Password=" + txtPassword.getText().toString());
                        try {
                            result = task.get();
                            if (Function.isNumeric(result)) {
                                if (Integer.parseInt(result) != 0) {
                                    Boolean remember = ckRememberMe.isChecked();
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(EConstant.MY_PREFERENCES_USER_NAME, txtAccount.getText().toString());
                                    editor.putInt(EConstant.MY_PREFERENCES_USER_ID, Integer.parseInt(result));
                                    if (remember) {
                                        editor.putString(EConstant.MY_PREFERENCES_PASSWORD, txtPassword.getText().toString());
                                        editor.putBoolean(EConstant.MY_PREFERENCES_REMEMBER, true);
                                    } else {
                                        editor.putString(EConstant.MY_PREFERENCES_PASSWORD, "");
                                        editor.putBoolean(EConstant.MY_PREFERENCES_REMEMBER, false);
                                    }
                                    result = "Login successfully";
                                    editor.commit();
                                    startActivityMain();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_REMEMBER)) {
            if (sharedpreferences.getBoolean(EConstant.MY_PREFERENCES_REMEMBER, false)) {
                startActivityMain();
            }
        }
    }

    private void startActivityMain() {
        Intent myIntent = new Intent(this, MainMenuActivity.class);
        startActivity(myIntent);
    }
}
