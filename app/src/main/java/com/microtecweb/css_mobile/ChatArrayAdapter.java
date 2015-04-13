package com.microtecweb.css_mobile;

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

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/12/2015.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {
    private TextView chatText;
    private List<ChatMessage> chatMessageList=new ArrayList<ChatMessage>();
    private LinearLayout singMessageContainer;
    public ChatArrayAdapter(Context context,int textViewResourceID){
        super(context,textViewResourceID);
    }
    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        if(row==null){
            LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.chat_singlemessage,parent,false);
        }
        singMessageContainer=(LinearLayout)row.findViewById(R.id.singleMessageContainer);
        ChatMessage chatMessageObj=getItem(position);//=getItemId(position);
        chatText=(TextView)row.findViewById(R.id.singleMessage);
        chatText.setText(chatMessageObj.messsage);
        chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_a : R.drawable.bubble_b);
        singMessageContainer.setGravity(chatMessageObj.left? Gravity.LEFT:Gravity.RIGHT);
        return row;
    }
    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
