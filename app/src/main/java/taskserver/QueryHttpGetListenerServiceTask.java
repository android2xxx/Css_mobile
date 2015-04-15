package taskserver;

/**
 * Created by HieuHT on 04/01/2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import function.Function;


public class QueryHttpGetListenerServiceTask extends AsyncTask<String, Integer, String> {
    private MicFragment fragment;
    private Context mContext;
    ProgressDialog asyncDialog;

    public QueryHttpGetListenerServiceTask(Context context, MicFragment fragment) {
        mContext = context;
        asyncDialog = new ProgressDialog(mContext);
        this.fragment = fragment;
    }

    @Override
    protected String doInBackground(String... params) {
        String content = "";
        if (Function.isURLReachable(mContext, params[0])) {
            StringBuilder url = new StringBuilder(params[0]);
            HttpGet get = new HttpGet(url.toString());
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            try {
                response = client.execute(get);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                try {
                    content = EntityUtils.toString(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                content = "Server is not available";
            }
        } else
            content = "Server is not available";
        return content;
    }

    @Override
    protected void onPostExecute(String result) {
        asyncDialog.dismiss();
        if (fragment == null)
            ((MicActivity) mContext).onTaskCompleted(result);
        else
            fragment.onTaskCompleted(result);
        //super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        asyncDialog.setTitle("Loading");
        asyncDialog.setMessage("Please wait...");
        asyncDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }
}
