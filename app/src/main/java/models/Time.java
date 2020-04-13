package models;

public class Time {
    private String fromTime;
    private String toTime;


    public Time(String fromTime, String toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }


}
