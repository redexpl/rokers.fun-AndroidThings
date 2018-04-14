package com.example.alessandro.rokersfun_androidthingcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DashboardActivity extends Activity implements Runnable {

    private Counter counter;
    private WebView webView;
    private TextView mUrl;
    private TextView mCounter;
    private Handler handler;
    private String previousUrl="";

    private static long DELAY_MILLIS = 10 * 1000;

    private static String TOPIC = "fakenews";
    private static int QUANTITY = 1;
    private static String URL = "http://52.212.172.20:8080/" + TOPIC + "?qnt=" + Integer.toString(QUANTITY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        counter = new Counter();
        mUrl = findViewById(R.id.textView_url);
        mCounter = findViewById(R.id.textView_counter);
        webView = findViewById(R.id.webView);
        handler = new Handler();
        handler.post(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        counter.close();
    }

    @Override
    public void run() {
        new HttpGetter_TopFakeNews().execute(URL);
        handler.postDelayed(this,DELAY_MILLIS);
    }


    private class HttpGetter_TopFakeNews extends HttpGetter {

        @Override
        protected void onPostExecute(String s) {
            JSONObject topFake = null;
            try {
                JSONArray jsonArray = new JSONArray(s);
                //get first and only element sorted by counter
                topFake = jsonArray.getJSONObject(0);
            } catch (NullPointerException e) {
                Log.d("ERROR", "NullPointerException");
            } catch (JSONException e) {
                Log.d("ERROR", "JSONException");
            }
            try {
                String url=topFake.getString("url");
                if(url==null || url.isEmpty()) return;
                mUrl.setText("URL: " + url);
                mCounter.setText("Counter: " + topFake.getInt("counter"));
                if(!url.equals(previousUrl)) {
                    webView.loadUrl(url);
                    previousUrl=url;
                }
            } catch (JSONException e) {
                Log.d("ERROR", "JSONException");
            } catch (NullPointerException e) {
                Log.d("ERROR", "NullPointerException");
            }
        }
    }
}
