package com.microtecweb.css_mobile;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ChatActivity extends Activity {

    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;

    Intent intent;
    private boolean side = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.chat);
        buttonSend = (Button) findViewById(R.id.buttonSend);

        listView = (ListView) findViewById(R.id.listView1);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN) && (keycode==KeyEvent.KEYCODE_ENTER)){
                    return sendChatMessage();
                }
                    return false;

            }
        });
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                side=false;
                sendChatMessage();
            }
        });
        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }
    private boolean sendChatMessage(){
        ChatClientTask myClientTask=new ChatClientTask("192.168.35.101",8182);
        myClientTask.execute();
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        Log.d(TAG,"Message sent :"+chatText.getText().toString());
        chatText.setText("");
       // side = !side;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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
    public class ChatClientTask extends AsyncTask<Void,Void,Void>{
        String remoteIP;
        int Port;
        String reponse="";
        ChatClientTask(String IP,int port){
            this.remoteIP=IP;
            this.Port=port;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Socket client=new Socket(remoteIP,Port);
                OutputStream outToServer=client.getOutputStream();
                DataOutputStream out=new DataOutputStream(outToServer);
                out.writeUTF(chatText.getText().toString()+client.getLocalSocketAddress());
                InputStream inFromServer=client.getInputStream();
                DataInputStream in=new DataInputStream(inFromServer);
                reponse=in.readUTF();
                client.close();
            }catch (Exception e){
                Log.i("client", e.toString());
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            side=true;
            chatArrayAdapter.add(new ChatMessage(side, reponse));
            super.onPostExecute(aVoid);
        }
    }
}
