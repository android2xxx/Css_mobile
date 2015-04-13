package com.microtecweb.css_mobile;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class client extends ActionBarActivity {
    Button buttonConnect;
    TextView textResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        buttonConnect = (Button)findViewById(R.id.connect);
        textResponse = (TextView)findViewById(R.id.response);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyClientTask myClientTask=new MyClientTask("192.168.1.106",8182);
                myClientTask.execute();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class MyClientTask extends AsyncTask<Void,Void,Void> {
        String strAddress;
        int Port;
        String reponse="";
        TextView textView;

        MyClientTask(String ServerIP,int Port){
            this.strAddress=ServerIP;
            this.Port=Port;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Socket client=new Socket(strAddress,Port);
                OutputStream outToServer=client.getOutputStream();
                DataOutputStream out=new DataOutputStream(outToServer);
                out.writeUTF("I am a client"+client.getLocalSocketAddress());
                InputStream inFromServer=client.getInputStream();
                DataInputStream in=new DataInputStream(inFromServer);
                reponse=in.readUTF();
                client.close();
            }catch (Exception e){
                Log.i("client",e.toString());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.setText(reponse);
            super.onPostExecute(result);
        }
    }
}
