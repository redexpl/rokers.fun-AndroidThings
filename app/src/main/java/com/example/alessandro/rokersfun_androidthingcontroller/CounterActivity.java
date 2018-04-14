package com.example.alessandro.rokersfun_androidthingcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CounterActivity extends Activity {

    private Counter counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        counter = new Counter((TextView) findViewById(R.id.textView_counter));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        counter.close();
    }
}
