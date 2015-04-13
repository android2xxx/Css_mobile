package taskserver;

/**
 * Created by HieuHT on 04/01/2015.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import entity.EConstant;
import entity.EFileUploadResponse;
import entity.EResponse;

public class QueryHttpPostServiceTask extends AsyncTask<List<NameValuePair>, Integer, EResponse> {

    private  Context context;
    public QueryHttpPostServiceTask(Context context) {
        this.context = context;
    }

    @Override
    protected EResponse doInBackground(List<NameValuePair>... params) {
        EResponse objResponse = new EResponse();
        String url = "";
        for(NameValuePair valuePair : params[0])
        {
            if(valuePair.getName().equals(("URL")))
            {
                url = valuePair.getValue();
            }
        }
        Gson gson = new Gson();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params[0]));
            HttpResponse response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                objResponse = gson.fromJson(EntityUtils.toString(entity), EResponse.class);
            }
            else
                objResponse.setMessage("Error code is " + status);
        } catch (Exception e) {
            objResponse.setMessage( e.getMessage());
        }
        return objResponse;
    }

    @Override
    protected void onPostExecute(EResponse result) {
        Toast.makeText(context, "Post successfully", Toast.LENGTH_SHORT).show();
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }
}
