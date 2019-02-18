package com.example.saq.fyp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saq.fyp.common.Common;
import com.example.saq.fyp.model.Attendance;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class EditAttendance extends AppCompatActivity {
    EditText et_date;
    RecyclerView rv_attendance;
    Toolbar toolbar;
    List<Attendance> attendanceModels;
    String date;
    AlertDialog dialog;
    FloatingActionButton addRecord;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_edit);
        dialog = new SpotsDialog.Builder().setContext(this).build();
        dialog.setMessage("Please Wait");
        dialog.setTitle("Fetching Records");

        addRecord = findViewById(R.id.add_record);

        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditAttendance.this);
                builder.setTitle("Add Student Attendance");
                final EditText editText = new EditText(EditAttendance.this);
                builder.setView(editText);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editText.getText().toString().isEmpty()) {
                            String seatNo = editText.getText().toString();
                            String face_id = Common.getFaceId(seatNo);
                            Attendance attendance = new Attendance();
                            attendance.setIs_present(true);
                            attendance.setFace_id(face_id);
                            attendanceModels.add(attendance);
                            updateListToFireBase(attendanceModels);

                            //rv_attendance.getAdapter().notifyDataSetChanged();
                        } else
                            Toast.makeText(EditAttendance.this, "Empty TextBox!", Toast.LENGTH_SHORT).show();


                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rv_attendance = findViewById(R.id.rv_attendance);
        rv_attendance.setVisibility(View.GONE);

        et_date = findViewById(R.id.et_date);


        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
                //date = AppGenericClass.getInstance(EditAttendance.this).showDatePicker(et_date);
            }
        });

        rv_attendance.setLayoutManager(new LinearLayoutManager(this));
        //populateDummyData();
        //rv_attendance.setAdapter(new AttendanceListAdapter(this, attendanceModels));
    }

    private void updateListToFireBase(List<Attendance> attendanceModels) {
        FirebaseDatabase.getInstance().getReference("Attendance").child(Home.SELECTED_CLASS.courseNo).child(date).child("attendanceList").setValue(attendanceModels)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callService(date);
                    }
                });
    }

    private void showDatePicker() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = year + "-" + (month + 1) + "-" + day;
                et_date.setText(date);
                callService(date);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, day);

        dialog.show();
    }

    private void callService(String date) {
        dialog.show();
        attendanceModels = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Attendance").child(Home.SELECTED_CLASS.courseNo).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                if (dataSnapshot.getChildrenCount() > 0) {
                    Log.e("key", dataSnapshot.getKey());
                    Log.e("value", dataSnapshot.getValue().toString());
                    AttendanceModel attendanceModel = dataSnapshot.getValue(AttendanceModel.class);
                    if (isClassMatched(attendanceModel)) {
                        attendanceModels.addAll(attendanceModel.getAttendanceList());
                        rv_attendance.setVisibility(View.VISIBLE);
                        rv_attendance.setAdapter(new AttendanceListAdapter(EditAttendance.this, attendanceModels));
                    }
                } else
                    Toast.makeText(EditAttendance.this, "No records found!", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(EditAttendance.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isClassMatched(AttendanceModel attendanceModel) {
        if (attendanceModel.getYear().equals(Home.SELECTED_CLASS.getYearOfTeaching() + "-"
                + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()))
                && attendanceModel.getProgram().equals(Home.SELECTED_CLASS.getProgram())
                && attendanceModel.getSectionName().equals(Home.SELECTED_CLASS.getSection())
                && attendanceModel.getShift().equals(Home.SELECTED_CLASS.getShift())
                && attendanceModel.getCourseNo().equals(Home.SELECTED_CLASS.courseNo)) {
            return true;
        }
        return false;
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
