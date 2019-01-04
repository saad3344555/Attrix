package com.example.saq.fyp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MarkAttendance extends AppCompatActivity {
    private Button bt_markAttendance;
    private ImageView iv_pic;
    private Toolbar toolbar;
    private EditText et_date;
    private RecyclerView rv_attendance;
    byte[] imageBytes = {};
    List<AttendanceModel> attendanceModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Mark Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        imageBytes = getIntent().getByteArrayExtra("AttedanceImageBytes");



        iv_pic = findViewById(R.id.iv_pic);
        iv_pic.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));

        bt_markAttendance = findViewById(R.id.bt_login);
//        bt_markAttendance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                })
//            }
//        });


        et_date = findViewById(R.id.et_date);
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppGenericClass.getInstance(MarkAttendance.this).showDatePicker(et_date);
            }
        });

        rv_attendance = findViewById(R.id.rv_attendance);
        rv_attendance.setLayoutManager(new LinearLayoutManager(this));
        populateDummyData();
        rv_attendance.setAdapter(new AttendanceListAdapter(this, attendanceModels));
    }


    public void populateDummyData() {
        attendanceModels.add(new AttendanceModel("Muhammad Taha",
                "Muhammad Arshad",
                "B14101075", "Sec-A", "Morning", "BSCS", true));

        attendanceModels.add(new AttendanceModel("Maaz Aftab",
                "Aftab Ahemd",
                "B14101089", "Sec-A", "Morning", "BSCS", true));

        attendanceModels.add(new AttendanceModel("Muhammad Zakir",
                "Saleem Khan",
                "B14101090", "Sec-A", "Morning", "BSCS", true));

        attendanceModels.add(new AttendanceModel("Salman Ashraf",
                "Ashraf Khan",
                "B14101091", "Sec-A", "Morning", "BSCS", false));

        attendanceModels.add(new AttendanceModel("Wajeeha Chaudhry",
                "Chaudhry Ahmed",
                "B14101093", "Sec-A", "Morning", "BSCS", true));

        attendanceModels.add(new AttendanceModel("Huma Khan",
                "Ali Khan",
                "B14101094", "Sec-A", "Morning", "BSCS", false));

        attendanceModels.add(new AttendanceModel("Kamran Tariq",
                "Tariq Aziz",
                "B14101099", "Sec-A", "Morning", "BSCS", true));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
