package com.example.saq.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class EditAttendance extends AppCompatActivity {
    EditText et_date;
    RecyclerView rv_attendance;
    Toolbar toolbar;
    List<AttendanceModel> attendanceModels = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_edit);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rv_attendance = findViewById(R.id.rv_attendance);
        et_date = findViewById(R.id.et_date);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppGenericClass.getInstance(EditAttendance.this).showDatePicker(et_date);
            }
        });

        rv_attendance.setLayoutManager(new LinearLayoutManager(this));
        populateDummyData();
        rv_attendance.setAdapter(new AttendanceListAdapter(this,attendanceModels));
    }

    public void populateDummyData(){
        attendanceModels.add(new AttendanceModel("Muhammad Taha",
                "Muhammad Arshad",
                "B14101075","Sec-A","Morning","BSCS",true));

        attendanceModels.add(new AttendanceModel("Maaz Aftab",
                "Aftab Ahemd",
                "B14101089","Sec-A","Morning","BSCS",true));

        attendanceModels.add(new AttendanceModel("Muhammad Zakir",
                "Saleem Khan",
                "B14101090","Sec-A","Morning","BSCS",true));

        attendanceModels.add(new AttendanceModel("Salman Ashraf",
                "Ashraf Khan",
                "B14101091","Sec-A","Morning","BSCS",false));

        attendanceModels.add(new AttendanceModel("Wajeeha Chaudhry",
                "Chaudhry Ahmed",
                "B14101093","Sec-A","Morning","BSCS",true));

        attendanceModels.add(new AttendanceModel("Huma Khan",
                "Ali Khan",
                "B14101094","Sec-A","Morning","BSCS",false));

        attendanceModels.add(new AttendanceModel("Kamran Tariq",
                "Tariq Aziz",
                "B14101099","Sec-A","Morning","BSCS",true));

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
