package com.example.saq.fyp;

import android.support.v7.widget.RecyclerView;

public class ClassModel {
    int Image;
    String Title, Detail,courseNo;

    public ClassModel(String courseNo,int image, String Title, String Detail){
        this.Image = image;
        this.Title = Title;
        this.Detail = Detail;
        this.courseNo = courseNo;

    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public int getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public String getDetail() {
        return Detail;
    }
}
