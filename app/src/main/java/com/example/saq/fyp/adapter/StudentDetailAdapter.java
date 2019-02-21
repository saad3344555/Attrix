package com.example.saq.fyp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.saq.fyp.AttendanceModel;
import com.example.saq.fyp.R;
import com.example.saq.fyp.common.Common;
import com.example.saq.fyp.model.Attendance;
import com.example.saq.fyp.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2/13/2019.
 */

public class StudentDetailAdapter extends RecyclerView.Adapter<StudentDetailAdapter.MyVH> {
    private List<Student> students = new ArrayList<>();
    private Context con;

    public StudentDetailAdapter(List<Student> students, Context con) {
        this.students = students;
        this.con = con;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH holder, int position) {

        holder.seatNo.setText(students.get(position).getSeatNo());

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        TextView seatNo;
        FrameLayout details;

        public MyVH(View itemView) {
            super(itemView);
            details = itemView.findViewById(R.id.details);
            seatNo = itemView.findViewById(R.id.seatNo);

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View view = LayoutInflater.from(con).inflate(R.layout.detail_dialog, null);
                    String faceId = students.get(getAdapterPosition()).getFaceId();
                    int presentCount = getPresentCount(faceId);
                    int absentCount = getAbsentCount(faceId);

                    AlertDialog.Builder builder = new AlertDialog.Builder(con);
                    builder.setTitle("Details");
                    builder.setView(view);
                    TextView name, present, absent;
                    name = view.findViewById(R.id.name);
                    absent = view.findViewById(R.id.absent);
                    present = view.findViewById(R.id.present);
                    name.setText(Common.getName(faceId));
                    present.setText(presentCount + "");
                    absent.setText(absentCount + "");

                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    //show dialog
                    builder.show();
                }
            });

        }
    }

    private int getAbsentCount(String faceId) {
        int count = 0;
        for (AttendanceModel model : Common.attendanceModels) {
            List<Attendance> attendanceList = model.getAttendanceList();
            for (Attendance attendacne : attendanceList) {
                if (attendacne.getFace_id().equals(faceId)) {
                    if (!attendacne.is_present)
                        count = count + 1;
                    break;
                }
            }
        }
        return count;
    }

    private int getPresentCount(String faceId) {
        int count = 0;
        for (AttendanceModel model : Common.attendanceModels) {
            List<Attendance> attendanceList = model.getAttendanceList();
            for (Attendance attendacne : attendanceList) {
                if (attendacne.getFace_id().equals(faceId)) {
                    if (attendacne.is_present)
                        count = count + 1;
                    break;
                }
            }
        }
        return count;
    }
}
