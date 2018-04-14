package com.example.alessandro.rokersfun_androidthingcontroller;

import android.os.Handler;
import android.util.Log;

import com.google.android.things.contrib.driver.ht16k33.Ht16k33;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Counter implements Runnable {
    private Handler handler;
    private AlphanumericDisplay display;

    private static long DELAY_MILLIS=2*1000;
    //TODO: set right url address
    private static String URL="http://52.212.172.20:8080/counter";

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
        new HttpGetter_counter().execute(URL);
        handler.postDelayed(this,DELAY_MILLIS);
    }

    private class HttpGetter_counter extends HttpGetter {

        @Override
        protected void onPostExecute(String s) {
            int count=0;
            try {
                JSONObject jsonObject = new JSONObject(s);
                count=jsonObject.getInt(COUNTER_FIELD);
            } catch (NullPointerException e) {
                Log.d("ERROR","NullPointerException");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                display.display(count);
                display.setEnabled(true);
            } catch (IOException e) {
                Log.d("ERROR","IOException");
            }
        }
    }
}
