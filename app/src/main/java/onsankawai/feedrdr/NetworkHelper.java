package onsankawai.feedrdr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Streamphony on 14/05/2015.
 */
public class NetworkHelper{

    private static NetworkHelper mInstance = null;
    private GoogleApiClient mGoogleApiClient;

    public static NetworkHelper getInstance() {
        if(mInstance == null) {
            mInstance = new NetworkHelper();
        }
        return mInstance;
    }

    private NetworkHelper(){

    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }



    public static InputStream doRequest(String resURL, String queryString, String method) {
        try {
            URL url = new URL(resURL);
            if(method.equals("GET") && queryString != null) {
                url = new URL(resURL+"?"+queryString);
            }

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method);
            conn.setDoInput(true);

            if(method.equals("POST")) {
                conn.setDoOutput(true);
                Writer writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(queryString);
                writer.flush();
                writer.close();
            }

            conn.connect();
            return conn.getInputStream();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
