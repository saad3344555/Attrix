package com.example.saq.fyp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ClassModel {
    String title, detail, courseNo;
    private String teacherId;
    private String batchNo;
    private String section;
    private String classCode;
    private String yearOfTeaching;
    private String shift;
    private String program;
    private String shiftSectionProgram;
    private String classId;
    private List<String> enrolledStudents;

    public List<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<String> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }


    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public void setDetail() {
        this.detail = program.toUpperCase() + yearOfTeaching + " Year" + " " + shift + " " + section.toUpperCase();
    }


    public ClassModel() {

    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getYearOfTeaching() {
        return yearOfTeaching;
    }

    public void setYearOfTeaching(String yearOfTeaching) {
        this.yearOfTeaching = yearOfTeaching + "-" + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getShiftSectionProgram() {
        return shiftSectionProgram;
    }

    public void setShiftSectionProgram(String year) {
        this.shiftSectionProgram = this.section + "-" + this.shift + "-" + this.program + "-" + year;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String year) {
        this.classId = yearOfTeaching + "-" + year;
    }

    public ClassModel(String courseNo, String Title, String Detail) {
        this.title = Title;
        this.detail = Detail;
        this.courseNo = courseNo;

    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }


    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return this.detail;
    }
}
