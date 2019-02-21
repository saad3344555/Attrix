package com.example.saq.fyp.common;

import android.util.Log;

import com.example.saq.fyp.AttendanceModel;
import com.example.saq.fyp.SignInUpModel;
import com.example.saq.fyp.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2/12/2019.
 */

public class Common {
    public static List<Student> students = new ArrayList<>();
    public static List<AttendanceModel> attendanceModels = new ArrayList<>();

    public static void setStudents(List<Student> list) {
        students.clear();
        students.addAll(list);
    }

    //    public boolean isPresent(String faceId)
//    {
//        for (Student student: students) {
//
//        }
//    }
    public static String getSeatNo(String faceId) {
        Log.e("Size", String.valueOf(students.size()));
        String seatNo = "";
        for (Student student : students) {
            Log.e("SeatNofaceId", student.getSeatNo() + "" + student.getFaceId() + " Argument->" + faceId);
            if (student.getFaceId().equals(faceId)) {
                seatNo = student.getSeatNo();
                break;
            }
        }

        return seatNo;
    }

    public static String getFaceId(String seatNo) {
        Log.e("Size", String.valueOf(students.size()));
        String faceId = "";
        for (Student student : students) {
            //Log.e("SeatNofaceId", student.getSeatNo() + "" + student.getFaceId() + " Argument->" + faceId);
            if (student.getSeatNo().equals(seatNo)) {
                faceId = student.getFaceId();
                break;
            }
        }

        return faceId;
    }

    public static Student getStudent(String face_id) {
        for (Student model : students) {
            if (model.getFaceId().equals(face_id))
                return model;
        }
        return null;
    }

    public static String getName(String faceId) {
        Log.e("Size", String.valueOf(students.size()));
        String seatNo = "";
        for (Student student : students) {
            Log.e("SeatNofaceId", student.getSeatNo() + "" + student.getFaceId() + " Argument->" + faceId);
            if (student.getFaceId().equals(faceId)) {
                seatNo = student.getName();
                break;
            }
        }

        return seatNo;
    }
}
