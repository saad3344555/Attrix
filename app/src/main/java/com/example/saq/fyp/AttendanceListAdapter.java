package com.example.saq.fyp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saq.fyp.common.Common;
import com.example.saq.fyp.model.Attendance;
import com.example.saq.fyp.model.Student;

import java.util.List;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.AttendanceVH> implements
        AttedanceDialog.AttendanceInterface, FirebaseHelper.StudentCallback {
    private List<Attendance> attendanceModels;
    Context context;

    AttendanceListAdapter(Context context, List<Attendance> attendanceModels) {
        this.context = context;
        this.attendanceModels = attendanceModels;
    }

    @NonNull
    @Override
    public AttendanceListAdapter.AttendanceVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_attendance, parent, false);

        return new AttendanceListAdapter.AttendanceVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceListAdapter.AttendanceVH holder, int position) {
        Attendance model = attendanceModels.get(position);
        holder.tv_seatNo.setText(Common.getSeatNo(model.getFace_id()));
        if (model.is_present) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.color_light_blue));
            holder.tv_attendance.setText("Present");
        } else {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.color_light_red));
            holder.tv_attendance.setText("Absent");
        }
    }

    @Override
    public int getItemCount() {
        return attendanceModels.size();
    }

    @Override
    public void onAction(boolean isPresent, int itemPos) {
//        attendanceModels.get(itemPos).setIs_present(isPresent);
        notifyDataSetChanged();
    }

    @Override
    public void report(Student student,int pos) {
        AttedanceDialog dialog = new AttedanceDialog(context, attendanceModels.get(pos),student, new AttedanceDialog.AttendanceInterface() {
            @Override
            public void onAction(boolean isPresent, int itemPos) {
                notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    public class AttendanceVH extends RecyclerView.ViewHolder {
        TextView tv_seatNo, tv_attendance;
        CardView card;

        public AttendanceVH(View itemView) {
            super(itemView);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
            tv_seatNo = itemView.findViewById(R.id.tv_seatNo);
            card = itemView.findViewById(R.id.card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        FirebaseHelper fb = new FirebaseHelper(context);
                        fb.setStudentCallback(AttendanceListAdapter.this);
                        fb.getRecordFromFaceId(attendanceModels.get(getAdapterPosition()).getFace_id(),getAdapterPosition());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }
}