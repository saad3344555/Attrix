package com.example.saq.fyp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.AttendanceVH> implements AttedanceDialog.AttendanceInterface {
    private List<AttendanceModel> attendanceModels;
    Context context;

    AttendanceListAdapter(Context context, List<AttendanceModel> attendanceModels) {
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
        if (attendanceModels.get(position).isPresent()) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.color_light_blue));
            holder.tv_attendance.setText("Present");
        } else {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.color_light_red));
            holder.tv_attendance.setText("Absent");
        }

        holder.tv_seatNo.setText(attendanceModels.get(position).getSeatNo());
    }

    @Override
    public int getItemCount() {
        return attendanceModels.size();
    }

    @Override
    public void onAction(boolean isPresent,int itemPos) {
        attendanceModels.get(itemPos).setPresent(isPresent);
        notifyDataSetChanged();
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

                        AttedanceDialog attedanceDialog = new AttedanceDialog(context,
                                attendanceModels.get(getAdapterPosition()), AttendanceListAdapter.this);
                        attedanceDialog.setItemPos(getAdapterPosition());
                        attedanceDialog.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }


    }
}