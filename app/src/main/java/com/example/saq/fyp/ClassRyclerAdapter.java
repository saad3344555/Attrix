package com.example.saq.fyp;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saq.fyp.Interfaces.ClassSelectedListener;

import java.util.List;

public class ClassRyclerAdapter extends RecyclerView.Adapter<ClassRyclerAdapter.MyViewHolder> {
    private List<ClassModel> Classes;
    Context context;
    CameraInterface cameraInterface;
    ClassSelectedListener classSelectedListener;

    public interface CameraInterface {
        void openChooser();
    }

    ClassRyclerAdapter(List<ClassModel> Classes, Context context, CameraInterface cameraInterface, ClassSelectedListener classSelectedListener) {
        this.Classes = Classes;
        this.cameraInterface = cameraInterface;
        this.context = context;
        this.classSelectedListener = classSelectedListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classrwitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClassModel model = Classes.get(position);
        holder.Title.setText(model.getTitle());
        holder.tv_courseNo.setText(model.getCourseNo());
        String text = model.getYearOfTeaching() + " Year" + "-" + model.getShiftSectionProgram();
        Log.e("text", text);
        holder.clteachername.setText(model.getYearOfTeaching() +" "+model.getProgram() + "-" + model.getShift() + "-" + model.getSection());
    }

    @Override
    public int getItemCount() {
        return Classes.size();
    }

    public class MyViewHolder extends ViewHolder {
        TextView Title, tv_courseNo, clteachername;
        ImageView iv_report, iv_camera, iv_edit;

        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.cltitle);
            tv_courseNo = itemView.findViewById(R.id.tv_courseNo);
            clteachername = itemView.findViewById(R.id.clteachername);
            iv_report = itemView.findViewById(R.id.iv_report);
            iv_camera = itemView.findViewById(R.id.iv_camera);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraInterface.openChooser();
                    classSelectedListener.onClassSelected(Classes.get(getAdapterPosition()));
                }
            });

            iv_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classSelectedListener.onClassSelected(Classes.get(getAdapterPosition()));
                    context.startActivity(new Intent(context, ClassDetailsActivity.class));
                }
            });

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classSelectedListener.onClassSelected(Classes.get(getAdapterPosition()));
                    context.startActivity(new Intent(context, EditAttendance.class));
                }
            });
        }
    }
}

