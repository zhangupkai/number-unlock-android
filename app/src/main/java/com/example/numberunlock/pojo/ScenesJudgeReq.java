package com.example.numberunlock.pojo;

import java.util.ArrayList;
import java.util.List;

public class ScenesJudgeReq {

    private List<Long> durationList = new ArrayList<>();
//    private Long duration;

    public List<Long> getDurationList() {
        return durationList;
    }

    public void setDurationList(List<Long> durationList) {
        this.durationList = durationList;
    }

//    public Long getDuration() {
//        return duration;
//    }
//
//    public void setDuration(Long duration) {
//        this.duration = duration;
//    }
}
