package com.example.alessandro.rokersfun_androidthingcontroller;


import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Counter implements Runnable {
    private int count;
    private Handler handler;
    private TextView mCounter;

    private static long DELAY_MILLIS=60*1000;
    //TODO: set right url address
    private static String URL="http://www.google.com";

    public Counter(TextView mCounter) {
        this.count=0;
        this.handler=new Handler();
        this.mCounter = mCounter;
        handler.post(this);
    }

    public void close() {
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        //TODO: get data from rest api
        new HttpGetter().execute(URL);
        handler.postDelayed(this,DELAY_MILLIS);
    }

    class HttpGetter extends AsyncTask<String, String, String> {

        private final String COUNTER_FIELD = "cnt";

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream input = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(input));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line+"\n");

                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.d("ERROR","MalformedURLException@Counter:75");
            } catch (IOException e) {
                Log.d("ERROR","IOException@Counter:77");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(s);
                mCounter.setText(jsonObject.getInt("cnt"));
            } catch (ParseException e) {
                Log.d("ERROR","ParseException@Counter:88");
            } catch (JSONException e) {
                Log.d("ERROR","JSONException@Counter:90");
            } catch (NullPointerException e) {
                mCounter.setText("0");
                Log.d("ERROR","NullPointerException@Counter:93");
            }
        }
    }
}
