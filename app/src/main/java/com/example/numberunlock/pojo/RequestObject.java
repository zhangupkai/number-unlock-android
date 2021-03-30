package com.example.numberunlock.pojo;

public class RequestObject {
    private Long duration;
    private Float sizeAtDown;
    private Float sizeAtUp;
    private Float sizeAvg;
    private Float pressureAtDown;
    private Float pressureAtUp;
    private Float pressureAvg;

    public Float getSizeAtDown() {
        return sizeAtDown;
    }

    public void setSizeAtDown(Float sizeAtDown) {
        this.sizeAtDown = sizeAtDown;
    }

    public Float getSizeAtUp() {
        return sizeAtUp;
    }

    public void setSizeAtUp(Float sizeAtUp) {
        this.sizeAtUp = sizeAtUp;
    }

    public Float getSizeAvg() {
        return sizeAvg;
    }

    public void setSizeAvg(Float sizeAvg) {
        this.sizeAvg = sizeAvg;
    }

    public Float getPressureAtDown() {
        return pressureAtDown;
    }

    public void setPressureAtDown(Float pressureAtDown) {
        this.pressureAtDown = pressureAtDown;
    }

    public Float getPressureAtUp() {
        return pressureAtUp;
    }

    public void setPressureAtUp(Float pressureAtUp) {
        this.pressureAtUp = pressureAtUp;
    }

    public Float getPressureAvg() {
        return pressureAvg;
    }

    public void setPressureAvg(Float pressureAvg) {
        this.pressureAvg = pressureAvg;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
