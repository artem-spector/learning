package com.artem.server.api.drawing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * created by artem on 1/30/16.
 */
public class DrawingRawData {

    @JsonProperty("motions")
    private List<MotionData> motions;

    @JsonIgnore
    private long lastTime;

    @JsonIgnore
    public synchronized void add(MotionData[] newMotions) {
        for (MotionData motion : newMotions) {
            long motionTime = motion.time;
            motion.time -= lastTime;
            lastTime = motionTime;
            motions.add(motion);
        }
    }

    @JsonIgnore
    public synchronized MotionData[] getMotions(int startIdx) {
        int len = motions.size() - startIdx;
        if (len == 0) return new MotionData[0];

        MotionData[] res = new MotionData[len];
        for (int i = 0; i < res.length; i++)
            res[i] = motions.get(startIdx + i);
        return res;
    }

    @JsonIgnore
    public synchronized MotionData getLastMotion() {
        return motions.isEmpty() ? null : motions.get(motions.size() - 1);
    }

    @JsonIgnore
    public void clear(long uptimeMillis) {
        motions = new ArrayList<>();
        lastTime = uptimeMillis;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return motions.isEmpty();
    }
}
