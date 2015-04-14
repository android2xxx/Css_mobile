package adapter;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microtecweb.css_mobile.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.EDetailService;
import entity.EPart;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private final List<EDetailService> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public MyExpandableListAdapter(Activity act, List<EDetailService> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final EDetailService objDetailService = (EDetailService) getGroup(groupPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_closeservice_detail, null);
        }

        TextView tvATMID = (TextView) convertView.findViewById(R.id.tvATMID);
        TextView tvSerial = (TextView) convertView.findViewById(R.id.tvSerial);
        TextView tvBank = (TextView) convertView.findViewById(R.id.tvBank);
        TextView tvBranch = (TextView) convertView.findViewById(R.id.tvBranch);
        TextView tvServiceId = (TextView) convertView.findViewById(R.id.tvServiceId);
        TextView tvContract = (TextView) convertView.findViewById(R.id.tvContract);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
        TextView tvStartTime = (TextView) convertView.findViewById(R.id.tvStartTime);
        TextView tvIssue = (TextView) convertView.findViewById(R.id.tvIssue);
        TextView tvSolution = (TextView) convertView.findViewById(R.id.tvSolution);

        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

        tvATMID.setText(objDetailService.getAtmId());
        tvSerial.setText(objDetailService.getSerial());
        tvBank.setText(objDetailService.getBank());
        tvBranch.setText(objDetailService.getBranch());
        tvServiceId.setText(objDetailService.getServiceId().toString());
        tvContract.setText(objDetailService.getContract());
        tvLocation.setText(objDetailService.getLocation());
        tvStartTime.setText(df.format(objDetailService.getStartTime()));
        tvIssue.setText(objDetailService.getIssue());
        tvSolution.setText(objDetailService.getSolution());

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
            ListView lstServicePart = (ListView) convertView.findViewById(R.id.lstServicePart);
            OpenServicePartAdapter adapter = new OpenServicePartAdapter(activity, servicePartList);
            lstServicePart.setAdapter(adapter);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_closeservice, null);
        }
        EDetailService group = (EDetailService) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText("ServiceId: " + group.getServiceId().toString());
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
