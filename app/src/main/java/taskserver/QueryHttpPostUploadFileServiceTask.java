package taskserver;

/**
 * Created by HieuHT on 04/01/2015.
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.EConstant;
import entity.EFileUploadResponse;
import entity.EResponse;

public class QueryHttpPostUploadFileServiceTask extends AsyncTask<String, Integer, EResponse> {

    public QueryHttpPostUploadFileServiceTask(){

    }

    @Override
    protected EResponse doInBackground(String... params) {
        EResponse objResponse = new EResponse();
        StringBuilder url = new StringBuilder(params[0]);
        byte[] byte_arr = Base64.decode(params[1], Base64.DEFAULT);
        int Offset = 0; // starting offset.
        int ChunkSize = EConstant.CHUNK_SIZE;
        //define the buffer array
        byte[] Buffer = new byte[ChunkSize];
        //opening the file for read.
        ByteArrayInputStream fs = new ByteArrayInputStream(byte_arr);
        long FileSize = byte_arr.length;
        // reading the file.
        Gson gson = new Gson();
        int BytesRead = 0;
        String fileRename = "";
        while (Offset != FileSize) // continue uploading the file chunks until offset = file size.
        {
            BytesRead = fs.read(Buffer, 0, ChunkSize); // read the next chunk
            if (BytesRead != Buffer.length) {
                ChunkSize = BytesRead;
                byte[] TrimmedBuffer = new byte[BytesRead];
                System.arraycopy(Buffer, 0, TrimmedBuffer, 0, BytesRead);// .Copy(Buffer, TrimmedBuffer, BytesRead);
                Buffer = TrimmedBuffer; // the trimmed buffer should become the new 'buffer'
            }
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(String.valueOf(url));
            //Post Data
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("fileName", params[2]));
            nameValuePair.add(new BasicNameValuePair("fileServiceRename", fileRename));
            nameValuePair.add(new BasicNameValuePair("sBuffer", Base64.encodeToString(Buffer, Base64.DEFAULT)));
            nameValuePair.add(new BasicNameValuePair("Offset", String.valueOf(Offset)));
            nameValuePair.add(new BasicNameValuePair("username", params[3]));
            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                HttpResponse response = httpClient.execute(httpPost);
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    EFileUploadResponse objFileUploadResponse = gson.fromJson(EntityUtils.toString(entity), EFileUploadResponse.class);
                    objResponse.setStatus(objFileUploadResponse.getStatus());
                    if (objFileUploadResponse.getStatus())
                        fileRename = objFileUploadResponse.getFileReName();
                    else {
                        objResponse.setMessage(objFileUploadResponse.getMessage());
                        break;
                    }
                } else {
                    objResponse.setMessage("Server is not available");
                    break;
                }
            } catch (Exception e) {
                objResponse.setMessage(e.getMessage());
                break;
            }
            Offset += BytesRead;
        }
        return objResponse;
    }

    @Override
    protected void onPostExecute(EResponse result) {
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
