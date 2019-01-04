package com.example.saq.fyp;

import android.content.Intent;
import android.graphics.Movie;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimerTask timer = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        };

        Timer tm = new Timer();
        tm.schedule(timer,3000);

    }

}
