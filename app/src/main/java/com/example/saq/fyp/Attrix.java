package com.example.saq.fyp;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by hp on 2/12/2019.
 */

public class Attrix extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
    }
}
