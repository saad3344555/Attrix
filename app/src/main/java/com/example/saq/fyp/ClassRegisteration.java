package com.example.saq.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ClassRegisteration extends AppCompatActivity implements View.OnClickListener, FirebaseHelper.ClassCallback {

    Toolbar toolbar;
    TextView tv_register;
    private EditText et_program, et_section, et_shift, et_batch, et_classYear, et_courseName, et_courseNumber, et_classCode;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_registeration);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Class Registeration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tv_register = findViewById(R.id.tv_register);

        tv_register.setOnClickListener(this);


        et_classCode = findViewById(R.id.et_classCode);
        et_program = findViewById(R.id.et_program);
        et_shift = findViewById(R.id.et_shift);
        et_batch = findViewById(R.id.et_batchNo);
        et_courseName = findViewById(R.id.et_courseName);
        et_classYear = findViewById(R.id.et_classYear);
        et_section = findViewById(R.id.et_section);
        et_courseNumber = findViewById(R.id.et_courseId);

        firebaseHelper = new FirebaseHelper(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                firebaseHelper.setClassCallback(ClassRegisteration.this);
                firebaseHelper.createClass(getClassModel());
                break;
        }
    }

    private ClassModel getClassModel() {
        ClassModel cm = new ClassModel();
        List<String> enrolledStudents = new ArrayList<>();
        enrolledStudents.add("-1");
        cm.setYearOfTeaching(et_classYear.getText().toString());
        cm.setBatchNo(et_batch.getText().toString());
        cm.setYearOfTeaching(et_classYear.getText().toString());
        cm.setProgram(et_program.getText().toString());
        cm.setTitle(et_courseName.getText().toString());
        cm.setCourseNo(et_courseNumber.getText().toString());
        cm.setTeacherId(AppGenericClass.getInstance(this).getPrefs(AppGenericClass.TOKEN));
        cm.setShift(et_shift.getText().toString());
        cm.setClassCode(et_classCode.getText().toString());
        cm.setSection(et_section.getText().toString());
        cm.setClassId(AppGenericClass.getInstance(this).getCurrentYear());
        cm.setEnrolledStudents(enrolledStudents);
        cm.setShiftSectionProgram(AppGenericClass.getInstance(this).getCurrentYear());
        cm.setDetail();
        return cm;
    }

    @Override
    public void onCreated(int code) {
        if (code == 200) {
            Toast.makeText(this, "Class Created", Toast.LENGTH_SHORT).show();
            setResult(code);
            finish();
        } else {
            Toast.makeText(this, "Class Creation Failed", Toast.LENGTH_SHORT).show();
            setResult(code);
            finish();
        }

    }
}
