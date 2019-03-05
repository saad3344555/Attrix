package com.example.saq.fyp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.saq.fyp.model.Attendance;
import com.example.saq.fyp.model.Student;

public class AttedanceDialog extends Dialog {

    Attendance attendanceModel;
    Student student;
    AttendanceInterface attendanceInterface;
    int itemPos = -1;
    TextView tv_absent,tv_present, tv_name, tv_mobileNo,tv_classId,tv_seatNo;

    public interface AttendanceInterface{
        public void onAction(boolean isPresent,int itemPos);
    }

    public AttedanceDialog(@NonNull Context context, Attendance attendance, Student student, AttendanceInterface attendanceInterface) {
        super(context);
        this.attendanceInterface = attendanceInterface;
        this.student = student;
        this.attendanceModel = attendance;

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void setItemPos(int itemPos){
        this.itemPos = itemPos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_edit_dialog);
        tv_absent = findViewById(R.id.tv_absent);
        tv_present = findViewById(R.id.tv_present);
        tv_name = findViewById(R.id.tv_name);
        tv_mobileNo = findViewById(R.id.tv_mobileNo);
        tv_classId = findViewById(R.id.tv_classId);
        tv_seatNo = findViewById(R.id.tv_seatNo);


        tv_seatNo.setText(student.getSeatNo());

        tv_classId.setText(student.getProgram()+ " " +
                student.getShift() + " " +
                student.getSection());


        tv_name.setText(student.getName());
        tv_mobileNo.setText(student.getMobile());

        tv_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendanceModel.is_present = false;
                attendanceInterface.onAction(false,itemPos);
                dismiss();
            }
        });

        tv_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendanceModel.is_present = true;
                attendanceInterface.onAction(true,itemPos);
                dismiss();
            }
        });
    }
}
