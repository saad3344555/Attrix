package com.example.saq.fyp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saq.fyp.R;
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

        public MyVH(View itemView) {
            super(itemView);
            seatNo = itemView.findViewById(R.id.seatNo);

        }
    }
}
