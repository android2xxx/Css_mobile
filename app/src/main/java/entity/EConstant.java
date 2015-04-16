package entity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HieuHT on 04/08/2015.
 */
public class EConstant {
    private static final String URL = "http://192.168.66.87:5559/Home/";
    //public static final String URL = "http://192.168.68.119/CSS_MVC/Home/";
    public static final String FOLDER_NAME = "CSS_MOBILE";


    public static final int RESULT_OK = -1;
    public static final String ACTION_PICK = "luminous.ACTION_PICK";
    public static final String ACTION_MULTIPLE_PICK = "luminous.ACTION_MULTIPLE_PICK";


    public static final int REQUEST_CODE_TAKE_PHOTO = 8888;
    public static final int REQUEST_CODE_TAKE_PHOTO_FOR_REQUESET_SERVICE = 8000;
    public static final int REQUEST_CODE_PHOTO_GALLERY = 9999;
    public static final int REQUEST_CODE_PHOTO_GALLERY_FOR_REQUESET_SERVICE = 9000;
    public static final int CHUNK_SIZE = 65536; // 64 * 1024 kb - dung lượng file upload mỗi lần
    private static final String SERVICE_NUMBER_PHONE = "+84983241066";
    private static final int SERVER_PORT = 8668 ;
    private static final String SERVER_CHAT = "192.168.66.87" ;
    public static final int LAST_NUM_QUANLITY = 9;

    public static final String MY_PREFERENCES = "CSS_MOBILE_PREFERENCES";
    public static final String MY_PREFERENCES_USER_ID = "PREFERENCES_USER_ID";
    public static final String MY_PREFERENCES_USER_NAME = "PREFERENCES_USER_NAME";
    public static final String MY_PREFERENCES_PASSWORD = "PREFERENCES_PASSWORD";
    public static final String MY_PREFERENCES_REMEMBER = "PREFERENCES_REMEMBER";

    public static final String MY_PREFERENCES_URL = "PREFERENCES_URL";
    public static final String MY_PREFERENCES_SERVER_CHAT = "PREFERENCES_SERVER_CHAT";
    public static final String MY_PREFERENCES_SERVER_PORT = "PREFERENCES_SERVER_PORT";
    public static final String MY_PREFERENCES_SERVICE_NUMBER_PHONE = "PREFERENCES_SERVICE_NUMBER_PHONE";

    public static String getURL(Activity activity)
    {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_URL))
           return sharedpreferences.getString(EConstant.MY_PREFERENCES_URL, EConstant.URL);
        else
            return EConstant.URL;
    }

    public static String getSERVER_CHAT(Activity activity)
    {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_SERVER_CHAT))
            return sharedpreferences.getString(EConstant.MY_PREFERENCES_SERVER_CHAT, EConstant.SERVER_CHAT);
        else
            return EConstant.SERVER_CHAT;
    }

    public static int getSERVER_PORT(Activity activity)
    {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_SERVER_PORT))
            return sharedpreferences.getInt(EConstant.MY_PREFERENCES_SERVER_PORT, EConstant.SERVER_PORT);
        else
            return EConstant.SERVER_PORT;
    }

    public static String getSERVICE_NUMBER_PHONE(Activity activity)
    {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(EConstant.MY_PREFERENCES_SERVICE_NUMBER_PHONE))
            return sharedpreferences.getString(EConstant.MY_PREFERENCES_SERVICE_NUMBER_PHONE, EConstant.SERVICE_NUMBER_PHONE);
        else
            return EConstant.SERVICE_NUMBER_PHONE;
    }    
}
