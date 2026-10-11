package com.qurious.utils;

import org.springframework.stereotype.Component;

@Component
public class CalcDuration {
    public String calcDurationString(Long millis) {

        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        return (minutes+":"+seconds);
    }
}
