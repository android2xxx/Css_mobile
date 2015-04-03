package adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.microtecweb.css_mobile.OpenServiceFragment;
import com.microtecweb.css_mobile.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HieuHT on 04/03/2015.
 */


public class OpenServiceAdapter extends BaseAdapter {


    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;


    public OpenServiceAdapter(Context  a, ArrayList<HashMap<String, String>> d) {

        data = d;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position * 100;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.row_openservice, null);

        TextView tvATMID = (TextView) vi.findViewById(R.id.tvATMID);
        TextView tvBank = (TextView) vi.findViewById(R.id.tvBank);
        TextView tvLocation = (TextView) vi.findViewById(R.id.tvLocation);
        TextView tvIssue = (TextView) vi.findViewById(R.id.tvIssue);

        HashMap<String, String> lstOpenService = new HashMap<String, String>();
        lstOpenService = data.get(position);

        // Setting all values in listview
        tvATMID.setText(lstOpenService.get(OpenServiceFragment.KEY_ATMID));
        tvBank.setText(lstOpenService.get(OpenServiceFragment.KEY_BANK));
        tvLocation.setText(lstOpenService.get(OpenServiceFragment.KEY_LOCATION));
        tvIssue.setText(lstOpenService.get(OpenServiceFragment.KEY_ISSUE));
        return vi;
    }
}