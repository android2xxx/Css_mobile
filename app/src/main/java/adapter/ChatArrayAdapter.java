package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import entity.EChatMessage;
import com.microtecweb.css_mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/12/2015.
 */
public class ChatArrayAdapter extends ArrayAdapter<EChatMessage> {
    private TextView chatText;
    private List<EChatMessage> EChatMessageList =new ArrayList<EChatMessage>();
    private LinearLayout singMessageContainer;
    public ChatArrayAdapter(Context context,int textViewResourceID){
        super(context,textViewResourceID);
    }
    @Override
    public void add(EChatMessage object) {
        EChatMessageList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.EChatMessageList.size();
    }

    public EChatMessage getItem(int index) {
        return this.EChatMessageList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        if(row==null){
            LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.chat_singlemessage,parent,false);
        }
        singMessageContainer=(LinearLayout)row.findViewById(R.id.singleMessageContainer);
        EChatMessage EChatMessageObj =getItem(position);//=getItemId(position);
        chatText=(TextView)row.findViewById(R.id.singleMessage);
        chatText.setText(EChatMessageObj.messsage);
        chatText.setBackgroundResource(EChatMessageObj.left ? R.drawable.bubble_a : R.drawable.bubble_b);
        singMessageContainer.setGravity(EChatMessageObj.left? Gravity.LEFT:Gravity.RIGHT);
        return row;
    }
    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
