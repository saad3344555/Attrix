package com.example.saq.fyp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.saq.fyp.adapter.StudentDetailAdapter;
import com.example.saq.fyp.common.Common;
import com.example.saq.fyp.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassDetailsActivity extends AppCompatActivity {

    RecyclerView rv_students;
    StudentDetailAdapter adapter;
    List<Student> students;
    TextView classCountTv;
    List<AttendanceModel> attendanceModelList = new ArrayList<>();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        rv_students = findViewById(R.id.rv_students);
        rv_students.setLayoutManager(new LinearLayoutManager(this));

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching details");
        dialog.setMessage("Please Wait");
        dialog.show();
        classCountTv = findViewById(R.id.class_count);

        getAttendanceCount();
    }

    private void getAttendanceCount() {
        DatabaseReference attRef = FirebaseDatabase.getInstance().getReference("Attendance").child(Home.SELECTED_CLASS.courseNo);
        attRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classCountTv.setText("Total Classes = " + dataSnapshot.getChildrenCount());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AttendanceModel model = data.getValue(AttendanceModel.class);
                    Common.attendanceModels.add(model);
                }
                getEnrolledStudentsList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getEnrolledStudentsList() {
        //Use static students
        students = new ArrayList<>();
        List<String> studentFaces = Home.SELECTED_CLASS.getEnrolledStudents();
        if (!studentFaces.get(0).equals("-1")) {
            Log.e("studentFaces", studentFaces.size() + "");
            for (String face_id : studentFaces) {
                students.add(Common.getStudent(face_id));
            }
            Log.e("StudentSize", students.size() + "");
            if (students.size() > 0) {
                adapter = new StudentDetailAdapter(students, ClassDetailsActivity.this);
                rv_students.setAdapter(adapter);
            }
        }


    }
}
