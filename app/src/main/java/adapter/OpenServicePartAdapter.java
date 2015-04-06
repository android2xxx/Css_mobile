package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.microtecweb.css_mobile.R;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HieuHT on 04/03/2015.
 */


public class OpenServicePartAdapter extends BaseAdapter {


    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public static final String KEY_PART_ID = "PartId";
    public static final String KEY_PART_NAME = "PartName";
    public static final String KEY_PART_QUANTITY = "Quantity";
    public static final String KEY_STORE_NAME = "StoreName";

    public OpenServicePartAdapter(Context a, ArrayList<HashMap<String, String>> d) {

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
        HashMap<String, String> lstOpenService = new HashMap<String, String>();
        return Long.parseLong(data.get(position).get(KEY_PART_ID));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.row_servicepart, null);

        TextView tvPartName = (TextView) vi.findViewById(R.id.tvPartName);
        TextView tvQuantity = (TextView) vi.findViewById(R.id.tvQuantity);
        TextView tvStore = (TextView) vi.findViewById(R.id.tvStore);

        HashMap<String, String> lstServicePart = new HashMap<String, String>();
        lstServicePart = data.get(position);

        // Setting all values in listview
        tvPartName.setText(lstServicePart.get(KEY_PART_NAME));
        tvQuantity.setText(lstServicePart.get(KEY_PART_QUANTITY));
        tvStore.setText(lstServicePart.get(KEY_STORE_NAME));
        return vi;
    }
}