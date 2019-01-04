package com.example.saq.fyp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

public class AttedanceDialog extends Dialog {

    AttendanceModel attendanceModel;
    AttendanceInterface attendanceInterface;
    int itemPos = -1;
    TextView tv_absent,tv_present, tv_name,tv_fatherName,tv_classId,tv_seatNo;

    public interface AttendanceInterface{
        public void onAction(boolean isPresent,int itemPos);
    }

    public AttedanceDialog(@NonNull Context context,AttendanceModel attendanceModel,AttendanceInterface attendanceInterface) {
        super(context);
        this.attendanceInterface = attendanceInterface;
        this.attendanceModel = attendanceModel;

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
        tv_fatherName = findViewById(R.id.tv_fatherName);
        tv_classId = findViewById(R.id.tv_classId);
        tv_seatNo = findViewById(R.id.tv_seatNo);

        tv_seatNo.setText(attendanceModel.getSeatNo());
        tv_fatherName.setText(attendanceModel.getFathersName());
        tv_name.setText(attendanceModel.getStudentName());
        tv_classId.setText(attendanceModel.getProgram()+ " " +
                attendanceModel.getShift() + "" +
                attendanceModel.getSectionName());


        tv_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendanceInterface.onAction(false,itemPos);
                dismiss();
            }
        });

        tv_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendanceInterface.onAction(true,itemPos);
                dismiss();
            }
        });
    }
}
