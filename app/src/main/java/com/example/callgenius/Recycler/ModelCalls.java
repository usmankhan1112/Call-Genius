package com.example.callgenius.Recycler;

public class ModelCalls {
    private String Caller;
    private String Number;
    private int Status;
    private String Duration;
    private String Day;
    private String Time;

    private boolean isHeader;


    public ModelCalls(String caller, String number, int status, String duration, String day, String time, boolean isHeader) {
        Caller = caller;
        Number = number;
        Status = status;
        Duration = duration;
        Day = day;
        Time = time;
        this.isHeader = isHeader;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getCaller() {
        return Caller;
    }

    public void setCaller(String caller) {
        Caller = caller;
    }

    public int getStatus() {
        return Status;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

}
