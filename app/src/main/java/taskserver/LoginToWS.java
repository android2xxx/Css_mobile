package taskserver;

/**
 * Created by HieuHT on 04/01/2015.
 */
import android.content.Context;
import android.os.AsyncTask;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.util.Log;

import function.Function;


public class LoginToWS extends AsyncTask<String, Integer, String> {
    private static final String SOAP_ACTION_UPLOAD_ATM_MAP = "http://tempuri.org/UploadAtmMap";
    private static final String OPERATION_NAME_UPLOAD_ATM_MAP = "UploadAtmMap";
    private static final String SOAP_ACTION_LOGIN = "http://tempuri.org/Login";
    private static final String OPERATION_NAME_LOGIN = "Login";
    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    private Context mContext;

    public LoginToWS(Context context) {
        mContext = context;
    }

    private String TAG = "Mic";
    @Override
    protected String doInBackground(String... params) {
        Log.i(TAG, "doInBackground");
        String content = "";
        if(Function.isURLReachable(mContext, params[2])) {
            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
                    OPERATION_NAME_LOGIN);
            request.addProperty("userName", params[0]);
            request.addProperty("passWord", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(
                    params[2]);
            try {
                httpTransport.call(SOAP_ACTION_LOGIN, envelope);
                Object obj1 = (Object) envelope.getResponse();
                content = obj1.toString();
            } catch (Exception exception) {
                content = "false";
                Log.i(TAG, "Some errors below: " + exception.toString());
            }
        }
        else
            content = "Server is not available";
        return content;
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }
}
