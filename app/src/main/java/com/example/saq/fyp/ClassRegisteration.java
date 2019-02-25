package com.example.saq.fyp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClassRegisteration extends AppCompatActivity implements View.OnClickListener, FirebaseHelper.ClassCallback {

    Toolbar toolbar;
    TextView tv_register;
    private EditText et_program, et_section, et_shift, et_batch, et_classYear, et_courseName, et_courseNumber, et_classCode;
    FirebaseHelper firebaseHelper;
    String shiftsOptions[] = {"Morning", "Evening"};
    String programOptions[] = {"CS", "SE"};
    String yearOptions[] = {"1", "2", "3", "4"};
    String sectionOptions[] = {"A", "B"};
    String selectedShift = "";
    String selectedPgm = "";
    String selectedYear = "";
    String selectedSec = "";

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
        et_shift.setClickable(true);
        et_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ClassRegisteration.this);
                mBuilder.setTitle("Choose Shift");
                mBuilder.setSingleChoiceItems(shiftsOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedShift = shiftsOptions[i];
                        et_shift.setText(selectedShift);
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.show();
            }
        });
        et_classYear.setClickable(true);
        et_classYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ClassRegisteration.this);
                mBuilder.setTitle("Choose Year");
                mBuilder.setSingleChoiceItems(yearOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedYear = yearOptions[i];
                        et_classYear.setText(selectedYear);
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.show();
            }
        });
        et_program.setClickable(true);
        et_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ClassRegisteration.this);
                mBuilder.setTitle("Choose Program");
                mBuilder.setSingleChoiceItems(programOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedPgm = programOptions[i];
                        et_program.setText(selectedPgm);
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.show();
            }
        });
        et_section.setClickable(true);
        et_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ClassRegisteration.this);
                mBuilder.setTitle("Choose Section");
                mBuilder.setSingleChoiceItems(sectionOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedSec = sectionOptions[i];
                        et_section.setText(selectedSec);
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.show();
            }
        });

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
                if (validateFields()) {

                    firebaseHelper.setClassCallback(ClassRegisteration.this);
                    firebaseHelper.createClass(getClassModel());
                    break;
                } else Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        if (selectedPgm.equals("") || selectedSec.equals("") || selectedShift.equals("") || selectedYear.equals("")
                || et_batch.getText().toString().isEmpty() || et_classCode.getText().toString().isEmpty() || et_courseName.getText().toString().isEmpty() ||
                et_courseNumber.getText().toString().isEmpty())
            return false;
        else return true;
    }

    private ClassModel getClassModel() {
        ClassModel cm = new ClassModel();
        List<String> enrolledStudents = new ArrayList<>();
        enrolledStudents.add("-1");
        cm.setBatchNo(et_batch.getText().toString());
        cm.setYearOfTeaching(et_classYear.getText().toString() + "-" + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()));
        cm.setProgram(et_program.getText().toString());
        cm.setTitle(et_courseName.getText().toString());
        cm.setCourseNo(et_courseNumber.getText().toString());
        cm.setTeacherId(AppGenericClass.getInstance(this).getPrefs(AppGenericClass.TOKEN));
        cm.setShift(et_shift.getText().toString());
        cm.setClassCode(et_classCode.getText().toString());
        cm.setSection(et_section.getText().toString());
        //cm.setClassId(AppGenericClass.getInstance(this).getCurrentYear());
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
