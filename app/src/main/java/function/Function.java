package function;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import entity.EConstant;
import entity.ESmsRep;

/**
 * Created by HieuHT on 04/02/2015.
 */
public class Function {
    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean isURLReachable(Context ctx, String soapAddress) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL(soapAddress);
                HttpURLConnection uric = (HttpURLConnection) url
                        .openConnection();
                uric.setConnectTimeout(10 * 1000); // 10 s.
                uric.connect();
                if (uric.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        try {
        /* int i = */
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        // Get the size of the file
        long length = file.length();
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static List<String> getOutboxSms(Context ctx) {
        if (null == ctx) {
            return new ArrayList<String>();
        }
        Uri uriSms = Uri.parse("content://sms/sent");
        String phoneNumber = EConstant.SERVICE_NUMBER_PHONE ;
        String sms = "address='"+ phoneNumber + "'";
//        Cursor cursor = contentResolver.query(uri, new String[] { "_id", "body", "date" }, sms, null,   null);
        Cursor cursor = ctx.getContentResolver().query(uriSms, null, null, null, null);
        List<String> listSms = new ArrayList<String>();
        String str_last_address = EConstant.SERVICE_NUMBER_PHONE.substring(4);
        String straddress, strbody;
        while (cursor.moveToNext())
        {
            straddress  = cursor.getString( cursor.getColumnIndex("address") );
//            straddress = "84983241066";
            if (true)//(straddress.contains(str_last_address))
            {
                strbody = cursor.getString(cursor.getColumnIndex("address")) + " | " + cursor.getString(cursor.getColumnIndex("body"));
                //strbody = cursor.getString(cursor.getColumnIndex("body"));
                listSms.add(strbody);
            }
        }
        return listSms;
//        List<ESmsRep> outboxSms = cursor2SmsArray(cursor);
//        if (!cursor.isClosed()) {
//            cursor.close();
//        }
//        return outboxSms;
    }

//    private static List<ESmsRep> cursor2SmsArray(Cursor cursor) {
//        if (null == cursor || 0 == cursor.getCount()) {
//            return new ArrayList<ESmsRep>();
//        }
//        List<ESmsRep> messages = new ArrayList<ESmsRep>();
//        try {
//            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                ESmsRep singleSms = new ESmsRep();
//                singleSms.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
//                singleSms.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
//                singleSms.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("date")) / 1000);
//                singleSms.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));
//                singleSms.setProtocol(cursor.getInt(cursor.getColumnIndexOrThrow("protocol")));
//                String smsBody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
//                byte[] bodyBytes = smsBody.getBytes("UTF8");
//                singleSms.setBody(TextUtils.htmlEncode(new String(bodyBytes, "UTF8")));
//                messages.add(singleSms);
//            }
//        } catch (Exception e) {
//        } finally {
//            cursor.close();
//        }
//        return messages;
//    }


    private static List<String> cursor2SmsArray(Cursor cursor) {
        List<String> listSms = new ArrayList<String>();
        while (cursor.moveToNext())
        {
            String strbody = cursor.getString( cursor.getColumnIndex("body") );
            listSms.add(strbody);
        }
        return listSms;
    }

}
