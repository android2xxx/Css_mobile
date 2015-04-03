package taskserver;

/**
 * Created by HieuHT on 04/01/2015.
 */
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.util.Log;

import java.io.IOException;

import function.Function;


public class GetOpenServiceTask extends AsyncTask<String, Integer, String> {
    private static final String SOAP_ACTION_UPLOAD_ATM_MAP = "http://tempuri.org/UploadAtmMap";
    private static final String OPERATION_NAME_UPLOAD_ATM_MAP = "UploadAtmMap";
    private static final String SOAP_ACTION_LOGIN = "http://tempuri.org/Login";
    private static final String OPERATION_NAME_LOGIN = "Login";
    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    private Context mContext;

    public GetOpenServiceTask(Context context) {
        mContext = context;
    }

    private String TAG = "Mic";
    @Override
    protected String doInBackground(String... params) {
        Log.i(TAG, "doInBackground");
        String content = "";
        if(Function.isURLReachable(mContext, params[0])) {
            StringBuilder url = new StringBuilder(params[0]);

            HttpGet get = new HttpGet (url.toString());
            DefaultHttpClient client = new DefaultHttpClient();


         /*   ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                content = client.execute(get, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
            HttpResponse response = null;



            try {
                response = client.execute(get);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int status = response.getStatusLine().getStatusCode();

            if(status == 200)
            {
                HttpEntity entity = response.getEntity();
                try {
                    content = EntityUtils.toString(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                content = "Server is not available";
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