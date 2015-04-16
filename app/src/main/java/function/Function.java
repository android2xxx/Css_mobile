package function;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.EConstant;

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
        String phoneNumber = EConstant.getSERVICE_NUMBER_PHONE((Activity) ctx);
        String sms = "address='" + phoneNumber + "'";
        Date dateSms;
        long l;
//        Cursor cursor = contentResolver.query(uri, new String[] { "_id", "body", "date" }, sms, null,   null);
        Cursor cursor = ctx.getContentResolver().query(uriSms, null, null, null, null);
        List<String> listSms = new ArrayList<String>();
        String str_last_address = EConstant.getSERVICE_NUMBER_PHONE((Activity) ctx).substring(4);
        String straddress, strbody;
        while (cursor.moveToNext()) {
            straddress = cursor.getString(cursor.getColumnIndex("address")).replace(" ", "");
            l = (long) cursor.getColumnIndex("date");
            dateSms = new java.util.Date(l);
            if (straddress.contains(str_last_address)) {
                strbody = cursor.getString(cursor.getColumnIndex("date")) + " | " + cursor.getString(cursor.getColumnIndex("body"));
                listSms.add(strbody);
            }
        }
        return listSms;
    }


    public static List<String> getOutboxSms(Context ctx, long from, long to) {
        if (null == ctx) {
            return new ArrayList<String>();
        }
        Uri uriSms = Uri.parse("content://sms/sent");
        String phoneNumber = EConstant.getSERVICE_NUMBER_PHONE((Activity) ctx);
        String sms = "address='" + phoneNumber + "'";
        long l;
        Cursor cursor = ctx.getContentResolver().query(uriSms, null, null, null, null);
        List<String> listSms = new ArrayList<String>();
        String str_last_address = EConstant.getSERVICE_NUMBER_PHONE((Activity) ctx).substring(4);
        String straddress, strbody, time;
        while (cursor.moveToNext()) {
            straddress = cursor.getString(cursor.getColumnIndex("address")).replace(" ", "");
            String d = cursor.getString(cursor.getColumnIndex("date"));
            l = (long) Long.parseLong(d);

            if (straddress.contains(str_last_address) && (from <= l) && (l <= to)) {
                strbody = cursor.getString(cursor.getColumnIndex("date")) + " | " + cursor.getString(cursor.getColumnIndex("body"));
                Date date = new Date(l);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = formatter.format(date);
                listSms.add(time + " | " + strbody);
            }
        }
        return listSms;
    }


}
