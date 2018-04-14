package com.example.alessandro.rokersfun_androidthingcontroller;


import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.contrib.driver.ht16k33.Ht16k33;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Counter implements Runnable {
    private Handler handler;
    private AlphanumericDisplay display;

    //TODO: check delay
    private static long DELAY_MILLIS=10*1000;
    //TODO: set right url address
    private static String URL="http://10.17.2.120:8080/test/counter";

    private static String COUNTER_FIELD = "counter";


    public Counter() {
        this.handler=new Handler();
        try {
            this.display = RainbowHat.openDisplay();
            this.display.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX);
        } catch (IOException e) {
            //TODO: handle exception
            Log.d("ERROR","Unable to open alphanumeric dispaly");
        }
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


        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream input = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(input));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.d("ERROR","MalformedURLException");
            } catch (IOException e) {
                Log.d("ERROR","IOException");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            long count=0;
            try {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(s);
                count=(long)jsonObject.get(COUNTER_FIELD);
            } catch (ParseException e) {
                Log.d("ERROR","ParseException");
            } catch (NullPointerException e) {
                Log.d("ERROR","NullPointerException");
            }

            try {
                display.display((int)count);
                display.setEnabled(true);
            } catch (IOException e) {
                Log.d("ERROR","IOException");
            }
        }
    }
}
