package com.microtecweb.css_mobile;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapter.Task;
import adapter.TaskAdapter;


public class TodoList extends Activity implements AdapterView.OnItemClickListener {
    private EditText mTaskInput;
    private ListView mListView;
    private TaskAdapter mAdapter;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Task task = mAdapter.getItem(position);
        TextView taskDescription = (TextView) view.findViewById(R.id.task_description);

        task.setCompleted(!task.isCompleted());

        if(task.isCompleted()){
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        task.saveEventually();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Parse.initialize(this,"1hMy3Pm1yLVIZk92xgUu0SYv14Np1FNjGEbudBg7","MrDHHAq4drp3ZwVmWl9r55NgHmoacrBDQJEH8YfM");
        ParseAnalytics.trackAppOpened(getIntent());
        ParseObject.registerSubclass(Task.class);
        mAdapter=new TaskAdapter(this,new ArrayList<Task>());
        mTaskInput=(EditText)findViewById(R.id.task_input);
        mListView=(ListView)findViewById(R.id.task_list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        updateData();
    }
    public void updateData(){
        ParseQuery<Task> query=ParseQuery.getQuery(Task.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (tasks!=null){
                    mAdapter.clear();
                    for (int i=0;i<tasks.size();i++){
                        mAdapter.add(tasks.get(i));
                    }
                }
            }
        });

    }
    public void createTask(View v){
        if(mTaskInput.getText().length()>0){
            Task t=new Task();
            t.setACL(new ParseACL(ParseUser.getCurrentUser()));
            //t.setUser(ParseUser.getCurrentUser());
            t.setDescription(mTaskInput.getText().toString());
            t.setCompleted(false);
            t.saveEventually();
            mAdapter.insert(t, 0);
            mTaskInput.setText("");
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
}
