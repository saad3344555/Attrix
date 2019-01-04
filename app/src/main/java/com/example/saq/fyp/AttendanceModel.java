package com.example.saq.fyp;

public class AttendanceModel {
    private String studentName;
    private String fathersName;
    private String seatNo;
    private String sectionName;
    private String shift;
    private String program;

    private boolean isPresent;

    public AttendanceModel() {
    }

    public AttendanceModel(String studentName, String fathersName, String seatNo, String sectionName, String shift, String program, boolean isPresent) {
        this.studentName = studentName;
        this.fathersName = fathersName;
        this.seatNo = seatNo;
        this.sectionName = sectionName;
        this.shift = shift;
        this.program = program;
        this.isPresent = isPresent;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
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

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
