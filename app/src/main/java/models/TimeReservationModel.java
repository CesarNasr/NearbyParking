package models;

public class TimeReservationModel {

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Boolean getCanReserve() {
        return canReserve;
    }

    public void setCanReserve(Boolean canReserve) {
        this.canReserve = canReserve;
    }

    public TimeReservationModel(Long startTime, Boolean canReserve) {
        this.startTime = startTime;
        this.canReserve = canReserve;
    }


    Long startTime;
    Boolean canReserve;
}
