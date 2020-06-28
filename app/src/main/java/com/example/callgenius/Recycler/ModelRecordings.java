package com.example.callgenius.Recycler;

public class ModelRecordings {
    private String Caller;
    private String Number;
    private int Status;
    private String Duration;
    private String Day;
    private String Time;
    private String fileName;

    public ModelRecordings(String caller, String number, int status, String duration, String day, String time, String fileName, boolean isHeader) {
        Caller = caller;
        Number = number;
        Status = status;
        Duration = duration;
        Day = day;
        Time = time;
        this.fileName = fileName;
        this.isHeader = isHeader;
    }

    public String getCaller() {
        return Caller;
    }

    public void setCaller(String caller) {
        Caller = caller;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    private boolean isHeader;
}
