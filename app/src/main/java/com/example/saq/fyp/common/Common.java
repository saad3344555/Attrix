package com.example.saq.fyp.common;

import android.util.Log;

import com.example.saq.fyp.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2/12/2019.
 */

public class Common {
    public static List<Student> students = new ArrayList<>();

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
}
