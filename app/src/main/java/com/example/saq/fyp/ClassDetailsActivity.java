package com.example.saq.fyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.saq.fyp.adapter.StudentDetailAdapter;
import com.example.saq.fyp.common.Common;
import com.example.saq.fyp.model.Student;

import java.util.ArrayList;
import java.util.List;

public class ClassDetailsActivity extends AppCompatActivity {

    RecyclerView rv_students;
    StudentDetailAdapter adapter;
    List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        rv_students = findViewById(R.id.rv_students);
        rv_students.setLayoutManager(new LinearLayoutManager(this));
        getEnrolledStudentsList();
    }

    private void getEnrolledStudentsList() {
        //Use static students
        students = new ArrayList<>();
        List<String> studentFaces = Home.SELECTED_CLASS.getEnrolledStudents();
        if(!studentFaces.get(0).equals("-1"))
        {
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
