package com.artem.server.api.drawing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * created by artem on 1/23/16.
 */
public class MotionData {

    public enum MotionType {Up, Down, Move}

    @JsonProperty("m")
    public MotionType motion;
    @JsonProperty("t")
    public long time;
    @JsonProperty("x")
    public float x;
    @JsonProperty("y")
    public float y;

    public MotionData() {
    }

    @JsonIgnore
    public MotionData(long time, float x, float y, MotionType motion) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.motion = motion;
    }

    public String toString() {
        return "{ motion=" + motion + " , x=" + x + ", y=" + y +" }";
    }

}
