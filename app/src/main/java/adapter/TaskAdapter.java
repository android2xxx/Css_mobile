package adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.microtecweb.css_mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/15/2015.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    private Context mContext;
    private List<Task> mTasks;
    public TaskAdapter(Context context, List<Task>objects) {
        super(context, R.layout.task_row_item,objects);
        this.mContext= context;
        this.mTasks=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater myLayoutInflater=LayoutInflater.from(mContext);
            convertView=myLayoutInflater.inflate(R.layout.task_row_item,null);
        }
        Task task=mTasks.get(position);
        TextView desTextView=(TextView)convertView.findViewById(R.id.task_description);
        desTextView.setText(task.getDescription());
        if(task.isCompleted()){
           desTextView.setPaintFlags(desTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            desTextView.setPaintFlags(desTextView.getPaintFlags()| ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        return convertView;
    }
}
