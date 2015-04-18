package adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.microtecweb.css_mobile.R;

import java.util.List;

import entity.ETask;

/**
 * Created by User on 4/15/2015.
 */
public class TaskAdapter extends ArrayAdapter<ETask> {
    private Context mContext;
    private List<ETask> mETasks;

    public TaskAdapter(Context context, List<ETask> objects) {
        super(context, R.layout.row_task, objects);
        this.mContext = context;
        this.mETasks = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater myLayoutInflater = LayoutInflater.from(mContext);
            convertView = myLayoutInflater.inflate(R.layout.row_task, null);
        }
        ETask eTask = mETasks.get(position);
        TextView desTextView = (TextView) convertView.findViewById(R.id.task_description);
        desTextView.setText(eTask.getDescription());

        if (eTask.getCompleted()) {
            desTextView.setPaintFlags(desTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        return convertView;
    }
}
