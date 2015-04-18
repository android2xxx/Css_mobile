package com.microtecweb.css_mobile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapter.TaskAdapter;
import entity.EConstant;
import entity.ETask;


public class TodoListActivity extends Activity implements AdapterView.OnItemClickListener {
    private EditText mTaskInput;
    private ListView mListView;
    private TaskAdapter mAdapter;
    AdapterView.OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        //Parse.initialize(this,"1hMy3Pm1yLVIZk92xgUu0SYv14Np1FNjGEbudBg7","MrDHHAq4drp3ZwVmWl9r55NgHmoacrBDQJEH8YfM");

        //ParseObject.registerSubclass(Task.class);
        //Parse.enableLocalDatastore(this);

        //Parse.initialize(this, "DOXl5evSUwJQGgjjxhhYpMWd8DwcJtQXadCwdCQ5", "CJUWQH7og4si4CjAn1tL9CwSHnsxiYARzce1fSkB");

       /* ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/
        final Activity activity = this;
        onItemClickListener = this;
        mTaskInput = (EditText) findViewById(R.id.task_input);
        mListView = (ListView) findViewById(R.id.task_list);
        mListView.setOnItemClickListener(onItemClickListener);
        mAdapter = new TaskAdapter(activity, new ArrayList<ETask>());
        /*
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            ParseUser.logInInBackground("Hieu", "123456", new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        ParseAnalytics.trackAppOpened(getIntent());
                        ParseObject.registerSubclass(Task.class);
                        updateData();
                    } else {

                    }
                }
            });
        }
        else
        {
            ParseAnalytics.trackAppOpened(getIntent());
            ParseObject.registerSubclass(Task.class);
            updateData();
        }*/
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null)
            updateData();
        else {

            if (EConstant.getPasswordToDoList(activity).equals("") && EConstant.getPasswordToDoList(activity).equals("")) {
                Toast.makeText(this, "Update user - pass", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Check user - pass", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void updateData() {
        ParseQuery<ETask> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("ACL", ParseUser.getCurrentUser().getACL());
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<ETask>() {
            @Override
            public void done(List<ETask> eTasks, ParseException e) {
                if (eTasks != null) {
                    mAdapter.clear();
                    for (int i = 0; i < eTasks.size(); i++) {
                        mAdapter.add(eTasks.get(i));
                    }
                    mListView.setAdapter(mAdapter);
                }
            }
        });
    }

    public void createTask(View v) {
        if (mTaskInput.getText().length() > 0) {
            ETask t = new ETask();
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                t.setACL(new ParseACL(currentUser));
                t.setDescription(mTaskInput.getText().toString());
                t.setCompleted(false);
                t.saveEventually();
                mAdapter.insert(t, 0);
                mTaskInput.setText("");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_todo_list, menu);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ETask eTask = mAdapter.getItem(position);
        TextView taskDescription = (TextView) view.findViewById(R.id.task_description);

        eTask.setCompleted(!eTask.getCompleted());

        if (eTask.getCompleted()) {
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        /*task.pinInBackground(EConstant.FOLDER_NAME, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (isFinishing()) {
                    return;
                }
                if (e == null) {

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });*/
        eTask.saveEventually();
    }
}
